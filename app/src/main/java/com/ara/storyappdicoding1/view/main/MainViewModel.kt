package com.ara.storyappdicoding1.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ara.storyappdicoding1.data.local.UserModel
import com.ara.storyappdicoding1.data.remote.repository.StoryRepository
import com.ara.storyappdicoding1.data.remote.repository.UserRepository
import com.ara.storyappdicoding1.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        userRepository.register(name, email, password)

    fun login(email: String, password: String) = userRepository.login(email, password)

    fun getSession(): LiveData<UserModel> = userRepository.getSession().asLiveData()

    fun setLogin(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun deleteLogin() {
        viewModelScope.launch { userRepository.logout() }
    }

//    fun getStories(token: String) = storyRepository.getStories(token)
fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
    return storyRepository.getStories(token).cachedIn(viewModelScope)
}

    fun addNewStory(token: String, description: String, photo: File) =
        storyRepository.addNewStory(token, description, photo)
}