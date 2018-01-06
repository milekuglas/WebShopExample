package repository

import slick.jdbc.PostgresProfile.api._
import model.Processor
import javax.inject.{Inject, Singleton}

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait ProcessorComponent extends ProductComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
  class ProcessorTable(tag: Tag)
    extends Table[Processor](tag, "PROCESSORS") {

    val Products = TableQuery[ProductTable]

    def productId = column[Long]("product_id")
    def socket = column[String]("socket")
    def processorType = column[String]("processorType")
    def cores = column[Int]("cores")
    def cache = column[Int]("cache")
    def thread = column[Int]("thread")
    def baseFrequency = column[Double]("baseFrequency")
    def turboFrequency = column[Double]("turboFrequency")

    def product = foreignKey("product_FK", productId, Products)(_.id)

    def * = (productId, socket, processorType, cores, cache, thread, baseFrequency, turboFrequency) <> (Processor.tupled, Processor.unapply)

  }
}

@Singleton()
class ProcessorRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                                    (implicit executionContext: ExecutionContext) extends ProcessorComponent
  with HasDatabaseConfigProvider[JdbcProfile] {

  val Processors = TableQuery[ProcessorTable]

  def insert(processors: Seq[Processor]): Future[Unit] =
    db.run(Processors ++= processors).map(_ => ())

  def create(): Future[Unit] = db.run(Processors.schema.create)

}
