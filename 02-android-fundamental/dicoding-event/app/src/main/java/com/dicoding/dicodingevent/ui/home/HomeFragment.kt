package com.dicoding.dicodingevent.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.adapter.LandscapeEventAdapter
import com.dicoding.dicodingevent.adapter.PortraitEventAdapter
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.dicoding.dicodingevent.ui.DetailEventActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel> ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManagerHorizontal = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvents.layoutManager = layoutManagerHorizontal

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinishedEvents.layoutManager = layoutManager

        // mengatur jumlah card pada recycler view finished event jika layar landscape atau portrait
        val spanCount = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 1
            else -> 2
        }

        binding.rvFinishedEvents.layoutManager = GridLayoutManager(context, spanCount)

        // Observe Upcoming Events
        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { eventList ->
            setUpcomingEventData(eventList)
        }

        // Observe finished events
        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { eventList ->
            setFinishedEventData(eventList)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return root
    }

    private fun setUpcomingEventData (events: List<ListEventsItem>) {
        val upcomingAdapter = PortraitEventAdapter { selectedEvent ->
            // Handle item click - Navigate to detail activity
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", selectedEvent.id)
            startActivity(intent)
        }
        binding.rvUpcomingEvents.adapter = upcomingAdapter
        upcomingAdapter.submitList(events)

    }

    private fun setFinishedEventData (events: List<ListEventsItem>) {
        val finishedAdapter = LandscapeEventAdapter { selectedEvent ->
            // Handle item click - Navigate to detail activity
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", selectedEvent.id)
            startActivity(intent)
        }
        binding.rvFinishedEvents.adapter = finishedAdapter
        finishedAdapter.submitList(events)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}