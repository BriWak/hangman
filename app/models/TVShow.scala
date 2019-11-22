package models

import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, OFormat, OWrites, Reads, Writes, __}

case class TVShow(name: String, id: Int, genre_ids: List[Int]) {
  def url = s"https://www.themoviedb.org/tv/${id}"
}

object TVShow {
  implicit val reads: Reads[TVShow] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "id").read[Int] and
      (JsPath \ "genre_ids").read[List[Int]]
    )(TVShow.apply _)

  implicit val writes: Writes[TVShow] = Json.writes[TVShow]
}

case class TVShows(results: List[TVShow], createdAt: DateTime = DateTime.now)

object TVShows {

  implicit val dateTimeRead: Reads[DateTime] =
    (__ \ "$date").read[Long].map { dateTime =>
      new DateTime(dateTime, DateTimeZone.UTC)
    }

  implicit val dateTimeWrite: Writes[DateTime] = (dateTime: DateTime) => Json.obj(
    "$date" -> dateTime.getMillis
  )

  implicit val reads: Reads[TVShows] = (
    (JsPath \ "results").read[Array[TVShow]].map(tvShows => TVShows(tvShows.toList)))

  implicit val writes: OWrites[TVShows] = Json.writes[TVShows]

}
