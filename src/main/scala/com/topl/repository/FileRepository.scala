package com.topl.repository

import com.topl.model.{MeasurementKey, Traffic}
import java.net.URI
import scala.math.BigDecimal.{RoundingMode, int2bigDecimal}
import zio.ZIO
import zio.json.DecoderOps
import zio.stream.{ZSink, ZStream}

object FileRepository {

  private def parseFileFromFile(uri: URI): ZStream[Any, Throwable, String] = {
    ZStream
      .fromIteratorScoped(
        ZIO
          .fromAutoCloseable(
            ZIO.attempt(scala.io.Source.fromFile(uri))
          )
          .map(_.getLines())
      )
  }

  private def parseFileFromResource(fileName: String): ZStream[Any, Throwable, String] = {
    ZStream
      .fromIteratorScoped(
        ZIO
          .fromAutoCloseable(
            ZIO.attempt(scala.io.Source.fromResource(fileName))
          )
          .map(_.getLines())
      )
  }

  private[repository] def parse(fileName: String, file: Option[URI] = None): ZIO[Any, Throwable, Traffic] = {
    file
      .map(parseFileFromFile)
      .getOrElse(parseFileFromResource(fileName))
      .run(ZSink.collectAll[String])
      .map(_.mkString)
      .map(_.fromJson[Traffic])
      .flatMap {
        case Left(value)  => ZIO.fail(new IllegalStateException(value))
        case Right(value) => ZIO.succeed(value)
      }

  }

  def averageTraffic(
      fileName: String,
      file: Option[URI]
  ): ZIO[Any, Throwable, Map[MeasurementKey, BigDecimal]] = {
    parse(fileName, file)
      .map(
        _.trafficMeasurements
          .flatMap(_.measurements)
          .map(m => (m.measurementKey, m.transitTime))
          .groupBy(_._1)
          .map { case (k, v) => (k, v.map(_._2)) }
          .map { case (k, values) =>
            val (sum, length) =
              values.foldLeft((BigDecimal(0), 0)) { case ((time, length), acc) =>
                (acc + time, 1 + length)
              }
            // TODO scale should be on config file, need it for flaky unit test
            val avg = (sum / length).setScale(14, RoundingMode.HALF_UP)
            (k, avg)
          }
          .toMap
      )

  }

}
