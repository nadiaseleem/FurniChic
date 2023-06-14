package com.example.furnichic.fragments.categories

import CartProduct
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.furnichic.R
import com.example.furnichic.adapters.BestDealsAdapter
import com.example.furnichic.adapters.BestProductsAdapter
import com.example.furnichic.data.Product
import com.example.furnichic.databinding.FragmentMainCategoryBinding
import com.example.furnichic.fragments.shopping.HomeFragmentDirections
import com.example.furnichic.util.Resource
import com.example.furnichic.viewmodel.DetailsViewModel
import com.example.furnichic.viewmodel.MainCategoryViewModel
import com.example.kelineyt.adapters.SpecialProductsAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
private val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {
    private lateinit var binding:FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter:SpecialProductsAdapter
    private lateinit var bestDealsAdapter:BestDealsAdapter
    private lateinit var bestProductsAdapter:BestProductsAdapter
    private val cartViewModel by viewModels<DetailsViewModel>()
    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpecialProductsRv()
        setupBestDealsRv()
        setupBestProductsRv()
        onBtnAddToCartClick()
        observeAddToCart()
        lifecycleScope.launchWhenStarted {
            viewModel.specialProducts.collectLatest{
                when(it) {
                    is Resource.Loading -> {
                        showLoadingBestDeals()
                    }

                    is Resource.Success -> {
                        specialProductsAdapter.differ.submitList(it.data)
                        hideLoadingBestDeals()
                    }

                    is Resource.Error -> {
                        hideLoadingBestDeals()
                        Log.d(TAG,it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                    }

                    else -> Unit

                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestDealsProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoadingBestDeals()
                    }
                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoadingBestDeals()
                    }
                    is Resource.Error -> {
                        hideLoadingBestDeals()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoadingBestProducts()
                    }
                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        hideLoadingBestProducts()


                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        hideLoadingBestProducts()

                    }
                    else -> Unit
                }
            }
        }

        onBestProductsPagingRequest()

        specialProductsAdapter.onClick ={
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        bestDealsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
        bestDealsAdapter.onClickSeeProduct = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        bestProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

    }

    private fun hideLoadingBestProducts() {
        binding.bestProductsProgressbar.visibility = View.GONE
    }

    private fun showLoadingBestProducts() {
        binding.bestProductsProgressbar.visibility = View.VISIBLE
    }

    private fun onBestProductsPagingRequest() {
        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->

            if (v.getChildAt(0).bottom <= (v.height + scrollY)) {
                viewModel.fetchBestProducts()
            }
        })
    }


    private fun setupBestProductsRv() {
        bestProductsAdapter = BestProductsAdapter()
        binding.rvBestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    private fun setupBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }
    private fun hideLoadingBestDeals() {
        binding.mainCategoryProgressbar.visibility = View.GONE
    }

    private fun showLoadingBestDeals() {
        binding.mainCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun setupSpecialProductsRv() {
       specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = specialProductsAdapter
        }
    }

    private fun onBtnAddToCartClick() {
        specialProductsAdapter.onAddToCartClick = {product->

            cartViewModel.addUpdateProductInCart(
                CartProduct(
                    product, 1,
                    product.colors?.get(0), product.sizes?.get(0)
                )
            )
        }
    }




    private fun observeAddToCart() {
        lifecycleScope.launchWhenStarted {
            cartViewModel.addToCart.collectLatest {
                when (it) {

                    is Resource.Success -> {
                        val snackBarPosition = requireActivity().findViewById<CoordinatorLayout>(R.id.snackBar_coordinator)
                        Snackbar.make(snackBarPosition,requireContext().getText(R.string.product_added),2500).show()


                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

}