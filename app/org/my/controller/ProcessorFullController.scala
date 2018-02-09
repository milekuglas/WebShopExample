package org.my.controller

import javax.inject.{Inject, Singleton}
import org.my.dto.PostProcessorFull
import org.my.service.ProcessorFullService
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import scala.concurrent.{ExecutionContext, Future}
@Singleton()
class ProcessorFullController @Inject()(
    cc: ControllerComponents,
    processorFullService: ProcessorFullService)(implicit executionContext: ExecutionContext)
    extends AbstractController(cc) {

  def get(id: Long) = Action.async {
    processorFullService.get(id) map {
      case Some(result) => Ok(Json.toJson(result))
      case None         => NotFound
    }
  }

  def add: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalProcessorFull = request.body.validate[PostProcessorFull]
    optionalProcessorFull match {
      case JsSuccess(postProcessorFull: PostProcessorFull, _) =>
        processorFullService.save(postProcessorFull) map { result =>
          Created(Json.toJson(result))
        }
      case _: JsError => Future.successful(BadRequest)
    }
  }

  def delete(id: Long) = Action.async {
    processorFullService.delete(id) map {
      case x if x < 1 => NotFound
      case _          => Ok
    }
  }

  def update(id: Long): Action[JsValue] = Action.async(parse.json) { request =>
    val optionalProcessorFull = request.body.validate[PostProcessorFull]
    optionalProcessorFull match {
      case JsSuccess(postProcessorFull: PostProcessorFull, _) =>
        processorFullService.update(id, postProcessorFull) map { result =>
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
             socket: Option[String],
             processorType: Option[String],
             coresFrom: Option[Int],
             coresTo: Option[Int],
             cacheFrom: Option[Int],
             cacheTo: Option[Int],
             threadFrom: Option[Int],
             threadTo: Option[Int],
             baseFrequencyFrom: Option[Double],
             baseFrequencyTo: Option[Double],
             turboFrequencyFrom: Option[Double],
             turboFrequencyTo: Option[Double],
             categoryId: Option[Long]) = Action.async {

    processorFullService.search(
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
      socket,
      processorType,
      coresFrom,
      coresTo,
      cacheFrom,
      cacheTo,
      threadFrom,
      threadTo,
      baseFrequencyFrom,
      baseFrequencyTo,
      turboFrequencyFrom,
      turboFrequencyTo,
      categoryId
    ) map (result => Ok(Json.toJson(result)))
  }
}
