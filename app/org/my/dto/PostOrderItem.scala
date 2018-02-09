package org.my.dto

import play.api.libs.json._
import org.my.model.OrderItem

case class PostOrderItem(
    quantity: Long,
    price: Double,
    productId: Long,
    shoppingCartId: Option[Long],
    orderId: Option[Long]
)

object PostOrderItem {

  implicit val orderItemReads = Json.reads[PostOrderItem]

  implicit def postOrderItemToOrderItem(postOrderItem: PostOrderItem): OrderItem =
    new OrderItem(
      -1,
      postOrderItem.quantity,
      postOrderItem.price,
      postOrderItem.productId,
      postOrderItem.shoppingCartId,
      postOrderItem.orderId
    )
}
