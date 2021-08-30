package com.danchez.keddit.commons

import android.os.Parcel
import android.os.Parcelable
import com.danchez.keddit.commons.adapter.AdapterConstants
import com.danchez.keddit.commons.adapter.ViewType

data class RedditNews(
    val after: String,
    val before: String,
    val news: List<RedditNewsItem>
) : Parcelable {

    constructor(source: Parcel) : this(source.readString().toString(), source.readString().toString(), source.createTypedArrayList(RedditNewsItem.CREATOR)!!)

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(after)
        dest?.writeString(before)
        dest?.writeTypedList(news)
    }


    companion object {
        @Suppress("unused")
        @JvmField val CREATOR: Parcelable.Creator<RedditNews> = object : Parcelable.Creator<RedditNews> {
            override fun createFromParcel(source: Parcel): RedditNews = RedditNews(source)
            override fun newArray(size: Int): Array<RedditNews?> = arrayOfNulls(size)
        }
    }
}

data class RedditNewsItem(val author: String, val title: String, val numComments: Int, val created: Long, val thumbnail: String, val url: String?) : ViewType, Parcelable {

    override fun getViewType() = AdapterConstants.NEWS

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(author)
        parcel?.writeString(title)
        parcel?.writeInt(numComments)
        parcel?.writeLong(created)
        parcel?.writeString(thumbnail)
        parcel?.writeString(url)
    }

    override fun describeContents() = 0

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<RedditNewsItem> = object : Parcelable.Creator<RedditNewsItem> {
            override fun createFromParcel(source: Parcel): RedditNewsItem = RedditNewsItem(source)
            override fun newArray(size: Int): Array<RedditNewsItem?> = arrayOfNulls(size)
        }

    }

    constructor(source: Parcel) : this(
        source.readString().toString(),
        source.readString().toString(),
        source.readInt(),
        source.readLong(),
        source.readString().toString(),
        source.readString()
    )


}