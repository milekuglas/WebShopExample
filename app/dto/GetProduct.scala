package dto

import play.api.libs.json._
import model.Product


case class GetProduct(
                       id: Long,
                       name: String,
                       manufacturer: String,
                       price: Double,
                       description: String,
                       productURl: String,
                       quantity: Int
                       )

object GetProduct {

  implicit val productWrites = Json.writes[GetProduct]

  implicit def productToGetProduct(product: Product): GetProduct =
    new GetProduct(
      product.id,
      product.name,
      product.manufacturer,
      product.price,
      product.description,
      product.productURl,
      product.quantity
    )
}
