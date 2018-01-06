package controller

import javax.inject.{Inject, Singleton}

import dto.PostProcessorFull
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import service.ProcessorFullService

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class ProcessorFullController @Inject()(cc: ControllerComponents, processorFullService: ProcessorFullService)
                                       (implicit executionContext: ExecutionContext)
  extends AbstractController(cc) {


  def getAll = Action.async {
    processorFullService.getAll map(processors => Ok(Json.toJson(processors)))
  }

  def get(id: Long) = Action.async {
    processorFullService.get(id) map {
      case Some(processor) => Ok(Json.toJson(processor))
      case None => NotFound
    }
  }

  def add: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalProcessorFull = request.body.validate[PostProcessorFull]
    optionalProcessorFull match {
      case JsSuccess(postProcessorFull: PostProcessorFull, _) =>
        processorFullService.save(postProcessorFull) map { result => Created(Json.toJson(result)) }
      case _: JsError => Future.successful(BadRequest)
    }
  }

  def delete(id: Long) = Action.async {
    processorFullService.delete(id) map {
      case x if x < 1 => NotFound
      case _ => Ok
    }
  }

  def update(id: Long): Action[JsValue] = Action.async(parse.json) { request =>
    val optionalProcessorFull = request.body.validate[PostProcessorFull]
    optionalProcessorFull match {
      case JsSuccess(postProcessorFull: PostProcessorFull, _) =>
        processorFullService.update(id, postProcessorFull) map { result => Ok(Json.toJson(result)) }
      case _:JsError => Future.successful(BadRequest)
    }
  }
}


