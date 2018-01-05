package dto

import play.api.libs.json._
import play.api.libs.functional.syntax._
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

  implicit val processorReads: Reads[PostProcessor] = (
    (JsPath \ "productId").read[Long] and
      (JsPath \ "socket").read[String] and
      (JsPath \ "processorType").read[String] and
      (JsPath \ "cores").read[Int] and
      (JsPath \ "cache").read[Int] and
      (JsPath \ "thread").read[Int] and
      (JsPath \ "baseFrequency").read[Double] and
      (JsPath \ "turboFrequency").read[Double]
    )(PostProcessor.apply _)

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
