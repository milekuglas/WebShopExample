package org.my.model

case class Product(
    id: Long,
    name: String,
    manufacturer: String,
    price: Double,
    description: String,
    productUrl: String,
    quantity: Int,
    categoryId: Long
)
