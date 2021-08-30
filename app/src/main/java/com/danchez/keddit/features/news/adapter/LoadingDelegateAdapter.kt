package com.danchez.keddit.features.news.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danchez.keddit.R
import com.danchez.keddit.commons.adapter.ViewType
import com.danchez.keddit.commons.adapter.ViewTypeDelegateAdapter
import com.danchez.keddit.commons.extensions.inflate

class LoadingDelegateAdapter: ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {

    }

    class TurnsViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(parent.inflate(R.layout.news_item_loading))

}