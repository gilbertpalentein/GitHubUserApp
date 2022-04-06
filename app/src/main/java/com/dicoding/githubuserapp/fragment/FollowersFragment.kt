package com.dicoding.githubuserapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.activity.DetailUserActivity
import com.dicoding.githubuserapp.adapter.ListGitHubUserAdapter
import com.dicoding.githubuserapp.data.model.FollowersViewModel
import com.dicoding.githubuserapp.databinding.FragmentFollowBinding

class FollowersFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var followersAdapter: ListGitHubUserAdapter
    private lateinit var username: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_DATA_USERNAME).toString()

        _binding = FragmentFollowBinding.bind(view)

        followersAdapter = ListGitHubUserAdapter()
        followersAdapter.notifyDataSetChanged()

        binding.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.layoutManager = LinearLayoutManager(activity)
            rvUsers.adapter = followersAdapter
        }
        showLoading(true)
        followersViewModel = ViewModelProvider(this).get(FollowersViewModel::class.java)
        followersViewModel.setListUserFollowers(username)
        followersViewModel.getListUserFollowers().observe(viewLifecycleOwner, {
            if (it != null) {
                followersAdapter.setList(it)
                showLoading(false)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}