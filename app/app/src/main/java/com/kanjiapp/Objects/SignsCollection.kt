package com.kanjiapp.Objects

import com.kanjiapp.Models.Sign

object SignsCollection {
    val signs: MutableList<Sign> = mutableListOf(
        Sign("あ", "a", "1"),
        Sign("は", "ha", "2"),
        Sign("ま", "ma", "3"),
        Sign("お", "o", "4"),
        Sign("い", "i", "5"),
        Sign("さ", "sa", "6"))

    fun getRandomSign(): Sign {
        return signs.random()
    }
}