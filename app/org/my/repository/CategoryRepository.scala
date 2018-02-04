package org.my.repository

import slick.jdbc.PostgresProfile.api._
import org.my.model.Category
import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import org.my.repository.table.CategoryTable

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class CategoryRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit executionContext: ExecutionContext) {

  val Categories = TableQuery[CategoryTable]

  val db = dbConfigProvider.get.db

  def all(): Future[Seq[Category]] = db.run(Categories.result)

  def get(id: Long): Future[Option[Category]] =
    db.run(Categories.filter(_.id === id).result.headOption)

  def insert(categories: Seq[Category]): Future[Unit] =
    db.run(Categories ++= categories).map(_ => ())

  def insert(category: Category): Future[Category] =
    db.run((Categories returning Categories) += category)

  def create(): Future[Unit] = db.run(Categories.schema.create)

  def delete(id: Long): Future[Int] =
    db.run(Categories.filter(_.id === id).delete)

  def update(id: Long, category: Category): Future[Int] =
    db.run(Categories.filter(_.id === id).update(category.copy(id)))

  def count(): Future[Int] = db.run(Categories.length.result)
}
