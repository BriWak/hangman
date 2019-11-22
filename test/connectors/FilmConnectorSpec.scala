package connectors

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, anyUrl, get}
import models.{Film, TVShow}
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Injecting
import stubData.StubData
import util.WireMockHelper

import scala.concurrent.Await
import scala.concurrent.duration._

class FilmConnectorSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting with WireMockHelper with ScalaFutures {

  override lazy val app: Application = GuiceApplicationBuilder()
    .configure("movie.api.url" -> s"http://localhost:${server.port}/stubApi/")
    .configure("tv.api.url" -> s"http://localhost:${server.port}/stubApi/")
    .build()

  "getFilms" must {
    "return a list of films from the API without any numbers in the titles" in {

      server.stubFor(
        get(anyUrl())
          .willReturn(
            aResponse()
              .withStatus(200)
              .withBody(StubData.filmData(1).toString())
          ))

      val filmConnector = app.injector.instanceOf[FilmConnector]

      val result = filmConnector.getFilms(1)

      Await.result(result, 5.seconds) mustBe
        List(Film("The Usual Suspects", 629, List(18, 80, 53)))
    }
  }

  "getTVShows" must {
    "return a list of TV shows from the API without any numbers in the titles" in {

      server.stubFor(
        get(anyUrl())
          .willReturn(
            aResponse()
              .withStatus(200)
              .withBody(StubData.tvData(1).toString())
          ))

      val filmConnector = app.injector.instanceOf[FilmConnector]

      val result = filmConnector.getTVShows(1)

      Await.result(result, 5.seconds) mustBe
        List(TVShow("Stranger Things", 66732, List(18, 9648, 10765)))
    }
  }

}
