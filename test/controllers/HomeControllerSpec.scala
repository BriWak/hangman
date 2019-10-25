package controllers

import connectors.FilmConnector
import controllers.auth.AuthAction
import models.Hangman
import org.joda.time.DateTime
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import services.{DataService, HangmanService}
import viewModels.Letter

import scala.concurrent.Future

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  "HomeController GET" should {

    val mockHangmanService = mock[HangmanService]
    val mockDataService = mock[DataService]
    val mockFilmConnector = mock[FilmConnector]

    val newGame = Hangman("game1", "fakeUrl", "FILM", "____", List(), 6, alreadyGuessed = false, DateTime.parse("01-01-20"))

    when(mockHangmanService.getRandomFilm(any())).thenReturn(Future.successful(newGame))
    when(mockDataService.deleteGame(any())).thenReturn(Future.successful(true))
    when(mockDataService.createGame(any())).thenReturn(Future.successful(newGame))
    when(mockDataService.readGame(any())).thenReturn(Future.successful(Some(newGame)))
    when(mockHangmanService.formatGameWord(any())).thenReturn("FormattedWord")
    when(mockHangmanService.checkGameState(any())).thenReturn("")
    when(mockHangmanService.createLetters(any(), any(), any())).thenReturn(List(Letter('A', "imageUrl", "guessUrl", used = false)))

    "render the index page from a new instance of controller" in {
      val controller = new HomeController(stubControllerComponents(), inject[AuthAction], mockHangmanService, mockDataService, mockFilmConnector)
      val home = controller.index().apply(FakeRequest(GET, "/").withSession("UUID" -> "randomUUID"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Hangman")
    }

    "render the index page from the application" in {
      val controller = inject[HomeController]
      val home = controller.index().apply(FakeRequest(GET, "/").withSession("UUID" -> "randomUUID"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Hangman")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/").withSession("UUID" -> "randomUUID")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Hangman")
    }
  }
}
