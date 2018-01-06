package controller

import javax.inject.{Inject, Singleton}

import dto.{GetProduct, PostProduct}
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import service.{ProcessorService, ProductService}

import scala.concurrent.{ExecutionContext, Future}

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

  def add: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalProduct = request.body.validate[PostProduct]
    optionalProduct match {
      case JsSuccess(postProduct: PostProduct, _) =>
        productService.save(postProduct) map { result => Created(Json.toJson(result)) }
      case _: JsError => Future.successful(BadRequest)
    }
  }
}

