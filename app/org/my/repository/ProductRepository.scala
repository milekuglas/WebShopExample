package org.my.repository

import slick.jdbc.PostgresProfile.api._
import org.my.model.Product
import org.my.repository.table.ProductTable
import javax.inject.{Inject, Singleton}

import org.my.search.filter.MaybeFilter
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class ProductRepository @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit executionContext: ExecutionContext) {

  val Products = TableQuery[ProductTable]

  val db = dbConfigProvider.get.db

  def all(): Future[Seq[Product]] = db.run(Products.result)

  def get(id: Long): Future[Option[Product]] =
    db.run(Products.filter(_.id === id).result.headOption)

  def insert(products: Seq[Product]): Future[Unit] =
    db.run(Products ++= products).map(_ => ())

  def search(name: Option[String],
             manufacturer: Option[String],
             priceFrom: Option[Double],
             priceTo: Option[Double],
             description: Option[String],
             productUrl: Option[String],
             quantityFrom: Option[Int],
             quantityTo: Option[Int],
             categoryId: Option[Long]): Future[Seq[Product]] =
    db.run(find(name, manufacturer, priceFrom, priceTo, description, productUrl, quantityFrom, quantityTo, categoryId).result)

  def find(name: Option[String],
           manufacturer: Option[String],
           priceFrom: Option[Double],
           priceTo: Option[Double],
           description: Option[String],
           productUrl: Option[String],
           quantityFrom: Option[Int],
           quantityTo: Option[Int],
           categoryId: Option[Long]) = {

    MaybeFilter(Products)
      .filter(name)(value => product => product.name.toLowerCase like "%"+value.toLowerCase+"%")
      .filter(manufacturer)(value => product => product.manufacturer.toLowerCase like "%"+value.toLowerCase+"%")
      .filter(priceFrom)(value => product => product.price >= value)
      .filter(priceTo)(value => product => product.price <= value)
      .filter(description)(value => product => product.description.toLowerCase like "%"+value.toLowerCase+"%")
      .filter(productUrl)(value => product => product.productURl.toLowerCase like "%"+value.toLowerCase+"%")
      .filter(quantityFrom)(value => product => product.quantity >= value)
      .filter(quantityTo)(value => product => product.quantity <= value)
      .filter(categoryId)(value => product => product.categoryId === categoryId)
      .query
  }

  def create(): Future[Unit] = db.run(Products.schema.create)

  def count(): Future[Int] = db.run(Products.length.result)
}
