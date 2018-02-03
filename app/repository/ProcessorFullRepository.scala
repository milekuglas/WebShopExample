package repository

import slick.jdbc.PostgresProfile.api._
import model.{Category, Processor, ProcessorFull}
import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import repository.table.{CategoryTable, ProcessorTable, ProductTable}
import scala.concurrent.{ExecutionContext, Future}


@Singleton()
class ProcessorFullRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                                        (implicit executionContext: ExecutionContext) {

  private val Processors = TableQuery[ProcessorTable]
  private val Products = TableQuery[ProductTable]
  private val Categories = TableQuery[CategoryTable]

  val db = dbConfigProvider.get.db

  def all(): Future[Seq[ProcessorFull]] = db.run({
    for {
      ((processor, product), category) <- Processors join Products on (_.productId === _.id) join Categories on (_._2.categoryId === _.id)
    } yield (processor, product, category)
  }.result.map(_ map {
    case (processor: Processor, product: Product, category: Category) =>ProcessorFull(processor, product, category)
  })
  )

  def get(id: Long):Future[Option[ProcessorFull]] = db.run({
    for {
      ((processor, product), category) <-Processors.filter(_.productId === id) join Products on (_.productId === _.id) join Categories on (_._2.categoryId === _.id)
    } yield (processor, product, category)
  }.result.head map {
    case (processor: Processor, product: Product, category: Category) => Some(ProcessorFull(processor, product, category))
    case _ => None
  })

  def insert(processorFull: ProcessorFull): Future[ProcessorFull] = db.run((for {
    product <- (Products returning Products) += processorFull.product
    processor <- (Processors returning Processors) += processorFull.processor.copy(productId = product.id)
    category <- Categories.filter(_.id === product.categoryId).result.head
  } yield ProcessorFull(processor, product, category)).transactionally)

  def delete(id: Long): Future[Int] = db.run((for {
    rowsProcessor <- Processors.filter(_.productId === id).delete if rowsProcessor == 1
    rowsProduct <- Products.filter(_.id === id).delete if rowsProduct == 1
  } yield rowsProduct).transactionally)

  def update(id: Long, processorFull: ProcessorFull): Future[Int] = db.run((for {
    rowsProcessor <- Processors.filter(_.productId === id).update(processorFull.processor.copy(id)) if rowsProcessor == 1
    rowsProduct <- Products.filter(_.id === id).update(processorFull.product.copy(id)) if rowsProduct == 1
  } yield rowsProduct).transactionally)
}