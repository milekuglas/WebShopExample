package org.my.dto

import org.my.model.RAMFull
import play.api.libs.json._

case class GetRAMFull(
    ram: GetRAM,
    product: GetProduct,
    category: GetCategory
)

object GetRAMFull {

  implicit val ramFullWrites = Json.writes[GetRAMFull]

  implicit def ramFullToGetRAMFull(ramFull: RAMFull): GetRAMFull =
    new GetRAMFull(
      ramFull.ram,
      ramFull.product,
      ramFull.category
    )
}
