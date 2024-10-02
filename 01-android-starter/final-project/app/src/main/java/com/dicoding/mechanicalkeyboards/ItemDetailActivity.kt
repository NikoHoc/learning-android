package com.dicoding.mechanicalkeyboards

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.util.Locale

class ItemDetailActivity : AppCompatActivity() {
    private lateinit var ivKeyboardPhoto : ImageView
    private lateinit var tvKeyboardName : TextView
    private lateinit var tvKeyboardDescription : TextView
    private lateinit var tvKeyboardPrice : TextView
    private lateinit var tvKeyboardSpecification : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //get detail item from main activity
        val data = intent.getParcelableExtra<Keyboard>("DATA")

        //change actionbar title
        supportActionBar?.title = data?.name.toString()

        //get the id view
        ivKeyboardPhoto = findViewById(R.id.detail_image_view)
        tvKeyboardName = findViewById(R.id.detail_item_name)
        tvKeyboardDescription = findViewById(R.id.detail_item_description)
        tvKeyboardPrice = findViewById(R.id.detail_item_price)
        tvKeyboardSpecification = findViewById(R.id.detail_item_specification)

        //put data into view
        ivKeyboardPhoto.setImageResource(data?.photo ?: R.drawable.ajazz_k685t) // Use a default placeholder image if data is null
        tvKeyboardName.text = data?.name ?: "Unknown Keyboard"
        tvKeyboardDescription.text = data?.description ?: "No description available"

        //format price to rupiah
        val decimalFormat = DecimalFormat("#,###")
        tvKeyboardPrice.text = data?.price?.let { "Rp ${decimalFormat.format(it)}" } ?: "N/A"

        tvKeyboardSpecification.text = data?.specification ?: "N/A"

    }
}