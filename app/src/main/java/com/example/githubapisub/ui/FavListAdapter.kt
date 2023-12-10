package com.example.githubapisub.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapisub.database.entity.FavUser
import com.example.githubapisub.databinding.ItemRowUserBinding

class FavListAdapter : ListAdapter<FavUser, FavListAdapter.FavUserViewHolder>(DIFF_CALLBACK) {

    class FavUserViewHolder(var binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favUser: FavUser) {
            binding.tvUsername.text = favUser.username
            Glide.with(itemView.context)
                .load(favUser.avatarUrl)
                .into((binding.profileImage))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavUserViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavListAdapter.FavUserViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FavUserViewHolder, position: Int) {
        val currentUser = getItem(position)
        holder.bind(currentUser)
        with(holder) {
            itemView.setOnClickListener {
                val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.EXTRA_USER, currentUser.username)
                itemView.context.startActivity(intentDetail)

            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavUser>() {
            override fun areItemsTheSame(oldItem: FavUser, newItem: FavUser): Boolean {
                return oldItem.username == newItem.username
            }

            override fun areContentsTheSame(oldItem: FavUser, newItem: FavUser): Boolean {
                return oldItem == newItem
            }
        }
    }
}