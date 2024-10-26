package com.dicoding.dicodingevent.ui.finished

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
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.adapter.PortraitEventAdapter
import com.dicoding.dicodingevent.data.Result
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentFinishedBinding
import com.dicoding.dicodingevent.ui.ViewModelFactory
import com.dicoding.dicodingevent.ui.detail.DetailEventActivity
import com.dicoding.dicodingevent.ui.upcoming.UpcomingViewModel

class FinishedFragment : Fragment() {
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

//    companion object {
//        const val FINISHED_EVENT_TAG = "finished event"
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: FinishedViewModel by viewModels {
            factory
        }

        val portraitEventAdapter = PortraitEventAdapter()
        viewModel.getUpcomingEvent().observe(viewLifecycleOwner) { result ->
            if (result != null) {
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
                        portraitEventAdapter.submitList(events)
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
            2
        } else {
            3
        }

        binding.rvEvents.apply {
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            setHasFixedSize(true)
            adapter = portraitEventAdapter
        }

//        with(binding) {
//            searchView.setupWithSearchBar(searchBar)
//            searchView.editText.setOnEditorActionListener { _, _, _ ->
//                val query = searchView.text.toString()  // Convert Editable to String
//                searchBar.setText(query)
//                searchView.hide()
//
//                // Call the new search method in ViewModel
//                finishViewModel.searchEvents(query)
//
//                false
//            }
//        }
    }



    //private fun showLoading(isLoading: Boolean) = binding.progressBar.isVisible == isLoading
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
