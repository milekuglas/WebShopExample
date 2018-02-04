package org.my.repository.table

import org.my.model.Category
import slick.jdbc.PostgresProfile.api._

class CategoryTable(tag: Tag) extends Table[Category](tag, "CATEGORIES") {

  val Categories = TableQuery[CategoryTable]

  def id            = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name          = column[String]("name")
  def subcategoryId = column[Option[Long]]("subcategory_id")

  def subcategory = foreignKey("subcategory_FK", subcategoryId, Categories)(_.id)

  def * = (id, name, subcategoryId) <> (Category.tupled, Category.unapply)
}

object CategoryTable {

  lazy val Categories = TableQuery[CategoryTable]

}
