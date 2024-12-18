package com.dicoding.dicodingstory.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingstory.R
import com.dicoding.dicodingstory.data.local.StoryEntity
import com.dicoding.dicodingstory.data.remote.response.ListStoryItem
import com.dicoding.dicodingstory.databinding.ItemStoryLayoutBinding
import com.dicoding.dicodingstory.view.detail.DetailStoryActivity


class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    class MyViewHolder(private val binding: ItemStoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide.with(binding.ivStory.context)
                .load(story.photoUrl)
                .error(R.drawable.image_placeholder)
                .into(binding.ivStory)

            binding.storyOwner.text = itemView.context.getString(R.string.story_owner, story.name)

            binding.root.setOnClickListener {
                val storyData = ListStoryItem(
                    photoUrl = story.photoUrl,
                    name = story.name,
                    description = story.description
                )
                val context = it.context
                val intent = Intent(context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY_DATA, storyData)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        Pair(binding.ivStory, "story_image"),
                        Pair(binding.storyOwner, "story_owner")
                    )

                context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}