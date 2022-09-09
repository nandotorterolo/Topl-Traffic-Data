package com.topl.model

import scalax.collection.GraphPredef.EdgeAssoc
import scalax.collection.edge.WDiEdge
import scalax.collection.edge.Implicits._

object GraphNodes {

  /** requirement does not talk about upper bound, assumptions here
    */
  val nodes: Seq[String] = for {
    avenue <- 'A' to 'Z'
    street <- 1 to 99
  } yield s"$avenue$street"

  def edgeFactory(key_time: (MeasurementKey, BigDecimal)): WDiEdge[String] =
    key_time._1.from ~> key_time._1.to % key_time._2.doubleValue

}
