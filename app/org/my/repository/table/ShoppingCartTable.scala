package org.my.repository.table

import org.my.model.ShoppingCart
import slick.jdbc.PostgresProfile.api._

class ShoppingCartTable(tag: Tag) extends Table[ShoppingCart](tag, "SHOPPINGCARTS") {

  val Users = TableQuery[UserTable]

  def id     = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Long]("user_id")

  def user = foreignKey("user_FK", userId, Users)(_.id)

  def * = (id, userId) <> (ShoppingCart.tupled, ShoppingCart.unapply)
}

object ShoppingCartTable {

  lazy val ShoppingCarts = TableQuery[ShoppingCartTable]

}
