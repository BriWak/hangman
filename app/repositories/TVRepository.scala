package repositories

import com.google.inject.Inject
import conf.ApplicationConfig
import models.{Films, TVShows}
import play.api.libs.json.Json
import play.modules.reactivemongo.{ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json._
import reactivemongo.play.json.collection._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TVRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi,
                             config: ApplicationConfig
                              ) extends ReactiveMongoComponents {

  def collection: Future[JSONCollection] = {
    reactiveMongoApi.database.map(_.collection[JSONCollection]("TV"))
  }

  def createTVList(value: TVShows): Future[TVShows] = {
    collection.flatMap(_.indexesManager.ensure(Index(Seq("createdAt" -> IndexType.Ascending),
      name = Some("createdAt"),
      options = BSONDocument("expireAfterSeconds" -> config.expireAfterSeconds))))
    collection.flatMap(_.insert.one(value)).map(result => if (result.ok) value else throw new RuntimeException("Error storing TV Shows"))
  }

  def findTVList(): Future[Option[TVShows]] = {
    collection.flatMap(_.find(Json.obj(), Some(Json.obj())).one[TVShows])
  }

  def flush: Future[Boolean] = {
    collection.flatMap(_.drop(false))
  }
}
