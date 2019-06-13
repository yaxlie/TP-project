package com.kanjiapp.api

import android.app.Activity
import com.kanjiapp.api.utils.Get

abstract class GetSigns(activity: Activity, address: String) : Get(activity, address) {
    override val path = "/recognition/signs"
}