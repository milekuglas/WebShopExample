package org.my.controller

import javax.inject.{Inject, Singleton}

import org.my.dto.PostOrder
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import org.my.service.OrderService

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class OrderController @Inject()(cc: ControllerComponents, orderService: OrderService)(
    implicit executionContext: ExecutionContext)
    extends AbstractController(cc) {

  def getAll = Action.async {
    orderService.getAll map (result => Ok(Json.toJson(result)))
  }

  def add: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalOrder = request.body.validate[PostOrder]
    optionalOrder match {
      case JsSuccess(postOrder: PostOrder, _) =>
        orderService.insert(postOrder) map { result =>
          Created(Json.toJson(result))
        }
      case _: JsError => Future.successful(BadRequest)
    }
  }
}
