package dto

import play.api.libs.json._
import model.Processor


case class PostProcessor(
                          productId: Long,
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

  implicit def postProcessorToProcessor(newProcessor: PostProcessor): Processor =
    new Processor(
      -1,
      newProcessor.socket,
      newProcessor.processorType,
      newProcessor.cores,
      newProcessor.cache,
      newProcessor.thread,
      newProcessor.baseFrequency,
      newProcessor.turboFrequency
    )

}
