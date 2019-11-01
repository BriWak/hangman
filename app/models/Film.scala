package models

import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, OFormat, OWrites, Reads, Writes, __}

case class Film(title: String, id: Int) {
  def url = s"https://www.themoviedb.org/movie/${id}"
}

object Film {
  implicit val reads: Reads[Film] = (
    (JsPath \ "title").read[String] and
      (JsPath \ "id").read[Int]
    )(Film.apply _)

  implicit val writes: Writes[Film] = Json.writes[Film]
}

case class Films(results: List[Film], createdAt: DateTime = DateTime.now)

object Films {

  implicit val dateTimeRead: Reads[DateTime] =
    (__ \ "$date").read[Long].map { dateTime =>
      new DateTime(dateTime, DateTimeZone.UTC)
    }

  implicit val dateTimeWrite: Writes[DateTime] = (dateTime: DateTime) => Json.obj(
    "$date" -> dateTime.getMillis
  )

  implicit val reads: Reads[Films] = (
    (JsPath \ "results").read[Array[Film]].map(films => Films(films.toList)))

  implicit val writes: OWrites[Films] = Json.writes[Films]

}
