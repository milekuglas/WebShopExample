package service

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}
import dto.{GetProduct, PostProduct}
import repository.ProductRepository

@Singleton()
class ProductService @Inject()(productRepository: ProductRepository)(implicit executionContext: ExecutionContext) {

  def getAll: Future[Seq[GetProduct]] = {
    productRepository.all().map(_.map(GetProduct.productToGetProduct))
  }

  def get(id: Long): Future[Option[GetProduct]] = {
    productRepository.get(id).map(_.map(GetProduct.productToGetProduct))
  }

  def save(product: PostProduct): Future[GetProduct] = {
    productRepository.insert(product).map(GetProduct.productToGetProduct)
  }
}
