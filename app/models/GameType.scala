package models

import play.api.libs.json.{Json, OFormat}

sealed trait GameType {
  def newGameUrl: String
}

object GameType {
  implicit val fmts: OFormat[GameType] = Json.format[GameType]
}

case class TVGame(newGameUrl: String = controllers.routes.HomeController.films().url) extends GameType

object TVGame {
  implicit val fmts: OFormat[TVGame] = Json.format[TVGame]
}

case class FilmGame(newGameUrl: String = controllers.routes.HomeController.tvShows().url) extends GameType

object FilmGame {
  implicit val fmts: OFormat[FilmGame] = Json.format[FilmGame]
}