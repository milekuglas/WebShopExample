package org.my.dto

import play.api.libs.json._
import org.my.model.Order

case class GetOrder(
    id: Long,
    totalPrice: Double,
    userId: Long
)

object GetOrder {

  implicit val orderWrites = Json.writes[GetOrder]

  implicit def orderToGetOrder(order: Order): GetOrder =
    new GetOrder(
      order.id,
      order.totalPrice,
      order.userId
    )
}
