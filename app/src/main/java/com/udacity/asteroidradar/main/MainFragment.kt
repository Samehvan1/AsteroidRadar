package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapters.AstroAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.selectedAstro.observe(viewLifecycleOwner, Observer {
            if (it!=null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.showAstroidDetailCompleted()
            }
        })
        binding.asteroidRecycler.adapter = AstroAdapter(
            AstroAdapter.OnClickListener {
                viewModel.showAstroidDetail(it)
            })
        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_saved_astros->viewModel.menuItemClicked("saved")
            R.id.show_week_astros->viewModel.menuItemClicked("week")
            R.id.show_toady_astros->viewModel.menuItemClicked("today")
        }
        return true
    }
}
