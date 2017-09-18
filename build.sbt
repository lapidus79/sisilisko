name := """sisilisko"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += jdbc
libraryDependencies += "com.h2database" % "h2" % "1.4.194"

libraryDependencies ++= Seq(
  "org.awaitility" % "awaitility" % "2.0.0" % "test",
  "org.assertj" % "assertj-core" % "3.6.2" % "test",
  "org.mockito" % "mockito-core" % "2.1.0" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.4" % "test"
)
