package com.dicoding.githubuserapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.githubuserapp.data.model.GitHubUser
import com.dicoding.githubuserapp.databinding.ItemRowGithubUserBinding

class ListGitHubUserAdapter : RecyclerView.Adapter<ListGitHubUserAdapter.ListViewHolder>() {

    private val listUser = ArrayList<GitHubUser>()
    private var ItemClickCallback: OnItemClickCallback? = null

    fun SetOnItemClickCallback(ItemClickCallback: OnItemClickCallback) {
        this.ItemClickCallback = ItemClickCallback
    }

    inner class ListViewHolder(private var binding: ItemRowGithubUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindingView(githubUser: GitHubUser) {
            binding.root.setOnClickListener {
                ItemClickCallback?.ItemClicked(githubUser)
            }

            binding.apply {
                Glide.with(itemView).load(githubUser.avatar_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(imgItemPhoto)
                tvItemType.text = StringBuilder( "Type: ${githubUser.type}")
                tvItemUserName.text = StringBuilder( "Username: ${githubUser.login}")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(listGitHubUser: ArrayList<GitHubUser>) {
        listUser.clear()
        listUser.addAll(listGitHubUser)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowGithubUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindingView(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun ItemClicked(userData: GitHubUser)
    }
}