package com.am.gsproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.am.gsproject.R
import com.am.gsproject.data.db.entities.ApodEntity
import com.am.gsproject.databinding.ItemFragBinding
import com.am.gsproject.utils.MEDIA_TYPE_VIDEO
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import javax.inject.Inject

class FavFragmentAdapter @Inject constructor(
    private val mContext: Context
) : RecyclerView.Adapter<FavFragmentAdapter.MainViewHolder>() {
    var onVideoClick: ((String?) -> Unit)? = null
    var onItemFavClick: ((Long) -> Unit)? = null
    var itemsList = mutableListOf<ApodEntity>()

    fun setItemList(aList: MutableList<ApodEntity>) {
        this.itemsList = aList
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFragBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = itemsList[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.tvDate.text = item.date
        holder.binding.tvDesc.text = item.explanation
        holder.binding.ivFav.apply {
//            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
//            }

            if (item.isFav == "Y")
                this.setBackgroundResource(R.drawable.ic_fav_y)
            else
                this.setBackgroundResource(R.drawable.ic_fav_n)
            setColorFilter(mContext.getColor(R.color.home_item_fav_tint))
        }
        holder.binding.imvVideoYT.apply {
            visibility = if (item.mediaType == MEDIA_TYPE_VIDEO) View.VISIBLE else View.GONE
        }
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(mContext).load(item.url).apply(requestOptions).into(holder.binding.imvApod)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class MainViewHolder(val binding: ItemFragBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imvVideoYT.setOnClickListener {
                onVideoClick?.invoke(
                    itemsList[adapterPosition].url
                )
            }
            binding.ivFav.setOnClickListener {
                onItemFavClick?.invoke(
                    itemsList[adapterPosition].apod_id
                )
            }
        }
    }
}