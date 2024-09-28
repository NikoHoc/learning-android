package com.dicoding.myintentapp

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MoveWithObjectActivity : AppCompatActivity() {

    //buat objek untuk menyimpan data dari activity asal
    companion object {
        const val EXTRA_PERSON = "extra_person"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_move_with_object)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //ngambil textview di activity xml activity_move_with_object
        val tvObject: TextView = findViewById(R.id.tv_object_received)

        //simpan objek yg dikirim ke val person
        val person = if (Build.VERSION.SDK_INT >= 33) {
            //berhubungan "Person" ada bawaan android, pastikan makai class yg kita buat
            intent.getParcelableExtra<Person>(EXTRA_PERSON, Person::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Person>(EXTRA_PERSON)
        }

        if (person != null) {
            val text = "Name : ${person.name.toString()},\nEmail : ${person.email},\nAge : ${person.age},\nLocation : ${person.city}"
            tvObject.text = text
        }
    }
}