package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.CancerArticleEntity
import com.dicoding.asclepius.databinding.ItemArticleLayoutBinding

class CancerArticleAdapter: ListAdapter<CancerArticleEntity, CancerArticleAdapter.MyViewHolder> (
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemArticleLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    class MyViewHolder(private val binding: ItemArticleLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: CancerArticleEntity) {
            Glide.with(binding.ivArticle.context)
                .load(article.urlToImage)
                .error(R.drawable.image_placeholder) // Replace with an actual error drawable resource
                .into(binding.ivArticle)
            binding.tvArticleName.text = article.title
            binding.tvArticleDescription.text = article.description

            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(article.url)
                }
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CancerArticleEntity>() {
            override fun areItemsTheSame(oldItem: CancerArticleEntity, newItem: CancerArticleEntity): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: CancerArticleEntity, newItem: CancerArticleEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}