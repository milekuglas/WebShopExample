package org.my.dto

import org.my.model.ProcessorFull
import play.api.libs.json._

case class GetProcessorFull(
    processor: GetProcessor,
    product: GetProduct,
    category: GetCategory
)

object GetProcessorFull {

  implicit val processorFullWrites = Json.writes[GetProcessorFull]

  implicit def processorFullToGetProcessorFull(processorFull: ProcessorFull): GetProcessorFull =
    new GetProcessorFull(
      processorFull.processor,
      processorFull.product,
      processorFull.category
    )
}
