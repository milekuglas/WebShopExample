import com.google.inject.AbstractModule
import controller.ProcessorController
import repository.{ProcessorRepository, ProductRepository}
import service.ProcessorService

class Module extends AbstractModule {
  protected def configure: Unit = {
    bind(classOf[InitialData]).asEagerSingleton()
    bind(classOf[ProcessorService])asEagerSingleton()
    bind(classOf[ProcessorRepository])asEagerSingleton()
    bind(classOf[ProductRepository])asEagerSingleton()
  }
}
