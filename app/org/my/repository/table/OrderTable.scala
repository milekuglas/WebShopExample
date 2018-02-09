package org.my.repository.table

import org.my.model.Order
import slick.jdbc.PostgresProfile.api._

class OrderTable(tag: Tag) extends Table[Order](tag, "ORDERS") {

  val Users = TableQuery[UserTable]

  def id         = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def totalPrice = column[Double]("total_price")
  def userId     = column[Long]("user_id")

  def user = foreignKey("user_FK", userId, Users)(_.id)

  def * = (id, totalPrice, userId) <> (Order.tupled, Order.unapply)
}

object OrderTable {

  lazy val Orders = TableQuery[OrderTable]

}
