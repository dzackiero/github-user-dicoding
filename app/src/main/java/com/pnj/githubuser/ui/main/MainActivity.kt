package com.pnj.githubuser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pnj.githubuser.R
import com.pnj.githubuser.adapter.UserAdapter
import com.pnj.githubuser.data.model.response.UserItem
import com.pnj.githubuser.databinding.ActivityMainBinding
import com.pnj.githubuser.helper.ViewModelFactory
import com.pnj.githubuser.helper.preferences.SettingPreferences
import com.pnj.githubuser.helper.preferences.datastore
import com.pnj.githubuser.ui.detail.DetailActivity
import com.pnj.githubuser.ui.favorite.FavoriteActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var isDarkModeActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.datastore)

        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]



        viewModel.result.observe(this) { results -> setupRecyclerView(results) }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        viewModel.getThemeSettings().observe(this) { isDarkMode ->
            val item = menu?.findItem(R.id.toggle_theme)
            isDarkModeActive = isDarkMode
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                item?.icon = ContextCompat.getDrawable(this, R.drawable.ic_light_mode)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                item?.icon = ContextCompat.getDrawable(this, R.drawable.ic_dark_mode)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toggle_theme -> {
                viewModel.saveThemeSetting(!isDarkModeActive)
                true
            }

            R.id.goto_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView(results: List<UserItem>) {
        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerViewUser.apply {
            setLayoutManager(layoutManager)
            addItemDecoration(itemDecoration)
            adapter = UserAdapter(results) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("USERNAME", it.login)
                startActivity(intent)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}