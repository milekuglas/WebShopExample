package org.my.service

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}
import org.my.dto.{GetOrderItem, PostOrderItem}
import org.my.repository.OrderItemRepository
@Singleton()
class OrderItemService @Inject()(orderItemRepository: OrderItemRepository)(
    implicit executionContext: ExecutionContext) {

  def getAll(page: Int, size: Int): Future[Seq[Seq[GetOrderItem]]] = {
    orderItemRepository.all(page, size).map(_.map(_.map(GetOrderItem.orderItemToGetOrderItem)))
  }

  def getByShoppingCartId(id: Long): Future[Seq[GetOrderItem]] = {
    orderItemRepository.getByShoppingCartId(id).map(_.map(GetOrderItem.orderItemToGetOrderItem))
  }

  def getByUserId(id: Long, page: Int, size: Int): Future[Seq[Seq[GetOrderItem]]] = {
    orderItemRepository
      .getByUserId(id, page, size)
      .map(_.map(_.map(GetOrderItem.orderItemToGetOrderItem)))
  }

  def insert(orderItem: PostOrderItem): Future[GetOrderItem] = {
    orderItemRepository.insert(orderItem).map(GetOrderItem.orderItemToGetOrderItem)
  }

  def update(id: Long, orderItem: PostOrderItem): Future[Int] = {
    orderItemRepository.update(id, orderItem)
  }

  def delete(id: Long): Future[Int] = {
    orderItemRepository.delete(id)
  }
}
