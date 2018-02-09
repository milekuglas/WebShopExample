package org.my.repository

import slick.jdbc.PostgresProfile.api._
import org.my.model.User
import org.my.repository.table.UserTable
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit executionContext: ExecutionContext) {

  val Users = TableQuery[UserTable]

  val db = dbConfigProvider.get.db

  def all(): Future[Seq[User]] = db.run(Users.result)

  def get(id: Long): Future[Option[User]] =
    db.run(Users.filter(_.id === id).result.headOption)

  def insert(user: User): Future[User] =
    db.run((Users returning Users) += user)

  def delete(id: Long): Future[Int] =
    db.run(Users.filter(_.id === id).delete)

  def create(): Future[Unit] = db.run(Users.schema.create)
}
