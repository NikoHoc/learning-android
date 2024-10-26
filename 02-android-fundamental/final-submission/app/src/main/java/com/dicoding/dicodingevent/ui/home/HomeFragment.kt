package com.dicoding.dicodingevent.ui.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.adapter.LandscapeEventAdapter
import com.dicoding.dicodingevent.adapter.PortraitEventAdapter
import com.dicoding.dicodingevent.data.Result
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.dicoding.dicodingevent.ui.ViewModelFactory
import com.dicoding.dicodingevent.utils.NetworkUtils

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (!NetworkUtils.isInternetAvailable(requireActivity())) {
            showNoInternetDialog()
        }

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())

        val homeViewModel: HomeViewModel by viewModels {
            factory
        }

        val portraitEventAdapter = PortraitEventAdapter()
        homeViewModel.getUpcomingEvent().observe(viewLifecycleOwner) { result ->
            if(result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val events = result.data
                            .map { ListEventsItem(id = it.id, name = it.name, mediaCover = it.mediaCover, imageLogo = it.imageLogo) }
                            .take(5)
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
        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = portraitEventAdapter
        }

        val landscapeEventAdapter = LandscapeEventAdapter()
        homeViewModel.getFinishedEvent().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val events = result.data
                            .map { ListEventsItem(id = it.id, name = it.name, mediaCover = it.mediaCover) }
                            .take(5)
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
        // mengatur jumlah card pada recycler view finished event jika layar landscape atau portrait
        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            1
        } else {
            2
        }

        binding.rvFinishedEvents.apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
