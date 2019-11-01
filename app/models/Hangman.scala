package models

import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json._

case class Hangman(gameId: String, gameType: GameType, url: String, word: String, partialWord: String, guessedLetters: List[String], remainingGuesses: Int, alreadyGuessed: Boolean = false, createdAt: DateTime = DateTime.now)

object Hangman {

  implicit val dateTimeRead: Reads[DateTime] =
    (__ \ "$date").read[Long].map { dateTime =>
      new DateTime(dateTime, DateTimeZone.UTC)
    }

  implicit val dateTimeWrite: Writes[DateTime] = (dateTime: DateTime) => Json.obj(
    "$date" -> dateTime.getMillis
  )

  implicit val fmts: OFormat[Hangman] = Json.format[Hangman]
}
