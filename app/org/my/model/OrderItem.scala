package org.my.model

case class OrderItem(id: Long,
                     quantity: Long,
                     price: Double,
                     productId: Long,
                     shoppingCartId: Option[Long],
                     orderId: Option[Long])
