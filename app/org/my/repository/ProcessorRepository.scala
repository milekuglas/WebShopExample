package org.my.repository

import slick.jdbc.PostgresProfile.api._
import org.my.model.Processor
import org.my.repository.table.ProcessorTable
import org.my.search.filter.MaybeFilter
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class ProcessorRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit executionContext: ExecutionContext) {

  val Processors = TableQuery[ProcessorTable]

  val db = dbConfigProvider.get.db

  def insert(processors: Seq[Processor]): Future[Unit] =
    db.run(Processors ++= processors).map(_ => ())

  def create(): Future[Unit] = db.run(Processors.schema.create)

}
