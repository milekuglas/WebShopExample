package org.my.repository.table

import org.my.model.RAM
import slick.jdbc.PostgresProfile.api._

class RAMTable(tag: Tag) extends Table[RAM](tag, "RAMS") {

  val Categories = TableQuery[CategoryTable]

  val Products = TableQuery[ProductTable]

  def productId = column[Long]("product_id")
  def ram_type = column[String]("ram_type")
  def maxFrequency = column[Double]("maxFrequency")
  def capacity = column[Int]("capacity")
  def voltage = column[Double]("voltage")
  def latency = column[Int]("latency")

  def product = foreignKey("product_FK", productId, Products)(_.id)

  def * =
    (productId, ram_type, maxFrequency, capacity, voltage, latency) <> (RAM.tupled, RAM.unapply)
}

object RAMTable {

  lazy val RAMs = TableQuery[RAMTable]

}
