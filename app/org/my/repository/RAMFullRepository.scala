package org.my.repository

import slick.jdbc.PostgresProfile.api._
import org.my.model.{Category, RAM, RAMFull}
import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import org.my.repository.table.{CategoryTable, RAMTable, ProductTable}
import scala.concurrent.{ExecutionContext, Future}
@Singleton()
class RAMFullRepository @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit executionContext: ExecutionContext) {

  private val RAMs = TableQuery[RAMTable]
  private val Products = TableQuery[ProductTable]
  private val Categories = TableQuery[CategoryTable]

  val db = dbConfigProvider.get.db

  def all(): Future[Seq[RAMFull]] =
    db.run({
      for {
        ((ram, product), category) <- RAMs join Products on (_.productId === _.id) join Categories on (_._2.categoryId === _.id)
      } yield (ram, product, category)
    }.result.map(_ map {
      case (ram: RAM, product: Product, category: Category) =>
        RAMFull(ram, product, category)
    }))

  def get(id: Long): Future[Option[RAMFull]] =
    db.run({
      for {
        ((ram, product), category) <- RAMs
          .filter(_.productId === id) join Products on (_.productId === _.id) join Categories on (_._2.categoryId === _.id)
      } yield (ram, product, category)
    }.result.head map {
      case (ram: RAM, product: Product, category: Category) =>
        Some(RAMFull(ram, product, category))
      case _ => None
    })

  def insert(ramFull: RAMFull): Future[RAMFull] =
    db.run((for {
      product <- (Products returning Products) += ramFull.product
      ram <- (RAMs returning RAMs) += ramFull.ram.copy(productId = product.id)
      category <- Categories.filter(_.id === product.categoryId).result.head
    } yield RAMFull(ram, product, category)).transactionally)

  def delete(id: Long): Future[Int] =
    db.run((for {
      rowsRAM <- RAMs.filter(_.productId === id).delete if rowsRAM == 1
      rowsProduct <- Products.filter(_.id === id).delete if rowsProduct == 1
    } yield rowsProduct).transactionally)

  def update(id: Long, ramFull: RAMFull): Future[Int] =
    db.run((for {
      rowsRAM <- RAMs.filter(_.productId === id).update(ramFull.ram.copy(id))
      if rowsRAM == 1
      rowsProduct <- Products
        .filter(_.id === id)
        .update(ramFull.product.copy(id)) if rowsProduct == 1
    } yield rowsProduct).transactionally)
}
