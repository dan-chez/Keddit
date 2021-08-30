package com.danchez.keddit.features.news

import com.danchez.keddit.api.NewsAPI
import com.danchez.keddit.api.NewsRestAPI
import com.danchez.keddit.commons.RedditNews
import com.danchez.keddit.commons.RedditNewsItem
import rx.Observable

class NewsManager(private val api: NewsAPI = NewsRestAPI()) {

    fun getNews(after: String, limit: String = "10"): Observable<RedditNews> {
        return Observable.create { subscriber ->

            val callResponse  = api.getNews(after, limit)

            val response = callResponse.execute()

            if (response.isSuccessful) {
                val dataResponse = response.body().data
                val news = dataResponse.children.map {
                    val item = it.data
                    RedditNewsItem(item.author, item.title, item.num_comments, item.created, item.thumbnail, item.url)
                }
                val redditNews = RedditNews(dataResponse.after ?: "", dataResponse.before ?: "", news)
                subscriber.onNext(redditNews)
                subscriber.onCompleted()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }

}