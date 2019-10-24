package services

import com.google.inject.Inject
import models.Hangman
import repositories.GameRepository

import scala.concurrent.Future

class DataService @Inject()(gameRepository: GameRepository) {

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
}
