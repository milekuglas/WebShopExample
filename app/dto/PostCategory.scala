package dto

import play.api.libs.json._
import model.Category

case class PostCategory(
    name: String,
    subcategoryId: Option[Long]
)

object PostCategory {

  implicit val categoryReads = Json.reads[PostCategory]

  implicit def postCategoryToCategory(postCategory: PostCategory): Category =
    new Category(
      -1,
      postCategory.name,
      postCategory.subcategoryId
    )

}
