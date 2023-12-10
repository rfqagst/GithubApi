package com.example.githubapisub.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapisub.R
import com.example.githubapisub.databinding.ActionbarCustomViewBinding
import com.example.githubapisub.databinding.ActivityMainBinding
import com.example.githubapisub.databinding.ActivityThemeBinding
import com.example.githubapisub.ui.viewmodel.MainViewModel
import com.example.githubapisub.ui.viewmodel.ThemeViewModel
import com.example.githubapisub.ui.viewmodel.ThemeViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var menuBinding: ActionbarCustomViewBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var themeViewModel: ThemeViewModel
    private lateinit var themeBinding: ActivityThemeBinding

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intentTheme = Intent(this, ThemeActivity::class.java)
        val intentFavorite = Intent(this, FavUserActivity::class.java)
        when (item.itemId) {
            R.id.menu_mode -> startActivity(intentTheme)
            R.id.menu_favorite -> startActivity(intentFavorite)
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        menuBinding = ActionbarCustomViewBinding.inflate(layoutInflater)
        themeBinding = ActivityThemeBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        with(supportActionBar) {
            this?.setDisplayShowCustomEnabled(true)
            this?.setCustomView(R.layout.actionbar_custom_view)
            this?.setDisplayShowTitleEnabled(false)
            this?.setDisplayShowHomeEnabled(false)
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.currentQuery.let {
            viewModel.getUserData(it)
        }
        val layoutManager = LinearLayoutManager(this@MainActivity)
        val adapter = ListUserAdapter()
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        with(mainBinding) {
            recyclerView.layoutManager = layoutManager
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.setHasFixedSize(true)

        }

        viewModel.detailUser.observe(this, Observer { userData ->
            adapter.submitList(userData?.items)
            mainBinding.recyclerView.adapter = adapter
        })


        val switchTheme = themeBinding.switchTheme
        val pref = SettingPreferences.getInstance(application.dataStore)
        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )

        themeViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                menuBinding.actionBarLogo.setImageResource(R.drawable.github)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                menuBinding.actionBarLogo.setImageResource(R.drawable.github)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }

        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveThemeSetting(isChecked)
        }

        with(mainBinding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.text = searchView.text
                    val queryResult = searchView.text
                    searchView.hide()
                    viewModel.getUserData("$queryResult")
                    false
                }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        mainBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
