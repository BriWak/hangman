package services

import connectors.FilmConnector
import models.Hangman
import org.joda.time.DateTime
import org.scalatest.MustMatchers
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

class HangmanServiceSpec extends PlaySpec with MustMatchers with MockitoSugar {

  val mockFilmConnector = mock[FilmConnector]
  val testService = new HangmanService(mockFilmConnector)

  "guessLetter" must {
    "set the alreadyGuessed flag when a guess is incorrect and in the list of previous guesses" in {
      val game = Hangman("game1", "fakeUrl", "FILM", "____", List("S"), 5, createdAt = DateTime.parse("01-01-20"))

      val result = testService.guessLetter('s', game)

      result mustEqual Hangman("game1", "fakeUrl", "FILM", "____", List("S"), 5, alreadyGuessed = true, DateTime.parse("01-01-20"))
    }

    "set the alreadyGuessed flag when a guess is correct but in the list of previous guesses" in {
      val game = Hangman("game1", "fakeUrl", "FILM", "F___", List("F"), 6, createdAt = DateTime.parse("01-01-20"))

      val result = testService.guessLetter('f', game)

      result mustEqual Hangman("game1", "fakeUrl", "FILM", "F___", List("F"), 6, alreadyGuessed = true, DateTime.parse("01-01-20"))
    }

    "add the letter to previous guesses and reduce the remaining guess count when a guess is wrong and not in the list of previous guesses" in {
      val game = Hangman("game1", "fakeUrl", "FILM", "____", List("S"), 5)

      val result = testService.guessLetter('t', game)

      result mustEqual Hangman("game1", "fakeUrl", "FILM", "____", List("S", "T"), 4, alreadyGuessed = false)
    }


    "add the letter to previous guesses and update the partial word when a guess is correct and not in the list of previous guesses" in {
      val game = Hangman("game1", "fakeUrl", "FILM", "____", List("S"), 5)

      val result = testService.guessLetter('i', game)

      result mustEqual Hangman("game1", "fakeUrl", "FILM", "_I__", List("S", "I"), 5, alreadyGuessed = false)
    }
  }

  "checkGameState" must {
    "return 'Game Over' when no guesses remain" in {
      val game = Hangman("game1", "fakeUrl", "FILM", "____", List("S"), 0)

      testService.checkGameState(game) mustEqual "Game Over"
    }

    "return 'Winner' when the word has been fully guessed" in {
      val game = Hangman("game1", "fakeUrl", "FILM", "FILM", List("S"), 3)

      testService.checkGameState(game) mustEqual "Winner"
    }

    "return 'You have already guessed that letter' when the duplicate guess flag is set " in {
      val game = Hangman("game1", "fakeUrl", "FILM", "____", List("S"), 3, alreadyGuessed = true)

      testService.checkGameState(game) mustEqual "You have already guessed that letter"
    }

    "return an empty string when the game is in play" in {
      val game = Hangman("game1", "fakeUrl", "FILM", "____", List("S"), 5)

      testService.checkGameState(game) mustEqual ""
    }
  }

}
