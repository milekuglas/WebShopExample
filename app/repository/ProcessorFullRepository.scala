package repository

import slick.jdbc.PostgresProfile.api._
import model.{ Processor, ProcessorFull }
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}


@Singleton()
class ProcessorFullRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                                        (implicit executionContext: ExecutionContext) extends ProcessorComponent
  with HasDatabaseConfigProvider[JdbcProfile] {

  private val Processors = TableQuery[ProcessorTable]
  private val Products = TableQuery[ProductTable]

  def all(): Future[Seq[ProcessorFull]] = db.run({
    for {
      (processor, product) <- Processors join Products on (_.productId === _.id)
    } yield (processor, product)
  }.result.map(_ map {
    case (processor: Processor, product:Product) =>ProcessorFull(processor, product)
  })
  )

  def get(id: Long):Future[Option[ProcessorFull]] = db.run({
    for {
      (processor, product) <-Processors.filter(_.productId === id) join Products on (_.productId === _.id)
    } yield (processor, product)
  }.result.head map {
    case (processor: Processor,product:Product) => Some(ProcessorFull(processor, product))
    case _ => None
  })

  def insert(processorFull: ProcessorFull): Future[ProcessorFull] = db.run((for {
    product <- (Products returning Products) += processorFull.product
    processor <- (Processors returning Processors) += processorFull.processor.copy(productId = product.id)
  } yield ProcessorFull(processor, product)).transactionally)

  def delete(id: Long): Future[Int] = db.run((for {
    rowsProcessor <- Processors.filter(_.productId === id).delete if rowsProcessor == 1
    rowsProduct <- Products.filter(_.id === id).delete if rowsProduct == 1
  } yield rowsProduct).transactionally)

  def update(id: Long, processorFull: ProcessorFull): Future[Int] = db.run((for {
    rowsProcessor <- Processors.filter(_.productId === id).update(processorFull.processor.copy(id)) if rowsProcessor == 1
    rowsProduct <- Products.filter(_.id === id).update(processorFull.product.copy(id)) if rowsProduct == 1
  } yield rowsProduct).transactionally)
}