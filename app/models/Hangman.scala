package models

import play.api.libs.json.{Json, OFormat}

case class Hangman(word: String, partialWord: String, guessedLetters: List[String], remainingGuesses: Int, alreadyGuessed: Boolean = false)

object Hangman {

  implicit val fmts: OFormat[Hangman] = Json.format[Hangman]
}