package com.eunyoung.android_class

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PhotoSaveActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_photo_save)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        val tv = findViewById<TextView>(R.id.textSelectedFilter)
        val filter = intent.getStringExtra("FILTER_NAME") ?: "None"
        tv.text = "선택된 필터: $filter"

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            Toast.makeText(this, "사진이 저장되었습니다!", Toast.LENGTH_SHORT).show()
        }
    }
}
