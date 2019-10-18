package controllers

import connectors.ImdbConnector
import javax.inject._
import models.Hangman
import play.api._
import play.api.mvc._
import services.{DataService, HangmanService}

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, hangmanService: HangmanService, dataService: DataService, imdbConnector: ImdbConnector) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    hangmanService.getRandomFilm.flatMap { newGame =>
      dataService.createGame(newGame).map { game =>
        displayView(game)
      }
    }
  }

  def guess(letter: Char): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    dataService.readGame("game1").flatMap { gameOption =>
      gameOption.fold(throw new Exception("Error retrieving game")) { game =>
        val updatedGame = hangmanService.guessLetter(letter, game)
        dataService.updateGame("game1", updatedGame).map { game =>
          displayView(game)
        }
      }
    }
  }

  private def displayView(game: Hangman): Result = {
    val partialWord = if (game.remainingGuesses <= 0) game.word else game.partialWord
      val gameWord = hangmanService.formatGameWord(partialWord)
      val gameState = hangmanService.checkGameState(game)
      Ok(views.html.index(gameWord, gameState, game))
  }
}
