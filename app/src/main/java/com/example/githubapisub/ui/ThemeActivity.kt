package com.example.githubapisub.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubapisub.R
import com.example.githubapisub.databinding.ActivityThemeBinding
import com.example.githubapisub.ui.viewmodel.ThemeViewModel
import com.example.githubapisub.ui.viewmodel.ThemeViewModelFactory

class ThemeActivity : AppCompatActivity() {
    private lateinit var themeBinding: ActivityThemeBinding
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
        themeBinding = ActivityThemeBinding.inflate(layoutInflater)
        setContentView(themeBinding.root)

        with(supportActionBar) {
            this?.setDisplayShowCustomEnabled(true)
            this?.setCustomView(R.layout.actionbar_custom_view)
            this?.setDisplayShowTitleEnabled(false)
            this?.setDisplayShowHomeEnabled(false)
            this?.setDisplayHomeAsUpEnabled(true)
        }

        val pref = SettingPreferences.getInstance(application.dataStore)
        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )

        val switchTheme = themeBinding.switchTheme
        themeViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }

        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveThemeSetting(isChecked)
        }
    }
}