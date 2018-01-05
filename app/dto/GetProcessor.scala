package dto

import play.api.libs.json._
import play.api.libs.functional.syntax._
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

  implicit val processorReads: Writes[GetProcessor] = (
    (JsPath \ "productId").write[Long] and
      (JsPath \ "socket").write[String] and
      (JsPath \ "processorType").write[String] and
      (JsPath \ "cores").write[Int] and
      (JsPath \ "cache").write[Int] and
      (JsPath \ "thread").write[Int] and
      (JsPath \ "baseFrequency").write[Double] and
      (JsPath \ "turboFrequency").write[Double]
    )(unlift(GetProcessor.unapply))

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
