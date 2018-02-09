package org.my.repository

import slick.jdbc.PostgresProfile.api._
import org.my.model.{Category, Processor, ProcessorFull}
import javax.inject.{Inject, Singleton}
import org.my.search.filter.MaybeFilter
import play.api.db.slick.DatabaseConfigProvider
import org.my.repository.table.{CategoryTable, ProcessorTable, ProductTable}
import scala.concurrent.{ExecutionContext, Future}
@Singleton()
class ProcessorFullRepository @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider,
    productRepository: ProductRepository)(implicit executionContext: ExecutionContext) {

  private val Processors = TableQuery[ProcessorTable]
  private val Products   = TableQuery[ProductTable]
  private val Categories = TableQuery[CategoryTable]

  val db = dbConfigProvider.get.db

  def all(): Future[Seq[ProcessorFull]] =
    db.run({
      for {
        ((processor, product), category) <- Processors join Products on (_.productId === _.id) join Categories on (_._2.categoryId === _.id)
      } yield (processor, product, category)
    }.result.map(_ map {
      case (processor: Processor, product: Product, category: Category) =>
        ProcessorFull(processor, product, category)
    }))

  def get(id: Long): Future[Option[ProcessorFull]] =
    db.run({
      for {
        ((processor, product), category) <- Processors
          .filter(_.productId === id) join Products on (_.productId === _.id) join Categories on (_._2.categoryId === _.id)
      } yield (processor, product, category)
    }.result.head map {
      case (processor: Processor, product: Product, category: Category) =>
        Some(ProcessorFull(processor, product, category))
      case _ => None
    })

  def insert(processorFull: ProcessorFull): Future[ProcessorFull] =
    db.run((for {
      product <- (Products returning Products) += processorFull.product
      processor <- (Processors returning Processors) += processorFull.processor.copy(
        productId = product.id)
      category <- Categories.filter(_.id === product.categoryId).result.head
    } yield ProcessorFull(processor, product, category)).transactionally)

  def delete(id: Long): Future[Int] =
    db.run((for {
      rowsProcessor <- Processors.filter(_.productId === id).delete if rowsProcessor == 1
      rowsProduct   <- Products.filter(_.id === id).delete if rowsProduct == 1
    } yield rowsProduct).transactionally)

  def update(id: Long, processorFull: ProcessorFull): Future[Int] =
    db.run((for {
      rowsProcessor <- Processors
        .filter(_.productId === id)
        .update(processorFull.processor.copy(id)) if rowsProcessor == 1
      rowsProduct <- Products.filter(_.id === id).update(processorFull.product.copy(id))
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
             socket: Option[String],
             processorType: Option[String],
             coresFrom: Option[Int],
             coresTo: Option[Int],
             cacheFrom: Option[Int],
             cacheTo: Option[Int],
             threadFrom: Option[Int],
             threadTo: Option[Int],
             baseFrequencyFrom: Option[Double],
             baseFrequencyTo: Option[Double],
             turboFrequencyFrom: Option[Double],
             turboFrequencyTo: Option[Double],
             categoryId: Option[Long]): Future[Seq[ProcessorFull]] = {

    db.run(
      (for {
        ((processor, product), category) <- find(socket,
                                                 processorType,
                                                 coresFrom,
                                                 coresTo,
                                                 cacheFrom,
                                                 cacheTo,
                                                 threadFrom,
                                                 threadTo,
                                                 baseFrequencyFrom,
                                                 baseFrequencyTo,
                                                 turboFrequencyFrom,
                                                 turboFrequencyTo) join productRepository.find(
          name,
          manufacturer,
          priceFrom,
          priceTo,
          description,
          productUrl,
          quantityFrom,
          quantityTo,
          categoryId) on (_.productId === _.id) join Categories on (_._2.categoryId === _.id)
      } yield (processor, product, category))
        .drop((page - 1) * size)
        .take(size)
        .result
        .map(_ map {
          case (processor: Processor, product: Product, category: Category) =>
            ProcessorFull(processor, product, category)
        }))
  }

  def find(
      socket: Option[String],
      processorType: Option[String],
      coresFrom: Option[Int],
      coresTo: Option[Int],
      cacheFrom: Option[Int],
      cacheTo: Option[Int],
      threadFrom: Option[Int],
      threadTo: Option[Int],
      baseFrequencyFrom: Option[Double],
      baseFrequencyTo: Option[Double],
      turboFrequencyFrom: Option[Double],
      turboFrequencyTo: Option[Double],
  ) = {

    MaybeFilter(Processors)
      .filter(socket)(value =>
        processor => processor.socket.toLowerCase like "%" + value.toLowerCase + "%")
      .filter(processorType)(value =>
        processor => processor.processorType.toLowerCase like "%" + value.toLowerCase + "%")
      .filter(coresFrom)(value => processor => processor.cores >= value)
      .filter(coresTo)(value => processor => processor.cores <= value)
      .filter(cacheFrom)(value => processor => processor.cache >= value)
      .filter(cacheTo)(value => processor => processor.cache <= value)
      .filter(threadFrom)(value => processor => processor.thread >= value)
      .filter(threadTo)(value => processor => processor.thread <= value)
      .filter(baseFrequencyFrom)(value => processor => processor.baseFrequency >= value)
      .filter(baseFrequencyTo)(value => processor => processor.baseFrequency <= value)
      .filter(turboFrequencyFrom)(value => processor => processor.turboFrequency >= value)
      .filter(turboFrequencyTo)(value => processor => processor.turboFrequency <= value)
      .query
  }
}
