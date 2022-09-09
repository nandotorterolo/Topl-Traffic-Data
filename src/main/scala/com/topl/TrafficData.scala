package com.topl

import com.topl.model.{ArgConf, GraphNodes, TrafficResponse}
import com.topl.repository.FileRepository
import java.io.File
import java.net.URI
import java.nio.file.Files
import scalax.collection.Graph
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import zio._
import zio.{Console, ZIO, ZIOAppDefault}
import zio.Console._
import zio.json._

object TrafficData extends ZIOAppDefault {

  /** Graph logic validates that nodes belong to the graph, group and average of times, create the graph, find the short
    * path
    */
  private def graphLogic(
      from: String,
      to: String,
      fileName: String = "sample-data.json",
      file: Option[URI]
  ): ZIO[Any, Throwable, String] = {
    for {
      nodeFrom <- ZIO.cond(GraphNodes.nodes.contains(from), from, new Exception("Node `from` not found"))
      nodeTo   <- ZIO.cond(GraphNodes.nodes.contains(to), to, new Exception("Node `to` not found"))
      averages <- FileRepository.averageTraffic(fileName, file)
      edges    <- ZIO.attempt(averages.map(GraphNodes.edgeFactory))
      graph    <- ZIO.attempt(Graph.from(GraphNodes.nodes, edges))
      shortPath <- ZIO.getOrFailWith(new Exception("Path not found"))(
        graph.get(nodeFrom) shortestPathTo graph.get(nodeTo)
      )
      jsonResp <- ZIO.attempt(
        TrafficResponse(shortPath.nodes.mkString(","), shortPath.edges.mkString(","), shortPath.weight).toJsonPretty
      )
    } yield (jsonResp)
  }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {

    for {
      args    <- getArgs.map(_.toList)
      argConf <- ZIO.attempt(new ArgConf(args)) //..orDie
//      _ <- printLine(argsValidator.to)
//      size    <- ZIO.cond(args.size == 2 || args.size == 3, args.size, new Exception("This app requires 2 or 3 arguments to works"))
//      _    <- printLine(s"Welcome: ${if(size==2) "using dataset provided" else "using external file"}")
      _ <- printLine(s"Welcome: ${if (argConf.file.isEmpty) "using dataset provided" else "using external file"}")
//      res1 <- ZIO.when(args.size == 2) { graphLogic(from = args(0), to= args(1), file = None) }
//      res2 <- ZIO.when(args.size == 3) { graphLogic(from = args(0), to= args(1), file = Some(URI.create("file://"+args(2)))) }
      res1 <- ZIO.when(argConf.file.isEmpty) {
        graphLogic(from = argConf.from.apply(), to = argConf.to.apply(), file = None)
      }
      res2 <- ZIO.when(argConf.file.isDefined) {
        graphLogic(
          from = argConf.from.apply(),
          to = argConf.to.apply(),
          file = Some(URI.create("file://" + argConf.file.apply()))
        )
      }
//      res2 <- ZIO.when(args.size == 3) { graphLogic(from = args(0), to= args(1), file = Some(URI.create("file://"+args(2)))) }
      _ <- printLine(res1.orElse(res2).getOrElse("No path found"))
    } yield (())
  }
}
