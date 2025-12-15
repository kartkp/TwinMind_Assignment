package com.example.rec.model

data class Meeting(
    val id: String,
    val duration: String,
    val audioPath: String,
    val transcript: String = "",
    val summary: String = "",
    val actionItems: String = "",
    val keyPoints: String = "",
)
