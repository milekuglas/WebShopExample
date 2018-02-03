package repository

import slick.jdbc.PostgresProfile.api._
import model.Processor
import javax.inject.{Inject, Singleton}

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import repository.table.ProcessorTable
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}


@Singleton()
class ProcessorRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                                    (implicit executionContext: ExecutionContext) {

  val Processors = TableQuery[ProcessorTable]
  val db = dbConfigProvider.get.db


  def insert(processors: Seq[Processor]): Future[Unit] =
    db.run(Processors ++= processors).map(_ => ())

  def create(): Future[Unit] = db.run(Processors.schema.create)

}