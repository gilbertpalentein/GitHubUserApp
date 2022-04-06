package com.dicoding.githubuserapp.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.adapter.ListGitHubUserAdapter
import com.dicoding.githubuserapp.data.model.GitHubUser
import com.dicoding.githubuserapp.data.model.MainViewModel
import com.dicoding.githubuserapp.data.model.ViewModelFactory
import com.dicoding.githubuserapp.databinding.ActivityHomeBinding
import com.dicoding.githubuserapp.setting.SettingPreferences
import com.google.android.material.switchmaterial.SwitchMaterial

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ListGitHubUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = "GitHub User's"

        adapter = ListGitHubUserAdapter()
        adapter.SetOnItemClickCallback(object : ListGitHubUserAdapter.OnItemClickCallback {
            override fun ItemClicked(userData: GitHubUser) {
                Intent(this@HomeActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_DATA_USERNAME, userData.login)
                    it.putExtra(DetailUserActivity.EXTRA_DATA_ID, userData.id)
                    it.putExtra(DetailUserActivity.EXTRA_DATA_TYPE, userData.type)
                    it.putExtra(DetailUserActivity.EXTRA_DATA_AVATAR, userData.avatar_url)
                    startActivity(it)
                }
            }
        })
        val pref = SettingPreferences.getInstance(dataStore)
        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        viewModel = ViewModelProvider(
            this, ViewModelFactory(pref)
        ).get(MainViewModel::class.java)

        viewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            })

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }

        binding.apply {
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvUsers.layoutManager = GridLayoutManager(this@HomeActivity, 2)
            } else {
                rvUsers.layoutManager = LinearLayoutManager(this@HomeActivity)
            }
            rvUsers.adapter = adapter

            btnSearch.setOnClickListener {
                searchUser()
            }

            etQuery.setOnKeyListener { v, keycode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        viewModel.getSearchGithubuser().observe(this, {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        })
    }

    private fun searchUser() {
        binding.apply {
            val query = etQuery.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.setSearchGithubUser(query)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val actionbar = supportActionBar
        actionbar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000000")))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav_menu -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}