package com.example.myapplication

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import org.json.JSONException


class MainActivity : AppCompatActivity() {

    private var gifImageView : ImageView? = null
    private var textView: TextView? = null
    private var lostConnectionLayout: LinearLayout? = null
    private val gifURL = "https://developerslife.ru/random?json=true"
    private val gifList = ArrayList<Pair<String, String>>()
    private var currentGif = -1
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main)
        gifImageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.description)
        lostConnectionLayout = findViewById(R.id.lostConnection)
        queue = Volley.newRequestQueue(this)
        nextGIF(null)
    }

    fun nextGIF(view: View?) {
        if (currentGif < gifList.lastIndex) {
            currentGif+=1
            displayCurrent()
        } else {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, gifURL, null,
                { response ->
                    try {
                        gifList.add(Pair(response.getString("gifURL"), response.getString("description")))
                        currentGif += 1
                        displayCurrent()
                    } catch (e: JSONException) {
                        nextGIF(view)
                    }

                },
                {
                    textView?.text = ""
                    gifImageView?.setImageResource(android.R.color.transparent)
                    lostConnectionLayout?.setVisibility(View.VISIBLE)
                }
            )
            queue?.add(jsonObjectRequest)
        }


    }

    fun prevGIF(view: View?) {
        if (currentGif > 0) {
            currentGif--
            displayCurrent()
        }
    }

    fun displayCurrent () {
        lostConnectionLayout?.setVisibility(View.INVISIBLE)
        textView?.text = gifList[currentGif].second
        Glide.with(this).load(gifList[currentGif].first)
            .thumbnail(Glide.with(this).load(R.drawable.loading))
            .fitCenter()
            .into(gifImageView!!)
    }

}
