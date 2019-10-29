package models

import java.util.UUID

import play.api.mvc.{Request, Session, WrappedRequest}

case class GameRequest[+A](gameId: String, request: Request[A]) extends WrappedRequest[A](request)
//{
//  override val session: Session =
//    request.session.get("UUID")
//      .fold[Session](request.session + ("UUID" -> UUID.randomUUID().toString))(_ => request.session)
//}
