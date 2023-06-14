package com.example.kelineyt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furnichic.data.Category
import com.example.furnichic.data.Product
import com.example.furnichic.util.PagingInfo
import com.example.furnichic.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModel() {

    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProducts = _offerProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts = _bestProducts.asStateFlow()
    private val pagingInfo = PagingInfo()
    private val pagingInfo2 = PagingInfo()


    init {
        fetchOfferProducts()
        fetchBestProducts()
    }

    fun fetchOfferProducts() {
        if (!pagingInfo2.isPagingEnd) {

            viewModelScope.launch {
                _offerProducts.emit(Resource.Loading())
            }
            firestore.collection("Products").whereEqualTo("category", category.category)
                .whereNotEqualTo("offerPercentage", null).limit(pagingInfo2.bestProductsPage * 5).get()
                .addOnSuccessListener {
                    val products = it.toObjects(Product::class.java)
                    pagingInfo2.isPagingEnd = products == pagingInfo2.oldBestProducts
                    pagingInfo2.oldBestProducts = products
                    viewModelScope.launch {
                        _offerProducts.emit(Resource.Success(products))
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _offerProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }

    fun fetchBestProducts() {
        if (!pagingInfo.isPagingEnd) {
            viewModelScope.launch {
                _bestProducts.emit(Resource.Loading())
            }
            firestore.collection("Products").whereEqualTo("category", category.category)
                .whereEqualTo("offerPercentage", null).limit(pagingInfo.bestProductsPage * 10).get()
                .addOnSuccessListener {
                    val products = it.toObjects(Product::class.java)
                    pagingInfo.isPagingEnd = products == pagingInfo.oldBestProducts
                    pagingInfo.oldBestProducts = products
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Success(products))
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }

}
