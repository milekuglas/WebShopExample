package controller

import javax.inject.{Inject, Singleton}

import dto.{GetProcessorFull, PostProcessor}
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import service.ProcessorService

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class ProcessorController @Inject()(cc: ControllerComponents, processorService: ProcessorService)
                    (implicit executionContext: ExecutionContext)
  extends AbstractController(cc) {


  def getAll = Action.async {
    processorService.getAll map(processors => Ok(Json.toJson(processors)))
  }

  def get(id: Long) = Action.async {
    processorService.get(id) map {
      case Some(processor) =>
        Ok(Json.toJson(processor))
      case None => NotFound

    }
  }

  def add: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalProcessor = request.body.validate[PostProcessor]
    optionalProcessor match {
      case JsSuccess(postProcessor: PostProcessor, _) =>
        processorService.save(postProcessor) map { result => Created(Json.toJson(result)) }
      case _: JsError => Future.successful(BadRequest)
    }
  }

  def delete(id: Long) = Action.async {
    processorService.delete(id) map {
      case x if x < 1 => NotFound
      case _ => Ok
    }
  }

  def update(id: Long): Action[JsValue] = Action.async(parse.json) { request =>
    val optionalProcessor = request.body.validate[PostProcessor]
    optionalProcessor match {
      case JsSuccess(postProcessor: PostProcessor, _) =>
        processorService.update(id, postProcessor) map { result => Created(Json.toJson(result)) }
      case _:JsError => Future.successful(BadRequest)
    }
  }
}

