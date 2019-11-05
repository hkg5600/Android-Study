package com.example.sns.adapter

import android.app.Application
import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sns.R
import com.example.sns.databinding.ImageItemBinding
import com.example.sns.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.image_item.view.*

class ImageAdapter(val application: Application) :
    RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    var overSize: SingleLiveEvent<Any> = SingleLiveEvent()
    var selectedItem = SparseBooleanArray(0)
    var selectedImageList = MutableLiveData<ArrayList<Image>>(ArrayList<Image>())
    var isFull = false

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
            if (selectedImageList.value?.size!! >= 13)
                isFull = true
            if (selectedItem.get(position, false)) {
                holder.holder.setBackgroundColor(Color.parseColor("#ffffff"))
                holder.count.visibility = View.GONE
                selectedItem.put(position, false)
                selectedImageList.value?.remove(imageList[position])
                isFull = false
            } else {
                if (isFull) {
                    overSize.call()
                } else {
                    holder.holder.setBackgroundColor(Color.parseColor("#2167E0"))
                    holder.count.visibility = View.VISIBLE
                    selectedItem.put(position, true)
                    selectedImageList.value?.add(imageList[position])
                    holder.count.text = ((selectedImageList.value?.indexOf(imageList[position])!! + 1).toString())
                    isFull = false
                }
            }
            loadValue()
        }

        if (selectedItem.get(position, false)) {
            holder.holder.setBackgroundColor(Color.parseColor("#2167E0"))
            holder.count.visibility = View.VISIBLE
            holder.count.text = ((selectedImageList.value?.indexOf(imageList[position])!! + 1).toString())
        } else {
            holder.holder.setBackgroundColor(Color.parseColor("#ffffff"))
            holder.count.visibility = View.GONE
        }

        holder.bind(imageList[position])
    }

    inner class ImageHolder(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val count: TextView = binding.textViewNumber
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

    private fun loadValue() {
        var position: Int
        for (index: Int in 0 until selectedItem.size()) {
            position = selectedItem.keyAt(index)
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