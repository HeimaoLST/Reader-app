package com.reader.app.di

import android.content.Context
import androidx.room.Room
import com.reader.app.data.local.AppDatabase
import com.reader.app.data.local.dao.UrlDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "reader_db"
        ).build()
    }

    @Provides
    fun provideUrlDao(database: AppDatabase): UrlDao {
        return database.urlDao()
    }
}
