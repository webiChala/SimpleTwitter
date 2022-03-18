package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import com.codepath.apps.restclienttemplate.TimeFormatter
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import java.security.KeyStore

@Parcelize
data class Tweet (
    var body: String = "",
    var createdAt: String = "",
    var user: User? = null,
    var favorited: Boolean = true,
    var retweeted: Boolean = true,
    var retweetCount : Int = 0,
    var favoriteCount : Int = 0) : Parcelable {
    fun getFormattedTimeStamp() : String {
        return TimeFormatter.getTimeDifference(this.createdAt).toString()
    }

    companion object {
        fun fromJson(jsonObject: JSONObject) : Tweet {
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            tweet.favorited = jsonObject.getBoolean("favorited")
            tweet.retweeted = jsonObject.getBoolean("retweeted")
            tweet.favoriteCount = jsonObject.getInt("favorite_count")
            tweet.retweetCount = jsonObject.getInt("retweet_count")
            return tweet
        }

        fun fromJsonArray(jsonArray: JSONArray) : List<Tweet> {
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length()){
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }

    }
}