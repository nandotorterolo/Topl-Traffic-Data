package com.topl.model

object Util {

  val testTrafficMock: Traffic =
    Traffic(
      trafficMeasurements = List(
        TrafficMeasurements(
          measurementTime = 86544,
          measurements = List(
            Measurement(
              startAvenue = "A",
              startStreet = "1",
              transitTime = 28.000987663134676,
              endAvenue = "B",
              endStreet = "1"
            ),
            Measurement(
              startAvenue = "A",
              startStreet = "2",
              transitTime = 59.71131185379898,
              endAvenue = "A",
              endStreet = "1"
            )
          )
        ),
        TrafficMeasurements(
          measurementTime = 86575,
          measurements = List(
            Measurement(
              startAvenue = "A",
              startStreet = "1",
              transitTime = 28.000987663134676,
              endAvenue = "B",
              endStreet = "1"
            ),
            Measurement(
              startAvenue = "A",
              startStreet = "2",
              transitTime = 59.71131185379898,
              endAvenue = "A",
              endStreet = "1"
            )
          )
        )
      )
    )

}
