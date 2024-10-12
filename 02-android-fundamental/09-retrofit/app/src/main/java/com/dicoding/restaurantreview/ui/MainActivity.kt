    package com.dicoding.restaurantreview.ui

    import android.content.Context
    import android.os.Bundle
    import android.telecom.Call
    import android.util.Log
    import android.view.View
    import android.view.inputmethod.InputMethodManager
    import androidx.activity.enableEdgeToEdge
    import androidx.activity.viewModels
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import androidx.lifecycle.ViewModelProvider
    import androidx.recyclerview.widget.DividerItemDecoration
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.bumptech.glide.Glide
    import com.dicoding.restaurantreview.R
    import com.dicoding.restaurantreview.data.response.CustomerReviewsItem
    import com.dicoding.restaurantreview.data.response.PostReviewResponse
    import com.dicoding.restaurantreview.data.response.Restaurant
    import com.dicoding.restaurantreview.data.response.RestaurantResponse
    import com.dicoding.restaurantreview.data.retrofit.ApiConfig
    import com.dicoding.restaurantreview.databinding.ActivityMainBinding
    import com.google.android.material.snackbar.Snackbar
    import retrofit2.Response
    import javax.security.auth.callback.Callback

    class MainActivity : AppCompatActivity() {
        private lateinit var binding: ActivityMainBinding

        private val mainViewModel by viewModels<MainViewModel>()

//        companion object {
//            private const val TAG = "MainActivity"
//            private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
//        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            supportActionBar?.hide()

            // memakai view model provider
//            val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
//            mainViewModel.restaurant.observe(this) { restaurant ->
//                setRestaurantData(restaurant)
//            }

            // menggunakan ktx
            mainViewModel.restaurant.observe(this, { restaurant ->
                setRestaurantData(restaurant)
            })

            val layoutManager = LinearLayoutManager(this)
            binding.rvReview.layoutManager = layoutManager
            val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
            binding.rvReview.addItemDecoration(itemDecoration)

//            findRestaurant()
//
//            binding.btnSend.setOnClickListener { view ->
//                postReview(binding.edReview.text.toString())
//                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(view.windowToken, 0)
//            }

            // gunakan view model
            mainViewModel.listReview.observe(this) { consumerReviews ->
                setReviewData(consumerReviews)
            }
            mainViewModel.isLoading.observe(this) {
                showLoading(it)
            }




            binding.btnSend.setOnClickListener { view ->
                mainViewModel.postReview(binding.edReview.text.toString())
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

            //single event -> menampilkan snackbar msg hasil dari aksi post review
            // namun jika hanya begini, saat hp di rotate, snackbarnya muncul terus
//            mainViewModel.snackbarText.observe(this) {
//                Snackbar.make(window.decorView.rootView, it, Snackbar.LENGTH_SHORT).show()
//            }

            // cara baru snackbar dengan wrapper Event pada utils
            mainViewModel.snackbarText.observe(this, {
                // di cek dulu apakah sudah terhandle apa belum
                it.getContentIfNotHandled()?.let { snackBarText ->
                    Snackbar.make(
                        window.decorView.rootView,
                        snackBarText,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })


        }

        /* pindah ke view model */
//        private fun findRestaurant() {
//            showLoading(true)
//            val client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID)
//            client.enqueue(object : retrofit2.Callback<RestaurantResponse> {
//                override fun onResponse(
//                    call: retrofit2.Call<RestaurantResponse>,
//                    response: Response<RestaurantResponse>
//                ) {
//                    showLoading(false)
//                    if (response.isSuccessful) {
//                        val responseBody = response.body()
//                        if (responseBody != null) {
//                            setRestaurantData(responseBody.restaurant)
//                            setReviewData(responseBody.restaurant.customerReviews)
//                        }
//                    } else {
//                        Log.e(TAG, "onFailure: ${response.message()}")
//                    }
//                }
//                override fun onFailure(call: retrofit2.Call<RestaurantResponse>, t: Throwable) {
//                    showLoading(false)
//                    Log.e(TAG, "onFailure: ${t.message}")
//                }
//            })
//        }
        /* pindah ke view model */


        private fun setRestaurantData(restaurant: Restaurant) {
            binding.tvTitle.text = restaurant.name
            binding.tvDescription.text = restaurant.description
            Glide.with(this@MainActivity)
                .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
                .into(binding.ivPicture)
        }
        private fun setReviewData(consumerReviews: List<CustomerReviewsItem>) {
            val adapter = ReviewAdapter()
            adapter.submitList(consumerReviews)
            binding.rvReview.adapter = adapter
            binding.edReview.setText("")
        }
        private fun showLoading(isLoading: Boolean) {
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        /* pindah ke view model */
//        private fun postReview(review: String) {
//            showLoading(true)
//            val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Dicoding", review)
//            client.enqueue(object : retrofit2.Callback<PostReviewResponse> {
//                override fun onResponse(
//                    call: retrofit2.Call<PostReviewResponse>,
//                    response: Response<PostReviewResponse>
//                ) {
//                    showLoading(false)
//                    val responseBody = response.body()
//                    if (response.isSuccessful && responseBody != null) {
//                        setReviewData(responseBody.customerReviews)
//                    } else {
//                        Log.e(TAG, "onFailure: ${response.message()}")
//                    }
//                }
//                override fun onFailure(call: retrofit2.Call<PostReviewResponse>, t: Throwable) {
//                    showLoading(false)
//                    Log.e(TAG, "onFailure: ${t.message}")
//                }
//            })
//        }
        /* pindah ke view model */
    }