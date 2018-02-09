package org.my.controller

import javax.inject.{Inject, Singleton}

import org.my.dto.PostCategory
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import org.my.service.CategoryService

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class CategoryController @Inject()(cc: ControllerComponents, categoryService: CategoryService)(
    implicit executionContext: ExecutionContext)
    extends AbstractController(cc) {

  def getAll = Action.async {
    categoryService.getAll map (result => Ok(Json.toJson(result)))
  }

  def get(id: Long) = Action.async {
    categoryService.get(id) map {
      case Some(result) => Ok(Json.toJson(result))
      case None         => NotFound
    }
  }

  def add: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalCategory = request.body.validate[PostCategory]
    optionalCategory match {
      case JsSuccess(postCategory: PostCategory, _) =>
        categoryService.save(postCategory) map { result =>
          Created(Json.toJson(result))
        }
      case _: JsError => Future.successful(BadRequest)
    }
  }

  def delete(id: Long) = Action.async {
    categoryService.delete(id) map {
      case x if x < 1 => NotFound
      case _          => Ok
    }
  }

  def update(id: Long): Action[JsValue] = Action.async(parse.json) { request =>
    val optionalCategory = request.body.validate[PostCategory]
    optionalCategory match {
      case JsSuccess(postCategory: PostCategory, _) =>
        categoryService.update(id, postCategory) map { result =>
          Ok(Json.toJson(result))
        }
      case _: JsError => Future.successful(BadRequest)
    }
  }

  def getAllSubcategories(id: Long) = Action.async {
    categoryService.getAllSubcategories(id) map (result => Ok(Json.toJson(result)))
  }
}
