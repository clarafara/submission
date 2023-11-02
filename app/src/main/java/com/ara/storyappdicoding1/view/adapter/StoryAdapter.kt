package com.ara.storyappdicoding1.view.adapter

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
import com.ara.storyappdicoding1.data.remote.response.ListStoryItem
import com.ara.storyappdicoding1.databinding.ItemRowBinding
import com.ara.storyappdicoding1.view.detail.DetailActivity
import com.bumptech.glide.Glide


class StoryAdapter:
//    ListAdapter<ListStoryItem, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    PagingDataAdapter<ListStoryItem, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = getItem(position)
//
//        Glide.with(holder.itemView.context).load(item.photoUrl).into(holder.imageStory)
//        holder.tvNameStory.text = item.name
//        holder.tvDescStory.text = item.description
//        holder.bind(item)
//
//    }
override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val list = getItem(position)
    if (list != null) {
        holder.bind(list)
    }
}

    inner class ViewHolder(binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageStory = binding.ivItemPhoto
        val tvNameStory = binding.tvItemName
        val tvDescStory = binding.tvItemDescription

        fun bind(story: ListStoryItem) {
            itemView.setOnClickListener {

                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY, story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imageStory, "photo"),
                        Pair(tvNameStory, "name"),
                        Pair(tvDescStory, "description"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}