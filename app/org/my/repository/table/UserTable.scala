package org.my.repository.table

import org.my.model.User
import slick.jdbc.PostgresProfile.api._

class UserTable(tag: Tag) extends Table[User](tag, "USERS") {

  def id        = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def username  = column[String]("username")
  def password  = column[String]("password")
  def address   = column[String]("address")
  def phone     = column[String]("phone")
  def email     = column[String]("email")
  def money     = column[Double]("money")
  def telephone = column[String]("telephone")

  def * =
    (id, username, password, address, phone, email, money, telephone) <> (User.tupled, User.unapply)
}

object UserTable {

  lazy val Users = TableQuery[UserTable]

}
