package com.eunyoung.android_class

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommunityAdapter(private val items: List<BoardData>)
    : RecyclerView.Adapter<CommunityAdapter.VH>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    var itemClickListener: OnItemClickListener? = null

    inner class VH(v: View): RecyclerView.ViewHolder(v) {
        val title = v.findViewById<TextView>(R.id.tvItemTitle)
        val author= v.findViewById<TextView>(R.id.tvItemAuthor)
        init {
            v.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, vt: Int): VH {
        val v = LayoutInflater.from(p.context)
            .inflate(R.layout.item_community, p, false)
        return VH(v)
    }
    override fun getItemCount() = items.size
    override fun onBindViewHolder(h: VH, pos: Int) {
        val b = items[pos]
        h.title.text  = b.Title
        h.author.text = "${b.UserName} (${b.ViewCount} views)"
    }
}
