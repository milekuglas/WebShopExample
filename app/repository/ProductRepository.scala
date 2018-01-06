package repository

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ Future, ExecutionContext}
import model.Product
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.jdbc.JdbcProfile
import javax.inject.{ Inject, Singleton }

trait ProductComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
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
}

@Singleton()
class ProductRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends ProductComponent
    with HasDatabaseConfigProvider[JdbcProfile] {

  val Products = TableQuery[ProductTable]

  def all(): Future[Seq[Product]] = db.run(Products.result)

  def get(id: Long): Future[Option[Product]] = db.run(Products.filter(_.id === id).result.headOption)

  def insert(products: Seq[Product]): Future[Unit] =
    db.run(Products ++= products).map(_ => ())

  def create(): Future[Unit] = db.run(Products.schema.create)

  def count(): Future[Int] = db.run(Products.length.result)

}
