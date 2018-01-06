package service

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}
import dto.{GetProcessorFull, PostProcessorFull}
import repository.ProcessorFullRepository

@Singleton()
class ProcessorFullService @Inject()(processorFullRepository: ProcessorFullRepository)(implicit executionContext: ExecutionContext) {

    def getAll: Future[Seq[GetProcessorFull]] = {
      processorFullRepository.all().map(_.map(GetProcessorFull.processorFullToGetProcessorFull))
    }

  def get(id: Long): Future[Option[GetProcessorFull]] = {
    processorFullRepository.get(id).map(_.map(GetProcessorFull.processorFullToGetProcessorFull))
  }

  def save(processorFull: PostProcessorFull): Future[GetProcessorFull] = {
    processorFullRepository.insert(processorFull).map(GetProcessorFull.processorFullToGetProcessorFull)
  }

  def delete(id: Long): Future[Int] = {
    processorFullRepository.delete(id)
  }

  def update(id: Long, processorFull: PostProcessorFull): Future[Int] = {
    processorFullRepository.update(id, processorFull)
  }
}