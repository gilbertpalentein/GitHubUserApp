package com.dicoding.githubuserapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.adapter.ListGitHubUserAdapter
import com.dicoding.githubuserapp.data.database.FavoriteGithubUser
import com.dicoding.githubuserapp.data.model.FavoriteViewModel
import com.dicoding.githubuserapp.data.model.GitHubUser
import com.dicoding.githubuserapp.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: ListGitHubUserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListGitHubUserAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.SetOnItemClickCallback(object : ListGitHubUserAdapter.OnItemClickCallback {
            override fun ItemClicked(userData: GitHubUser) {
                Intent(this@FavoriteActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_DATA_USERNAME, userData.login)
                    it.putExtra(DetailUserActivity.EXTRA_DATA_ID, userData.id)
                    it.putExtra(DetailUserActivity.EXTRA_DATA_TYPE, userData.type)
                    it.putExtra(DetailUserActivity.EXTRA_DATA_AVATAR, userData.avatar_url)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUsers.adapter = adapter
        }

        viewModel.getFavUser()?.observe(this,{
            if(it!=null){
                val listFavUser = mapList(it)
                adapter.setList(listFavUser)
            }
        })
    }

    private fun mapList(users: List<FavoriteGithubUser>): ArrayList<GitHubUser> {
        val listUsers = ArrayList<GitHubUser>()
        for (user in users){
            val userMapped = GitHubUser(
                user.id,
                user.login,
                user.type,
                user.avatarUrl
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}