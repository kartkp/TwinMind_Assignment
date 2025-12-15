package com.example.rec.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.twinmind.model.RecordingStatus
import com.example.twinmind.model.SummaryStatus

@Entity(tableName = "meetings")
data class MeetingEntity(
    @PrimaryKey val meetingId: String,

    val audioPath: String? = null,

    val startTime: Long,

    val recordingStatus: RecordingStatus = RecordingStatus.STOPPED,

    val transcript: String = "",

    val title: String? = null,
    val summary: String? = null,
    val actionItems: String? = null,
    val keyPoints: String? = null,

    val summaryStatus: SummaryStatus = SummaryStatus.NOT_STARTED
)
