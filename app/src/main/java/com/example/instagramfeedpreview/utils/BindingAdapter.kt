package com.example.instagramfeedpreview.utils

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.instagramfeedpreview.R

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("loadThumbnail")
    fun loadThumbnail(imageView: ImageView, url: String) {
        try {
            Glide.with(imageView).load(url).error(R.drawable.no_image).placeholder(R.drawable.no_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
        } catch (e: Exception) {
            Log.d("TAG", e.message.toString())
        }
    }

}
