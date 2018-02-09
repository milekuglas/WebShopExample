package org.my.repository.table

import org.my.model.OrderItem
import slick.jdbc.PostgresProfile.api._

class OrderItemTable(tag: Tag) extends Table[OrderItem](tag, "ORDERITEMS") {

  val Products      = TableQuery[ProductTable]
  val ShoppingCarts = TableQuery[ShoppingCartTable]
  val Orders        = TableQuery[OrderTable]

  def id             = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def quantity       = column[Long]("quantity")
  def price          = column[Double]("price")
  def productId      = column[Long]("product_id")
  def shoppingCartId = column[Option[Long]]("shopping_cart_id")
  def orderId        = column[Option[Long]]("order_id")

  def product      = foreignKey("product_FK", productId, Products)(_.id)
  def shoppingCart = foreignKey("shopping_cart_FK", shoppingCartId, ShoppingCarts)(_.id)
  def order        = foreignKey("order_FK", orderId, Orders)(_.id)

  def * =
    (id, quantity, price, productId, shoppingCartId, orderId) <> (OrderItem.tupled, OrderItem.unapply)
}

object OrderItemTable {

  lazy val OrderItems = TableQuery[OrderItemTable]

}
