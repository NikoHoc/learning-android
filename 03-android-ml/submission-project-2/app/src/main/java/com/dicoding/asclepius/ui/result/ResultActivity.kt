package com.dicoding.asclepius.ui.result

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.ui.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private val viewModel: ResultViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.result_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.result_title)
        }

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val result = intent.getStringExtra(EXTRA_RESULT)
        val createdAt = intent.getStringExtra(EXTRA_CREATED_AT) ?: getCurrentDateTime()

        imageUri?.let { uri ->
            Log.d("Image URI", "showImage: $uri")
            binding.resultImage.setImageURI(uri)

            viewModel.isResultSaved(uri.toString()) { isSaved ->
                if (isSaved) {
                    binding.saveButton.isEnabled = false
                    binding.saveButton.text = getString(R.string.already_saved)

                    binding.tvSavedAt.text = createdAt
                    binding.tvSavedAt.visibility = View.VISIBLE
                } else {
                    binding.saveButton.apply {
                        isEnabled = true
                        setOnClickListener {
                            viewModel.saveResult(uri.toString(), result)
                            Log.d("ResultActivity", getString(R.string.result_saved))
                            Toast.makeText(this@ResultActivity, getString(R.string.result_saved), Toast.LENGTH_SHORT).show()

                            isEnabled = false
                            text = getString(R.string.already_saved)

                            binding.tvSavedAt.text = createdAt
                            binding.tvSavedAt.visibility = View.VISIBLE
                        }
                    }
                }
            }
        } ?: run {
            Log.e("ResultActivity", "Image URI is null")
            Toast.makeText(this, getString(R.string.error_image_not_found), Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.resultText.text = getString(R.string.result) + " $result"
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy | hh:mm a", Locale.getDefault())
        return dateFormat.format(Date())
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_CREATED_AT = "extra_created_at"
    }
}