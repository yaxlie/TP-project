package com.kanjiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kanjiapp.api.Check
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import androidx.core.view.drawToBitmap
import com.google.gson.GsonBuilder
import com.kanjiapp.Models.Sign
import com.kanjiapp.Models.Task
import com.kanjiapp.Objects.SignsCollection
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    var sign: Sign = SignsCollection.getRandomSign()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signText.text = sign.rom

        checkButton.setOnClickListener {
            val image = draw_view.drawToBitmap()
            val label = sign.label
            val task = Task(label, BitMapToString(image))
            val gson = GsonBuilder().create()
            val jsonObject =  JSONObject(gson.toJson(task))

            object: Check(this, jsonObject.toString()){
                override fun onSuccess(response: String) {
                    Log.i(TAG, response)
                    nextSign()
                }
                override fun onFailure(error: Exception) {
                    Log.e(TAG, "Błąd: ${error.message}")
                }
            }.execute()
        }

        refreshButton.setOnClickListener {
            draw_view.clearCanvas()
        }
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
