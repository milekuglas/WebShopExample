package org.my.repository

import slick.jdbc.PostgresProfile.api._
import org.my.model.RAM
import org.my.repository.table.RAMTable
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class RAMRepository @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit executionContext: ExecutionContext) {

  val RAMs = TableQuery[RAMTable]

  val db = dbConfigProvider.get.db

  def insert(rams: Seq[RAM]): Future[Unit] =
    db.run(RAMs ++= rams).map(_ => ())

  def create(): Future[Unit] = db.run(RAMs.schema.create)

}
