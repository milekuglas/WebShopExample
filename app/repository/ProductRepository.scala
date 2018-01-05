package repository

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import model.Product

class ProductTable(tag: Tag)
  extends Table[Product](tag, "PRODUCTS") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def manufacturer = column[String]("manufacturer")
  def price = column[Double]("price")
  def description = column[String]("description")
  def productURl = column[String]("productURl")
  def quantity = column[Int]("quantity")

  def * = (id, name, manufacturer,price, description, productURl, quantity) <> (Product.tupled, Product.unapply)

}

class ProductRepository(db: Database) {

  lazy val Products = TableQuery[ProductTable]

  def all(): Future[Seq[Product]] = db.run(Products.result)

  def get(id: Long): Future[Option[Product]] = db.run(Products.filter(_.id === id).result.headOption)

  def insert(processor: Product): Future[Product] = db.run((Products returning Products) += processor)

  def delete(id: Long): Future[Int] = db.run(Products.filter(_.id === id).delete)

  def update(id: Long, processor: Product): Future[Int] = db.run(Products.filter(_.id === id).update(processor.copy(id)))

  def create() = db.run(Products.schema.create)

}
