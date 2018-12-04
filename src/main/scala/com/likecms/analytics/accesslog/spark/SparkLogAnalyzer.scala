package com.likecms.analytics.accesslog.spark

import com.likecms.analytics.accesslog.AccessLogRecord
import com.likecms.analytics.accesslog.LogAnalyzer
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

class SparkLogAnalyzer extends LogAnalyzer {
  override type T = RDD[AccessLogRecord]

  private val conf: SparkConf = new SparkConf()
    .setMaster("local")
    .setAppName("likecms-log-anaylytics")

  implicit val sparkContext: SparkContext = new SparkContext(conf)

  override def setLogFile(path: String): RDD[AccessLogRecord] = SparkParser.fromFile(path)

  override def getPageView(accessLogRecord: RDD[AccessLogRecord]): Long = {
    accessLogRecord
      .map(record => (record.request.fold("")(req => req.uri), 1))
      .map { case (_, pageView) => pageView } // page view by uri
      .reduce(_ + _) // total page view
  }

  override def getUniqueVisitor(accessLogRecord: RDD[AccessLogRecord]): Long = {
    accessLogRecord
      .map(record => (record.httpXForwardedFor, 1))
      .reduceByKey(_ + _)
      .count()
  }
}
