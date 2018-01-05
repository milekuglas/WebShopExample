import controller.ProcessorController
import play.api.ApplicationLoader
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.db.slick.DbName
import play.api.db.slick.SlickComponents
import play.filters.HttpFiltersComponents
import repository.{ProcessorRepository, ProductRepository}
import router.Routes
import service.ProcessorService
import slick.jdbc.JdbcProfile

class MainApplicationLoader extends ApplicationLoader {
  def load(context: Context) = {
    new ApplicationComponents(context).application
  }
}

class ApplicationComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents
    with SlickComponents {

  lazy val dbConfig = slickApi.dbConfig[JdbcProfile](DbName("default"))

  lazy val processorRepository = new ProcessorRepository(dbConfig.db)
  processorRepository.create

  lazy val productRepository = new ProductRepository(dbConfig.db)
  productRepository.create

  lazy val processorService = new ProcessorService(processorRepository)

  lazy val processorController = new ProcessorController(controllerComponents, processorService)

  lazy val router = new Routes(httpErrorHandler, processorController)
}