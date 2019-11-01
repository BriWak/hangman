package services

import com.google.inject.Inject
import models.{FilmGame, Hangman, Letter, TVGame}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

class HangmanService @Inject()(dataService: DataService) {

  val displayableChars: List[Char] = List(' ', '\'', '!', '?', ',', '.', '-', ':')

  def getRandomFilm(newGameId: String): Future[Hangman] = {
    dataService.getFilms().map { films =>
      val film = films.results(Random.nextInt(films.results.length))
      val word = film.title.toUpperCase
      Hangman(newGameId, FilmGame(), film.url, word, word.map(char => if (displayableChars.contains(char)) char else '_'), List.empty[String], 6)
    }
  }

  def getRandomTVShow(newGameId: String): Future[Hangman] = {
    dataService.getTVShows().map { tvShows =>
      val tvShow = tvShows.results(Random.nextInt(tvShows.results.length))
      val word = tvShow.name.toUpperCase
      Hangman(newGameId, TVGame(), tvShow.url, word, word.map(char => if (displayableChars.contains(char)) char else '_'), List.empty[String], 6)
    }
  }

  def formatGameWord(word: String): String = {
    val formattedWord = word.trim.toList.map(char => if (char == ' ') "\u00A0\n" else s"$char\u00A0").mkString
    formattedWord.take(formattedWord.length-1)
  }

  def guessLetter(letter: Char, game: Hangman): Hangman = {
    val formattedLetter = letter.toUpper
    if (game.guessedLetters.contains(formattedLetter.toString)) {
      game.copy(alreadyGuessed = true)
    } else {
      val wordSoFar = showLetters(formattedLetter, game)
      if (wordSoFar == game.partialWord) {
        val remainingGuesses = if (game.remainingGuesses <= 0) 0 else game.remainingGuesses - 1
        game.copy(guessedLetters = game.guessedLetters :+ formattedLetter.toString, remainingGuesses = remainingGuesses)
      } else {
        game.copy(partialWord = wordSoFar, guessedLetters = game.guessedLetters :+ formattedLetter.toString)
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
      case Hangman(_,_,_,_, _, _, 0, _,_) => "Game Over"
      case Hangman(_,_,_,word, partialWord, _, _, _,_) if word == partialWord => "Winner"
      case Hangman(_,_,_,_, _, _, _, true,_) => "You have already guessed that letter"
      case _ => ""
    }
  }

  def createLetters(firstLetter: Char, secondLetter: Char, game: Hangman): List[Letter] = {
    (firstLetter to secondLetter).toList.map { letter =>
      Letter(letter, controllers.routes.Assets.versioned("images/" + letter + ".png").url,
        controllers.routes.HomeController.guess(letter).url, game.guessedLetters.contains(letter.toString))
    }
  }
}
