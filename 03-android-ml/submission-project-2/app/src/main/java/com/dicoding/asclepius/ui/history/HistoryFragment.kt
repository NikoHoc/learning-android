package com.dicoding.asclepius.ui.history

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.ui.ViewModelFactory
import com.dicoding.asclepius.ui.home.HomeViewModel

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HistoryViewModel by viewModels {
            factory
        }

        val historyAdapter = HistoryAdapter()
        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            1
        } else {
            2
        }
        binding.rvHistory.apply {
            //layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, spanCount)
            setHasFixedSize(true)
            adapter = historyAdapter
        }

        viewModel.getHistory().observe(viewLifecycleOwner) { result ->
            Log.d("FavFragment", "${result.size}")
            if (result.isEmpty()) {
                binding.tvEventNotFound.visibility = View.VISIBLE
            } else {
                binding.tvEventNotFound.visibility = View.INVISIBLE

                val items = arrayListOf<HistoryEntity>()
                result.map {
                    val item = HistoryEntity(id = it.id, imageData = it.imageData, result = it.result, created_at = it.created_at)
                    items.add(item)
                }
                historyAdapter.submitList(items)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HistoryViewModel by viewModels {
            factory
        }
        viewModel.getHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}