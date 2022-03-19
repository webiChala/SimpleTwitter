package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var tweetCount: TextView
    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etCompose)
        btnTweet = findViewById(R.id.btnTweet)
        tweetCount = findViewById(R.id.tweetCount)

        client = TwitterApplication.getRestClient(this)

        btnTweet.setOnClickListener {
            //Grab the content of edittext (etCompose)
            val tweetContent = etCompose.text.toString()
            //Make sure the tweet isn't empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweets is not allowed!", Toast.LENGTH_SHORT).show()

            }else{
                //Make sure the tweet is under character count
                if (tweetContent.length > 280) {
                    Toast.makeText(this, "Tweet is too long! Limit is 140 characters!", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    //Make an Api call
                    client.publishTweet(tweetContent, object : JsonHttpResponseHandler(){
                        override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?)
                        {
                            Log.e(TAG, "On Failure: $statusCode", throwable)
                        }

                        override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                            Log.i(TAG, "Successful published message!")
                            val tweet = Tweet.fromJson(json.jsonObject)
                            val intent = Intent()
                            intent.putExtra("tweet", tweet)
                            setResult(RESULT_OK, intent)
                            finish()
                        }

                    })
                }


            }

        }

        etCompose.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
                val tweetContent = etCompose.text.toString()
                tweetCount.text = (280 - tweetContent.length).toString()
                if (280 - tweetContent.length < 0) {
                    tweetCount.setTextColor(Color.parseColor("#FF0000"))
                }else{
                    tweetCount.setTextColor(Color.parseColor("#000000"))
                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed

            }
        })

    }
    companion object{
        val TAG = "ComposeActivity"
    }
}
