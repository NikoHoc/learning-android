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
import com.dicoding.dicodingevent.databinding.ActivityDetailEventBinding

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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val eventId = intent.getIntExtra("EVENT_ID", -1)

        if (eventId != -1) {
            // fetch data first
            detailEventViewModel.fetchEventDetail(eventId)
        }

        detailEventViewModel.eventDetail.observe(this) { eventDetail ->
            if (eventDetail != null) {
                supportActionBar?.title = eventDetail.name

                val eventQuota: Int = if (eventDetail.registrants == null || eventDetail.registrants == 0) {
                    eventDetail.quota ?: 0
                } else {
                    (eventDetail.quota ?: 0) - eventDetail.registrants
                }

                /* sebelumnya binding satu-satu */
//                Glide.with(this@DetailEventActivity)
//                    .load(eventDetail.mediaCover)
//                    .into(binding.ivMediaCover)
//                binding.tvEventCategoryAndLocation.text = getString(R.string.event_category_location, eventDetail.category, eventDetail.cityName)
//                binding.tvEventName.text = eventDetail.name
//                binding.tvEventOwner.text = getString(R.string.event_owner, eventDetail.ownerName)
//                binding.tvSummary.text = eventDetail.summary
//                binding.tvDescription.text = Html.fromHtml(eventDetail.description, Html.FROM_HTML_MODE_LEGACY)
//                binding.tvEventQuota.text = getString(R.string.event_quota, eventQuota)
//                binding.tvEventStart.text = eventDetail.beginTime
//                binding.tvEventEnd.text = eventDetail.endTime
//
//                binding.registerButton.setOnClickListener {
//                    val url = eventDetail.link
//                    val intent = Intent(Intent.ACTION_VIEW).apply {
//                        data = Uri.parse(url)
//                    }
//                    startActivity(intent)
//                }

                /* pakai binding.apply
                * apply menggunakan binding sebagai receiver */
                binding.apply {
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

                /* Atau pakai with(), -> mengoper objek binding sebagai param
                with(binding) { }
                 */
            } else {
                Log.e("DetailEventActivity", "Event details not available")

                binding.tvEventNotFound.visibility = View.VISIBLE
            }
        }

        detailEventViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailEventViewModel.toastMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
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

//    private fun showLoading(isLoading: Boolean) {
//        if (isLoading) {
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
//    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
