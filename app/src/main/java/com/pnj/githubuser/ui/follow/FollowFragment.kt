package com.pnj.githubuser.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pnj.githubuser.adapter.UserAdapter
import com.pnj.githubuser.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FollowViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tab = arguments?.getInt(FollowViewModel.TAB)
        val username = arguments?.getString(FollowViewModel.USERNAME)

        viewModel.result.observe(viewLifecycleOwner) {
            val layoutManager = LinearLayoutManager(context)
            val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
            binding.recyclerViewUser.apply {
                adapter = UserAdapter(it) {}
                setLayoutManager(layoutManager)
                addItemDecoration(itemDecoration)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        if (tab != null && username != null) {
            viewModel.getFollow(username, tab)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}