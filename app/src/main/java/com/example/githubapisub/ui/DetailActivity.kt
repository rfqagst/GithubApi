package com.example.githubapisub.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapisub.R
import com.example.githubapisub.database.FavUserDatabase
import com.example.githubapisub.database.entity.FavUser
import com.example.githubapisub.databinding.ActivityDetailBinding
import com.example.githubapisub.ui.viewmodel.DetailViewModel
import com.example.githubapisub.util.AppExecutors
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout
    private val appExecutors = AppExecutors()
    private var isFavorited: Boolean = false


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intentFavorite = Intent(this, FavUserActivity::class.java)
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.menu_favorite -> startActivity(intentFavorite)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        with(supportActionBar) {
            this?.setDisplayShowCustomEnabled(true)
            this?.setCustomView(R.layout.actionbar_custom_view)
            this?.setDisplayShowTitleEnabled(false)
            this?.setDisplayShowHomeEnabled(false)
            this?.setDisplayHomeAsUpEnabled(true)
        }

        val username: String? = intent.getStringExtra(EXTRA_USER)
        val sectionsPagerAdapter = username?.let { SectionsPagerAdapter(this, it) }

        viewPager = detailBinding.viewPager
        tabs = detailBinding.tabs
        viewPager.adapter = sectionsPagerAdapter
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        if (username != null && username.isNotEmpty()) {
            viewModel.getDetailUser(username)
        } else {
            Toast.makeText(this@DetailActivity, "Username Not Found",Toast.LENGTH_LONG).show()
        }
        viewModel.detailUser.observe(this, Observer { _ ->
            detailBinding.apply {
                tvUsername.text = viewModel.detailUser.value?.login
                tvFollowersDetail.text =
                    "${viewModel.detailUser.value?.followers} ${getString(R.string.tab_text_1)}"
                tvFollowingDetail.text =
                    "${viewModel.detailUser.value?.following} ${getString(R.string.tab_text_2)}"
                tvName.text = viewModel.detailUser.value?.name
                Glide.with(this@DetailActivity)
                    .load(viewModel.detailUser.value?.avatarUrl)
                    .into(detailBinding.imageViewProfile)
            }
        })

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        val dao = FavUserDatabase.getDatabase(this).favUserDao()
        dao.getFavoriteUserByUsername(username ?: "").observe(this, Observer { user ->
            isFavorited = user != null
            updateFabIcon()
        })

        detailBinding.favFab.setOnClickListener {
            val currentUser = viewModel.detailUser.value
            val favUser = FavUser(
                currentUser?.login ?: "Not Found",
                currentUser?.avatarUrl ?: "Not Found"
            )

            if (isFavorited) {
                deleteUserFromFavorites(favUser)
            } else {
                saveUserToFavorites(favUser)
            }
        }
    }

    private fun saveUserToFavorites(favUser: FavUser) {
        val dao = FavUserDatabase.getDatabase(this).favUserDao()
        appExecutors.diskIO.execute {
            dao.insert(favUser)
            runOnUiThread {
                isFavorited = true
                updateFabIcon()
            }
        }
    }

    private fun deleteUserFromFavorites(favUser: FavUser) {
        val dao = FavUserDatabase.getDatabase(this).favUserDao()
        appExecutors.diskIO.execute {
            dao.delete(favUser)
            runOnUiThread {
                isFavorited = false
                updateFabIcon()
            }
        }
    }

    private fun updateFabIcon() {
        if (isFavorited) {
            detailBinding.favFab.setImageResource(R.drawable.favorite) // Change to your favorited icon
        } else {
            detailBinding.favFab.setImageResource(R.drawable.favorite_border) // Change to your not favorited icon
        }
    }

    private fun showLoading(isLoading: Boolean) {
        detailBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
