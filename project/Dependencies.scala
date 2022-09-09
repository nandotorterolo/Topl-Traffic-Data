import sbt._

object Version {
  lazy val zioV = "2.0.2"
}

object Dependencies {
  // sources
  private lazy val zio        = "dev.zio"         %% "zio"             % Version.zioV
  private lazy val zioStreams = "dev.zio"         %% "zio-streams"     % Version.zioV
  private lazy val zioJson    = "dev.zio"         %% "zio-json"        % "0.3.0-RC11"
  private lazy val graph      = "org.scala-graph" %% "graph-core"      % "1.13.5"
  private lazy val logback    = "ch.qos.logback"   % "logback-classic" % "1.2.3"
  private lazy val scallop    = "org.rogach"      %% "scallop"         % "4.1.0"

  // test sources
  private lazy val zioTest    = "dev.zio" %% "zio-test"     % Version.zioV
  private lazy val zioTestSbt = "dev.zio" %% "zio-test-sbt" % Version.zioV

  lazy val sourceDependencies: Seq[ModuleID] = Seq(
    zio,
    zioStreams,
    zioJson,
    graph,
    logback,
    scallop
  )

  lazy val testDependencies: Seq[ModuleID] = Seq(
    zioTest,
    zioTestSbt
  ).map(_ % Test)
}
