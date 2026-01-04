package com.reader.app.di

import com.reader.app.data.repository.UrlRepositoryImpl
import com.reader.app.domain.repository.UrlRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UrlRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUrlRepository(
        urlRepositoryImpl: UrlRepositoryImpl
    ): UrlRepository
}
