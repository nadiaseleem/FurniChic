package com.example.furnichic.helper

fun Float?.getProductPrice(price: Float): Float{
    //this --> Percentage
    if (this == null)
        return price
    val remainingPricePercentage = (100 - this)/100
    val priceAfterOffer = remainingPricePercentage * price

    return priceAfterOffer
}