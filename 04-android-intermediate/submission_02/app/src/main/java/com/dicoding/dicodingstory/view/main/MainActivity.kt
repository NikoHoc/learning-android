package com.dicoding.dicodingstory.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ProgressDialog.show
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingstory.R
import com.dicoding.dicodingstory.adapter.LoadingStateAdapter
import com.dicoding.dicodingstory.adapter.StoryAdapter
import com.dicoding.dicodingstory.data.Result
import com.dicoding.dicodingstory.data.local.StoryEntity
import com.dicoding.dicodingstory.data.remote.response.ListStoryItem
import com.dicoding.dicodingstory.databinding.ActivityMainBinding
import com.dicoding.dicodingstory.view.ViewModelFactory
import com.dicoding.dicodingstory.view.addstory.AddStoryActivity
import com.dicoding.dicodingstory.view.maps.MapsActivity
import com.dicoding.dicodingstory.view.welcome.WelcomeActivity


class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
        setupAction()
        playAnimation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logout()
                showMapOptionMenu(true)
            }
            R.id.action_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                showMapOptionMenu(true)
            }
            R.id.action_map -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showMapOptionMenu(isShow: Boolean) {
        val view = findViewById<View>(R.id.action_map) ?: return
        view.visibility = if (isShow) View.VISIBLE else View.GONE
    }


    private fun setupView() {
        val storyAdapter = StoryAdapter()
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                binding.username.text = getString(R.string.greeting, user.name)

                viewModel.story.observe(this) { result ->
                    val pagingDataStory: PagingData<StoryEntity> = result
                    val listStoryItem: PagingData<ListStoryItem> = pagingDataStory.map { story ->
                        ListStoryItem(
                            photoUrl = story.photoUrl,
                            createdAt = story.createdAt,
                            name = story.name,
                            description = story.description,
                            lon = story.lon,
                            id = story.id,
                            lat = story.lat
                        )
                    }
                    storyAdapter.submitData(lifecycle, listStoryItem)
//                    storyAdapter.submitData(lifecycle, result)
                }
                showRecyclerList(storyAdapter)
            }
        }
    }

    private fun setupAction() {
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showRecyclerList(storyAdapter: StoryAdapter) {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
        }
        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
    }

    private fun playAnimation() {
        binding.username.translationX = 500f
        binding.rvStory.translationX = 500f

        val usernameAnimator = ObjectAnimator.ofFloat(binding.username, View.TRANSLATION_X, 0f).apply {
            duration = 1000
        }
        val rvStoryAnimator = ObjectAnimator.ofFloat(binding.rvStory, View.TRANSLATION_X, 0f).apply {
            duration = 1000
        }

        AnimatorSet().apply {
            playTogether(usernameAnimator, rvStoryAnimator)
            start()
        }
    }
}