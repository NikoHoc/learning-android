package com.dicoding.dicodingevent.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.adapter.PortraitEventAdapter.Companion.DIFF_CALLBACK
import com.dicoding.dicodingevent.data.local.entity.UpcomingEventEntity
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.ItemLandscapeEventBinding
import com.dicoding.dicodingevent.ui.detail.DetailEventActivity

class LandscapeEventAdapter : ListAdapter<ListEventsItem, LandscapeEventAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLandscapeEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(private val binding: ItemLandscapeEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            Glide.with(binding.ivEvent.context)
                .load(event.mediaCover)
                .into(binding.ivEvent)
            binding.eventTitle.text = event.name

            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, DetailEventActivity::class.java)
                intent.putExtra("EVENT_ID", event.id)  // Send the event id to DetailActivity
                context.startActivity(intent)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}