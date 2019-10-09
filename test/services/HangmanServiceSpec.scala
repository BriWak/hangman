package services

import models.Hangman
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec

class HangmanServiceSpec extends PlaySpec with MustMatchers {

  "guessLetter" must {
    "set the alreadyGuessed flag when a guess is incorrect and in the list of previous guesses" in new HangmanService {
      val game = Hangman("FILM", "____", List('S'), 5)

      val result = guessLetter('s', game)

      result mustEqual Hangman("FILM", "____", List('S'), 5, alreadyGuessed = true)
    }

    "set the alreadyGuessed flag when a guess is correct but in the list of previous guesses" in new HangmanService {
      val game = Hangman("FILM", "F___", List('F'), 6)

      val result = guessLetter('f', game)

      result mustEqual Hangman("FILM", "F___", List('F'), 6, alreadyGuessed = true)
    }

    "add the letter to previous guesses and reduce the remaining guess count when a guess is wrong and not in the list of previous guesses" in new HangmanService {
      val game = Hangman("FILM", "____", List('S'), 5)

      val result = guessLetter('t', game)

      result mustEqual Hangman("FILM", "____", List('S', 'T'), 4, alreadyGuessed = false)
    }


    "add the letter to previous guesses and update the partial word when a guess is correct and not in the list of previous guesses" in new HangmanService {
      val game = Hangman("FILM", "____", List('S'), 5)

      val result = guessLetter('i', game)

      result mustEqual Hangman("FILM", "_I__", List('S', 'I'), 5, alreadyGuessed = false)
    }
  }

  "checkGameState" must {
    "return 'Game Over' when no guesses remain" in new HangmanService {
      val game = Hangman("FILM", "____", List('S'), 0)

      checkGameState(game) mustEqual "Game Over"
    }

    "return 'Winner' when the word has been fully guessed" in new HangmanService {
      val game = Hangman("FILM", "FILM", List('S'), 3)

      checkGameState(game) mustEqual "Winner"
    }

    "return 'You have already guessed that letter' when the duplicate guess flag is set " in new HangmanService {
      val game = Hangman("FILM", "____", List('S'), 3, alreadyGuessed = true)

      checkGameState(game) mustEqual "You have already guessed that letter"
    }

    "return an empty string when the game is in play" in new HangmanService {
      val game = Hangman("FILM", "____", List('S'), 5)

      checkGameState(game) mustEqual ""
    }
  }

}
