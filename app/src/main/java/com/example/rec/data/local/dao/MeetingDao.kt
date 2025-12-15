package com.example.twinmind.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rec.data.local.entity.MeetingEntity
import com.example.twinmind.model.SummaryStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MeetingDao {

    // Insert / update meeting
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meeting: MeetingEntity)

    // Observe meetings list (Dashboard)
    @Query("""
        SELECT * FROM meetings
        ORDER BY startTime DESC
    """)
    fun observeMeetings(): Flow<List<MeetingEntity>>

    // Get single meeting
    @Query("""
        SELECT * FROM meetings
        WHERE meetingId = :meetingId
        LIMIT 1
    """)
    suspend fun getMeeting(meetingId: String): MeetingEntity?

    // Append transcript chunk-by-chunk
    @Query("""
        UPDATE meetings
        SET transcript = transcript || :text
        WHERE meetingId = :meetingId
    """)
    suspend fun appendTranscript(
        meetingId: String,
        text: String
    )

    // Update summary after Gemini
    @Query("""
        UPDATE meetings SET
            title = :title,
            summary = :summary,
            actionItems = :actionItems,
            keyPoints = :keyPoints,
            summaryStatus = :status
        WHERE meetingId = :meetingId
    """)
    suspend fun updateSummary(
        meetingId: String,
        title: String,
        summary: String,
        actionItems: String,
        keyPoints: String,
        status: SummaryStatus
    )
}
