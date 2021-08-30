package com.danchez.keddit.di

import com.danchez.keddit.api.NewsAPI
import com.danchez.keddit.api.NewsRestAPI
import com.danchez.keddit.api.RedditApi
import com.danchez.keddit.features.news.NewsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesNewsAPI(redditApi: RedditApi): NewsAPI {
        return NewsRestAPI(redditApi)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): RedditApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(RedditApi::class.java)
    }

}