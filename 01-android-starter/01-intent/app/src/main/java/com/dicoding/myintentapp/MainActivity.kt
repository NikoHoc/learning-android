package com.dicoding.myintentapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvResult: TextView
    //buat launcher registerForActivityResult untuk menerima nilai return
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == MoveForResultActivity.RESULT_CODE && result.data != null) {
            val selectedValue = result.data?.getIntExtra(MoveForResultActivity.EXTRA_SELECTED_VALUE, 0)
            tvResult.text = "Hasil : $selectedValue"
        }
    }


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


        /* Pindah activity dengan data class, implement : Parcelable */
        val btnMoveWithObject:Button = findViewById(R.id.btn_move_activity_object)
        btnMoveWithObject.setOnClickListener(this)


        /* Implicit Intent */
        val btnDialPhone:Button = findViewById(R.id.btn_dial_number)
        btnDialPhone.setOnClickListener(this)


        /* Dapatkan nilai return dri intent */
        val btnMoveForResult:Button = findViewById(R.id.btn_move_for_result)
        btnMoveForResult.setOnClickListener(this)
        tvResult = findViewById(R.id.tv_result)
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
                //EXTRA_NAME & EXTRA_AGE -> merupakan objek yg dibuat di tujuan activity
                moveWithDataIntent.putExtra(MoveWithDataActivity.EXTRA_NAME, "DicodingAcademy Boy")
                moveWithDataIntent.putExtra(MoveWithDataActivity.EXTRA_AGE, 5)
                startActivity(moveWithDataIntent)
            }

            /* Pindah activity dengan dataclass, implement Parcelable*/
            R.id.btn_move_activity_object -> {
                val person = Person(
                    "DicodingAcademy",
                    5,
                    "academy@dicoding.com",
                    "Bandung"
                )
                val moveWithObjectIntent = Intent(this@MainActivity, MoveWithObjectActivity::class.java)
                //EXTRA_PERSON merupakan objek yg dibuat di MoveWithObjectActivity.kt (activity tujuan)
                moveWithObjectIntent.putExtra(MoveWithObjectActivity.EXTRA_PERSON, person)
                startActivity(moveWithObjectIntent)


                /*
                misal ingin ngirim list of object:
                var persons = ArrayList<Person>()
                moveWithObjectIntent.putParcelableArrayListExtra(KEY,persons)
                contoh:
                 */
                val persons = arrayListOf(
                    Person("DicodingAcademy", 5, "academy@dicoding.com", "Bandung"),
                    Person("John Doe", 30, "johndoe@example.com", "Jakarta"),
                    Person("Jane Doe", 28, "janedoe@example.com", "Surabaya")
                )

                // Mengirim ArrayList<Person> melalui Intent
                val moveWithListObjectIntent = Intent(this@MainActivity, MoveWithObjectActivity::class.java)
                moveWithListObjectIntent.putParcelableArrayListExtra(MoveWithObjectActivity.EXTRA_PERSON_LIST, persons)
                startActivity(moveWithListObjectIntent)
            }


            /* Implicit Intent */
            R.id.btn_dial_number -> {
                val phoneNumber = "081257026600"
                val dialPhoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                startActivity(dialPhoneIntent)
            }

            /* mendapatkan nilai return, pindah activity dulu*/
            R.id.btn_move_for_result -> {
                val moveForResultIntent = Intent(this@MainActivity, MoveForResultActivity::class.java)
                resultLauncher.launch(moveForResultIntent)
            }
        }
    }
}