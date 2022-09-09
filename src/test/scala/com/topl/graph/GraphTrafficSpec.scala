package com.topl.graph

import com.topl.model.GraphNodes
import com.topl.repository.FileRepository
import scalax.collection.Graph
import zio.ZIO
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assertZIO}

/** Spec Traffic
  */
object GraphTrafficSpec extends ZIOSpecDefault {

  override def spec = suite("Graph Traffic Spec")(
    test("Path and Short Path with Partial Sample Data 2") {

      val result = for {
        nodeFrom  <- ZIO.succeed("A1")
        nodeTo    <- ZIO.succeed("B1")
        averages  <- FileRepository.averageTraffic("partial-sample-data-2.json")
        edges     <- ZIO.attempt(averages.map(GraphNodes.edgeFactory))
        graph     <- ZIO.attempt(Graph.from(GraphNodes.nodes, edges))
        path      <- ZIO.attempt(graph.get(nodeFrom) pathTo graph.get(nodeTo))
        shortPath <- ZIO.attempt(graph.get(nodeFrom) shortestPathTo graph.get(nodeTo))
      } yield (path, shortPath)

      assertZIO(result.map(_._1.isDefined))(equalTo(true))
      assertZIO(result.map(_._1.get.mkString(",")))(equalTo("A1,A1~>B1 %2.0,B1"))

      assertZIO(result.map(_._2.get.nodes.mkString(" : ")))(equalTo("A1 : B1"))
      assertZIO(result.map(_._2.get.weight))(equalTo(2.0))

    },
    test("Path and Short Path with Sample Data") {

      val result = for {
        nodeFrom  <- ZIO.succeed("A1")
        nodeTo    <- ZIO.succeed("C3")
        averages  <- FileRepository.averageTraffic("sample-data.json")
        edges     <- ZIO.attempt(averages.map(GraphNodes.edgeFactory))
        graph     <- ZIO.attempt(Graph.from(GraphNodes.nodes, edges))
        shortPath <- ZIO.attempt(graph.get(nodeFrom) shortestPathTo graph.get(nodeTo))
      } yield (shortPath)

      assertZIO(result.map(_.isDefined))(equalTo(true))
      assertZIO(result.map(_.get.nodes.mkString(":")))(
        equalTo("""A1:B1:C1:D1:E1:F1:F2:F3:F4:F5:E5:D5:C5:C4:C3""".stripMargin)
      )
      assertZIO(result.map(_.get.weight))(equalTo(624.0617202725404))

    }
  )
}
