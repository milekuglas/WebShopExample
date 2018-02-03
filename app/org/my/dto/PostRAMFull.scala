package org.my.dto

import play.api.libs.json._
import org.my.model.RAMFull

case class PostRAMFull(
    ram: PostRAM,
    product: PostProduct
)

object PostRAMFull {

  implicit val ramFullReads = Json.reads[PostRAMFull]

  implicit def postRAMFullToRAM(postRAMFull: PostRAMFull): RAMFull =
    new RAMFull(
      postRAMFull.ram,
      postRAMFull.product,
      null
    )

}
