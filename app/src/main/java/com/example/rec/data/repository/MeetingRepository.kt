//package com.example.twinmind.data.repository
//
//import com.example.rec.data.local.AppDatabase
//import com.example.rec.data.local.entity.MeetingEntity
//import kotlinx.coroutines.flow.Flow
//
//class MeetingRepository(
//    private val db: AppDatabase
//) {
//    fun observeMeetings(): Flow<List<MeetingEntity>> =
//        db.meetingDao().observeMeetings()
//
//    suspend fun getMeeting(meetingId: String): MeetingEntity? =
//        db.meetingDao().getMeeting(meetingId)
//
//    suspend fun insertMeeting(meeting: MeetingEntity) =
//        db.meetingDao().insert(meeting)
//
//    suspend fun appendTranscript(
//        meetingId: String,
//        text: String
//    ) {
//        db.meetingDao().appendTranscript(meetingId, text)
//    }
//}
