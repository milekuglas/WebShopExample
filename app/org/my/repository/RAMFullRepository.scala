package org.my.repository

import slick.jdbc.PostgresProfile.api._
import org.my.model.{Category, RAM, RAMFull}
import javax.inject.{Inject, Singleton}
import org.my.search.filter.MaybeFilter
import play.api.db.slick.DatabaseConfigProvider
import org.my.repository.table.{CategoryTable, RAMTable, ProductTable}
import scala.concurrent.{ExecutionContext, Future}
@Singleton()
class RAMFullRepository @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider,
    productRepository: ProductRepository)(implicit executionContext: ExecutionContext) {

  private val RAMs       = TableQuery[RAMTable]
  private val Products   = TableQuery[ProductTable]
  private val Categories = TableQuery[CategoryTable]

  val db = dbConfigProvider.get.db

  def all(): Future[Seq[RAMFull]] =
    db.run({
      for {
        ((ram, product), category) <- RAMs join Products on (_.productId === _.id) join Categories on (_._2.categoryId === _.id)
      } yield (ram, product, category)
    }.result.map(_ map {
      case (ram: RAM, product: Product, category: Category) => RAMFull(ram, product, category)
    }))

  def get(id: Long): Future[Option[RAMFull]] =
    db.run({
      for {
        ((ram, product), category) <- RAMs
          .filter(_.productId === id) join Products on (_.productId === _.id) join Categories on (_._2.categoryId === _.id)
      } yield (ram, product, category)
    }.result.head map {
      case (ram: RAM, product: Product, category: Category) => Some(RAMFull(ram, product, category))
      case _                                                => None
    })

  def insert(ramFull: RAMFull): Future[RAMFull] =
    db.run((for {
      product  <- (Products returning Products) += ramFull.product
      ram      <- (RAMs returning RAMs) += ramFull.ram.copy(productId = product.id)
      category <- Categories.filter(_.id === product.categoryId).result.head
    } yield RAMFull(ram, product, category)).transactionally)

  def delete(id: Long): Future[Int] =
    db.run((for {
      rowsRAM     <- RAMs.filter(_.productId === id).delete if rowsRAM == 1
      rowsProduct <- Products.filter(_.id === id).delete if rowsProduct == 1
    } yield rowsProduct).transactionally)

  def update(id: Long, ramFull: RAMFull): Future[Int] =
    db.run((for {
      rowsRAM     <- RAMs.filter(_.productId === id).update(ramFull.ram.copy(id)) if rowsRAM == 1
      rowsProduct <- Products.filter(_.id === id).update(ramFull.product.copy(id))
      if rowsProduct == 1
    } yield rowsProduct).transactionally)

  def search(page: Int,
             size: Int,
             name: Option[String],
             manufacturer: Option[String],
             priceFrom: Option[Double],
             priceTo: Option[Double],
             description: Option[String],
             productUrl: Option[String],
             quantityFrom: Option[Int],
             quantityTo: Option[Int],
             ram_type: Option[String],
             maxFrequencyFrom: Option[Double],
             maxFrequencyTo: Option[Double],
             capacityFrom: Option[Int],
             capacityTo: Option[Int],
             voltageFrom: Option[Double],
             voltageTo: Option[Double],
             latencyFrom: Option[Int],
             latencyTo: Option[Int],
             categoryId: Option[Long]): Future[Seq[RAMFull]] = {

    db.run(
      (for {
        ((ram, product), category) <- find(ram_type,
                                           maxFrequencyFrom,
                                           maxFrequencyTo,
                                           capacityFrom,
                                           capacityTo,
                                           voltageFrom,
                                           voltageTo,
                                           latencyFrom,
                                           latencyTo) join productRepository.find(
          name,
          manufacturer,
          priceFrom,
          priceTo,
          description,
          productUrl,
          quantityFrom,
          quantityTo,
          categoryId) on (_.productId === _.id) join Categories on (_._2.categoryId === _.id)
      } yield (ram, product, category))
        .drop((page - 1) * size)
        .take(size)
        .result
        .map(_ map {
          case (ram: RAM, product: Product, category: Category) =>
            RAMFull(ram, product, category)
        }))
  }

  def find(
      ram_type: Option[String],
      maxFrequencyFrom: Option[Double],
      maxFrequencyTo: Option[Double],
      capacityFrom: Option[Int],
      capacityTo: Option[Int],
      voltageFrom: Option[Double],
      voltageTo: Option[Double],
      latencyFrom: Option[Int],
      latencyTo: Option[Int],
  ) = {

    MaybeFilter(RAMs)
      .filter(ram_type)(value => ram => ram.ram_type.toLowerCase like "%" + value.toLowerCase + "%")
      .filter(maxFrequencyFrom)(value => ram => ram.maxFrequency >= value)
      .filter(maxFrequencyTo)(value => ram => ram.maxFrequency <= value)
      .filter(capacityFrom)(value => ram => ram.capacity >= value)
      .filter(capacityTo)(value => ram => ram.capacity <= value)
      .filter(voltageFrom)(value => ram => ram.voltage >= value)
      .filter(voltageTo)(value => ram => ram.voltage <= value)
      .filter(latencyFrom)(value => ram => ram.latency >= value)
      .filter(latencyTo)(value => ram => ram.latency <= value)
      .query
  }
}
