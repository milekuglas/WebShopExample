package dto

import play.api.libs.json._
import play.api.libs.functional.syntax._
import model.Processor


case class GetProcessor(
                         id: Long,
                         name: String,
                         manufacturer: String,
                         price: Double,
                         description: String,
                         productURl: String,
                         quantity: Int,
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
    (JsPath \ "id").write[Long] and
      (JsPath \ "name").write[String] and
      (JsPath \ "manufacturer").write[String] and
      (JsPath \ "price").write[Double] and
      (JsPath \ "description").write[String] and
      (JsPath \ "productURl").write[String] and
      (JsPath \ "quantity").write[Int] and
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
      processor.id,
      processor.name,
      processor.manufacturer,
      processor.price,
      processor.description,
      processor.productURl,
      processor.quantity,
      processor.socket,
      processor.processorType,
      processor.cores,
      processor.cache,
      processor.thread,
      processor.baseFrequency,
      processor.turboFrequency
    )

}
