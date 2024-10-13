package com.dicoding.dicodingevent.ui.upcoming

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.adapter.LandscapeEventAdapter
import com.dicoding.dicodingevent.adapter.PortraitEventAdapter
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentUpcomingBinding
import com.dicoding.dicodingevent.ui.DetailEventActivity

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val upcomingViewModel by viewModels<UpcomingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.layoutManager = layoutManager

        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            1
        } else {
            2
        }

        binding.rvEvents.layoutManager = GridLayoutManager(context, spanCount)


        upcomingViewModel.events.observe(viewLifecycleOwner) { eventList ->
            setEventData(eventList)
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return root
    }

    private fun setEventData(events: List<ListEventsItem>) {
        val upcomingAdapter = LandscapeEventAdapter { selectedEvent ->
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", selectedEvent.id)
            startActivity(intent)
        }
        binding.rvEvents.adapter = upcomingAdapter
        upcomingAdapter.submitList(events)

        // Cek apakah daftar events kosong
        if (events.isEmpty()) {
            binding.tvEventNotFound.visibility = View.VISIBLE
            binding.rvEvents.visibility = View.GONE
        } else {
            binding.tvEventNotFound.visibility = View.GONE
            binding.rvEvents.visibility = View.VISIBLE
        }
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