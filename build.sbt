import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

lazy val scalatestVersion = "3.2.10"

lazy val root = (project in file("."))
    .settings(
        name := "DoD",

        libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % Test,
    )
