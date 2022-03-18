package com.codepath.apps.restclienttemplate

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.codepath.apps.restclienttemplate.models.Tweet

public const val TWEET_EXTRA = "TWEET_EXTRA"

class TweetsAdapter (private val context : Context, val tweets : ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_tweet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        val tweet = tweets[position]
        holder.bind(tweet, position)
    }

    override fun getItemCount() = tweets.size

    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener{


        init {
            itemView.setOnClickListener(this)

        }

        fun bind(tweet: Tweet, position: Int) {

            tvUsername.text = tweet.user?.name
            tvTweetBody.text = tweet.body
            Glide.with(itemView).load(tweet.user?.publicImageUrl).transform(CircleCrop()).into(ivProfileImage)
            timeStamp.setText(tweet.getFormattedTimeStamp())
            screenName.text = tweet.user?.screenName
            retweetCount.text = tweet.retweetCount.toString()
            favoriteCount.text = tweet.favoriteCount.toString()
            pos = position

            if (tweet.retweeted == true){
                retweetIcon.setBackgroundResource(R.drawable.green_retweet_icon)
            }
            if (tweet.favorited == true) {
                favoriteIcon.setBackgroundResource(R.drawable.favorite_red)
            }

            retweetIcon.setOnClickListener(){
                if (tweet.retweeted == true){
                    tweet.retweetCount -= 1
                    tweet.retweeted = false

                    retweetIcon.setBackgroundResource(R.drawable.retweet_ic)
                    notifyDataSetChanged()
                }else{
                    tweet.retweetCount += 1
                    tweet.retweeted = true
                    retweetIcon.setBackgroundResource(R.drawable.green_retweet_icon)
                    notifyDataSetChanged()

                }
            }
            favoriteIcon.setOnClickListener(){

                if (tweets[pos].favorited == true){
                    tweets[pos].favoriteCount -= 1
                    tweets[pos].favorited = false
                    notifyDataSetChanged()
                    favoriteIcon.setBackgroundResource(R.drawable.favorite_gray)
                }else{
                    tweets[pos].favoriteCount += 1
                    tweets[pos].favorited = true
                    notifyDataSetChanged()
                    favoriteIcon.setBackgroundResource(R.drawable.favorite_red)

                }
            }


        }

        val ivProfileImage = itemView.findViewById<ImageView>(R.id.detailProfileImage)
        val tvUsername = itemView.findViewById<TextView>(R.id.detailUsername)
        val tvTweetBody = itemView.findViewById<TextView>(R.id.detailTweetBody)
        val timeStamp = itemView.findViewById<TextView>(R.id.timeStamp)
        val screenName = itemView.findViewById<TextView>(R.id.detailScreeName)
        val retweetCount = itemView.findViewById<TextView>(R.id.detailRetweetedAmount)
        val favoriteCount = itemView.findViewById<TextView>(R.id.detailFavoritedAmount)
        val retweetIcon = itemView.findViewById<Button>(R.id.detailretweet)
        val favoriteIcon = itemView.findViewById<Button>(R.id.detailFavorite)
        var pos: Int = 0




        override fun onClick(p0: View?) {
            val tweet = tweets[pos]
            Log.i("Webi", "${tweets[pos].user?.name}")
//            Toast.makeText(context, movie.title, Toast.LENGTH_SHORT).show()
            val intent = Intent(context, TweetDetail::class.java)
            intent.putExtra(TWEET_EXTRA, tweet)
            context.startActivity(intent)


        }

    }
}