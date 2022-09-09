import Dependencies._

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.topl"
ThisBuild / organizationName := "topl"

lazy val root = (project in file("."))
  .settings(
    name := "topl-traffic-data",
    libraryDependencies ++= sourceDependencies ++ testDependencies,
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )


assembly / assemblyMergeStrategy := {
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.first
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}
