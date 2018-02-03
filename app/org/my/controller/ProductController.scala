package org.my.controller

import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import org.my.service.ProductService

import scala.concurrent.ExecutionContext

@Singleton()
class ProductController @Inject()(
    cc: ControllerComponents,
    productService: ProductService)(implicit executionContext: ExecutionContext)
    extends AbstractController(cc) {

  def getAll = Action.async {
    productService.getAll map (result => Ok(Json.toJson(result)))
  }

  def get(id: Long) = Action.async {
    productService.get(id) map {
      case Some(result) =>
        Ok(Json.toJson(result))
      case None => NotFound

    }
  }
}
