package connectors

import com.google.inject.Inject
import conf.ApplicationConfig
import models.{Film, Films, TVShow, TVShows}
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FilmConnector @Inject()(appConfig: ApplicationConfig,
                              wsClient: WSClient) {

  def getFilms(pagesToGet: Int): Future[List[Film]] = {
    Future.sequence((1 to pagesToGet).toList.map(page => getFilmPage(page)))
      .map {
        _.flatten
          .filter(_.title.matches("([\\w '!?,-:]+)"))
          .filterNot(_.title.matches(".*[0-9].*"))
          .filterNot(_.genre_ids.contains(16))
      }
  }

  private def getFilmPage(page: Int): Future[List[Film]] = {

    wsClient.url(s"${appConfig.movieApiUrl}${page}").get.map { response =>
      response.json.as[Films].results
    }
  }

  def getTVShows(pagesToGet: Int): Future[List[TVShow]] = {
    Future.sequence((1 to pagesToGet).toList.map(page => getTVPage(page)))
      .map {
        _.flatten
          .filter(_.name.matches("([\\w '!?,-:]+)"))
          .filterNot(_.name.matches(".*[0-9].*"))
          .filterNot(_.genre_ids.contains(16))
      }
  }

  private def getTVPage(page: Int): Future[List[TVShow]] = {
    wsClient.url(s"${appConfig.tvApiUrl}${page}").get.map { response =>
      response.json.as[TVShows].results
    }
  }
}
