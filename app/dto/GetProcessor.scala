package dto

import play.api.libs.json._
import model.Processor


case class GetProcessor(
                         productId: Long,
                         socket: String,
                         processorType: String,
                         cores: Int,
                         cache: Int,
                         thread: Int,
                         baseFrequency: Double,
                         turboFrequency: Double
                       )

object GetProcessor {

  implicit val processorWrites = Json.writes[GetProcessor]

  implicit def processorToGetProcessor(processor: Processor): GetProcessor =
    new GetProcessor(
      processor.productId,
      processor.socket,
      processor.processorType,
      processor.cores,
      processor.cache,
      processor.thread,
      processor.baseFrequency,
      processor.turboFrequency
    )

}
