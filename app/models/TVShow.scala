package models

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads}

case class TVShow(name: String, id: Int) {
  def url = s"https://www.themoviedb.org/tv/${id}"
}

object TVShow {
  implicit val reads: Reads[TVShow] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "id").read[Int]
    )(TVShow.apply _)
}

case class TVShows(results: List[TVShow])

object TVShows {
  implicit val reads: Reads[TVShows] = Json.reads[TVShows]
}