package com.eunyoung.android_class

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.GsonBuilder

class BoardDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_board_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val tvTitle   = findViewById<TextView>(R.id.tvDetailTitle)
        val tvAuthor  = findViewById<TextView>(R.id.tvDetailAuthor)
        val tvDate    = findViewById<TextView>(R.id.tvDetailDate)
        val tvContent = findViewById<TextView>(R.id.tvDetailContent)

        val postJson = intent.getStringExtra("post_json")
        if (postJson == null) {
            println("ERROR ▶ invalid post_id")
            finish()
            return
        }

        val post = GsonBuilder().create().fromJson(postJson, BoardData::class.java)

        tvTitle.text = post.Title
        tvAuthor.text = "by ${post.UserName}"
        tvDate.text = post.InsertedDate
        tvContent.text = post.Content
    }
}
