package com.dicoding.dicodingstory.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingstory.R
import com.dicoding.dicodingstory.data.remote.response.ListStoryItem
import com.dicoding.dicodingstory.databinding.ItemStoryLayoutBinding
import com.dicoding.dicodingstory.view.detail.DetailStoryActivity

class StoryAdapter: ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    class MyViewHolder(private val binding: ItemStoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide.with(binding.ivStory.context)
                .load(story.photoUrl)
                .error(R.drawable.image_placeholder)
                .into(binding.ivStory)

            binding.storyOwner.text = story.name

            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, DetailStoryActivity::class.java)
                intent.putExtra("STORY_ID", story.id)
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}