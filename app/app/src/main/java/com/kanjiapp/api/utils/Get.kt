package com.kanjiapp.api.utils

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

abstract class Get(private val activity: Activity, url: String) : ApiClient() {
    override val URL = url
    abstract val path: String

    override fun execute(){
        Thread {
            try {
                val url = "$URL$path"
                val queue = Volley.newRequestQueue(activity)

                val request = object : StringRequest(
                    Request.Method.GET, url,
                    Response.Listener { response ->
                        Log.i(TAG, "Otrzymano odpowiedź:\n$response")
                        onSuccess(response)
                    },
                    Response.ErrorListener { _error ->
                        Log.e(TAG, _error.toString())
                        onFailure(_error)
                    }) {
                }

                queue.add(request)
            }
            catch (ex: Exception){
                Log.e(TAG, ex.toString())
                Toast.makeText(activity, ex.message, Toast.LENGTH_LONG).show()
            }
        }.start()
    }
}