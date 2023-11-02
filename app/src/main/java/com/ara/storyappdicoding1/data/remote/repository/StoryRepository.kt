package com.ara.storyappdicoding1.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ara.storyappdicoding1.data.StoryPagingSource
import com.ara.storyappdicoding1.data.remote.api.ApiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import com.ara.storyappdicoding1.data.remote.repository.Result
import com.ara.storyappdicoding1.data.remote.response.ErrorResponse
import com.ara.storyappdicoding1.data.remote.response.ListStoryItem


class StoryRepository private constructor(
    private val apiService: ApiService

){
//    fun getStories(token: String) = liveData {
//        emit(Result.Loading)
//        try {
//            val responseBody = apiService.getStories("Bearer $token")
//            emit(Result.Success(responseBody.listStory))
//        } catch (e: HttpException) {
//
//            val errorBody = e.response()?.errorBody()?.string()
//            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
//            emit(Result.Error(errorResponse.message))
//        }
//    }
fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
    return Pager(
        config = PagingConfig(
            pageSize = 5,
            initialLoadSize = 5
        ),
        pagingSourceFactory = {
            StoryPagingSource(token, apiService)
        }
    ).liveData
}


    fun addNewStory(token: String, description: String, imageFile: File) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.addNewStory("Bearer $token", requestBody,multipartBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(Result.Error(errorResponse.message))

        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiService: ApiService
        ) : StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}