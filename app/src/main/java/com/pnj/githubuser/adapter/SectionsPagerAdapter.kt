package com.pnj.githubuser.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pnj.githubuser.ui.follow.FollowFragment
import com.pnj.githubuser.ui.follow.FollowViewModel

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = FollowFragment()
        val bundle = Bundle()
        bundle.putString(FollowViewModel.USERNAME, username)
        fragment.arguments = when (position) {
            FollowViewModel.FOLLOWERS -> {
                bundle.putInt(FollowViewModel.TAB, FollowViewModel.FOLLOWERS)
                bundle
            }

            else -> {
                bundle.putInt(FollowViewModel.TAB, FollowViewModel.FOLLOWING)
                bundle
            }
        }

        return fragment
    }

}