package com.kanjiapp.api.utils

import android.app.Activity
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley



abstract class Post(private val activity: Activity, url: String) : ApiClient() {
    override val URL = url
    abstract val path: String
    abstract val content: String

    override fun execute(){
        Thread {
            val url = "$URL$path"
            val queue = Volley.newRequestQueue(activity)

            Log.i(TAG, content)

            val request = object: StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    Log.i(TAG, response.toString())
                    onSuccess(response.toString())
                },
                Response.ErrorListener { _error ->
                    Log.e(TAG, _error.toString())
                    onFailure(_error)
                })
            {
                @Throws(com.android.volley.AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return content.toByteArray()
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }
            }
            request.retryPolicy = DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            queue.add(request)
        }.start()
    }
}