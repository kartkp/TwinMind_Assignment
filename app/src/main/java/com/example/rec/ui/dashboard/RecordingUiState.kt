package com.example.twinmind.ui.dashboard

data class RecordingUiState(
    val isRecording: Boolean = false,
    val startTime: Long? = null,
    val meetingId: String? = null
)
