package com.topl.model

import zio.json._

final case class MeasurementKey(from: String, to: String)

final case class Measurement(
    startAvenue: String,
    startStreet: String,
    transitTime: BigDecimal,
    endAvenue: String,
    endStreet: String
) {
  def measurementKey: MeasurementKey = MeasurementKey(from = s"$startAvenue$startStreet", to = s"$endAvenue$endStreet")
}

object Measurement {
  implicit val decoder: JsonDecoder[Measurement] = DeriveJsonDecoder.gen[Measurement]
}
