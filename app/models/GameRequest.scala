package models

import play.api.mvc.{Request, Session, WrappedRequest}

case class GameRequest[A](gameId: String, request: Request[A]) extends WrappedRequest[A](request)

