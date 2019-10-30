package controllers

import connectors.FilmConnector
import controllers.actions.SessionAction
import models.{FilmGame, Hangman, Letter}
import org.joda.time.DateTime
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._
import services.{DataService, HangmanService}

import scala.concurrent.Future

class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  "HomeController" should {

    val mockHangmanService = mock[HangmanService]
    val mockDataService = mock[DataService]

    val game = Hangman("game1", FilmGame(), "fakeUrl", "FILM", "____", List(), 6, alreadyGuessed = false, DateTime.parse("01-01-20"))

    when(mockHangmanService.getRandomFilm(any())).thenReturn(Future.successful(game))
    when(mockDataService.deleteGame(any())).thenReturn(Future.successful(true))
    when(mockDataService.createGame(any())).thenReturn(Future.successful(game))
    when(mockDataService.updateGame(any(), any())).thenReturn(Future.successful(game))
    when(mockDataService.readGame(any())).thenReturn(Future.successful(Some(game)))
    when(mockHangmanService.formatGameWord(any())).thenReturn("FormattedWord")
    when(mockHangmanService.checkGameState(any())).thenReturn("")
    when(mockHangmanService.createLetters(any(), any(), any())).thenReturn(List(Letter('A', "imageUrl", "guessUrl", used = false)))

    "render the game page with a new game from the game method" in {
      val controller = new HomeController(stubControllerComponents(), inject[SessionAction], mockHangmanService, mockDataService, inject[FilmConnector])
      val home = controller.films()(FakeRequest().withSession("UUID" -> "randomUUID"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Hangman")
    }


    "render the game page with an updated game from the guess method" in {
      val controller = new HomeController(stubControllerComponents(), inject[SessionAction], mockHangmanService, mockDataService, inject[FilmConnector])
      val home = controller.guess('A')(FakeRequest().withSession("UUID" -> "randomUUID"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Hangman")
    }
  }

}
