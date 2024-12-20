package com.dicoding.dicodingevent.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.ItemPortraitEventBinding
import com.dicoding.dicodingevent.ui.detail.DetailEventActivity


class PortraitEventAdapter : ListAdapter<ListEventsItem, PortraitEventAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPortraitEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(private val binding: ItemPortraitEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            Glide.with(binding.ivEvent.context)
                .load(event.imageLogo)
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