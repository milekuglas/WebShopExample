package org.my.repository

import slick.jdbc.PostgresProfile.api._
import org.my.model.OrderItem
import org.my.repository.table.{OrderItemTable, OrderTable, UserTable}
import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class OrderItemRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit executionContext: ExecutionContext) {

  val OrderItems = TableQuery[OrderItemTable]
  val Orders     = TableQuery[OrderTable]
  val Users      = TableQuery[UserTable]

  val db = dbConfigProvider.get.db

  def all(page: Int, size: Int): Future[Seq[Seq[OrderItem]]] =
    db.run({
      for {
        orderItem <- OrderItems
      } yield orderItem
    }.result.map({
      case orderItems: Seq[OrderItem] =>
        orderItems.groupBy(_.orderId).drop((page - 1) * size).take(size).valuesIterator.toSeq
    }))

  def getByShoppingCartId(id: Long): Future[Seq[OrderItem]] =
    db.run(OrderItems.filter(_.shoppingCartId === id).result)

  def getByUserId(id: Long, page: Int, size: Int): Future[Seq[Seq[OrderItem]]] =
    db.run({
      for {
        orderItem <- OrderItems
        order     <- Orders if order.id === orderItem.orderId && order.userId === id
      } yield orderItem
    }.result.map({
      case orderItems: Seq[OrderItem] =>
        orderItems.groupBy(_.orderId).drop((page - 1) * size).take(size).valuesIterator.toSeq
    }))

  def insert(orderItem: OrderItem): Future[OrderItem] =
    db.run((OrderItems returning OrderItems) += orderItem)

  def insert(orderItems: Seq[OrderItem]): Future[Unit] =
    db.run(OrderItems ++= orderItems).map(_ => ())

  def update(id: Long, orderItem: OrderItem): Future[Int] =
    db.run(OrderItems.filter(_.id === id).update(orderItem.copy(id)))

  def delete(id: Long): Future[Int] =
    db.run(OrderItems.filter(_.id === id).delete)

  def create(): Future[Unit] = db.run(OrderItems.schema.create)

  def count(): Future[Int] = db.run(OrderItems.length.result)
}
