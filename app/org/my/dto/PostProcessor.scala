package org.my.dto

import play.api.libs.json._
import org.my.model.Processor

case class PostProcessor(
    socket: String,
    processorType: String,
    cores: Int,
    cache: Int,
    thread: Int,
    baseFrequency: Double,
    turboFrequency: Double
)

object PostProcessor {

  implicit val processorReads = Json.reads[PostProcessor]

  implicit def postProcessorToProcessor(postProcessor: PostProcessor): Processor =
    new Processor(
      -1,
      postProcessor.socket,
      postProcessor.processorType,
      postProcessor.cores,
      postProcessor.cache,
      postProcessor.thread,
      postProcessor.baseFrequency,
      postProcessor.turboFrequency
    )
}
