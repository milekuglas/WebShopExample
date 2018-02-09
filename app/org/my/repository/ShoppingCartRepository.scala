package org.my.repository

import slick.jdbc.PostgresProfile.api._
import org.my.model.ShoppingCart
import org.my.repository.table.ShoppingCartTable
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class ShoppingCartRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit executionContext: ExecutionContext) {

  val ShoppingCartItems = TableQuery[ShoppingCartTable]

  val db = dbConfigProvider.get.db

  def all(): Future[Seq[ShoppingCart]] = db.run(ShoppingCartItems.result)

  def getByUserId(id: Long): Future[Option[ShoppingCart]] =
    db.run(ShoppingCartItems.filter(_.userId === id).result.headOption)

  def insert(shoppingCart: ShoppingCart): Future[ShoppingCart] =
    db.run((ShoppingCartItems returning ShoppingCartItems) += shoppingCart)

  def delete(id: Long): Future[Int] =
    db.run(ShoppingCartItems.filter(_.id === id).delete)

  def create(): Future[Unit] = db.run(ShoppingCartItems.schema.create)
}
