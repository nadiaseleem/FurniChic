package com.example.furnichic.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnichic.R
import com.example.furnichic.adapters.BestProductsAdapter
import com.example.furnichic.databinding.FragmentBaseCategoryBinding
import com.example.furnichic.fragments.shopping.HomeFragmentDirections
import com.example.furnichic.util.showBottomNavigationView


open class BaseCategoryFragment: Fragment(R.layout.fragment_base_category) {
private lateinit var binding:FragmentBaseCategoryBinding
    protected val offerAdapter by lazy {  BestProductsAdapter()}
    protected val bestProductsAdapter by lazy { BestProductsAdapter()}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOfferRv()
        setupBestProductsRv()

        binding.rvOfferProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollHorizontally(1)&& dx != 0) {
                    onOfferProductsPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->

            if (v.getChildAt(0).bottom <= (v.height + scrollY)) {
                onBestProductsPagingRequest()
            }
        })

        offerAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        bestProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
    }


    private fun setupBestProductsRv() {
        binding.rvBestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    private fun setupOfferRv() {
        binding.rvOfferProducts.apply {
            layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
        }
    }
    open fun onBestProductsPagingRequest() {

    }
    open fun onOfferProductsPagingRequest() {

    }

    open fun hideLoadingBestProducts() {
        binding.bestProductsProgressBar.visibility = View.GONE
    }

    open fun showLoadingBestProducts() {
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }
    open fun hideLoadingOfferProducts() {
        binding.offerProductsProgressBar.visibility = View.GONE
    }

    open fun showLoadingOfferProducts() {
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }
    override fun onResume() {
        super.onResume()

        showBottomNavigationView()
    }
}