package com.kanjiapp.api.utils

import java.lang.Exception

interface IResponseListener {
    fun onSuccess(response: String)
    fun onFailure(error: Exception)
}