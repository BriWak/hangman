package services

import com.google.inject.Inject
import connectors.FilmConnector
import models.{Films, Hangman, TVShows}
import repositories.{DataRepository, GameRepository}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DataService @Inject()(gameRepository: GameRepository, dataRepository: DataRepository, filmConnector: FilmConnector) {

  def createGame(newGame: Hangman): Future[Hangman] = {
    gameRepository.create(newGame)
  }

  def readGame(id: String): Future[Option[Hangman]] = {
    gameRepository.findByGameId(id)
  }

  def updateGame(id: String, newBoard: Hangman): Future[Hangman] = {
    gameRepository.updateGame(id, newBoard)
  }

  def deleteGame(id: String): Future[Boolean] = {
    gameRepository.deleteByGameId(id)
  }

  def createFilms(filmList: Films): Future[Films] = {
    dataRepository.createFilmList(filmList)
  }

  def getFilms(): Future[Films] = {
    dataRepository.findFilmList().flatMap{
      case Some(films) => Future.successful(films)
      case None => filmConnector.getFilms(20).flatMap(filmList => createFilms(Films(filmList)))
    }
  }

  def createTVShows(TVList: TVShows): Future[TVShows] = {
    dataRepository.createTVList(TVList)
  }

  def getTVShows(): Future[TVShows] = {
    dataRepository.findTVList().flatMap{
      case Some(tvShows) => Future.successful(tvShows)
      case None => filmConnector.getTVShows(20).flatMap(tvList => createTVShows(TVShows(tvList)))
    }
  }

}
