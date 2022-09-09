package com.topl.graph

import scalax.collection.Graph
import scalax.collection.GraphPredef.EdgeAssoc
import scalax.collection.edge.Implicits._
import scalax.collection.edge.{WDiEdge, WUnDiEdge}
import zio.ZIO
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assertZIO}

/**
 * Spec example to learn graph Library
 * @ see http://www.scala-graph.org/guides/core-traversing.html
 */
object GraphSpec extends ZIOSpecDefault {

  override def spec = suite("GraphSpec")(

    test("Graph Spec Example") {

      val g: Graph[Int, WUnDiEdge] =
        Graph(1 ~ 2 % 4, 2 ~ 3 % 2, 1 ~> 3 % 5, 1 ~ 5 % 3, 3 ~ 5 % 2, 3 ~ 4 % 1, 4 ~> 4 % 1, 4 ~> 5 % 0)
      def n(outer: Int): g.NodeT = g get outer // look up 'outer' that is known to be contained

      val result = for {
        path <- ZIO.succeed(n(1) pathTo n(4))
        shortPath <- ZIO.succeed(n(3) shortestPathTo (n(1)))
      } yield (path, shortPath)

      assertZIO(result.map(_._1.isDefined))(equalTo(true))
      assertZIO(result.map(_._2.isDefined))(equalTo(true))

      assertZIO(result.map(_._1.get.mkString(" : ")))(equalTo("1 : 1~5 %3.0 : 5 : 3~5 %2.0 : 3 : 3~4 %1.0 : 4"))

      assertZIO(result.map(_._2.get.mkString(" : ")))(equalTo("3 : 3~4 %1.0 : 4 : 4~>5 %0.0 : 5 : 1~5 %3.0 : 1"))
      assertZIO(result.map(_._2.get.weight))(equalTo(4.0))

    },

    /**
     * @see https://www.tutorialspoint.com/design_and_analysis_of_algorithms/design_and_analysis_of_algorithms_shortest_paths.htm
     */
    test("Graph tutorial points") {
      val g: Graph[Int, WDiEdge] =
        Graph(
          1 ~> 2 % 5,
          1 ~> 3 % 2,
          2 ~> 4 % 3,
          2 ~> 5 % 7,
          3 ~> 2 % 2,
          3 ~> 7 % 9,
          4 ~> 3 % 3,
          4 ~> 5 % 2,
          4 ~> 7 % 6,
          5 ~> 6 % 8,
          5 ~> 7 % 5,
          5 ~> 8 % 7,
          6 ~> 9 % 4,
          7 ~> 8 % 2,
          8 ~> 6 % 3,
        )

      def n(outer: Int): g.NodeT = g get outer
      val result = for {
        shortPath <- ZIO.succeed(n(1) shortestPathTo (n(9)))
      } yield (shortPath)
      assertZIO(result.map(_.isDefined))(equalTo(true))
      assertZIO(result.map(_.get.weight))(equalTo(20d))
    }
  )
}
