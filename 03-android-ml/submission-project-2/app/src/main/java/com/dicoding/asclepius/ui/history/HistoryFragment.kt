package com.dicoding.asclepius.ui.history

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.ui.ViewModelFactory

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val historyAdapter = HistoryAdapter()
        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            1
        } else {
            2
        }

        // Set up RecyclerView
        binding.rvHistory.apply {
            layoutManager = GridLayoutManager(context, spanCount)
            setHasFixedSize(true)
            adapter = historyAdapter
        }

        // Observe data from ViewModel
        viewModel.getHistory().observe(viewLifecycleOwner) { result ->
            Log.d("HistoryFragment", "Data size: ${result?.size ?: 0}")
            binding.progressBar.visibility = if (result == null) View.VISIBLE else View.GONE
            if (result.isNullOrEmpty()) {
                binding.tvEventNotFound.visibility = View.VISIBLE
            } else {
                binding.tvEventNotFound.visibility = View.INVISIBLE
                historyAdapter.submitList(result)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}