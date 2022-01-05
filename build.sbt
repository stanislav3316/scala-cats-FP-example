ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

libraryDependencies +=
  "org.typelevel" %% "cats-core" % "2.1.0"

scalacOptions ++= Seq(
  "-Xfatal-warnings"
)

lazy val root = (project in file("."))
  .settings(
    name := "scala-cats"
  )
