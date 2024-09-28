package com.dicoding.myintentapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    //private lateinit var btnMoveActivity: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /* Pindah activity tanpa data*/
        //dapatkan button
        val btnMoveActivity: Button = findViewById(R.id.btn_move_activity)
        //pasang onClickListner
        btnMoveActivity.setOnClickListener(this)

        /* Pindah activity dengan data*/
        val btnMoveWithDataActivity: Button = findViewById(R.id.btn_move_activity_data)
        btnMoveWithDataActivity.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            /* Pindah activity tanpa data*/
            R.id.btn_move_activity -> {
                //intent
                val moveIntent = Intent(this@MainActivity, MoveActivity::class.java)
                //menjalankan activity baru tanpa bawa data
                startActivity(moveIntent)
            }

            /* Pindah activity dengan data*/
            R.id.btn_move_activity_data -> {
                val moveWithDataIntent = Intent(this@MainActivity, MoveWithDataActivity::class.java)
                //kirim data dengan key dan value dengan .putExtra()
                moveWithDataIntent.putExtra(MoveWithDataActivity.EXTRA_NAME, "DicodingAcademy Boy")
                moveWithDataIntent.putExtra(MoveWithDataActivity.EXTRA_AGE, 5)
                startActivity(moveWithDataIntent)
            }
        }
    }
}