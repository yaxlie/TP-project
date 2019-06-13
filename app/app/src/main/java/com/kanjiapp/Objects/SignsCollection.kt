package com.kanjiapp.Objects

import com.kanjiapp.Models.Sign

object SignsCollection {
    val signs: MutableList<Sign> = mutableListOf()

    fun getRandomSign(): Sign {
        return signs.random()
    }
}