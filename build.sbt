com.github.retronym.SbtOneJar.oneJarSettings

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "com.yammer.metrics" % "metrics-core" % "2.2.0",
  "com.typesafe.akka" %% "akka-actor" % "2.1.2",
  "com.ning" % "async-http-client" % "1.7.12",
  "org.scalaz" %% "scalaz-core" % "6.0.4"
)

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)
