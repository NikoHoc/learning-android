package com.dicoding.mechanicalkeyboards

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mechanicalkeyboards.databinding.ActivityItemDetailBinding
import com.dicoding.mechanicalkeyboards.databinding.ActivityMainBinding
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.util.Locale

class ItemDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //get detail item from main activity
        val data = intent.getParcelableExtra<Keyboard>("DATA")

        //ubah title actionbar
        supportActionBar?.title = data?.name.toString()

        // Mengisi data ke dalam view menggunakan binding
        binding.detailImageView.setImageResource(data?.photo ?: R.drawable.ajazz_k685t) // Placeholder jika data kosong
        binding.detailItemName.text = data?.name ?: "Unknown Keyboard"
        binding.detailItemDescription.text = data?.description ?: "No description available"

        // Format harga ke Rupiah
        val decimalFormat = DecimalFormat("#,###")
        binding.detailItemPrice.text = data?.price?.let { "Rp ${decimalFormat.format(it)}" } ?: "not available"

        binding.detailItemSpecification.text = data?.specification ?: "There is no specification yet"

        binding.shareButton.setOnClickListener {
            shareItem(data)
        }
    }

    private fun shareItem(data: Keyboard?) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, """
                Check out this keyboard:
                Name: ${data?.name ?: "Unknown Keyboard"}
                Price: ${data?.price?.let { "Rp ${DecimalFormat("#,###").format(it)}" } ?: "No Price"}
                Description: ${data?.description ?: "No description available"}
                Specifications: ${data?.specification ?: "No Spec"}
            """.trimIndent())
            type = "text/plain"
        }

        // Launch the share intent
        startActivity(Intent.createChooser(shareIntent, "Share keyboard details via"))
    }
}