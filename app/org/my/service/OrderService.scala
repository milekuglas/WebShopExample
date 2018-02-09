package org.my.service

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}
import org.my.dto.{GetOrder, PostOrder}
import org.my.repository.OrderRepository
@Singleton()
class OrderService @Inject()(orderRepository: OrderRepository)(
    implicit executionContext: ExecutionContext) {

  def getAll: Future[Seq[GetOrder]] = {
    orderRepository.all().map(_.map(GetOrder.orderToGetOrder))
  }

  def insert(order: PostOrder): Future[GetOrder] = {
    orderRepository.insert(order).map(GetOrder.orderToGetOrder)
  }

}
