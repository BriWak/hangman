package repositories

import com.google.inject.Inject
import conf.ApplicationConfig
import models.Hangman
import play.api.libs.json.Json
import play.modules.reactivemongo.{ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json._
import reactivemongo.play.json.collection._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GameRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi,
                               config: ApplicationConfig
                              ) extends ReactiveMongoComponents {

  def collection: Future[JSONCollection] = {
    reactiveMongoApi.database.map(_.collection[JSONCollection]("Game"))
  }

  def create(value: Hangman): Future[Hangman] = {
    collection.flatMap(_.indexesManager.ensure(Index(Seq("createdAt" -> IndexType.Ascending),
      name = Some("createdAt"),
      options = BSONDocument("expireAfterSeconds" -> config.expireAfterSeconds))))
    collection.flatMap(_.insert.one(value)).map(result => if (result.ok) value else throw new RuntimeException("Error creating game"))
  }

  def findByGameId(value: String): Future[Option[Hangman]] = {
    collection.flatMap(_.find(Json.obj("gameId" -> value), Some(Json.obj())).one[Hangman])
  }

  def deleteByGameId(gameId: String): Future[Boolean] = {
    collection.flatMap(_.delete.one(Json.obj("gameId" -> gameId))).map(_.ok)
  }

  def updateGame(gameId: String, newValue: Hangman): Future[Hangman] = {
    collection.flatMap(_.update.one(Json.obj("gameId" -> gameId), newValue, false)).map(result => if (result.ok) newValue else throw new RuntimeException("Error updating game"))
  }

  def flush: Future[Boolean] = {
    collection.flatMap(_.drop(false))
  }
}
