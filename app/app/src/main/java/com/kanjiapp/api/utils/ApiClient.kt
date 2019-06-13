package com.kanjiapp.api.utils

import com.android.volley.VolleyError
import java.lang.Exception

abstract class ApiClient : IResponseListener {
    abstract val URL: String
    val TAG = "API_CLIENT"

    abstract fun execute()

    open fun translateVolleyError(error: VolleyError): Exception {
        val msg = String(error.networkResponse.data, Charsets.UTF_8)
        return Exception(if (msg.isEmpty()) "Error" else msg)
    }
}