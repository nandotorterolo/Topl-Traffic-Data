package com.topl.model

import zio.json.{DeriveJsonDecoder, JsonDecoder}

case class TrafficMeasurements(measurementTime: Int, measurements: Seq[Measurement])

object TrafficMeasurements {
  implicit val decoder: JsonDecoder[TrafficMeasurements] = DeriveJsonDecoder.gen[TrafficMeasurements]
}
