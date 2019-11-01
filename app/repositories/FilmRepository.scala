package repositories

import com.google.inject.Inject
import conf.ApplicationConfig
import models.{Films, Hangman, TVShows}
import play.api.libs.json.Json
import play.modules.reactivemongo.{ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json._
import reactivemongo.play.json.collection._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FilmRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi,
                               config: ApplicationConfig
                              ) extends ReactiveMongoComponents {

  def collection: Future[JSONCollection] = {
    reactiveMongoApi.database.map(_.collection[JSONCollection]("Film"))
  }

  def createFilmList(value: Films): Future[Films] = {
    collection.flatMap(_.indexesManager.ensure(Index(Seq("createdAt" -> IndexType.Ascending),
      name = Some("createdAt"),
      options = BSONDocument("expireAfterSeconds" -> config.expireAfterSeconds))))
    collection.flatMap(_.insert.one(value)).map(result => if (result.ok) value else throw new RuntimeException("Error storing films"))
  }

  def findFilmList(): Future[Option[Films]] = {
    collection.flatMap(_.find(Json.obj(), Some(Json.obj())).one[Films])
  }

  def flush: Future[Boolean] = {
    collection.flatMap(_.drop(false))
  }
}
