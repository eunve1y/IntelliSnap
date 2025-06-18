package com.eunyoung.android_class

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout

class RootActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var homeActivity: HomeActivity
    lateinit var boardActivity: BoardActivity
    lateinit var aiCuratorActivity: AiCuratorActivity
    lateinit var communityActivity: CommunityActivity
    lateinit var myInfoActivity: MyInfoActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tabLayout = findViewById(R.id.tabLayout)
        homeActivity = HomeActivity()
        boardActivity = BoardActivity()
        aiCuratorActivity = AiCuratorActivity()
        communityActivity   = CommunityActivity()
        myInfoActivity = MyInfoActivity()


        replaceView(homeActivity)

        tabLayout.getTabAt(0)?.select()

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                println("tab id ${tab?.position}")
                when(tab?.position) {
                    0-> {
                        replaceView(homeActivity)
                    }
                    1-> {
                        replaceView(boardActivity)
                    }
                    2 -> {
                        replaceView(aiCuratorActivity)
                    }
                    3 -> {
                        replaceView(communityActivity)
                    }
                    4 -> {
                        replaceView(myInfoActivity)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun replaceView(tab: Fragment) {
        var fragment: Fragment = tab
        fragment.let {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, it).commit()
        }
    }
}