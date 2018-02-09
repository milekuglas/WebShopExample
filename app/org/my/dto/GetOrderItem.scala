package org.my.dto

import play.api.libs.json._
import org.my.model.OrderItem

case class GetOrderItem(
    id: Long,
    quantity: Long,
    price: Double,
    productId: Long,
    shoppingCartId: Option[Long],
    orderId: Option[Long]
)

object GetOrderItem {

  implicit val orderItemsWrites = Json.writes[GetOrderItem]

  implicit def orderItemToGetOrderItem(orderItem: OrderItem): GetOrderItem =
    new GetOrderItem(
      orderItem.id,
      orderItem.quantity,
      orderItem.price,
      orderItem.productId,
      orderItem.shoppingCartId,
      orderItem.orderId
    )
}
