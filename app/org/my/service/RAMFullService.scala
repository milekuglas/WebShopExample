package org.my.service

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import org.my.dto.{GetRAMFull, PostRAMFull}
import org.my.repository.RAMFullRepository
@Singleton()
class RAMFullService @Inject()(ramFullRepository: RAMFullRepository)(
    implicit executionContext: ExecutionContext) {

  def getAll: Future[Seq[GetRAMFull]] = {
    ramFullRepository.all().map(_.map(GetRAMFull.ramFullToGetRAMFull))
  }

  def get(id: Long): Future[Option[GetRAMFull]] = {
    ramFullRepository.get(id).map(_.map(GetRAMFull.ramFullToGetRAMFull))
  }

  def save(ramFull: PostRAMFull): Future[GetRAMFull] = {
    ramFullRepository.insert(ramFull).map(GetRAMFull.ramFullToGetRAMFull)
  }

  def delete(id: Long): Future[Int] = {
    ramFullRepository.delete(id)
  }

  def update(id: Long, ramFull: PostRAMFull): Future[Int] = {
    ramFullRepository.update(id, ramFull)
  }

  def search(name: Option[String],
             manufacturer: Option[String],
             priceFrom: Option[Double],
             priceTo: Option[Double],
             description: Option[String],
             productUrl: Option[String],
             quantityFrom: Option[Int],
             quantityTo: Option[Int],
             ram_type: Option[String],
             maxFrequencyFrom: Option[Double],
             maxFrequencyTo: Option[Double],
             capacityFrom: Option[Int],
             capacityTo: Option[Int],
             voltageFrom: Option[Double],
             voltageTo: Option[Double],
             latencyFrom: Option[Int],
             latencyTo: Option[Int],
             categoryId: Option[Long]): Future[Seq[GetRAMFull]] = {

    ramFullRepository
      .search(
        name,
        manufacturer,
        priceFrom,
        priceTo,
        description,
        productUrl,
        quantityFrom,
        quantityTo,
        ram_type,
        maxFrequencyFrom,
        maxFrequencyTo,
        capacityFrom,
        capacityTo,
        voltageFrom,
        voltageTo,
        latencyFrom,
        latencyTo,
        categoryId
      )
      .map(_.map(GetRAMFull.ramFullToGetRAMFull))

  }

}
