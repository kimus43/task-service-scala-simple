import AssemblyKeys._
import Keys._

name                := "todo-play-service"

version             := "1.0"

scalaVersion        := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatestplus.play"              %% "scalatestplus-play"             % "2.0.1"                     % Test,
  "com.typesafe.play"                   %% "play"                           % "2.5.12",
  "com.google.inject"                   %  "guice"                          % "4.0",
  "org.typelevel"                       %% "cats"                           % "0.8.0",
  "com.typesafe.akka"                   %% "akka-actor"                     % "2.4.4",
  "com.typesafe.akka"                   %% "akka-http-experimental"         % "2.4.4",
  "com.typesafe.akka"                   %% "akka-http"                      % "10.0.2",
  "net.databinder.dispatch"             %% "dispatch-core"                  % "0.11.2",
  "org.scalatest"                       %% "scalatest"                      % "3.0.0"                     % "test",
  "com.github.tomakehurst"              %  "wiremock"                       % "1.33"                      % "test",
  "net.liftweb"                         %% "lift-webkit"                    % "2.6.3",
  "net.liftweb"                         %% "lift-record"                    % "2.6.3",
  "com.github.t3hnar"                   %% "scala-bcrypt"                   % "3.0",
  "io.circe"                            %% "circe-generic"                  % "0.9.3",
  "io.igl"                              %% "jwt"                            % "1.2.0",
  "com.sksamuel.scrimage"               %% "scrimage-core"                  % "2.1.7",
  "ch.qos.logback"                      % "logback-classic"                 % "1.2.3",
  "org.mongodb.scala"                   %% "mongo-scala-driver"             % "2.7.0",
  "org.reactivemongo"                   %% "reactivemongo"                  % "0.16.0",
  "com.github.fakemongo"                %  "fongo"                          % "2.0.9",
  "org.json4s"                          %% "json4s-native"                  % "3.4.0"
)

dependencyOverrides += "org.typelevel" %% "cats-core" % "0.8.0"

libraryDependencies += filters

libraryDependencies += ws

assemblySettings

javaOptions in Universal += "--Dpidfile.path=/dev/null"

mergeStrategy in assembly := {
  case x if Assembly.isConfigFile(x) => MergeStrategy.concat
  case PathList(ps @ _*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) => MergeStrategy.rename
  case PathList("META-INF", xs @ _*) =>
    xs map {_.toLowerCase} match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) => MergeStrategy.discard
      case ps @ (x :: _) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") => MergeStrategy.discard
      case "plexus" :: _ => MergeStrategy.discard
      case "services" :: _ => MergeStrategy.filterDistinctLines
      case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) => MergeStrategy.filterDistinctLines
      case _ => MergeStrategy.first
    }
  case PathList(_*) => MergeStrategy.first
}

mainClass in assembly := Some("play.core.server.ProdServerStart")

fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

enablePlugins(PlayScala)

coverageEnabled in Test := true

coverageExcludedPackages := "<empty>;router.*;controllers.javascript.*;utils.filters.*;metrics.*"

parallelExecution in Test := false

javaOptions in Test += "-Dconfig.resource=test.conf"

fork in Test := true

envVars in Test := Map("KP_BROKERS_HOST" -> "localhost:1234")

javaOptions ++= Seq(
  "-Xmx1024M",
  "-Xms1024M",
  "-Xmn128M",
  "-XX:+UseG1GC"
)

dockerExposedPorts := Seq(8080)
