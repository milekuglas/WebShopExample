package org.my.repository.table

import org.my.model.Processor
import slick.jdbc.PostgresProfile.api._

class ProcessorTable(tag: Tag) extends Table[Processor](tag, "PROCESSORS") {

  val Categories = TableQuery[CategoryTable]

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

  def * =
    (productId,
     socket,
     processorType,
     cores,
     cache,
     thread,
     baseFrequency,
     turboFrequency) <> (Processor.tupled, Processor.unapply)
}

object ProcessorTable {

  lazy val Processors = TableQuery[ProcessorTable]

}
