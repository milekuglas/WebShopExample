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
}
