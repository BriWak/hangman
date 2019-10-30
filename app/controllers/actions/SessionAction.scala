package controllers.actions

import java.util.UUID

import com.google.inject.Inject
import models.GameRequest
import play.api.mvc.Results.Redirect
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class SessionAction @Inject()(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[GameRequest, AnyContent] with ActionRefiner[Request, GameRequest] {

  override protected def refine[A](request: Request[A]): Future[Either[Result, GameRequest[A]]] = {
    Future.successful(
      request.session.get("UUID")
        .fold[Either[Result, GameRequest[A]]] {
          Left(Redirect(controllers.routes.HomeController.home()).withSession("UUID" -> UUID.randomUUID().toString))
        }(uuid => Right(GameRequest(uuid, request)))
    )
  }
}
