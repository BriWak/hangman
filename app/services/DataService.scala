package services

import models.Hangman

import scala.concurrent.Future

class DataService {

  var tempdb: Hangman = Hangman("", "", Nil, 6)

  def createGame(newGame: Hangman): Future[Hangman] = {
    tempdb = newGame
    Future.successful(newGame)
//    mongoDbConnector.insert(newGame)
  }

  def readGame(id: String): Future[Option[Hangman]] = {
    Future.successful(Some(tempdb))
    //    mongoDbConnector.fetch(id)
  }

  def updateGame(id: String, newBoard: Hangman): Future[Hangman] = {
    tempdb = newBoard
    Future.successful(newBoard)
//    mongoDbConnector.update(id, newBoard)
  }

  def deleteGame(id: String): Future[Boolean] = {
    Future.successful(true)
//    mongoDbConnector.destroy(id)
  }
}
