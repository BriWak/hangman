package connectors

import models.Film
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.Injecting

import scala.concurrent.Await
import scala.concurrent.duration._

class FilmConnectorSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting with ScalaFutures {

  "getFilmPage" must {

    val filmConnector = inject[FilmConnector]

    "return a list of films from the API without any numbers in the titles" in {
      val result = filmConnector.getFilms(1)

      Await.result(result, 5.seconds) mustBe
        List(Film("The Usual Suspects", 629))
    }
  }

}
