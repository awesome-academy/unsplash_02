package com.sun.unsplash_02.screen.main

import android.view.Menu
import androidx.fragment.app.Fragment
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseActivity
import com.sun.unsplash_02.screen.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutResourceId() = R.layout.activity_main

    override fun onInit() {
        setSupportActionBar(toolbarMain)
        loadFragment(HomeFragment.newInstance())
    }

    override fun onEvent() {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.frameContainer, fragment)
            addToBackStack(null)
            commit()
        }
    }
}
