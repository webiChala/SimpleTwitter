package com.codepath.apps.restclienttemplate

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONException

class TimelineActivity : AppCompatActivity() {

    lateinit var client: TwitterClient
    lateinit var rvTweets: RecyclerView
    lateinit var adapter: TweetsAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    val tweets = ArrayList<Tweet>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)
        swipeContainer = findViewById(R.id.swipeContainer)
        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refreshing timeline")
            // Your code to refresh the list here.
            populateHomeTimeline()
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.

        }

        //configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        client = TwitterApplication.getRestClient(this)
        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(this, tweets)

        rvTweets.layoutManager = LinearLayoutManager(this)
        rvTweets.adapter = adapter

        populateHomeTimeline()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.compose) {
            //Navigate to the compose activity
            val intent = Intent(this, ComposeActivity::class.java)
            editActivityResultLauncher.launch(intent)

            /*//startActivityForResult(intent, REQUEST_CODE)*/
        }
        Log.i("Amir", "Compose")
        return super.onContextItemSelected(item)
    }
    var editActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // If the user comes back to this activity from EditActivity
        // with no error or cancellation
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            // Get the data passed from EditActivity
            if (data != null) {
                val tweet = data.getParcelableExtra("tweet") as Tweet
                Log.i(TAG, "The reply is $tweet")
                //Update the timeline
                tweets.add(0, tweet)
                adapter.notifyItemInserted(0)
                rvTweets.smoothScrollToPosition(0)
            }
        }
    }

    fun populateHomeTimeline() {
        client.getHomeTimeline(object : JsonHttpResponseHandler() {
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?)
            {
                Log.i(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "OnSuccess : $json")

                if (json.jsonArray.length() > 0){
                    val jsonArray : JSONArray = json.jsonArray
                    try {
                        //clear out our current tweets
                        adapter.clear()
                        val listOfNewTweetsRetrieved = Tweet.fromJsonArray(jsonArray)
                        tweets.addAll(listOfNewTweetsRetrieved)
                        adapter.notifyDataSetChanged()
                        swipeContainer.setRefreshing(false)

                    }catch (e: JSONException){
                        Log.e(TAG, "JSONException $e")
                    }


                }

            }
        })
    }
    companion object {
        val TAG = "TimelineActivity"
        val REQUEST_CODE = 10

    }
}