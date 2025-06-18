package com.eunyoung.android_class

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BoardActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_board_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvFilterList)
        rv.layoutManager = LinearLayoutManager(requireContext())

        val data = listOf(
            "Warm Tone", "Cool Tone", "Pastel Tone",
            "Vintage", "Monotone", "Black & White",
            "HDR", "Retro", "Lomo", "Soft Focus"
        ).map { FilterData(it) }

        val adapter = FilterAdapter(data)
        rv.adapter = adapter

        adapter.itemClickListener = object : FilterAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val filterName = data[position].name
                val intent = Intent(requireContext(), PhotoSaveActivity::class.java)
                    .putExtra("FILTER_NAME", filterName)
                startActivity(intent)
            }
        }
    }
}
