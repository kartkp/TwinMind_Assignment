package com.example.rec.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rec.data.local.entity.MeetingEntity
import com.example.twinmind.data.local.dao.MeetingDao

@Database(
    entities = [MeetingEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun meetingDao(): MeetingDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "twinmind.db"
                ).build().also { INSTANCE = it }
            }
    }
}
