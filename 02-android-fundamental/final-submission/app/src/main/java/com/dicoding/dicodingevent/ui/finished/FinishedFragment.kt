package com.dicoding.dicodingevent.ui.finished

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.dicodingevent.adapter.PortraitEventAdapter
import com.dicoding.dicodingevent.data.Result
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentFinishedBinding
import com.dicoding.dicodingevent.ui.ViewModelFactory
import com.dicoding.dicodingevent.utils.NetworkUtils

class FinishedFragment : Fragment() {
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

//    companion object {
//        const val FINISHED_EVENT_TAG = "finished event"
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (!NetworkUtils.isInternetAvailable(requireActivity())) {
            showNoInternetDialog()
        }
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
        viewModel.getFinishedEvent().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val events = arrayListOf<ListEventsItem>()
                        result.data.map {
                            val event = ListEventsItem(id = it.id, name = it.name, mediaCover = it.mediaCover, imageLogo = it.imageLogo)
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
            //layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, spanCount)
            setHasFixedSize(true)
            adapter = portraitEventAdapter
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = searchView.text.toString()
                searchBar.setText(query)
                searchView.hide()
                viewModel.searchFinishedEvents(query).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val events = arrayListOf<ListEventsItem>()
                                result.data.map {
                                    val event = ListEventsItem(id = it.id, name = it.name, mediaCover = it.mediaCover, imageLogo = it.imageLogo)
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

                false
            }
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
        val viewModel: FinishedViewModel by viewModels {
            factory
        }
        viewModel.getFinishedEvent()
    }

    //private fun showLoading(isLoading: Boolean) = binding.progressBar.isVisible == isLoading
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
