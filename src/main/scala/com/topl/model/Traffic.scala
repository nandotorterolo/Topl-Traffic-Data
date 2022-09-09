package com.topl.model

import zio.json.{DeriveJsonDecoder, JsonDecoder}

case class Traffic(trafficMeasurements: Seq[TrafficMeasurements])

object Traffic {
  implicit val decoder: JsonDecoder[Traffic] = DeriveJsonDecoder.gen[Traffic]
}
