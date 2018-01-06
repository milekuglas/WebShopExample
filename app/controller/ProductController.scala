package controller

import javax.inject.{Inject, Singleton}

import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import service.ProductService

import scala.concurrent.ExecutionContext

@Singleton()
class ProductController @Inject()(cc: ControllerComponents, productService: ProductService)
                                   (implicit executionContext: ExecutionContext)
  extends AbstractController(cc) {


  def getAll = Action.async {
    productService.getAll map(products => Ok(Json.toJson(products)))
  }

  def get(id: Long) = Action.async {
    productService.get(id) map {
      case Some(product) =>
        Ok(Json.toJson(product))
      case None => NotFound

    }
  }
}

