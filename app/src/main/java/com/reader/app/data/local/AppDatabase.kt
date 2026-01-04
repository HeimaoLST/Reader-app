package com.reader.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reader.app.data.local.dao.UrlDao
import com.reader.app.data.local.entity.UrlEntity

@Database(entities = [UrlEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun urlDao(): UrlDao
}
