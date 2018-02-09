package org.my.service

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import org.my.dto.GetProduct
import org.my.repository.ProductRepository
@Singleton()
class ProductService @Inject()(productRepository: ProductRepository)(
    implicit executionContext: ExecutionContext) {

  def get(id: Long): Future[Option[GetProduct]] = {
    productRepository.get(id).map(_.map(GetProduct.productToGetProduct))
  }

  def search(page: Int,
             size: Int,
             name: Option[String],
             manufacturer: Option[String],
             priceFrom: Option[Double],
             priceTo: Option[Double],
             description: Option[String],
             productUrl: Option[String],
             quantityFrom: Option[Int],
             quantityTo: Option[Int],
             categoryId: Option[Long]): Future[Seq[GetProduct]] = {

    productRepository
      .search(page,
              size,
              name,
              manufacturer,
              priceFrom,
              priceTo,
              description,
              productUrl,
              quantityFrom,
              quantityTo,
              categoryId)
      .map(_.map(GetProduct.productToGetProduct))
  }
}
