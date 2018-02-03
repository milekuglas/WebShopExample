package dto

import play.api.libs.json._
import model.Category

case class GetCategory(
    id: Long,
    name: String,
    subcategoryId: Option[Long]
)

object GetCategory {

  implicit val categoryWrites = Json.writes[GetCategory]

  implicit def categoryToGetCategory(category: Category): GetCategory =
    new GetCategory(
      category.id,
      category.name,
      category.subcategoryId
    )
}
