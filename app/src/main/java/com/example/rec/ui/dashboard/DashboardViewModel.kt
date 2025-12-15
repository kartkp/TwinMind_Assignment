package com.example.twinmind.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rec.data.local.AppDatabase
import com.example.rec.data.local.entity.MeetingEntity
import com.example.twinmind.model.RecordingStatus
import com.example.twinmind.model.SummaryStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class DashboardViewModel(
    private val db: AppDatabase
) : ViewModel() {

    val meetings = db.meetingDao().observeMeetings()

    private val _uiState = MutableStateFlow(RecordingUiState())
    val uiState: StateFlow<RecordingUiState> = _uiState

    fun startRecording(): String {
        val meetingId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()

        viewModelScope.launch {
            db.meetingDao().insert(
                MeetingEntity(
                    meetingId = meetingId,
                    startTime = now,
                    recordingStatus = RecordingStatus.RECORDING,
                    transcript = "",
                    summaryStatus = SummaryStatus.NOT_STARTED
                )
            )
        }

        _uiState.value = RecordingUiState(
            isRecording = true,
            startTime = now,
            meetingId = meetingId
        )

        return meetingId
    }

    fun stopRecording() {
        _uiState.value = RecordingUiState()
    }
}
