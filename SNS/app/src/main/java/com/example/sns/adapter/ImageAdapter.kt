package com.example.sns.adapter

import android.app.Application
import android.database.Observable
import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.util.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sns.R
import com.example.sns.databinding.ImageItemBinding
import kotlinx.android.synthetic.main.image_item.view.*

class ImageAdapter(val application: Application) :
    RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    var selectedItem = SparseBooleanArray(0)
    var testNum = 0
    var selectedImageList = ArrayList<Image>()

    data class Image(val uri: String)

    var imageList = ArrayList<Image>()

    fun setImage(imageList: ArrayList<String>) {
        this.imageList.clear()
        imageList.forEach {
            this.imageList.add(Image(it))
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ImageHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.image_item, parent, false
        )
    )

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {

        holder.imgView.setOnClickListener {
            if (selectedItem.get(position, false)) {
                holder.holder.setBackgroundColor(Color.parseColor("#ffffff"))
                selectedItem.put(position, false)
                selectedImageList.remove(imageList[position])
                testNum--
            } else {
                holder.holder.setBackgroundColor(Color.parseColor("#FF0179FA"))
                selectedItem.put(position, true)
                selectedImageList.add(imageList[position])
                testNum++
            }
        }

        if (selectedItem.get(position, false)) {
            holder.holder.setBackgroundColor(Color.parseColor("#FF0179FA"))
        } else {
            holder.holder.setBackgroundColor(Color.parseColor("#ffffff"))
        }

        holder.bind(imageList[position])
    }

    inner class ImageHolder(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val holder: CardView = binding.holderLayout
        val imgView: ImageView = binding.imageView
        fun bind(item: Image) {
            itemView.run {
                Glide.with(context).load(item.uri).placeholder(R.drawable.ic_image_default)
                    .override(600, 600).into(image_view)
            }
            binding.item = item
        }
    }

    fun clearSelectedItem() {
        var position: Int
        for (index: Int in 0 until selectedItem.size()) {
            position = selectedItem.keyAt(index)
            selectedItem.put(position, false)
            notifyItemChanged(position)
        }
    }

    var onLongClickListener: OnItemLongClickListener? = null

    interface OnItemLongClickListener {
        fun onClick(
            view: View,
            position: Int,
            holder: ImageHolder
        ): Boolean
    }

}