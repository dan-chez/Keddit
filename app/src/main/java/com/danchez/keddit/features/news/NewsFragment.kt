package com.danchez.keddit.features.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.danchez.keddit.R
import com.danchez.keddit.commons.InfiniteScrollListener
import com.danchez.keddit.commons.RedditNews
import com.danchez.keddit.commons.RxBaseFragment
import com.danchez.keddit.commons.extensions.inflate
import com.danchez.keddit.features.news.adapter.NewsAdapter
import com.danchez.keddit.features.news.adapter.NewsDelegateAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_news.*
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : RxBaseFragment(), NewsDelegateAdapter.OnViewSelectedListener {

    companion object {
        private const val KEY_REDDIT_NEWS = "redditNews"
        private const val KEY_LIMIT = "newsLimit"

        fun newInstance(limit: String): NewsFragment {
            val args = Bundle()
            args.putString(KEY_LIMIT, limit)
            val fragment = NewsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var redditNews: RedditNews? = null

    private lateinit var newsLimit: String

    private val newsAdapter by lazy { NewsAdapter(this) }

    @Inject lateinit var newsManager: NewsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_news)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        news_list.apply {
            setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener({ requestNews() }, linearLayoutManager))
        }

        newsLimit = arguments?.getString(KEY_LIMIT).toString()

        news_list.adapter = newsAdapter

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_REDDIT_NEWS)) {
            redditNews = savedInstanceState.get(KEY_REDDIT_NEWS) as RedditNews
            (news_list.adapter as NewsAdapter).clearAndAddNews(redditNews!!.news)
        } else {
            requestNews()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val news = (news_list.adapter as NewsAdapter).getNews()
        if (redditNews != null && news.isNotEmpty()) {
            outState.putParcelable(KEY_REDDIT_NEWS, redditNews?.copy(news = news))
        }
    }

    private fun requestNews() {
        val subscription = newsManager.getNews(redditNews?.after ?: "", newsLimit)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { retrievedNews ->
                    redditNews = retrievedNews
                    (news_list.adapter as NewsAdapter).addNews(retrievedNews.news)
                },
                { e -> Snackbar.make(news_list, e.message ?: "", Snackbar.LENGTH_LONG).show() }
            )
        subscriptions.add(subscription)
    }

    override fun onItemSelected(url: String?) {
        if (url.isNullOrEmpty()) {
            Snackbar.make(news_list, "No URL assigned to this news", Snackbar.LENGTH_LONG).show()
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}