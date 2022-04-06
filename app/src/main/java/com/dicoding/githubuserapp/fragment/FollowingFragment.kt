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
import com.dicoding.githubuserapp.data.model.FollowingViewModel
import com.dicoding.githubuserapp.databinding.FragmentFollowBinding

class FollowingFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var followingAdapter: ListGitHubUserAdapter
    private lateinit var username: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_DATA_USERNAME).toString()

        _binding = FragmentFollowBinding.bind(view)

        followingAdapter = ListGitHubUserAdapter()
        followingAdapter.notifyDataSetChanged()

        binding.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.layoutManager = LinearLayoutManager(activity)
            rvUsers.adapter = followingAdapter
        }
        showLoading(true)
        followingViewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)
        followingViewModel.setListUserFollowing(username)
        followingViewModel.getListUserFollowing().observe(viewLifecycleOwner, {
            if (it != null) {
                followingAdapter.setList(it)
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