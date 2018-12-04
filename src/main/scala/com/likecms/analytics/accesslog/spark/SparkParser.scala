package com.likecms.analytics.accesslog.spark

import com.likecms.analytics.accesslog.AccessLogRecord
import com.likecms.analytics.accesslog.AccessLogParser
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object SparkParser {
  val redundantUri = List(
    "apple-touch-icon",
    "alive.html"
  )

  def fromFile(path: String)(implicit sparkContext: SparkContext): RDD[AccessLogRecord] = {
    val textFile = sparkContext.textFile(path)
    val parser = new AccessLogParser
    textFile.flatMap(line => line.split("\n"))
      .map(parser.parseRecordReturningNullObjectOnFailure)
      .filter(record => !redundantUri.exists(record.request.fold("")(req => req.uri).contains))
  }

}
