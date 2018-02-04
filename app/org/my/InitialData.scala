import javax.inject.Inject

import org.my.repository._
import org.my.model._

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration
import scala.util.Try

class InitialData @Inject()(
    productRepository: ProductRepository,
    processorRepository: ProcessorRepository,
    ramRepository: RAMRepository,
    categoryRepository: CategoryRepository)(implicit executionContext: ExecutionContext) {

  def createDatabase(): Unit = {
    val create = for {
      _ <- categoryRepository.create()
      _ <- productRepository.create()
      _ <- processorRepository.create()
      _ <- ramRepository.create()
    } yield ()

    val insert = for {
      count <- categoryRepository.count() if count == 0
      _     <- categoryRepository.insert(InitialData.categories)
    } yield ()

    Try(Await.result(create, Duration.Inf))
    Try(Await.result(insert, Duration.Inf))
  }

  createDatabase()
}

object InitialData {

  def categories =
    Seq(
      Category(1, "Computer", None),
      Category(2, "ComputerComponent", Some(1)),
      Category(3, "Processor", Some(2)),
      Category(4, "RAM_Memory", Some(2)),
      Category(5, "Intel", Some(3)),
      Category(6, "AMD", Some(3)),
      Category(7, "Kingston", Some(4))
    )

}
