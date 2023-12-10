package com.example.githubapisub.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapisub.data.response.ItemsItem
import com.example.githubapisub.databinding.ItemRowUserBinding


class ListUserAdapter : ListAdapter<ItemsItem, ListUserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.binding.tvUsername.text = user?.login
        Glide.with(holder.itemView.context)
            .load(user?.avatarUrl)
            .into(holder.binding.profileImage)

        with(holder) {
            itemView.setOnClickListener {
                val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.EXTRA_USER, user?.login)
                itemView.context.startActivity(intentDetail)

            }
        }
    }

    //    override fun getItemCount(): Int = userList?.size ?: 0
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem.login == newItem.login
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}