package controllers

import connectors.FilmConnector
import controllers.actions.SessionAction
import javax.inject._
import models.{FilmGame, GameRequest, GameType, Hangman, TVGame}
import play.api.mvc._
import services.{DataService, HangmanService}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               sessionAction: SessionAction,
                               hangmanService: HangmanService,
                               dataService: DataService,
                               filmConnector: FilmConnector) extends AbstractController(cc) {

  def home(): Action[AnyContent] = sessionAction { implicit request =>
    Ok(views.html.home())
  }

  def films(): Action[AnyContent] = sessionAction.async { implicit request =>
    val gameId = request.gameId
    for {
      newGame <- hangmanService.getRandomFilm(gameId)
      _ <- dataService.deleteGame(gameId)
      game <- dataService.createGame(newGame)
    } yield displayView(game)
  }

  def tvShows(): Action[AnyContent] = sessionAction.async { implicit request =>
    val gameId = request.gameId
    for {
      newGame <- hangmanService.getRandomTVShow(gameId)
      _ <- dataService.deleteGame(gameId)
      game <- dataService.createGame(newGame)
    } yield displayView(game)
  }

  def guess(letter: Char): Action[AnyContent] = sessionAction.async { implicit request =>
    val gameId = request.gameId
    for {
      game <- dataService.readGame(gameId).map(_.getOrElse(throw new Exception("Error retrieving game")))
      guessedGame = hangmanService.guessLetter(letter, game)
      updatedGame <- dataService.updateGame(gameId, guessedGame)
    } yield displayView(updatedGame)
  }

  private def displayView(game: Hangman): Result = {
    val partialWord = if (game.remainingGuesses <= 0) game.word else game.partialWord
    val gameWord = hangmanService.formatGameWord(partialWord)
    val gameState = hangmanService.checkGameState(game)
    val letters = hangmanService.createLetters('A', 'Z', game)
    Ok(views.html.game(gameWord, gameState, game, letters))
  }
}
