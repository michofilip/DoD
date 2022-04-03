import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.2"

lazy val scalatestVersion = "3.2.11"
lazy val akkaVersion = "2.6.19"
lazy val logbackVersion = "1.2.11"
lazy val scalafxVersion = "17.0.1-R26"
lazy val javafxVersion = "17"

lazy val root = (project in file("."))
    .settings(
        name := "DoD",

        libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % Test,
        libraryDependencies += "ch.qos.logback" % "logback-classic" % logbackVersion,
        libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
        libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
        libraryDependencies += "org.scalafx" %% "scalafx" % scalafxVersion,
        libraryDependencies ++= {
            // Determine OS version of JavaFX binaries
            lazy val osName = System.getProperty("os.name") match {
                case n if n.startsWith("Linux") => "linux"
                case n if n.startsWith("Mac") => "mac"
                case n if n.startsWith("Windows") => "win"
                case _ => throw new Exception("Unknown platform!")
            }
            Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
                .map(m => "org.openjfx" % s"javafx-$m" % javafxVersion classifier osName)
        }
    )
