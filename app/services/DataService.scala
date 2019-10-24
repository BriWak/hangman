package services

import com.google.inject.Inject
import models.Hangman
import repositories.GameRepository

import scala.concurrent.Future

class DataService @Inject()(gameRepository: GameRepository) {

//  var tempdb: Hangman = Hangman("", "", Nil, 6)

  def createGame(newGame: Hangman): Future[Hangman] = {
//    tempdb = newGame
//    Future.successful(newGame)
    gameRepository.create(newGame)
  }

  def readGame(id: String): Future[Option[Hangman]] = {
//    Future.successful(Some(tempdb))
    gameRepository.findByGameId(id)
  }

  def updateGame(id: String, newBoard: Hangman): Future[Hangman] = {
//    tempdb = newBoard
//    Future.successful(newBoard)
    gameRepository.updateGame(id, newBoard)
  }

  def deleteGame(id: String): Future[Boolean] = {
//    Future.successful(true)
    gameRepository.deleteByGameId(id)
  }
}
