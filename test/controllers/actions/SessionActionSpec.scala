package controllers.actions

import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.mvc.Results.Ok
import play.api.mvc.{Action, AnyContent}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Injecting}

class SessionActionSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {



object TestController {

  val sessionAction: SessionAction = inject[SessionAction]

  def testMethod(): Action[AnyContent] = sessionAction { implicit request =>
    request.session.get("UUID").fold(Ok("")) { uuid =>
      Ok(s"$uuid")
    }
  }
}

  "SessionAction" must {
    "redirect to the index page when a session does not contain a UUID" in {
      val result = TestController.testMethod()(FakeRequest())
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/")
    }

    "Allow the user through to the method when a session contains a UUID" in {
      val result = TestController.testMethod()(FakeRequest().withSession("UUID" -> "UUID in Session"))
      status(result) mustBe OK
      contentAsString(result) must include("UUID in Session")
    }
  }
}
