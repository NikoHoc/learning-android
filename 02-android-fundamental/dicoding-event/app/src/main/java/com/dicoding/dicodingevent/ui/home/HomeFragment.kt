package com.dicoding.dicodingevent.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { eventList ->
            setUpcomingEventData(eventList)
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { eventList ->
            setFinishedEventData(eventList)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.toastMessage.observe(requireActivity()) { message ->
            message?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun setUpcomingEventData (events: List<ListEventsItem>) {
        val upcomingAdapter = PortraitEventAdapter { selectedEvent ->
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", selectedEvent.id)
            startActivity(intent)
        }
        binding.rvUpcomingEvents.adapter = upcomingAdapter
        upcomingAdapter.submitList(events)

        // Cek apakah API ada data event
        if (events.isEmpty()) {
            binding.tvEventNotFound.visibility = View.VISIBLE
            binding.rvUpcomingEvents.visibility = View.GONE
        } else {
            binding.tvEventNotFound.visibility = View.GONE
            binding.rvUpcomingEvents.visibility = View.VISIBLE
        }

    }

    private fun setFinishedEventData (events: List<ListEventsItem>) {
        val finishedAdapter = LandscapeEventAdapter { selectedEvent ->
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", selectedEvent.id)
            startActivity(intent)
        }
        binding.rvFinishedEvents.adapter = finishedAdapter
        finishedAdapter.submitList(events)

        // Cek apakah API ada data event
        if (events.isEmpty()) {
            binding.tvEventNotFound.visibility = View.VISIBLE
            binding.rvFinishedEvents.visibility = View.GONE
        } else {
            binding.tvEventNotFound.visibility = View.GONE
            binding.rvFinishedEvents.visibility = View.VISIBLE
        }
    }

//    private fun showLoading(isLoading: Boolean) {
//        if (isLoading) {
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
//    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
