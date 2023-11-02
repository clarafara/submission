package com.ara.storyappdicoding1.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.ara.storyappdicoding1.R
import com.ara.storyappdicoding1.data.local.UserModel
import com.ara.storyappdicoding1.databinding.ActivityLoginBinding
import com.ara.storyappdicoding1.view.ViewModelFactory
import com.ara.storyappdicoding1.view.main.MainActivity
import com.ara.storyappdicoding1.data.remote.repository.Result
import com.ara.storyappdicoding1.view.main.MainViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener { processLogin() }

        setupView()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleTV = setOtherViewAnimation(binding.titleTextView)
        val emailTV = setOtherViewAnimation(binding.emailEditText)
        val emailET = setOtherViewAnimation(binding.emailEditTextLayout)
        val passTV = setOtherViewAnimation(binding.passwordEditText)
        val passET = setOtherViewAnimation(binding.passwordEditTextLayout)
        val signupBtn = setOtherViewAnimation(binding.loginButton)

        AnimatorSet().apply {
            playSequentially(titleTV, emailTV, emailET, passTV, passET, signupBtn)
            start()
        }
    }

    private fun setOtherViewAnimation(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 1f).apply {
            duration = 500
        }
    }

    private fun processLogin() {
        binding.apply {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.login(email, password).observe(this@LoginActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                            loginButton.isEnabled = false
                        }

                        is Result.Success -> {
                            showLoading(false)
                            loginButton.isEnabled = true
                            viewModel.setLogin(UserModel(email, result.data.loginResult.token))
                            showToast(getString(R.string.login_berhasil))
                            moveToMainActivity()
                        }

                        is Result.Error -> {
                            showLoading(false)
                            loginButton.isEnabled = true
                            showToast(getString(R.string.login_gagal))
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}