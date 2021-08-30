package com.danchez.keddit

import com.danchez.keddit.api.*
import com.danchez.keddit.commons.RedditNews
import com.danchez.keddit.features.news.NewsManager
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import retrofit2.Call
import retrofit2.Response
import rx.observers.TestSubscriber
import java.util.*

class NewsManagerTest {

    private var testSub = TestSubscriber<RedditNews>()
    private var apiMock = mock<NewsAPI>()
    private var callMock = mock<Call<RedditNewsResponse>>()

    @Before
    fun setup() {
        testSub = TestSubscriber<RedditNews>()
        apiMock = mock()
        callMock = mock()
        `when`(apiMock.getNews(anyString(), anyString())).thenReturn(callMock)
    }

    @Test
    fun testSuccess_basic() {
        val redditNewsResponse = RedditNewsResponse(RedditDataResponse(listOf(), null, null))
        val response = Response.success(redditNewsResponse)

        `when`(callMock.execute()).thenReturn(response)

        val newsManager = NewsManager(apiMock)
        newsManager.getNews("").subscribe(testSub)

        testSub.apply {
            assertNoErrors()
            assertValueCount(1)
            assertCompleted()
        }
    }

    @Test
    fun testSuccess_oneNews() {
        val newsData = RedditNewsDataResponse("author", "title", 10, Date().time, "thumbnail", "url")
        val newsResponse = RedditChildrenResponse(newsData)
        val redditNewsResponse = RedditNewsResponse(RedditDataResponse(listOf(newsResponse), null, null))
        val response = Response.success(redditNewsResponse)
        `when`(callMock.execute()).thenReturn(response)

        val newsManager = NewsManager(apiMock)
        newsManager.getNews("").subscribe(testSub)

        testSub.apply {
            assertNoErrors()
            assertValueCount(1)
            assertCompleted()
        }
        assert(testSub.onNextEvents[0].news[0].author == newsData.author)
        assert(testSub.onNextEvents[0].news[0].title == newsData.title)
    }

    @Test
    fun testError() {
        val responseError = Response.error<RedditNewsResponse>(500, ResponseBody.create(MediaType.parse("application/json"), ""))
        `when`(callMock.execute()).thenReturn(responseError)

        val newsManager = NewsManager(apiMock)
        newsManager.getNews("").subscribe(testSub)

        assert(testSub.onErrorEvents.size == 1)
    }

    private inline fun <reified T: Any> mock(): T = Mockito.mock(T::class.java)

}