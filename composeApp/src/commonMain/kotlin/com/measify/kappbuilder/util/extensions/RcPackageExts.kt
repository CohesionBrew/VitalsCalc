package com.measify.kappbuilder.util.extensions

import com.revenuecat.purchases.kmp.Package


val Package.productName :String
    get() = storeProduct.title.substringBefore("(")

val Package.productDescription :String
    get() = "${storeProduct.localizedDescription} just for ${storeProduct.price.formatted}"

val Package?.buttonText :String
    get() = if (this == null) "Buy Now" else "Buy Now (${storeProduct.price.formatted})"