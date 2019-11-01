package services

import connectors.FilmConnector
import models.{Film, FilmGame, Films, Hangman, TVShow, TVShows}
import org.joda.time.DateTime
import org.joda.time.DateTimeZone.UTC
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.MustMatchers
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.Injecting
import repositories.{FilmRepository, GameRepository, TVRepository}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class DataServiceSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting with MustMatchers with MockitoSugar with ScalaFutures {

  "DataService" must {

    val mockGameRepository = mock[GameRepository]
    val mockFilmRepository = mock[FilmRepository]
    val mockTVRepository = mock[TVRepository]
    val mockFilmConnector = mock[FilmConnector]

    val dataService = new DataService(mockGameRepository, mockFilmRepository, mockTVRepository, mockFilmConnector)

    val createdAtDate: DateTime = DateTime.parse("01-01-20").toDateTime(UTC)

    val game = Hangman("game1", FilmGame(), "fakeUrl", "FILM", "____", List(), 6, alreadyGuessed = false, createdAtDate)
    val filmList = Films(List(Film("Film", 1)), createdAtDate)
    val tvList = TVShows(List(TVShow("TV SHow", 1)), createdAtDate)

    "create a new game in the database" in {
      when(mockGameRepository.create(any())).thenReturn(Future.successful(game))
      val result = dataService.createGame(game)

      Await.result(result, 5.seconds) mustBe game
    }

    "read a game from the database when given it's ID" in {
      when(mockGameRepository.findByGameId(any())).thenReturn(Future.successful(Some(game)))
      val result = dataService.readGame("game1")

      Await.result(result, 5.seconds) mustBe Some(game)
    }

    "update a game in the database when given it's ID" in {
      when(mockGameRepository.updateGame(any(), any())).thenReturn(Future.successful(game))
      val result = dataService.updateGame("game1", game)

      Await.result(result, 5.seconds) mustBe game
    }

    "delete a game in the database when given it's ID" in {
      when(mockGameRepository.deleteByGameId(any())).thenReturn(Future.successful(true))
      val result = dataService.deleteGame("game1")

      Await.result(result, 5.seconds) mustBe true
    }

    "create a new Films list in the database" in {
      when(mockFilmRepository.createFilmList(any())).thenReturn(Future.successful(filmList))
      val result = dataService.createFilms(filmList)

      Await.result(result, 5.seconds) mustBe filmList
    }

    "read films from the database if they exist" in {
      when(mockFilmRepository.findFilmList()).thenReturn(Future.successful(Some(filmList)))
      val result = dataService.getFilms()

      Await.result(result, 5.seconds) mustBe filmList
    }

    "read films from the API if the database is empty" in {
      when(mockFilmRepository.findFilmList()).thenReturn(Future.successful(None))
      when(mockFilmConnector.getFilms(any())).thenReturn(Future.successful(List(Film("Film", 1))))
      val result = dataService.getFilms()

      Await.result(result, 5.seconds) mustBe filmList
    }

    "create a new TV Shows list in the database" in {
      when(mockTVRepository.createTVList(any())).thenReturn(Future.successful(tvList))
      val result = dataService.createTVShows(tvList)

      Await.result(result, 5.seconds) mustBe tvList
    }

    "read TV shows from the database if they exist" in {
      when(mockTVRepository.findTVList()).thenReturn(Future.successful(Some(tvList)))
      val result = dataService.getTVShows()

      Await.result(result, 5.seconds) mustBe tvList
    }

    "read TV shows from the API if the database is empty" in {
      when(mockTVRepository.findTVList()).thenReturn(Future.successful(None))
      when(mockFilmConnector.getTVShows(any())).thenReturn(Future.successful(List(TVShow("TV SHow", 1))))
      val result = dataService.getTVShows()

      Await.result(result, 5.seconds) mustBe tvList
    }
  }
}
