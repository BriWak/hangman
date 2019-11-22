package services

import connectors.FilmConnector
import models.{Film, FilmGame, Films, Hangman, Letter, TVGame, TVShow, TVShows}
import org.joda.time.DateTime
import org.scalatest.MustMatchers
import org.mockito.Mockito._
import org.mockito.Matchers.any
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.Future

class HangmanServiceSpec extends PlaySpec with MustMatchers with MockitoSugar {

  val mockDataService: DataService = mock[DataService]
  val testService = new HangmanService(mockDataService)
  val createdAtDate: DateTime = DateTime.parse("01-01-20")

  "getRandomFilm" must {
    "return a new film game with the correct game ID when given a game Id" in {

      when(mockDataService.getFilms()).thenReturn(Future.successful(Films(List(Film("Film Title", 1, List())))))
      testService.getRandomFilm("gameID") must be
        Future.successful(Hangman("gameID", FilmGame(), "https://www.themoviedb.org/movie/1", "FILM TITLE", "____ _____", List(), 6))
    }
  }

  "getRandomTVShow" must {
    "return a new TV game with the correct game ID when given a game Id" in {

      when(mockDataService.getTVShows()).thenReturn(Future.successful(TVShows(List(TVShow("Show Title", 1, List())))))
      testService.getRandomTVShow("gameID") must be
      Future.successful(Hangman("gameID", TVGame(), "https://www.themoviedb.org/tv/1", "SHOW TITLE", "____ _____", List(), 6))
    }
  }

  "formatGameWord" must {
    "correctly format a given word with non breaking spaces for display purposes" in {
      testService.formatGameWord("TEST") mustEqual "T\u00A0E\u00A0S\u00A0T"
    }

    "correctly format more than one word with non breaking spaces for display purposes" in {
      testService.formatGameWord("TEST WORD") mustEqual "T\u00A0E\u00A0S\u00A0T\u00A0\u00A0\nW\u00A0O\u00A0R\u00A0D"
    }
  }

  "guessLetter" must {
    "set the alreadyGuessed flag when a guess is incorrect and in the list of previous guesses" in {
      val game = Hangman("game1", FilmGame(), "fakeUrl", "FILM", "____", List("S"), 5, createdAt = createdAtDate)

      val result = testService.guessLetter('s', game)

      result mustEqual Hangman("game1", FilmGame(), "fakeUrl", "FILM", "____", List("S"), 5, alreadyGuessed = true, createdAt = createdAtDate)
    }

    "set the alreadyGuessed flag when a guess is correct but in the list of previous guesses" in {
      val game = Hangman("game1", FilmGame(), "fakeUrl", "FILM", "F___", List("F"), 6, createdAt = createdAtDate)

      val result = testService.guessLetter('f', game)

      result mustEqual Hangman("game1", FilmGame(), "fakeUrl", "FILM", "F___", List("F"), 6, alreadyGuessed = true, createdAt = createdAtDate)
    }

    "add the letter to previous guesses and reduce the remaining guess count when a guess is wrong and not in the list of previous guesses" in {
      val game = Hangman("game1", FilmGame(), "fakeUrl", "FILM", "____", List("S"), 5, createdAt = createdAtDate)

      val result = testService.guessLetter('t', game)

      result mustEqual Hangman("game1", FilmGame(), "fakeUrl", "FILM", "____", List("S", "T"), 4, createdAt = createdAtDate)
    }


    "add the letter to previous guesses and update the partial word when a guess is correct and not in the list of previous guesses" in {
      val game = Hangman("game1", FilmGame(), "fakeUrl", "FILM", "____", List("S"), 5, createdAt = createdAtDate)

      val result = testService.guessLetter('i', game)

      result mustEqual Hangman("game1", FilmGame(), "fakeUrl", "FILM", "_I__", List("S", "I"), 5, createdAt = createdAtDate)
    }
  }

  "checkGameState" must {
    "return 'Game Over' when no guesses remain" in {
      val game = Hangman("game1", FilmGame(), "fakeUrl", "FILM", "____", List("S"), 0)

      testService.checkGameState(game) mustEqual "Game Over"
    }

    "return 'Winner' when the word has been fully guessed" in {
      val game = Hangman("game1", FilmGame(), "fakeUrl", "FILM", "FILM", List("S"), 3)

      testService.checkGameState(game) mustEqual "Winner"
    }

    "return 'You have already guessed that letter' when the duplicate guess flag is set " in {
      val game = Hangman("game1", FilmGame(), "fakeUrl", "FILM", "____", List("S"), 3, alreadyGuessed = true)

      testService.checkGameState(game) mustEqual "You have already guessed that letter"
    }

    "return an empty string when the game is in play" in {
      val game = Hangman("game1", FilmGame(), "fakeUrl", "FILM", "____", List("S"), 5)

      testService.checkGameState(game) mustEqual ""
    }
  }

  "createLetters" must {
    "create a list of Letters when given a start letter and an end letter" in {
      val game = Hangman("game1", FilmGame(), "fakeUrl", "FILM", "____", List(), 6)

      testService.createLetters('A', 'A', game) mustEqual
        List(Letter('A', "/assets/images/A.png", "/A", used = false))
    }
  }

}
