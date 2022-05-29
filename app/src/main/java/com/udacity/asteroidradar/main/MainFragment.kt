package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        //The ViewModelProviders (plural) is deprecated.
        //ViewModelProviders.of(this, DevByteViewModel.Factory(activity.application)).get(DevByteViewModel::class.java)
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        )[MainViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val asteroidAdapter = AsteroidAdapter(AsteroidClick {
            val action = MainFragmentDirections.actionShowDetail(it)
            findNavController().navigate(action)
        })

        binding.viewModel = viewModel
        viewModel.picOfTheDay.observe(viewLifecycleOwner) {
            if ((it?.mediaType?.equals("image") ?: false) == true) {
                Picasso.with(requireContext()).load(it.url).into(binding.activityMainImageOfTheDay);
            }
        }

        viewModel.asteroids.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                asteroidAdapter.asteroids = it
                binding.asteroidRecycler.apply {
                    adapter = asteroidAdapter
                    layoutManager = LinearLayoutManager(context)
                }
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
        return true
    }
}
