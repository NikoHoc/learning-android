package com.dicoding.mechanicalkeyboards

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var rvKeyboards: RecyclerView
    private val list = ArrayList<Keyboard>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvKeyboards = findViewById(R.id.rv_keyboards)
        rvKeyboards.setHasFixedSize(true)

        list.addAll(getListKeyboards())
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about_me -> {
                val intentToDetail = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(intentToDetail)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun getListKeyboards(): ArrayList<Keyboard> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataSpecification = resources.getStringArray(R.array.data_specification)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val dataPrice = resources.getIntArray(R.array.data_price)
        val listKeyboard = ArrayList<Keyboard>()
        for (i in dataName.indices) {
            val keyboard = Keyboard(dataName[i], dataDescription[i], dataSpecification[i], dataPhoto.getResourceId(i, -1), dataPrice[i])
            listKeyboard.add(keyboard)
        }
        return listKeyboard
    }

    private fun showRecyclerList() {
        rvKeyboards.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = ListKeyboardAdapter(list)
        rvKeyboards.adapter = listHeroAdapter

        //jika ingin mengirim pada halaman detail
        listHeroAdapter.setOnItemClickCallback(object : ListKeyboardAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Keyboard) {
                val intentToDetail = Intent(this@MainActivity, ItemDetailActivity::class.java)
                intentToDetail.putExtra("DATA", data)
                startActivity(intentToDetail)
            }
        })
    }
}