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
    hangmanService.getRandomFilm(uuid).flatMap { newGame =>
      dataService.deleteGame(uuid).flatMap { _ =>
        dataService.createGame(newGame).map { game =>
          displayView(game)
        }
      }
    }
  }

  def guess(letter: Char): Action[AnyContent] = sessionAction.async { implicit request: Request[AnyContent] =>
    val uuid = request.session.get("UUID").get
    dataService.readGame(uuid).flatMap { gameOption =>
      gameOption.fold(throw new Exception("Error retrieving game")) { game =>
        val updatedGame = hangmanService.guessLetter(letter, game)
        dataService.updateGame(uuid, updatedGame).map { game =>
          displayView(game)
        }
      }
    }
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
