package org.my.repository

import slick.jdbc.PostgresProfile.api._
import org.my.model.Order
import org.my.repository.table.OrderTable
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class OrderRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit executionContext: ExecutionContext) {

  val Orders = TableQuery[OrderTable]

  val db = dbConfigProvider.get.db

  def all(): Future[Seq[Order]] = db.run(Orders.result)

  def insert(order: Order): Future[Order] =
    db.run((Orders returning Orders) += order)

  def create(): Future[Unit] = db.run(Orders.schema.create)
}
