package org.my.dto

import play.api.libs.json._
import org.my.model.RAM

case class PostRAM(
    ram_type: String,
    maxFrequency: Double,
    capacity: Int,
    voltage: Double,
    latency: Int
)

object PostRAM {

  implicit val ramReads = Json.reads[PostRAM]

  implicit def postRAMToRAM(postRAM: PostRAM): RAM =
    new RAM(-1,
            postRAM.ram_type,
            postRAM.maxFrequency,
            postRAM.capacity,
            postRAM.voltage,
            postRAM.latency)
}
