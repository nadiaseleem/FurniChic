package com.example.furnichic.fragments.shopping

import CartProduct
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.furnichic.R
import com.example.furnichic.data.Product
import com.example.furnichic.databinding.FragmentProductDetailsBinding
import com.example.furnichic.util.Constants.IMAGES
import com.example.furnichic.util.Resource
import com.example.furnichic.viewmodel.DetailsViewModel
import com.example.kelineyt.adapters.ColorsAdapter
import com.example.kelineyt.adapters.SizesAdapter
import com.example.kelineyt.adapters.ViewPager2ImagesAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.github.vejei.viewpagerindicator.indicator.CircleIndicator
import kotlinx.coroutines.flow.collectLatest

val TAG = "ProductPreviewFragment"
@AndroidEntryPoint
class ProductDetailsFragment: Fragment(R.layout.fragment_product_details) {
    private lateinit var binding:FragmentProductDetailsBinding
    private lateinit var imageAdapter: ViewPager2ImagesAdapter
    private lateinit var colorsAdapter: ColorsAdapter
    private lateinit var sizesAdapter: SizesAdapter
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val viewModel by viewModels<DetailsViewModel>()

    private val args by navArgs<ProductDetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.GONE
        setupImageViewPager()
        setupColorRv()
        setupSizeRv()
        val product = args.product
        setProductInformation(product)
        onBtnAddToCartClick(product)
        observeAddToCart()
        onColorClick()
        onSizeClick()
        onImageCloseClick()


    }
    private fun observeAddToCart() {
        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnAddToCart.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.btnAddToCart.revertAnimation()
                        binding.btnAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                    }

                    is Resource.Error -> {
                        binding.btnAddToCart.stopAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }
    private fun onBtnAddToCartClick(product: Product) {
        binding.btnAddToCart.apply {
            setOnClickListener {

                if (selectedColor==null) {
                    binding.tvColorError.visibility = View.VISIBLE
                    return@setOnClickListener
                }

                if (selectedSize==null) {
                    binding.tvSizeError.visibility = View.VISIBLE
                    return@setOnClickListener
                }
                viewModel.addUpdateProductInCart(CartProduct(product, 1, selectedColor, selectedSize))

                setBackgroundResource(R.color.g_black)
            }
        }
    }

    private fun startLoading() {
        binding.apply {
            btnAddToCart.visibility = View.INVISIBLE
            progressbar.visibility = View.VISIBLE
        }
    }
    private fun stopLoading() {
        binding.apply {
            btnAddToCart.visibility = View.VISIBLE
            progressbar.visibility = View.INVISIBLE
        }
    }

    private fun onColorClick() {
        colorsAdapter.onItemClick = { color ->
            selectedColor = color
            binding.tvColorError.visibility = View.INVISIBLE
        }
    }

    private fun onSizeClick() {
        sizesAdapter.onItemClick = { size ->
            selectedSize = size
            binding.tvSizeError.visibility = View.INVISIBLE

        }
    }

    private fun onImageCloseClick() {
        binding.imgClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    private fun setProductInformation(product: Product) {


        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description

            if (product.colors.isNullOrEmpty())
                tvColor.visibility = View.INVISIBLE
            if (product.sizes.isNullOrEmpty())
                tvSize.visibility = View.INVISIBLE
        }

        imageAdapter.differ.submitList(product.images)
        colorsAdapter.differ.submitList(product.colors)
        sizesAdapter.differ.submitList(product.sizes)



    }

    private fun setupSizeRv() {
        sizesAdapter = SizesAdapter()
        binding.rvSizes.apply {
            adapter = sizesAdapter

            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        }
    }

    private fun setupColorRv() {
        colorsAdapter = ColorsAdapter()
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }
    }

    private fun setupImageViewPager() {
        imageAdapter = ViewPager2ImagesAdapter()
        binding.viewpager2Images.apply {
            adapter = imageAdapter

        }
        binding.circleIndicator.setWithViewPager2(binding.viewpager2Images)
        binding.circleIndicator.itemCount = (args.product.images).size
        binding.circleIndicator.setAnimationMode(CircleIndicator.AnimationMode.SLIDE)
    }
}