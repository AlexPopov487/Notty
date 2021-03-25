package com.example.notes


data class Note(

    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    var urgencyLevel: Int = 0
) {
}