package controllers

import javax.inject._
import play.api.mvc._
import stubData.StubData

class StubDataController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def stubMovieDbUrl(page: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(StubData.filmData(page))
  }

  def stubTVShowUrl(page: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(StubData.tvData(page))
  }
}