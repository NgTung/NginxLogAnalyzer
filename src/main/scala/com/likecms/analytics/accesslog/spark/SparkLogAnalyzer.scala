package com.likecms.analytics.accesslog.spark

import com.likecms.analytics.accesslog.AccessLogRecord
import com.likecms.analytics.accesslog.LogAnalyzer
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import com.datastax.spark.connector._
import com.datastax.spark.connector.writer.WriteConf
import com.likecms.components.kafka.Config
import com.likecms.components.kafka.Config._
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.KafkaUtils

import scala.collection.JavaConverters._

class SparkLogAnalyzer extends LogAnalyzer {
  override type T = RDD[AccessLogRecord]

  private val conf: SparkConf = new SparkConf()
    .setMaster("local[2]")
    .setAppName("likecms-log-anaylytics")

  implicit val sparkContext: SparkContext = new SparkContext(conf)

  override def createStream(): Unit = {
    val streamContext: StreamingContext = new StreamingContext(sparkContext, Seconds(2))

    val kafkaConfig = Config.getConfigDetail
    val kafkaParams: Map[String, String] = Config.getConfigDetail.toProperties.asScala.toMap
    val kafkaInputStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      streamContext,
      kafkaParams,
      kafkaConfig.topics
    )

    pageViewStream(kafkaInputStream)

    streamContext.start()
    streamContext.awaitTermination()
  }

  def pageViewStream(inputStream: InputDStream[_]): Unit = SparkParser.fromStream(inputStream)
    .map(logRecord => (logRecord.request.fold("")(u => u.uri), 1))
    .reduceByKey(_ + _)
    .foreachRDD(rdd => saveToCassandra(rdd, "logaggregation", "page_access", SomeColumns("uri", "views")))

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

  private def saveToCassandra(inputRDD: RDD[(String, Int)], keySpace: String, tableName: String, columns: SomeColumns): Unit = {
    if (!inputRDD.isEmpty()) {
      inputRDD.saveToCassandra(keySpace, tableName, columns, writeConf = WriteConf(ifNotExists = true))
    }
  }

}
