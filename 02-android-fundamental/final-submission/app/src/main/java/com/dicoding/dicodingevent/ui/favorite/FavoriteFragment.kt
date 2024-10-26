package com.dicoding.dicodingevent.ui.favorite

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.adapter.LandscapeEventAdapter
import com.dicoding.dicodingevent.data.Result
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentFavoriteBinding
import com.dicoding.dicodingevent.databinding.FragmentUpcomingBinding
import com.dicoding.dicodingevent.ui.ViewModelFactory
import com.dicoding.dicodingevent.ui.upcoming.UpcomingViewModel
import com.dicoding.dicodingevent.utils.NetworkUtils

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (!NetworkUtils.isInternetAvailable(requireActivity())) {
            showNoInternetDialog()
        }
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: FavoriteEventViewModel by viewModels {
            factory
        }

        val landscapeEventAdapter = LandscapeEventAdapter()
        viewModel.getFavoriteEvent().observe(viewLifecycleOwner) { result ->
            Log.d("FavFragment", "${result.size}")
            if (result.isEmpty()) {
                binding.tvEventNotFound.visibility = View.VISIBLE
            } else {
                binding.tvEventNotFound.visibility = View.INVISIBLE
            }
            val items = arrayListOf<ListEventsItem>()
            result.map {
                val eventId = it.id
                val item = ListEventsItem(id = eventId, name = it.name, mediaCover = it.mediaCover)
                items.add(item)
            }
            landscapeEventAdapter.submitList(items)
        }

        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            1
        } else {
            2
        }
        binding.rvEvents.apply {
            //layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, spanCount)
            setHasFixedSize(true)
            adapter = landscapeEventAdapter
        }
    }

    private fun showNoInternetDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("No Internet")
            .setMessage("You are not connected to the internet. Please check your connection and try again.")
            .setPositiveButton("Retry") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onResume() {
        super.onResume()
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: FavoriteEventViewModel by viewModels {
            factory
        }
        viewModel.getFavoriteEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}