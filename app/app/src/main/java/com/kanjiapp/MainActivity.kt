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


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkButton.setOnClickListener {
            val content = draw_view.drawToBitmap()
            object: Check(this, BitMapToString(content)){
                override fun onSuccess(response: String) {
                    Log.i(TAG, response)
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

    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}
