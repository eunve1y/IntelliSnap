package com.eunyoung.android_class

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class CommunityActivity : Fragment() {
    private lateinit var rv: RecyclerView
    private val dataList = mutableListOf<BoardData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_community, container, false)

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom)
            insets
        }

        rv = view.findViewById(R.id.rvCommunity)
        rv.layoutManager = LinearLayoutManager(requireContext())

        val adapter = CommunityAdapter(dataList).apply {
            itemClickListener = object : CommunityAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val post = dataList[position]
                    val postJson = GsonBuilder().create().toJson(post)
                    val intent = Intent(requireContext(), BoardDetailActivity::class.java)
                        .putExtra("post_json", postJson)
                    startActivity(intent)
                }
            }
        }
        rv.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("http://39.118.94.53/android/board.php")
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type","application/x-www-form-urlencoded")
                    doOutput = true
                }

                val raw = conn.inputStream.bufferedReader().use { it.readText() }
                println("RAW JSON ▶ $raw")
                conn.disconnect()

                val arr = GsonBuilder().create()
                    .fromJson(raw, Array<BoardData>::class.java)
                    .toList()

                dataList.clear()
                dataList.addAll(arr)

                activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()
                }

            } catch (e: Exception) {
                println("ERROR ▶ ${e.localizedMessage}")
            }
        }
    }
}
