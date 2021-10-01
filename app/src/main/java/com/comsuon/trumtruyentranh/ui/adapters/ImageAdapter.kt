package com.comsuon.trumtruyentranh.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.comsuon.trumtruyentranh.R

class ImageAdapter(private val imageList: List<String>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun getItemCount(): Int = imageList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_image_row, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind(imageList[holder.adapterPosition])
    }

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imv_image)

        fun onBind(path: String) {
            val glideUrl = GlideUrl(path) { mapOf(Pair("Referer", "www.nettruyenpro.com")) }
            Glide.with(imageView).load(glideUrl).error(R.drawable.sharp_broken_image_white_36dp)
                .into(imageView)
        }
    }
}