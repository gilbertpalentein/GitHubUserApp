package com.dicoding.githubuserapp.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.adapter.SectionsPagerAdapter
import com.dicoding.githubuserapp.data.model.DetailUserViewModel
import com.dicoding.githubuserapp.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_DATA_USERNAME)
        val id = intent.getIntExtra(EXTRA_DATA_ID, 0)
        val type = intent.getStringExtra(EXTRA_DATA_TYPE)
        val avatarUrl = intent.getStringExtra(EXTRA_DATA_AVATAR)

        val bundle = Bundle()
        bundle.putString(EXTRA_DATA_USERNAME, username)

        viewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)
        if (username != null) {
            showLoading(true)
            viewModel.setGitHubUserDetail(username)
        }

        viewModel.getGitHubUserDetail().observe(this, {
            if (it != null) {
                binding.apply {
                    Glide.with(this@DetailUserActivity).load(it.avatar_url)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(detailAvatar)
                    detailName.text = StringBuilder("Name: ${it.name}")
                    detailUsername.text = StringBuilder("Username: ${it.login}")
                    detailRepo.text = StringBuilder("Repository: ${it.publicRepos}")
                    detailCompany.text = StringBuilder("Company: ${it.company}")
                    detailLocation.text = StringBuilder("Location: ${it.location}")
                    detailFollowers.text = StringBuilder("${it.followers} Followers")
                    detailFollowing.text = StringBuilder("${it.following} Following")
                }
                showLoading(false)
            }
        })

        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUserFav(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleFav.isChecked = true
                        isChecked = true
                    } else {
                        binding.toggleFav.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        binding.toggleFav.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                if (username != null && type != null && avatarUrl != null) {
                    viewModel.addToFavGithubUser(id, username, type, avatarUrl)
                }
            } else {
                viewModel.removeFromFav(id)
            }
            binding.toggleFav.isChecked = isChecked
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        binding.apply {
            detailViewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.detail_tab)
            TabLayoutMediator(tabs, detailViewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

        val actionbar = supportActionBar
        actionbar?.title = "Detail GitHub User"
        actionbar?.setDisplayHomeAsUpEnabled(false)
        actionbar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000000")))
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_DATA_USERNAME = "extra_username"
        const val EXTRA_DATA_ID = "extra_id"
        const val EXTRA_DATA_TYPE = "extra_type"
        const val EXTRA_DATA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}