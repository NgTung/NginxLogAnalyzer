package com.likecms.analytics.accesslog.spark

import com.likecms.analytics.accesslog.AccessLogRecord
import com.likecms.analytics.accesslog.AccessLogParser
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream

object SparkParser {
  val redundantUri = List(
    "apple-touch-icon",
    "alive.html"
  )
  val parser = new AccessLogParser

  def fromRDDString(rdd: RDD[String])(implicit sparkContext: StreamingContext): RDD[AccessLogRecord] = {
    rdd
      .flatMap(line => line.split("\n"))
      .map(parser.parseRecordReturningNullObjectOnFailure)
      .filter(record => !redundantUri.exists(record.request.fold("")(req => req.uri).contains))
  }

  def fromStream(stream: DStream[_]): DStream[AccessLogRecord] = {
    stream
      .map { case (_, value: String) => parser.parseRecordReturningNullObjectOnFailure(value) }
      .filter(record => !redundantUri.exists(record.request.fold("")(req => req.uri).contains))
  }

}
