package com.topl

import com.topl.model.{ArgConf, GraphNodes, TrafficResponse}
import com.topl.repository.FileRepository
import java.net.URI

import scalax.collection.Graph
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import zio.Console._
import zio.json._

object TrafficData extends ZIOAppDefault {

  /** Graph logic validates that nodes belong to the graph, group and average of times, create the graph, find the short
    * path
    */
  private def graphLogic(
      from: String,
      to: String,
      file: Option[URI]
  ): ZIO[Any, Throwable, String] = {
    for {
      nodeFrom <- ZIO.cond(GraphNodes.nodes.contains(from), from, new Exception("Node `from` not found"))
      nodeTo   <- ZIO.cond(GraphNodes.nodes.contains(to), to, new Exception("Node `to` not found"))
      averages <- FileRepository.averageTraffic(fileName = "sample-data.json", file)
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
      argConf <- ZIO.attempt(new ArgConf(args))

      message <- ZIO.succeed(argConf.file.toOption.map(_ => "using external file").getOrElse("using dataset provided"))
      _       <- printLine(s"Welcome: $message")

      res <- graphLogic(
        from = argConf.from.apply(),
        to = argConf.to.apply(),
        // TODO, works on linux, but Macos or Windows?
        file = argConf.file.map(path => URI.create("file://" + path)).toOption
      ).catchAll(_ => ZIO.succeed("Path not found, tyr other nodes"))

      _ <- printLine(res)
    } yield (())
  }
}
