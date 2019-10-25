package controllers.auth

import com.google.inject.Inject
import conf.ApplicationConfig
import play.api.mvc.Results.Redirect
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class AuthAction @Inject()(val parser: BodyParsers.Default,
                           appConfig: ApplicationConfig)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[Request, AnyContent] with ActionFilter[Request] {

  override def filter[A](request: Request[A]): Future[Option[Result]] = {
    Future.successful(
      request.session.get("UUID")
      .fold[Option[Result]](
        Some(Redirect(controllers.routes.HomeController.createSession())))(
        _ => None))
  }
}
