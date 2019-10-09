package services

import models.Hangman

class HangmanService {


  val films = List("Terminator Two: Judgement Day")

  def getRandomFilm: Hangman = {
    val word: String = films.head.toUpperCase

    Hangman(word, word.map(char => if (char == ' ' || char == ':') char else '_'), List.empty[String], 6)
  }

  def formatGameWord(word: String): String = {
    word.toList.map(char => if (char == ' ')  "\u00A0\u00A0" else s"$char\u00A0").mkString
  }

  def guessLetter(letter: String, game: Hangman): Hangman = {
    val formattedLetter = letter.toUpperCase
    if (game.guessedLetters.contains(formattedLetter)) {
      game.copy(alreadyGuessed = true)
    } else {
      val wordSoFar = showLetters(formattedLetter, game)
      if (wordSoFar == game.partialWord) {
        Hangman(game.word, game.partialWord, game.guessedLetters :+ formattedLetter, game.remainingGuesses - 1)
      } else {
        Hangman(game.word, wordSoFar, game.guessedLetters :+ formattedLetter, game.remainingGuesses)
      }
      }
  }

  private def showLetters(letter: String, game: Hangman): String = {
    val newGuess: String = game.word.toList.map(char => if (char.toString == letter) letter else "_").mkString

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
