package model

case class Product (
  id: Long,
  name: String,
  manufacturer: String,
  price: Double,
  description: String,
  productURl: String,
  quantity: Int,
  categoryId: Long
)