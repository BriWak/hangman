package services

import com.google.inject.Inject
import connectors.FilmConnector
import models.{Films, Hangman, TVShows}
import repositories.{FilmRepository, GameRepository, TVRepository}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DataService @Inject()(gameRepository: GameRepository, filmRepository: FilmRepository, tvRepository: TVRepository, filmConnector: FilmConnector) {

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
    filmRepository.createFilmList(filmList)
  }

  def getFilms(): Future[Films] = {
    filmRepository.findFilmList().flatMap{
      case Some(films) => Future.successful(films)
      case None => filmConnector.getFilms(25).flatMap(filmList => createFilms(Films(filmList)))
    }
  }

  def createTVShows(TVList: TVShows): Future[TVShows] = {
    tvRepository.createTVList(TVList)
  }

  def getTVShows(): Future[TVShows] = {
    tvRepository.findTVList().flatMap{
      case Some(tvShows) => Future.successful(tvShows)
      case None => filmConnector.getTVShows(25).flatMap(tvList => createTVShows(TVShows(tvList)))
    }
  }

}
