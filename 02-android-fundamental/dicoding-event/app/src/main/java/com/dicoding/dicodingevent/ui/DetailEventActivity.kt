package com.dicoding.dicodingevent.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.databinding.ActivityDetailEventBinding
import com.dicoding.dicodingevent.databinding.ActivityMainBinding
import com.dicoding.dicodingevent.ui.finished.FinishedViewModel

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private val detailEventViewModel by viewModels<DetailEventViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val eventId = intent.getIntExtra("EVENT_ID", -1)

        if (eventId != -1) {
            // fetch data first
            detailEventViewModel.fetchEventDetail(eventId)
        }

        detailEventViewModel.eventDetail.observe(this) { eventDetail ->
            if (eventDetail != null) {
//                Log.d("DetailEventActivity", "Event Name: ${eventDetail.name}")
//                Log.d("DetailEventActivity", "Event Category: ${eventDetail.summary}")

                binding.eventName.text = eventDetail.name
                binding.textView3.text = eventDetail.summary
            } else {
                Log.e("DetailEventActivity", "Event details not available")
            }
        }
        detailEventViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}