com.github.retronym.SbtOneJar.oneJarSettings

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "com.yammer.metrics" % "metrics-core" % "2.2.0",
  "com.typesafe.akka" %% "akka-actor" % "2.1.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.1.2",
  "com.ning" % "async-http-client" % "1.7.12",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)
