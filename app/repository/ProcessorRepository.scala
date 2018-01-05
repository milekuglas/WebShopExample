package repository

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import model.Processor

class ProcessorRepository(db: Database) {

  lazy val Processors = TableQuery[ProcessorTable]
  lazy val Products = TableQuery[ProductTable]

  private[ProcessorRepository] class ProcessorTable(tag: Tag)
    extends Table[Processor](tag, "PROCESSORS") {

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

  def all(): Future[Seq[Processor]] = db.run(Processors.result)

  def get(id: Long): Future[Option[Processor]] = db.run(Processors.filter(_.productId === id).result.headOption)

  def insert(processor: Processor): Future[Processor] = db.run((Processors returning Processors) += processor)

  def delete(id: Long): Future[Int] = db.run(Processors.filter(_.productId === id).delete)

  def update(id: Long, processor: Processor): Future[Int] = db.run(Processors.filter(_.productId === id).update(processor.copy(id)))

  def create() = db.run(Processors.schema.create)

}
