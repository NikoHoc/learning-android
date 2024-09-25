package com.dicoding.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //ctrl alt L -> auto format

        var input_field = findViewById<EditText>(R.id.in_tgl_lahir)
        var btnKlik = findViewById<Button>(R.id.button_submit)
        var generasi = findViewById<TextView>(R.id.generasi)

        btnKlik.setOnClickListener {
            val input = input_field.text.toString()
            val hasil = when (input.toInt()){
                in 1946..1964 -> "Baby Boomers"
                in 1965..1980 -> "X"
                in 1981..1995 -> "Millenial"
                in 1996..2010 -> "Z"

                else -> "gatau lu apa man"
            }
            generasi.text = "You are in $hasil generations"
        }
    }
}