package com.dicoding.dicodingstory.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
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
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.dicodingstory.R
import com.dicoding.dicodingstory.adapter.StoryAdapter
import com.dicoding.dicodingstory.data.Result
import com.dicoding.dicodingstory.databinding.ActivityMainBinding
import com.dicoding.dicodingstory.view.ViewModelFactory
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
            }
        }
        return super.onOptionsItemSelected(item)
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
    }

    private fun setupAction() {
        val storyAdapter = StoryAdapter()
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                binding.username.text = getString(R.string.greeting, user.name)

                viewModel.getStories().observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            val story = result.data.listStory
                            storyAdapter.submitList(story)

                            Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                            binding.tvStoryNotFound.visibility = View.GONE
                        }
                        is Result.Error -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                            binding.tvStoryNotFound.visibility = View.VISIBLE
                        }
                    }
                }

                val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    1
                } else {
                    2
                }
                binding.rvStory.apply {
                    layoutManager = GridLayoutManager(this@MainActivity, spanCount)
                    setHasFixedSize(true)
                    adapter = storyAdapter
                }
            }
        }

    }

    private fun playAnimation() {
        binding.username.translationX = 500f
        binding.rvStory.translationX = 500f

        val usernameAnimator = ObjectAnimator.ofFloat(binding.username, View.TRANSLATION_X, 0f).apply {
            duration = 1000
        }
        val rvStoryAnimator = ObjectAnimator.ofFloat(binding.rvStory, View.TRANSLATION_X, 0f).apply {
            duration = 1000
            startDelay = 200
        }

        AnimatorSet().apply {
            playSequentially(usernameAnimator, rvStoryAnimator)
            start()
        }

    }
}