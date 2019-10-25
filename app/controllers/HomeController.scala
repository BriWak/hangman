package controllers

import java.util.UUID

import connectors.FilmConnector
import controllers.auth.AuthAction
import javax.inject._
import models.Hangman
import play.api.mvc._
import services.{DataService, HangmanService}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               sessionAction: AuthAction,
                               hangmanService: HangmanService,
                               dataService: DataService,
                               filmConnector: FilmConnector) extends AbstractController(cc) {

  def index(): Action[AnyContent] = sessionAction.async { implicit request: Request[AnyContent] =>
    val uuid = request.session.get("UUID").get
    for {
      newGame <- hangmanService.getRandomFilm(uuid)
      _ <- dataService.deleteGame(uuid)
      game <- dataService.createGame(newGame)
    } yield displayView(game)
  }

  def guess(letter: Char): Action[AnyContent] = sessionAction.async { implicit request: Request[AnyContent] =>
    val uuid = request.session.get("UUID").get
    for {
      game <- dataService.readGame(uuid).map(_.getOrElse(throw new Exception("Error retrieving game")))
      guessedGame = hangmanService.guessLetter(letter, game)
      updatedGame <- dataService.updateGame(uuid, guessedGame)
    } yield displayView(updatedGame)
  }

  def createSession(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
      Redirect(routes.HomeController.index()).addingToSession("UUID" -> UUID.randomUUID().toString)
  }

  private def displayView(game: Hangman): Result = {
    val partialWord = if (game.remainingGuesses <= 0) game.word else game.partialWord
    val gameWord = hangmanService.formatGameWord(partialWord)
    val gameState = hangmanService.checkGameState(game)
    val letters = hangmanService.createLetters('A', 'Z', game)
    Ok(views.html.index(gameWord, gameState, game, letters))
  }
}
