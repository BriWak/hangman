import sbt.Keys.name
import scoverage.ScoverageKeys

val ScoverageExclusionPatterns = List(
  "<empty>",
  "app.*",
  "config.*",
  "views.*",
  "models.*",
  ".*StubDataController.*",
  ".*Routes.*",
  ".*Reverse.*"
)

lazy val scoverageSettings = {
  Seq(
    ScoverageKeys.coverageExcludedPackages := ScoverageExclusionPatterns.mkString("", ";", ""),
    ScoverageKeys.coverageMinimum := 85,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    scoverageSettings,
    scalaVersion := "2.13.0",
    name := """hangman""",
    organization := "com.example",
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      guice,
      ws,
      "org.reactivemongo"      %% "play2-reactivemongo" % "0.18.6-play27",
      "org.scalatestplus.play" %% "scalatestplus-play"  % "4.0.3"           % Test,
      "org.mockito"             % "mockito-all"         % "1.10.19"         % Test,
      "com.github.tomakehurst"  % "wiremock-standalone" % "2.17.0"          % Test
    )
  )

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
