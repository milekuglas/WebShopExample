package repository

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import model.Processor

class ProcessorRepository(db: Database) {

  lazy val Processors = TableQuery[ProcessorTable]

  private[ProcessorRepository] class ProcessorTable(tag: Tag) extends Table[Processor](tag, "PROCESSORS") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def manufacturer = column[String]("manufacturer")
    def price = column[Double]("price")
    def description = column[String]("description")
    def productURl = column[String]("productURl")
    def quantity = column[Int]("quantity")
    def socket = column[String]("socket")
    def processorType = column[String]("processorType")
    def cores = column[Int]("cores")
    def cache = column[Int]("cache")
    def thread = column[Int]("thread")
    def baseFrequency = column[Double]("baseFrequency")
    def turboFrequency = column[Double]("turboFrequency")

    def * = (id, name, manufacturer,price, description, productURl, quantity, socket,
    processorType, cores, cache, thread, baseFrequency, turboFrequency) <> ((Processor.apply _)tupled, Processor.unapply _)

  }

  def all(): Future[Seq[Processor]] = db.run(Processors.result)

  def get(id: Long): Future[Option[Processor]] = db.run(Processors.filter(_.id === id).result.headOption)

  def insert(processor: Processor): Future[Processor] = db.run((Processors returning Processors) += processor)

  def delete(id: Long): Future[Int] = db.run(Processors.filter(_.id === id).delete)

  def update(id: Long, processor: Processor): Future[Int] = db.run(Processors.filter(_.id === id).update(processor.copy(id)))

  def create() = db.run(Processors.schema.create)

}
