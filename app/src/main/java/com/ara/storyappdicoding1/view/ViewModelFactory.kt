package com.ara.storyappdicoding1.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ara.storyappdicoding1.data.remote.repository.StoryRepository
import com.ara.storyappdicoding1.data.remote.repository.UserRepository
import com.ara.storyappdicoding1.di.Injection
import com.ara.storyappdicoding1.view.main.MainViewModel

//
//class ViewModelFactory (private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return when {
//            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
//                MainViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
//                LoginViewModel(repository) as T
//            }
//            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//        }
//    }
//
//    companion object {
//        @Volatile
//        private var INSTANCE: ViewModelFactory? = null
//        @JvmStatic
//        fun getInstance(context: Context): ViewModelFactory {
//            if (INSTANCE == null) {
//                synchronized(ViewModelFactory::class.java) {
//                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
//                }
//            }
//            return INSTANCE as ViewModelFactory
//        }
//    }
//}

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository,storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideUserRepository(context),
                        Injection.provideStoryRepository()
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}