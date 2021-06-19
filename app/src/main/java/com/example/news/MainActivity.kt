package com.example.news

import android.app.DownloadManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), NEWsItemClicked {
    private lateinit var mAdapter: NEWsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getData()
        recyclerView.layoutManager=LinearLayoutManager(this)
        mAdapter=NEWsListAdapter(this)
        recyclerView.adapter=mAdapter

    }

    private fun getData(){
        val url="http://api.mediastack.com/v1/news?access_key=de3847fdf7ec61ae3927218f213ef32b"
        val jsonObject=JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                val newsJsonArray=it.getJSONArray("data")
                val newsArray=ArrayList<News>()
                for(i in 0..newsJsonArray.length()){
                    val newsJsonObject=newsJsonArray.getJSONObject(i)
                    val news=News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("image")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {

            }

        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObject)

    }


    override fun onItemClicked(item: News) {
        val builder=CustomTabsIntent.Builder()
        val customTabsIntent=builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }
}