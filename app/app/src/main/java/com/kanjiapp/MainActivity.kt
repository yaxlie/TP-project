package com.kanjiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kanjiapp.api.Check
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import android.graphics.Bitmap
import android.util.Base64
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.scale
import java.io.ByteArrayOutputStream
import androidx.core.view.drawToBitmap
import com.google.gson.GsonBuilder
import com.kanjiapp.Models.Sign
import com.kanjiapp.Models.Task
import com.kanjiapp.Objects.SignsCollection
import org.json.JSONObject

import kotlinx.android.synthetic.main.progress_dialog.*
import kotlinx.android.synthetic.main.settings_dialog.view.*
import java.util.*
import java.util.stream.IntStream

class MainActivity : AppCompatActivity() {
    val TAG = this.javaClass.name
    val BITMAP_WIDTH = 32
    val BITMAP_HEIGHT = 32

    var sign: Sign = SignsCollection.getRandomSign()

    var address = "192.168.1.105:8000"
    var signName = "tree"
    val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signText.text = sign.rom

        checkButton.setOnClickListener {
            val resultProgress = ProgressDialog.progressDialog(this)
            resultProgress.statusText.text = "Sprawdzanie wyniku..."
            resultProgress.show()

            var image = draw_view.drawToBitmap()
            image = process_image(image)
            val label = sign.label
            val task = Task(label, BitMapToString(image))
            val gson = GsonBuilder().create()
            val jsonObject =  JSONObject(gson.toJson(task))

            object: Check(this, "http://$address", jsonObject.toString()){
                override fun onSuccess(response: String) {
                    Log.i(TAG, response)
                    nextSign()
                    Toast.makeText(this@MainActivity, response, Toast.LENGTH_LONG).show()
                    resultProgress.dismiss()
                }
                override fun onFailure(error: Exception) {
                    Log.e(TAG, "Błąd: ${error.message}")
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
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") {
                    dialogInterface, i ->
                val addr = editText.text.toString()
                if (!addr.isNullOrEmpty()){
                    address = editText.text.toString()
                }
            }
            builder.show()
        }
    }

    fun process_image(bitmap: Bitmap): Bitmap {
//        bitmap.height = BITMAP_HEIGHT
//        bitmap.width = BITMAP_WIDTH
//        for(w in 0..bitmap.width){
//            for(h in 0..bitmap.height){
//                val x = bitmap.getPixel(w,h)
//
//            }
//        }
        var result = bitmap
//        result = Bitmap.createScaledBitmap(bitmap, BITMAP_WIDTH, BITMAP_HEIGHT, false)
        Log.i(TAG, "\n" + BitMapToString(result).trim() + "\n")
        return result
    }

    fun nextSign(){
        sign = SignsCollection.getRandomSign()
        signText.text = sign.rom
    }

    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}
