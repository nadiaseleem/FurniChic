package com.example.furnichic.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.furnichic.R
import com.example.furnichic.adapters.HomeViewPagerAdapter
import com.example.furnichic.databinding.FragmentHomeBinding
import com.example.furnichic.fragments.categories.AccessoryFragment
import com.example.furnichic.fragments.categories.ChairFragment
import com.example.furnichic.fragments.categories.CupboardFragment
import com.example.furnichic.fragments.categories.FurnitureFragment
import com.example.furnichic.fragments.categories.MainCategoryFragment
import com.example.furnichic.fragments.categories.TableFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment:Fragment(R.layout.fragment_home) {
    private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )


        val homeViewPagerAdapter = HomeViewPagerAdapter(categoriesFragments,childFragmentManager,lifecycle)
        binding.viewpagerHome.adapter = homeViewPagerAdapter
        TabLayoutMediator(binding.tabLayout,binding.viewpagerHome){ tab, position ->
            when(position){
                0-> tab.text = "Main"
                1-> tab.text = "chair"
                2-> tab.text = "Cupboard"
                3-> tab.text = "Table"
                4-> tab.text = "Accessory"
                5-> tab.text = "Furniture"
            }

        }.attach()

        binding.viewpagerHome.isUserInputEnabled = false
    }
}