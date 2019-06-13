package com.kanjiapp.api

import android.app.Activity
import com.kanjiapp.api.utils.Post

abstract class Check(activity: Activity, address: String, override val content: String) : Post(activity, address) {
    override val path = "/recognition/check"
}