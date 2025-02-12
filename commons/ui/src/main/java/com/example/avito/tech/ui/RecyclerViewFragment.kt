package com.example.avito.tech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.example.avito.tech.ui.databinding.FragmentRecyclerViewBinding


abstract class RecyclerViewFragment : Fragment() {
    protected lateinit var binding: FragmentRecyclerViewBinding
    abstract fun searchListener(searchView:SearchView)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search, menu)
                val searchItem = menu.findItem(R.id.menu_item_search)
                val searchView = searchItem.actionView as SearchView
                searchListener(searchView)
            }

            override fun onMenuItemSelected(menuItem: MenuItem) = false

        }, viewLifecycleOwner, Lifecycle.State.STARTED)
        return binding.root
    }
}