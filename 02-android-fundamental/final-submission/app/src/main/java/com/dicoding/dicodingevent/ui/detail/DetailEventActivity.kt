package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.Result
import com.dicoding.dicodingevent.data.remote.response.DetailEventResponse
import com.dicoding.dicodingevent.databinding.ActivityDetailEventBinding
import com.dicoding.dicodingevent.ui.ViewModelFactory

class DetailEventActivity : AppCompatActivity() {
    private var _activityDetailEventBinding: ActivityDetailEventBinding? = null
    private val binding get() = _activityDetailEventBinding

    val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    val viewModel: DetailEventViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        _activityDetailEventBinding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val eventId = intent.getIntExtra("EVENT_ID", -1)

        if (eventId != -1) {
            viewModel.getEventDetail(eventId).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            val eventData = result.data
                            setEventDetailsData(eventData)
                        }
                        is Result.Error -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan " + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }



    }

    private fun setEventDetailsData(eventData: DetailEventResponse) {
        val event = eventData.event
        event?.let { eventDetail ->
            supportActionBar?.title = eventDetail.name

            val eventQuota: Int = if (eventDetail.registrants == null || eventDetail.registrants == 0) {
                eventDetail.quota ?: 0
            } else {
                (eventDetail.quota ?: 0) - eventDetail.registrants
            }

            binding?.apply {
                Glide.with(this@DetailEventActivity)
                    .load(eventDetail.mediaCover)
                    .into(ivMediaCover)
                tvEventCategoryAndLocation.text = getString(R.string.event_category_location, eventDetail.category, eventDetail.cityName)
                tvEventName.text = eventDetail.name
                tvEventOwner.text = getString(R.string.event_owner, eventDetail.ownerName)
                tvSummary.text = eventDetail.summary
                tvDescription.text = Html.fromHtml(eventDetail.description, Html.FROM_HTML_MODE_LEGACY)
                tvEventQuota.text = getString(R.string.event_quota, eventQuota)
                tvEventStart.text = eventDetail.beginTime
                tvEventEnd.text = eventDetail.endTime

                registerButton.setOnClickListener {
                    val url = eventDetail.link
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(url)
                    }
                    startActivity(intent)
                }
                tvEventNotFound.visibility = View.GONE
            }
        } ?: run {
            Toast.makeText(this, "Event details not available", Toast.LENGTH_SHORT).show()
            binding?.tvEventNotFound?.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
