package org.my.dto

import play.api.libs.json._
import org.my.model.Product

case class PostProduct(
    name: String,
    manufacturer: String,
    price: Double,
    description: String,
    productUrl: String,
    quantity: Int,
    categoryId: Long
)

object PostProduct {

  implicit val productReads = Json.reads[PostProduct]

  implicit def postProductToProduct(postProduct: PostProduct): Product =
    new Product(
      -1,
      postProduct.name,
      postProduct.manufacturer,
      postProduct.price,
      postProduct.description,
      postProduct.productUrl,
      postProduct.quantity,
      postProduct.categoryId
    )
}
