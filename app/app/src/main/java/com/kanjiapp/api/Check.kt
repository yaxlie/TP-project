package com.kanjiapp.api

import android.app.Activity
import com.kanjiapp.api.utils.Post

abstract class Check(activity: Activity, override val content: String) : Post(activity) {
    override val path = "/check"
}