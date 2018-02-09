package org.my.controller

import javax.inject.{Inject, Singleton}
import org.my.dto.PostRAMFull
import org.my.service.RAMFullService
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import scala.concurrent.{ExecutionContext, Future}
@Singleton()
class RAMFullController @Inject()(cc: ControllerComponents, ramFullService: RAMFullService)(
    implicit executionContext: ExecutionContext)
    extends AbstractController(cc) {

  def get(id: Long) = Action.async {
    ramFullService.get(id) map {
      case Some(result) => Ok(Json.toJson(result))
      case None         => NotFound
    }
  }

  def add: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalRAMFull = request.body.validate[PostRAMFull]
    optionalRAMFull match {
      case JsSuccess(postRAMFull: PostRAMFull, _) =>
        ramFullService.save(postRAMFull) map { result =>
          Created(Json.toJson(result))
        }
      case _: JsError => Future.successful(BadRequest)
    }
  }

  def delete(id: Long) = Action.async {
    ramFullService.delete(id) map {
      case x if x < 1 => NotFound
      case _          => Ok
    }
  }

  def update(id: Long): Action[JsValue] = Action.async(parse.json) { request =>
    val optionalRAMFull = request.body.validate[PostRAMFull]
    optionalRAMFull match {
      case JsSuccess(postRAMFull: PostRAMFull, _) =>
        ramFullService.update(id, postRAMFull) map { result =>
          Ok(Json.toJson(result))
        }
      case _: JsError => Future.successful(BadRequest)
    }
  }

  def getAll(page: Int,
             size: Int,
             name: Option[String],
             manufacturer: Option[String],
             priceFrom: Option[Double],
             priceTo: Option[Double],
             description: Option[String],
             productUrl: Option[String],
             quantityFrom: Option[Int],
             quantityTo: Option[Int],
             ram_type: Option[String],
             maxFrequencyFrom: Option[Double],
             maxFrequencyTo: Option[Double],
             capacityFrom: Option[Int],
             capacityTo: Option[Int],
             voltageFrom: Option[Double],
             voltageTo: Option[Double],
             latencyFrom: Option[Int],
             latencyTo: Option[Int],
             categoryId: Option[Long]) = Action.async {

    ramFullService.search(
      page,
      size,
      name,
      manufacturer,
      priceFrom,
      priceTo,
      description,
      productUrl,
      quantityFrom,
      quantityTo,
      ram_type,
      maxFrequencyFrom,
      maxFrequencyTo,
      capacityFrom,
      capacityTo,
      voltageFrom,
      voltageTo,
      latencyFrom,
      latencyTo,
      categoryId
    ) map (result => Ok(Json.toJson(result)))
  }
}
