package org.my.dto

import play.api.libs.json._
import org.my.model.ProcessorFull

case class PostProcessorFull(
    processor: PostProcessor,
    product: PostProduct
)

object PostProcessorFull {

  implicit val processorFullReads = Json.reads[PostProcessorFull]

  implicit def postProcessorFullToProcessor(postProcessorFull: PostProcessorFull): ProcessorFull =
    new ProcessorFull(
      postProcessorFull.processor,
      postProcessorFull.product,
      null
    )

}
