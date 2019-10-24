package connectors

import com.google.inject.Inject
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ImdbConnector @Inject()(wsClient: WSClient) {

  def getFilms(): Future[List[String]] = {

//    val regex = """<h4><span class="unbold">(\d+\.)<\/span>([\w '!?,-:]+)<span class="unbold">(\([\d]{4}\))<\/span><\/h4>""".stripMargin.r("number", "title", "year")
    val regex = """<a href="(\S+)"class="btn-full" > <span class="media-body media-vertical-align"><h4><span class="unbold">(\d+\.)<\/span>([\w '!?,-:]+)<span class="unbold">(\([\d]{4}\))<\/span><\/h4>""".stripMargin.r("url", "number", "title", "year")

    wsClient.url("https://m.imdb.com/chart/top").get.map { response =>
      val body = response.body.replaceAll("\n", "")

      regex.findAllMatchIn(body).map(_.group("title")).toList.filterNot(_.matches(".*[0-9].*"))

    }
  }
}
