package com.dicoding.dicodingevent.ui.upcoming

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.adapter.LandscapeEventAdapter
import com.dicoding.dicodingevent.data.Result
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentUpcomingBinding
import com.dicoding.dicodingevent.ui.ViewModelFactory

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

//    companion object {
//        const val UPCOMING_EVENT_TAG = "upcoming event"
//    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): ScrollView? {
        _binding = FragmentUpcomingBinding.inflate(layoutInflater, container, false)
        val root: ScrollView = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UpcomingViewModel by viewModels {
            factory
        }

        val landscapeEventAdapter = LandscapeEventAdapter()
        viewModel.getUpcomingEvent().observe(viewLifecycleOwner) { result ->
            if(result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val events = arrayListOf<ListEventsItem>()
                        result.data.map {
                            val event = ListEventsItem(id = it.id, name = it.name, mediaCover = it.mediaCover)
                            events.add(event)
                        }
                        landscapeEventAdapter.submitList(events)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }

        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            1
        } else {
            2
        }

        binding.rvEvents.apply {
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            setHasFixedSize(true)
            adapter = landscapeEventAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
