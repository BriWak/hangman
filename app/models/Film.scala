package models


import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads}

case class Film(title: String, id: Int) {

  def url = s"https://www.themoviedb.org/movie/${id}"
}

object Film {

  implicit val reads: Reads[Film] = (
    (JsPath \ "title").read[String] and
      (JsPath \ "id").read[Int]
    )(Film.apply _)

}

case class Films(results: List[Film])

object Films {

  implicit val reads: Reads[Films] = Json.reads[Films]

}