package com.example.githubapisub.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapisub.R
import com.example.githubapisub.database.FavUserDatabase
import com.example.githubapisub.databinding.ActivityFavoriteBinding

class FavUserActivity : AppCompatActivity() {
    private var favListAdapter: FavListAdapter? = null
    private lateinit var favUserBinding: ActivityFavoriteBinding
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favUserBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favUserBinding.root)

        with(supportActionBar) {
            this?.setDisplayShowCustomEnabled(true)
            this?.setCustomView(R.layout.actionbar_custom_view)
            this?.setDisplayShowTitleEnabled(false)
            this?.setDisplayShowHomeEnabled(false)
            this?.setDisplayHomeAsUpEnabled(true)
        }

        val layoutManager = LinearLayoutManager(this@FavUserActivity)
        favListAdapter = FavListAdapter()
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        with(favUserBinding) {
            recyclerView.layoutManager = layoutManager
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = favListAdapter
        }
        loadFavoriteUsers()
    }

    private fun loadFavoriteUsers() {
        val dao = FavUserDatabase.getDatabase(this).favUserDao()
        dao.getAllFavUsers().observe(this) { favoriteUsersLiveData ->
            favoriteUsersLiveData?.let { favoriteUsers ->
                if (favoriteUsers.isEmpty()) {
                    favUserBinding.recyclerView.visibility = View.GONE
                } else {
                    favUserBinding.recyclerView.visibility = View.VISIBLE
                    favListAdapter?.submitList(favoriteUsers)
                }
            }
        }
    }
}