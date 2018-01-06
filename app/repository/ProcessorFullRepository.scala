package repository

import slick.jdbc.PostgresProfile.api._
import model.{Processor, ProcessorFull}
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
      (proc, prod) <- Processors join Products on (_.productId === _.id)
    } yield (proc, prod)
  }.result.map(_ map {
    case (proc: Processor, prod:Product) => ProcessorFull(proc, prod)
  })
  )

  def get(id: Long):Future[Option[ProcessorFull]] = db.run({
    for {
      (proc, prod) <- Processors.filter(_.productId === id) join Products on (_.productId === _.id)
    } yield (proc, prod)
  }.result.head map {
    case (proc: Processor, prod:Product) => Some(ProcessorFull(proc, prod))
    case _ => None
  })

  def insert(processorFull: ProcessorFull): Future[ProcessorFull] = db.run((for {
    product <- (Products returning Products) += processorFull.product
    processor <- (Processors returning Processors) += processorFull.processor.copy(productId = product.id)
  } yield ProcessorFull(processor, product)).transactionally)

  def delete(id: Long): Future[Int] = db.run((for {
    _ <- Processors.filter(_.productId === id).delete
    rows <- Products.filter(_.id === id).delete
  } yield rows).transactionally)

  def update(id: Long, processorFull: ProcessorFull): Future[Int] = db.run((for {
    procRow <- Processors.filter(_.productId === id).update(processorFull.processor.copy(id)) if procRow == 1
    prodRow <- Products.filter(_.id === id).update(processorFull.product.copy(id)) if prodRow == 1
  } yield math.max(procRow, prodRow)).transactionally)
}