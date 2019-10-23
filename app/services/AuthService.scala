package services

import java.util.UUID

import com.google.inject.Inject
import models.UserSession
import repositories.SessionRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AuthService @Inject()(sessionRepository: SessionRepository) {

  def isLoggedIn(uuid: String): Future[Boolean] = {
    sessionRepository.findByUuid(uuid).map(_.isDefined)
  }

  def addAuthUUID(): Future[UserSession] = {
    val userSession = UserSession(UUID.randomUUID().toString)
    sessionRepository.create(userSession).map { _ =>
      userSession
    }
  }
}
