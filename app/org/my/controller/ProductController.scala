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

  def search(name: Option[String],
             manufacturer: Option[String],
             priceFrom: Option[Double],
             priceTo: Option[Double],
             description: Option[String],
             productUrl: Option[String],
             quantityFrom: Option[Int],
             quantityTo: Option[Int],
             categoryId: Option[Long]) = Action.async {

    productService.search(name, manufacturer, priceFrom, priceTo, description, productUrl, quantityFrom, quantityTo, categoryId) map (result => Ok(Json.toJson(result)))
  }

}
