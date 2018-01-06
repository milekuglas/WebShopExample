package dto

import play.api.libs.json._
import model.Product


case class PostProduct(
                        name: String,
                        manufacturer: String,
                        price: Double,
                        description: String,
                        productURl: String,
                        quantity: Int
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
      postProduct.productURl,
      postProduct.quantity
    )

}
