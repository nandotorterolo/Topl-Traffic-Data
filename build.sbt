import Dependencies._

ThisBuild / scalaVersion      := "2.13.8"
ThisBuild / version           := "0.1.0-SNAPSHOT"
ThisBuild / organization      := "com.topl"
ThisBuild / organizationName  := "topl"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val root = (project in file("."))
  .settings(
    name := "topl-traffic-data",
    libraryDependencies ++= sourceDependencies ++ testDependencies,
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
    scalacOptions += "-Wunused:imports" // Scala 2.x only, required by `RemoveUnused`
  )

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.first
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}
