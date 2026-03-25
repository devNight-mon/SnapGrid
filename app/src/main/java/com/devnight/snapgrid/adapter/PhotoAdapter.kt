package com.devnight.snapgrid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.devnight.snapgrid.databinding.ItemPhotoBinding
import com.devnight.snapgrid.model.Photo
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PhotoAdapter(val lifecycleOwner: LifecycleOwner) :
    PagingDataAdapter<Photo, PhotoAdapter.PhotoVH>(DIFF) {

    inner class PhotoVH(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        private var job: Job? = null

        fun bind(photo: Photo) {
            job?.cancel()
            job = lifecycleOwner.lifecycleScope.launch {
                binding.ivPhoto.load(photo.thumbnailUrl) {
                    lifecycle(lifecycleOwner)
                    crossfade(true)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    diskCachePolicy(CachePolicy.ENABLED)
                }
            }
        }

        fun cancel() {
            job?.cancel()
        }
    }

    fun cancelInvisibleJobs(recyclerView: RecyclerView) {
        for (i in 0 until recyclerView.childCount) {
            val vh = recyclerView.findViewHolderForAdapterPosition(i) as? PhotoVH
            vh?.cancel()
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(old: Photo, new: Photo) = old.id == new.id
            override fun areContentsTheSame(old: Photo, new: Photo) = old == new
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoVH {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoVH(binding)
    }

    override fun onBindViewHolder(holder: PhotoVH, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}