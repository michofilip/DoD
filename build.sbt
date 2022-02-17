import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

lazy val scalatestVersion = "3.2.10"
lazy val akkaVersion = "2.6.18"
lazy val logbackVersion = "1.2.10"

lazy val root = (project in file("."))
    .settings(
        name := "DoD",

        libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % Test,
        libraryDependencies += "ch.qos.logback" % "logback-classic" % logbackVersion,

        libraryDependencies ++= Seq(
            "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
            "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
        ).map(_.cross(CrossVersion.for3Use2_13)),
    )
