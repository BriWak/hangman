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
         |      "popularity": 12.446,
         |      "vote_count": 1916,
         |      "video": false,
         |      "poster_path": "/hYzCLju3W74nLhhRXfPkwDi1Tun.jpg",
         |      "id": 11216,
         |      "adult": false,
         |      "backdrop_path": "/tVi83ttAeyMJinYpy6xfgJSpzvP.jpg",
         |      "original_language": "it",
         |      "original_title": "Nuovo Cinema Paradiso",
         |      "genre_ids": [
         |        18,
         |        10749
         |      ],
         |      "title": "Cinema Paradiso",
         |      "vote_average": 8.4,
         |      "overview": "A filmmaker recalls his childhood, when he fell in love with the movies at his village's theater and formed a deep friendship with the theater's projectionist.",
         |      "release_date": "2002-06-14"
         |    },
         |    {
         |      "popularity": 9.753,
         |      "vote_count": 497,
         |      "video": false,
         |      "poster_path": "/uHEmM49YphluJnGep8Ef1qwD2QX.jpg",
         |      "id": 40096,
         |      "adult": false,
         |      "backdrop_path": "/alQqTpmEkxSLgajfEYTsTH6nAKB.jpg",
         |      "original_language": "pt",
         |      "original_title": "O Auto da Compadecida",
         |      "genre_ids": [
         |        12,
         |        35
         |      ],
         |      "title": "A Dog's Will",
         |      "vote_average": 8.4,
         |      "overview": "The lively João Grilo and the sly Chicó are poor guys living in the hinterland who cheat a bunch of people in a small Northeast Brazil town. But when they die, they have to be judged by Christ, the Devil and the Virgin Mary, before they are admitted to paradise.",
         |      "release_date": "2000-09-15"
         |    },
         |    {
         |      "popularity": 19.258,
         |      "vote_count": 4354,
         |      "video": false,
         |      "poster_path": "/wfPHdfofBD5PN96dV96a51B3Ja2.jpg",
         |      "id": 429,
         |      "adult": false,
         |      "backdrop_path": "/xGC2fY5KFmtuXnsuQwYQKFOLZFy.jpg",
         |      "original_language": "it",
         |      "original_title": "Il buono, il brutto, il cattivo",
         |      "genre_ids": [
         |        37
         |      ],
         |      "title": "The Good, the Bad and the Ugly",
         |      "vote_average": 8.4,
         |      "overview": "While the Civil War rages between the Union and the Confederacy, three men – a quiet loner, a ruthless hit man and a Mexican bandit – comb the American Southwest in search of a strongbox containing £200,000 in stolen gold.",
         |      "release_date": "1967-12-29"
         |    }
         |  ]
         |}
         |""".stripMargin)
    )
  }
}