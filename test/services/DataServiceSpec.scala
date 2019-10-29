package services

import models.Hangman
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
import repositories.GameRepository

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class DataServiceSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting with MustMatchers with MockitoSugar with ScalaFutures {

  "DataService" must {

    val dataService = inject[DataService]
    val mockGameRepository = mock[GameRepository]

    val game = Hangman("game1", "fakeUrl", "FILM", "____", List(), 6, alreadyGuessed = false, DateTime.parse("01-01-20").toDateTime(UTC))

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
  }
}
