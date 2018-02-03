package org.my.dto

import play.api.libs.json._
import org.my.model.RAM

case class GetRAM(productId: Long,
                  ram_type: String,
                  maxFrequency: Double,
                  capacity: Int,
                  voltage: Double,
                  latency: Int)

object GetRAM {

  implicit val ramWrites = Json.writes[GetRAM]

  implicit def ramToGetRAM(ram: RAM): GetRAM =
    new GetRAM(ram.productId,
               ram.ram_type,
               ram.maxFrequency,
               ram.capacity,
               ram.voltage,
               ram.latency)
}
