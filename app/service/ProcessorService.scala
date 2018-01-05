package service

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}
import dto.{GetProcessor, PostProcessor}
import repository.ProcessorRepository

class ProcessorService @Inject()(processorRepository: ProcessorRepository)(implicit executionContext: ExecutionContext) {


  def getAll: Future[Seq[GetProcessor]] = {
    processorRepository.all().map(_.map(GetProcessor.processorToGetProcessor))
  }

  def get(id: Long): Future[Option[GetProcessor]] = {
    processorRepository.get(id).map(_.map(GetProcessor.processorToGetProcessor))
  }

  def save(processor: PostProcessor): Future[GetProcessor] = {
    processorRepository.insert(processor).map(GetProcessor.processorToGetProcessor)
  }

  def delete(id: Long): Future[Int] = {
    processorRepository.delete(id)
  }

  def update(id: Long, processor: PostProcessor): Future[Int] = {
    processorRepository.update(id, processor)
  }
}
