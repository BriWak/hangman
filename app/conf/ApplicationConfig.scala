package conf

import javax.inject._
import play.api.{Configuration, Environment}

@Singleton
class ApplicationConfig @Inject()(
                                   configuration: Configuration,
                                   environment: Environment
                                 ) {

  private def loadConfig(key: String): String = {
    configuration.get[String](key)
  }

  val expireAfterSeconds: Int = loadConfig("mongodb.expireAfterSeconds").toInt
  val movieApiUrl: String = loadConfig("movie.api.url")
  val tvApiUrl: String = loadConfig("tv.api.url")
}
