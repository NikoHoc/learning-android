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
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentFinishedBinding
import com.dicoding.dicodingevent.ui.detail.DetailEventActivity

class FinishedFragment : Fragment() {
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val finishViewModel by viewModels<FinishedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.layoutManager = layoutManager
        binding.rvEvents.layoutManager = GridLayoutManager(context, 2)

        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            2
        } else {
            3
        }

        binding.rvEvents.layoutManager = GridLayoutManager(context, spanCount)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = searchView.text.toString()  // Convert Editable to String
                searchBar.setText(query)
                searchView.hide()

                // Call the new search method in ViewModel
                finishViewModel.searchEvents(query)

                false
            }
        }

        finishViewModel.events.observe(viewLifecycleOwner) { eventList ->
            setEventData(eventList)
        }

        finishViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        finishViewModel.toastMessage.observe(requireActivity()) { message ->
            message?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun setEventData(events: List<ListEventsItem>) {
        val finishedAdapter = PortraitEventAdapter { selectedEvent ->
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", selectedEvent.id)
            startActivity(intent)
        }
        binding.rvEvents.adapter = finishedAdapter
        finishedAdapter.submitList(events)

        // Cek apakah daftar events kosong
        if (events.isEmpty()) {
            binding.tvEventNotFound.visibility = View.VISIBLE
            binding.rvEvents.visibility = View.GONE

            Toast.makeText(requireActivity(), getString(R.string.event_not_found), Toast.LENGTH_SHORT).show()
        } else {
            binding.tvEventNotFound.visibility = View.GONE
            binding.rvEvents.visibility = View.VISIBLE
        }
    }

//    private fun showLoading(isLoading: Boolean) {
//        if (isLoading) {
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
//    }

    // cara lebih simple
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    //private fun showLoading(isLoading: Boolean) = binding.progressBar.isVisible == isLoading
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
