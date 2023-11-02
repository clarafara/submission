package com.ara.storyappdicoding1.di

import android.content.Context
import com.ara.storyappdicoding1.data.remote.repository.UserRepository
import com.ara.storyappdicoding1.data.local.UserPreference
import com.ara.storyappdicoding1.data.local.dataStore
import com.ara.storyappdicoding1.data.remote.api.ApiConfig
import com.ara.storyappdicoding1.data.remote.repository.StoryRepository

object Injection {
    fun provideStoryRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }
}