package com.ara.storyappdicoding1.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ara.storyappdicoding1.R
import com.ara.storyappdicoding1.data.remote.response.ListStoryItem
import com.ara.storyappdicoding1.databinding.ActivityMainBinding
import com.ara.storyappdicoding1.view.adapter.StoryAdapter
import com.ara.storyappdicoding1.view.ViewModelFactory
import com.ara.storyappdicoding1.view.login.LoginActivity
import com.ara.storyappdicoding1.view.adapter.LoadingStateAdapter
import com.ara.storyappdicoding1.view.add.AddStoryActivity
import com.ara.storyappdicoding1.view.maps.MapsActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding.fab.setOnClickListener { view ->
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
        setupData()
    }

    private fun setupData() {
        viewModel.getSession().observe(this) { user ->
            if (user.token.isNotBlank()) {
                setListStory(user.token,)
            }
        }
    }
//
//    private fun processGetAllStories(token: String) {
//        viewModel.getStories(token).observe(this) { result ->
//            if (result != null) {
//                when (result) {
//                    is Result.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
//                    }
//
//                    is Result.Success -> {
//                        binding.progressBar.visibility = View.GONE
//                        setListStory(result.data)
//                    }
//
//                    is Result.Error -> {
//                        Toast.makeText(
//                            this,
//                            R.string.failed_to_load_data,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                    else -> {}
//                }
//            }
//        }
//    }
//
//    private fun setListStory(listStory: List<ListStoryItem>) {
//        val adapter = StoryAdapter()
//        adapter.submitList(listStory)
//        binding.rvUsers.adapter = adapter
//    }

    private fun setListStory(token: String, it: PagingData<ListStoryItem>) {
        val adapter = StoryAdapter()
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.refresh is LoadState.Loading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                    if (loadStates.refresh is LoadState.Error) {
                        if (adapter.itemCount < 1) {
                            binding.llError.visibility = View.VISIBLE
                            binding.btnRetry.setOnClickListener {
                                setListStory(token) // Retry fetching data
                            }
                        } else {
                            binding.llError.visibility = View.GONE
                        }
                    }
                }
            }
        }

        MainViewModel.getStories(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            R.id.action_logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.keluar)
                    .setMessage(R.string.anda_yakin)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        viewModel.deleteLogin()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }

                val alert = builder.create()
                alert.show()
                true
            }

            else -> true
        }
    }
}