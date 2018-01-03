package dto

import play.api.libs.json._
import play.api.libs.functional.syntax._
import model.Processor


case class PostProcessor(
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

object PostProcessor {

  implicit val processorReads: Reads[PostProcessor] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "manufacturer").read[String] and
      (JsPath \ "price").read[Double] and
      (JsPath \ "description").read[String] and
      (JsPath \ "productURl").read[String] and
      (JsPath \ "quantity").read[Int] and
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
      newProcessor.name,
      newProcessor.manufacturer,
      newProcessor.price,
      newProcessor.description,
      newProcessor.productURl,
      newProcessor.quantity,
      newProcessor.socket,
      newProcessor.processorType,
      newProcessor.cores,
      newProcessor.cache,
      newProcessor.thread,
      newProcessor.baseFrequency,
      newProcessor.turboFrequency
    )

}
