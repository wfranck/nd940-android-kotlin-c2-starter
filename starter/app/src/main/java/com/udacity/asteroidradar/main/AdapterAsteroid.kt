package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.udacity.asteroidradar.domain.Asteroid
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding


class AdapterAsteroid (
    val onClick: ((item: Asteroid) -> Unit)?): ListAdapter<Asteroid, AdapterAsteroid.AsteroidViewHolder>(
        AsteroidDiffCallback()
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
            val binding: ItemAsteroidBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_asteroid,
                parent,
                false
            )
            return AsteroidViewHolder( binding )
        }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.asteroid = item
        holder.binding.root.setOnClickListener { onClick?.invoke(item)}
        holder.binding.executePendingBindings()
    }

    class AsteroidViewHolder(val binding: ItemAsteroidBinding) : RecyclerView.ViewHolder(binding.root)

    class AsteroidDiffCallback: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return newItem == oldItem
        }
    }

    }