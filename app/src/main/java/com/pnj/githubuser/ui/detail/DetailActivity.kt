package com.pnj.githubuser.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pnj.githubuser.adapter.SectionsPagerAdapter
import com.pnj.githubuser.databinding.ActivityDetailBinding
import com.pnj.githubuser.helper.loadImage
import com.pnj.githubuser.ui.follow.FollowViewModel

class DetailActivity : AppCompatActivity() {
    companion object {
        val TAB_TITLES = listOf(
            "Followers",
            "Following",
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent?.getStringExtra("USERNAME")

        if (username != null) {
            viewModel.getUserDetail(username)
            viewModel.userDetail.observe(this) { userDetail ->
                binding.apply {
                    detailUsername.text = userDetail.username
                    detailName.text = userDetail.name
                    detailImage.loadImage(this@DetailActivity, userDetail.avatarUrl)
                    createViewPager(username, userDetail.followers, userDetail.following)
                }
            }

            viewModel.isLoading.observe(this) { isLoading ->
                binding.progressBar.isVisible = isLoading
            }

            viewModel.errorMessage.observe(this) { singleEvent ->
                singleEvent.getContentIfNotHandled()?.let {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun createViewPager(username: String, followers: Int, following: Int) {
        val sectionPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = when (position) {
                FollowViewModel.FOLLOWERS -> "${TAB_TITLES[position]} ($followers)"
                else -> "${TAB_TITLES[position]}($following)"
            }
        }.attach()

        supportActionBar?.elevation = 0f
    }
}