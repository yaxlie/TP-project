package com.kanjiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kanjiapp.api.Check
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import android.graphics.Bitmap
import android.util.Base64
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.scale
import java.io.ByteArrayOutputStream
import androidx.core.view.drawToBitmap
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kanjiapp.Models.Response
import com.kanjiapp.Models.Sign
import com.kanjiapp.Models.Task
import com.kanjiapp.Objects.SignsCollection
import com.kanjiapp.api.GetSigns
import org.json.JSONObject

import kotlinx.android.synthetic.main.progress_dialog.*
import kotlinx.android.synthetic.main.settings_dialog.view.*
import java.util.*
import java.util.stream.IntStream

class MainActivity : AppCompatActivity() {
    val TAG = this.javaClass.name

    var sign: Sign ?= null

    val activity = this

    var address = "192.168.43.148:8000"
    val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            get_aigns()
            set_pencil_size()

            checkButton.setOnClickListener {
                val resultProgress = ProgressDialog.progressDialog(this)
                resultProgress.statusText.text = "Sprawdzanie wyniku..."
                resultProgress.show()

                var image = draw_view.drawToBitmap()
                image = process_image(image)
                val label = sign?.rom.orEmpty()
                val task = Task(label, BitMapToString(image))
                val gson = GsonBuilder().create()
                val jsonObject = JSONObject(gson.toJson(task))

                nextButton.setOnClickListener {
                    nextSign()
                }

                object : Check(this, "http://$address", jsonObject.toString()) {
                    override fun onSuccess(response: String) {
                        Log.i(TAG, response)
                        val result = gson.fromJson(response, Response::class.java)
                        process_response(result)
                        resultProgress.dismiss()
                    }

                    override fun onFailure(error: Exception) {
                        Log.e(TAG, "Błąd: ${error.toString()}")
                        Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show()
                        resultProgress.dismiss()
                    }
                }.execute()
            }

            refreshButton.setOnClickListener {
                draw_view.clearCanvas()
            }

            settingsButton.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                val inflater = layoutInflater
                builder.setTitle("Set server address")
                val dialogLayout = inflater.inflate(R.layout.settings_dialog, null)
                dialogLayout.editText.hint = address
                val editText = dialogLayout.findViewById<EditText>(R.id.editText)
                builder.setView(dialogLayout)
                builder.setPositiveButton("OK") { dialogInterface, i ->
                    val addr = editText.text.toString()
                    get_aigns()
                    if (!addr.isNullOrEmpty()) {
                        address = editText.text.toString()
                    }
                }
                builder.show()
            }
        }
        catch (exception: Exception){
            Toast.makeText(this, exception.message, Toast.LENGTH_LONG)
        }
    }

    fun set_pencil_size(){
        val display = windowManager.defaultDisplay
        val size = display.width/25f
        draw_view.mPaint.strokeWidth = size
    }

    fun process_response(response: Response){
        if(response.correct){
            nextSign()
            Toast.makeText(
                this@MainActivity,
                "Poprawna odpowiedź! ${response.prob_correct}%", Toast.LENGTH_LONG
            ).show()
            mainLayout.setBackgroundColor(resources.getColor(R.color.colorCorrect))
        }
        else{
            Toast.makeText(
                this@MainActivity,
                "Błędna odpowiedź! ${response.prob_correct}%", Toast.LENGTH_LONG
            ).show()
            kanjiText.text = sign?.char.orEmpty()
            kanjiText.visibility = View.VISIBLE
//            draw_view.clearCanvas()
            mainLayout.setBackgroundColor(resources.getColor(R.color.colorWrong))
        }
        Thread {
            Thread.sleep(500)
            activity.runOnUiThread({
                mainLayout.setBackgroundColor(resources.getColor(R.color.colorBackground))
            })
        }
    }

    fun get_aigns() {
        val resultProgress = ProgressDialog.progressDialog(this)
        resultProgress.statusText.text = "Pobieranie znaków..."
//        resultProgress.setCancelable(false)
        resultProgress.show()

        object : GetSigns(this, "http://$address") {
            override fun onFailure(error: Exception) {
                Log.e(TAG, "Nie można pobrać znaków!")
                Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_LONG).show()
                resultProgress.dismiss()
            }

            override fun onSuccess(response: String) {
                Log.i(TAG, "Pobrano listę znaków:\n$response")
                val type = object : TypeToken<ArrayList<Sign>>() {}.type
                val signs = gson.fromJson<ArrayList<Sign>>(response, type)
                SignsCollection.signs.addAll(signs)
                nextSign()
                Toast.makeText(this@MainActivity, "Pobrano znaki.", Toast.LENGTH_LONG).show()
                resultProgress.dismiss()
            }
        }.execute()
    }

    fun process_image(bitmap: Bitmap): Bitmap {
        val result = bitmap
        Log.i(TAG, "\n" + BitMapToString(result).trim() + "\n")
        return result
    }

    fun nextSign(){
        draw_view.clearCanvas()
        sign = SignsCollection.getRandomSign()
        signText.text = sign?.rom.orEmpty()
        kanjiText.text = sign?.char.orEmpty()
        kanjiText.visibility = View.VISIBLE
        mainLayout.setBackgroundColor(resources.getColor(R.color.colorBackground))
    }

    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}
