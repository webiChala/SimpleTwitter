package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.codepath.apps.restclienttemplate.models.Tweet

private const val TAG = "TweetDetail"
class TweetDetail : AppCompatActivity() {
    private lateinit var detailProfileImage: ImageView
    private lateinit var detailUsername: TextView
    private lateinit var detailScreeName: TextView
    private lateinit var detailTimeStamp: TextView
    private lateinit var detailTweetBody: TextView
    private lateinit var detailRetweetedAmount: TextView
    private lateinit var detailFavoritedAmount: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_detail)
        detailProfileImage = findViewById(R.id.detailProfileImage)
        detailUsername = findViewById(R.id.detailUsername)
        detailScreeName = findViewById(R.id.detailScreeName)
        detailTimeStamp = findViewById(R.id.detailTimeStamp)
        detailTweetBody = findViewById(R.id.detailTweetBody)
        detailRetweetedAmount = findViewById(R.id.detailRetweetedAmount)
        detailFavoritedAmount = findViewById(R.id.detailFavoritedAmount)



        val tweet = intent.getParcelableExtra<Tweet>(TWEET_EXTRA) as Tweet

        Log.i(TAG, "Tweet: ${tweet.body}")

        Glide.with(this).load(tweet.user?.publicImageUrl).transform(CircleCrop()).into(detailProfileImage)
        detailUsername.text = tweet.user?.name
        detailScreeName.text = tweet.user?.screenName
        detailTimeStamp.setText(tweet.getFormattedTimeStamp())
        detailTweetBody.text = tweet.body
        detailRetweetedAmount.text = tweet.retweetCount.toString()
        detailFavoritedAmount.text = tweet.favoriteCount.toString()




    }
}