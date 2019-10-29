package controllers.actions

import java.util.UUID

import com.google.inject.Inject
import models.GameRequest
import play.api.mvc.Results.Redirect
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class SessionAction @Inject()(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[Request, AnyContent] with ActionRefiner[Request, GameRequest] {

  override protected def refine[A](request: Request[A]): Future[Either[Result, GameRequest[A]]] = {

    request.session.get("UUID")
      .fold[Future[Either[Result, GameRequest[A]]]] {
      Future.successful(Left(Redirect(controllers.routes.HomeController.index()).withSession("UUID" -> UUID.randomUUID().toString)))
    } {
      uuid =>
        Future.successful(Right(GameRequest(uuid, request)))
    }
  }
}
