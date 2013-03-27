resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "io.spray" % "spray-client" % "1.0-M7",
  "com.yammer.metrics" %% "metrics-scala" % "2.2.0",
  "com.typesafe.akka" % "akka-actor" % "2.0.5"
)
