package com.dicoding.dicodingstory.view.detail

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingstory.R
import com.dicoding.dicodingstory.data.remote.response.ListStoryItem
import com.dicoding.dicodingstory.databinding.ActivityDetailBinding
import com.dicoding.dicodingstory.databinding.ActivityMainBinding
import com.dicoding.dicodingstory.utils.formatDate

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY_DATA)
        story?.let {
            setData(it)
        }

        setupView()

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setData(story: ListStoryItem) {
        Glide.with(this)
            .load(story.photoUrl)
            .error(R.drawable.image_placeholder)
            .into(binding.ivStory)


        binding.storyOwner.text = getString(R.string.story_owner, story.name)
        binding.storyCreatedAt.text = getString(R.string.story_date, formatDate(story.createdAt))
        binding.storyDescription.text = getString(R.string.story_description, story.description)
    }

    override fun onSupportNavigateUp(): Boolean {
        finishAfterTransition()
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finishAfterTransition() // This will apply the transition animation
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_STORY_DATA = "STORY_DATA"
    }
}