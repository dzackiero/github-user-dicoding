package com.pnj.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pnj.githubuser.data.model.response.UserItem
import com.pnj.githubuser.databinding.ItemRowUserBinding
import com.pnj.githubuser.helper.loadImage

class UserAdapter(
    private val dataset: List<UserItem>,
    private val listener: (UserItem) -> Unit
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemRowUserBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = dataset[position]

        holder.apply {
            binding.textName.text = user.login
            binding.imageUser.loadImage(holder.itemView.context, user.avatarUrl)
            itemView.setOnClickListener { listener(user) }
        }
    }
}

