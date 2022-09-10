package com.topl.repository

import com.topl.model.{MeasurementKey, Util}
import zio.ZIO
import zio.test.Assertion._
import zio.test._

object FileRepositorySpec extends ZIOSpecDefault {
  override def spec = suite("FileRepositorySpec")(
    test("Parse partial sample data") {
      val result = for {
        parsed <- FileRepository.parse("partial-sample-data.json")
      } yield (parsed)
      assertZIO(result)(equalTo(Util.testTrafficMock))
    },

    // this test is to validate average measurement using partial data
    test("Average partial sample data 2") {
      val expected =
        Map(
          MeasurementKey("A1", "B1") -> BigDecimal(2), // avg of 1,2,3 = 6 / 3 = 2
          MeasurementKey("A2", "A1") -> BigDecimal(6)  // avg of 10,4,4 = 18 / 3 = 6
        )
      val result = for {
        average <- FileRepository.averageTraffic("partial-sample-data-2.json", file = None)
      } yield (average)
      assertZIO(result)(equalTo(expected))
    },

    // try to parse the whole file
    test("Parse sample data provided") {
      val size = for {
        start   <- ZIO.succeed(System.nanoTime)
        traffic <- FileRepository.parse("sample-data.json")
        end     <- ZIO.succeed(System.nanoTime)
        // Executed in 530 ms
        elapsed <- ZIO.succeed((end - start) / 1e9d)
        _       <- ZIO.logDebug(s"Time elapsed $elapsed")
      } yield (traffic.trafficMeasurements.size)
      assertZIO(size)(equalTo(10))
    },

    // this test is to validate average measurement using some keys of sample data provided
    test("Average sample data provided") {
      val k1 = MeasurementKey("A1", "B1")
      val k2 = MeasurementKey("A2", "A1")
      val expected = Map(
        k1 -> BigDecimal(35.32789981760104),
        k2 -> BigDecimal(58.34588078051082)
      )

      val result = for {
        average <- FileRepository.averageTraffic("sample-data.json", file = None)
        partialMap = Map((k1, average(k1)), (k2, average(k2)))
      } yield (partialMap)
      assertZIO(result)(equalTo(expected))
    }
  )
}
