package com.eunyoung.android_class

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvRecentPhotos)

        rv.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val searchBtn = view.findViewById<Button>(R.id.searchButton)

        searchBtn.setOnClickListener {
            val intent = Intent(requireContext(), PhotoSearchActivity::class.java)
            startActivity(intent)
        }

        val watchBtn = view.findViewById<Button>(R.id.watchPhoto)

        watchBtn.setOnClickListener {
            val tabLayout = requireActivity().findViewById<com.google.android.material.tabs.TabLayout>(R.id.tabLayout)
            tabLayout.getTabAt(2)?.select()
        }
    }
}
