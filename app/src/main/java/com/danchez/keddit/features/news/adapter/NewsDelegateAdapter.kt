package com.danchez.keddit.features.news.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danchez.keddit.R
import com.danchez.keddit.commons.RedditNewsItem
import com.danchez.keddit.commons.adapter.ViewType
import com.danchez.keddit.commons.adapter.ViewTypeDelegateAdapter
import com.danchez.keddit.commons.extensions.getFriendlyTime
import com.danchez.keddit.commons.extensions.inflate
import com.danchez.keddit.commons.extensions.loadImg
import kotlinx.android.synthetic.main.news_item.view.*

class NewsDelegateAdapter(val viewActions: OnViewSelectedListener) : ViewTypeDelegateAdapter {

    interface OnViewSelectedListener {
        fun onItemSelected(url: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return NewsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as NewsViewHolder
        holder.bind(item as RedditNewsItem)
    }

    inner class NewsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.news_item)) {

        private val imgThumbnail = itemView.img_thumbnail
        private val description = itemView.description
        private val author = itemView.author
        private val comments = itemView.comments
        private val time = itemView.time

        fun bind(item: RedditNewsItem) {
            imgThumbnail.loadImg(item.thumbnail)
            description.text = item.title
            author.text = item.author
            comments.text = "${item.numComments} comentarios"
            time.text = item.created.getFriendlyTime()

            super.itemView.setOnClickListener { viewActions.onItemSelected(item.url) }
        }

    }

}