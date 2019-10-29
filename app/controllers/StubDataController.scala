package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

class StubDataController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def stubMovieDbUrl(page: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(Json.parse(
      s"""
         |{
         |  "page": $page,
         |  "total_results": 6559,
         |  "total_pages": 328,
         |  "results": [
         |    {
         |      "popularity": 20.488,
         |      "id": 629,
         |      "video": false,
         |      "vote_count": 6012,
         |      "vote_average": 8.2,
         |      "title": "The Usual Suspects",
         |      "release_date": "1995-08-16",
         |      "original_language": "en",
         |      "original_title": "The Usual Suspects",
         |      "genre_ids": [
         |        18,
         |        80,
         |        53
         |      ],
         |      "backdrop_path": "/c7e3nH3wtSBDrHRB63onrGptl6D.jpg",
         |      "adult": false,
         |      "overview": "Held in an L.A. interrogation room, Verbal Kint attempts to convince the feds that a mythic crime lord, Keyser Soze, not only exists, but was also responsible for drawing him and his four partners into a multi-million dollar heist that ended with an explosion in San Pedro harbor – leaving few survivors. Verbal lures his interrogators with an incredible story of the crime lord's almost supernatural prowess.",
         |      "poster_path": "/jgJoRWltoS17nD5MAQ1yK2Ztefw.jpg"
         |    },
         |    {
         |      "popularity": 25.765,
         |      "vote_count": 6309,
         |      "video": false,
         |      "poster_path": "/90T7b2LIrL07ndYQBmSm09yqVEH.jpg",
         |      "id": 62,
         |      "adult": false,
         |      "backdrop_path": "/rkaP7dnHenOr2n2IqMxip6rtT4W.jpg",
         |      "original_language": "en",
         |      "original_title": "2001: A Space Odyssey",
         |      "genre_ids": [
         |        12,
         |        9648,
         |        878
         |      ],
         |      "title": "2001: A Space Odyssey",
         |      "vote_average": 8,
         |      "overview": "Humanity finds a mysterious object buried beneath the lunar surface and sets off to find its origins with the help of HAL 9000, the world's most advanced super computer.",
         |      "release_date": "1968-04-10"
         |    }
         |  ]
         |}
         |""".stripMargin)
    )
  }
}