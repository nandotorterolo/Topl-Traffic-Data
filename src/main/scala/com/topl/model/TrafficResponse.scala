package com.topl.model

import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class TrafficResponse(nodes: String, edges: String, weight: Double)

object TrafficResponse {
  implicit val encode: JsonEncoder[TrafficResponse] = DeriveJsonEncoder.gen[TrafficResponse]
}
