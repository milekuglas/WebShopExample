package org.my.service

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import org.my.dto.{GetProcessorFull, PostProcessorFull}
import org.my.repository.ProcessorFullRepository
@Singleton()
class ProcessorFullService @Inject()(
    processorFullRepository: ProcessorFullRepository)(
    implicit executionContext: ExecutionContext) {

  def getAll: Future[Seq[GetProcessorFull]] = {
    processorFullRepository
      .all()
      .map(_.map(GetProcessorFull.processorFullToGetProcessorFull))
  }

  def get(id: Long): Future[Option[GetProcessorFull]] = {
    processorFullRepository
      .get(id)
      .map(_.map(GetProcessorFull.processorFullToGetProcessorFull))
  }

  def save(processorFull: PostProcessorFull): Future[GetProcessorFull] = {
    processorFullRepository
      .insert(processorFull)
      .map(GetProcessorFull.processorFullToGetProcessorFull)
  }

  def delete(id: Long): Future[Int] = {
    processorFullRepository.delete(id)
  }

  def update(id: Long, processorFull: PostProcessorFull): Future[Int] = {
    processorFullRepository.update(id, processorFull)
  }

  def search(name: Option[String],
             manufacturer: Option[String],
             priceFrom: Option[Double],
             priceTo: Option[Double],
             description: Option[String],
             productUrl: Option[String],
             quantityFrom: Option[Int],
             quantityTo: Option[Int],
             categoryId: Option[Long],
             socket: Option[String],
             processorType: Option[String],
             coresFrom: Option[Int],
             coresTo: Option[Int],
             cacheFrom: Option[Int],
             cacheTo: Option[Int],
             threadFrom: Option[Int],
             threadTo: Option[Int],
             baseFrequencyFrom: Option[Double],
             baseFrequencyTo: Option[Double],
             turboFrequencyFrom: Option[Double],
             turboFrequencyTo: Option[Double]): Future[Seq[GetProcessorFull]] = {

    processorFullRepository.search(name, manufacturer, priceFrom, priceTo, description, productUrl, quantityFrom, quantityTo, categoryId,
      socket, processorType, coresFrom, coresTo, cacheFrom, cacheTo, threadFrom, threadTo, baseFrequencyFrom, baseFrequencyTo, turboFrequencyFrom, turboFrequencyTo).map(_.map(GetProcessorFull.processorFullToGetProcessorFull))

  }
}
