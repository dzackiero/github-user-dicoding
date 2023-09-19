package com.pnj.githubuser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pnj.githubuser.adapter.UserAdapter
import com.pnj.githubuser.databinding.ActivityMainBinding
import com.pnj.githubuser.ui.detail.DetailActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.result.observe(this) { result ->
            val layoutManager = LinearLayoutManager(this)
            val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
            binding.recyclerViewUser.apply {
                setLayoutManager(layoutManager)
                addItemDecoration(itemDecoration)
                adapter = UserAdapter(result) {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra("USERNAME", it.login)
                    startActivity(intent)
                }
            }
        }
        viewModel.isLoading.observe(this) { showLoading(it) }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                viewModel.searchUsers(searchView.text.toString())
                searchBar.text = searchView.text
                searchView.text.isNullOrEmpty()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}