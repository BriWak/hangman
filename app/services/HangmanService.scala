package services

import com.google.inject.Inject
import connectors.ImdbConnector
import models.Hangman

import scala.concurrent.Future
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

class HangmanService @Inject()(imdbConnector: ImdbConnector) {

  def getRandomFilm: Future[Hangman] = {
    imdbConnector.getFilms().map { films =>
      val word = films(Random.nextInt(films.length)).toUpperCase
      val displayableChars = List(' ', '\'', '!', '?', ',', '.', '-', ':')
      Hangman(word, word.map(char => if (displayableChars.contains(char)) char else '_'), List.empty[String], 6)
    }
  }

  def formatGameWord(word: String): String = {
//    val splitWord = if (word.length >= 18) {
//      println("***********"+ word.length)
//      val splitAt = word.substring(word.length/2).indexOf(" ")+(word.length/2)
//      println("***********"+ splitAt)
//      val (lineOne, lineTwo) = word.splitAt(splitAt)
//      s"${lineOne.trim}\n${lineTwo.trim}"
//    } else word
    val formattedWord = word.trim.toList.map(char => if (char == ' ') "\u00A0\n" else s"$char\u00A0").mkString
    formattedWord.take(formattedWord.length-1)
  }

  def guessLetter(letter: Char, game: Hangman): Hangman = {
    val formattedLetter = letter.toUpper
    if (game.guessedLetters.contains(formattedLetter)) {
      game.copy(alreadyGuessed = true)
    } else {
      val wordSoFar = showLetters(formattedLetter, game)
      if (wordSoFar == game.partialWord) {
        val remainingGuesses = if (game.remainingGuesses <= 0) 0 else game.remainingGuesses - 1
        Hangman(game.word, game.partialWord, game.guessedLetters :+ formattedLetter.toString, remainingGuesses)
      } else {
        Hangman(game.word, wordSoFar, game.guessedLetters :+ formattedLetter.toString, game.remainingGuesses)
      }
    }
  }

  private def showLetters(letter: Char, game: Hangman): String = {
    val newGuess: String = game.word.toList.map(char => if (char == letter) letter else "_").mkString

    game.partialWord.zip(newGuess).map {
      case (a, b) if a == b => a
      case ('_', other) => other
      case (other, '_') => other
    }.mkString

  }


  def checkGameState(game: Hangman): String = {
    game match {
      case Hangman(_, _, _, 0, _) => "Game Over"
      case Hangman(word, partialWord, _, _, _) if word == partialWord => "Winner"
      case Hangman(_, _, _, _, true) => "You have already guessed that letter"
      case _ => ""
    }
  }

}
