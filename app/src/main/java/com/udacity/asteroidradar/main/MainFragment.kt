package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        )[MainViewModel::class.java]

    }
    lateinit var asteroidAdapter: AsteroidAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        asteroidAdapter = AsteroidAdapter(AsteroidClick {
            val action = MainFragmentDirections.actionShowDetail(it)
            findNavController().navigate(action)
        })

        binding.viewModel = viewModel
        viewModel.picOfTheDay.observe(viewLifecycleOwner) {
            if ((it?.mediaType?.equals("image") ?: false) == true) {
                Picasso.with(requireContext()).load(it.url).into(binding.activityMainImageOfTheDay);
            }
        }
        binding.asteroidRecycler.apply {
            adapter = asteroidAdapter
            layoutManager = LinearLayoutManager(context)
        }

        lifecycle.coroutineScope.launch {

            viewModel.setAsteroidPeriod(Period.ALL)
                .collect {
                    asteroidAdapter.submitList(it)
                    binding.statusLoadingWheel.visibility = GONE
                }
        }


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lifecycle.coroutineScope.launch {
            when (item.itemId) {
                R.id.show_all_menu -> viewModel.setAsteroidPeriod(Period.ALL)
                R.id.show_week_menu -> viewModel.setAsteroidPeriod(Period.WEEK)
                R.id.show_today_menu -> viewModel.setAsteroidPeriod(Period.DAY)
                else -> throw Exception()
            }.collect {
                asteroidAdapter.submitList(it)
            }
        }
        return true
    }
}
