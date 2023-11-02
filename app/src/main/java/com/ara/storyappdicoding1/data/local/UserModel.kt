package com.ara.storyappdicoding1.data.local

data class UserModel (
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)