package com.topl.model

import zio.ZIO
import zio.json._
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assertZIO}

object JsonSpec extends ZIOSpecDefault {
  override def spec = suite("JsonSpec")(
    test("Json Spec validation") {
      val partialSampleData =
        """
          |{
          | "trafficMeasurements": [
          |   {
          |      "measurementTime": 86544,
          |      "measurements": [
          |        {
          |          "startAvenue": "A",
          |          "startStreet": "1",
          |          "transitTime": 28.000987663134676,
          |          "endAvenue": "B",
          |          "endStreet": "1"
          |        },
          |        {
          |          "startAvenue": "A",
          |          "startStreet": "2",
          |          "transitTime": 59.71131185379898,
          |          "endAvenue": "A",
          |          "endStreet": "1"
          |        }
          |    ]
          |   },
          |   {
          |      "measurementTime": 86575,
          |      "measurements": [
          |        {
          |          "startAvenue": "A",
          |          "startStreet": "1",
          |          "transitTime": 28.000987663134676,
          |          "endAvenue": "B",
          |          "endStreet": "1"
          |        },
          |        {
          |          "startAvenue": "A",
          |          "startStreet": "2",
          |          "transitTime": 59.71131185379898,
          |          "endAvenue": "A",
          |          "endStreet": "1"
          |        }
          |    ]
          |   }
          | ]
          |}
          |""".stripMargin

      val result = for {
        json <- ZIO.attempt(partialSampleData.fromJson[Traffic])
        res  <- ZIO.fromEither(json)
      } yield res

      val expected = Traffic(
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
      assertZIO(result)(equalTo(expected))

    }
  )
}
