ThisBuild / scalaVersion := "3.5.1"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "zioclidemo",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.11",
      "dev.zio" %% "zio-cli" % "0.5.0"
    )
  )

Compile / mainClass := Some("ZIOCLI")
