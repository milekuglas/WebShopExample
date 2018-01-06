import javax.inject.Inject

import repository._
import model.{Processor, Product}

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration
import scala.util.Try

/** Initial set of data to be imported into the sample application. */
class InitialData @Inject() (productRepository: ProductRepository, processorRepository: ProcessorRepository)
                            (implicit executionContext: ExecutionContext) {

  def createInsert(): Unit = {
    val createInsertData = for {
      _ <- productRepository.create()
      _ <- processorRepository.create()
      count <- productRepository.count() if count == 0
      _ <- productRepository.insert(InitialData.products)
      _ <- processorRepository.insert(InitialData.processors)
    } yield ()

    Try(Await.result(createInsertData, Duration.Inf))
  }

  createInsert()
}

object InitialData {

  def products = Seq(
    Product(1L, "Product1", "Manufacter1", 1000, "Description1", "Product1.html", 100),
    Product(2L, "Product2", "Manufacter2", 2000, "Description2", "Product2.html", 200),
    Product(3L, "Product3", "Manufacter3", 3000, "Description3", "Product3.html", 300),
    Product(4L, "Product4", "Manufacter4", 4000, "Description4", "Product4.html", 400),
    Product(5L, "Product5", "Manufacter5", 5000, "Description5", "Product5.html", 500)
  )

  def processors = Seq(
    Processor(1L, "Socket1", "ProcessorType1", 1, 1, 1, 1.1, 1.1),
    Processor(2L, "Socket2", "ProcessorType2", 2, 2, 2, 2.2, 2.2),
    Processor(3L, "Socket3", "ProcessorType3", 3, 3, 3, 3.3, 3.3),
    Processor(4L, "Socket4", "ProcessorType4", 4, 4, 4, 4.4, 4.4),
    Processor(5L, "Socket5", "ProcessorType5", 5, 5, 5, 5.5, 5.5)
  )
}