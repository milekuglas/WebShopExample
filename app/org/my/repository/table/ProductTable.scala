package org.my.repository.table

import org.my.model.Product
import slick.jdbc.PostgresProfile.api._

class ProductTable(tag: Tag) extends Table[Product](tag, "PRODUCTS") {

  val Categories = TableQuery[CategoryTable]

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def manufacturer = column[String]("manufacturer")
  def price = column[Double]("price")
  def description = column[String]("description")
  def productURl = column[String]("productURl")
  def quantity = column[Int]("quantity")

  def categoryId = column[Long]("category_id")

  def category = foreignKey("category_FK", categoryId, Categories)(_.id)

  def * =
    (id,
     name,
     manufacturer,
     price,
     description,
     productURl,
     quantity,
     categoryId) <> (Product.tupled, Product.unapply)
}

object ProductTable {

  lazy val Products = TableQuery[ProductTable]

}


