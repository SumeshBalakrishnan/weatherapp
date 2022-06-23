package com.test.gad.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.gad.database.DatabaseUserListItem
import com.test.gad.databinding.ItemWeatherListBinding
import com.test.gad.network.model.WeatherListItem
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UsersListAdapter @Inject constructor(val clickListener: ClickListener) :
    //ListAdapter<DatabaseUserListItem, UsersListAdapter.ViewHolder>(UsersListDiffCallback()) {
    ListAdapter<DatabaseUserListItem, UsersListAdapter.ViewHolder>(UsersListDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ItemWeatherListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DatabaseUserListItem, clickListener: ClickListener) {
            binding.data = item
            binding.executePendingBindings()
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemWeatherListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class UsersListDiffCallback : DiffUtil.ItemCallback<DatabaseUserListItem>() {

    override fun areItemsTheSame(oldItem: DatabaseUserListItem, newItem: DatabaseUserListItem): Boolean {
        return oldItem.tempMin == newItem.tempMin
    }

    override fun areContentsTheSame(oldItem: DatabaseUserListItem, newItem: DatabaseUserListItem): Boolean {
        return oldItem == newItem
    }
}

class ClickListener @Inject constructor() {
var onItemClick: ((DatabaseUserListItem) -> Unit)? = null
    fun onClick(data: DatabaseUserListItem) {
        onItemClick?.invoke(data)
    }
}
