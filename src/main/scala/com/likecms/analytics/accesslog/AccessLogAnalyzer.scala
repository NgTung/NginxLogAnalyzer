package com.likecms.analytics.accesslog

import com.google.inject.Guice
import com.likecms.components.kafka.Config
import com.likecms.components.kafka.Config._
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.log4j.Level

object AccessLogAnalyzer {

  def main(args: Array[String]): Unit = {
    setLogLevel(Level.ERROR) // Disable info logging

    val logAnalyzer = Guice
      .createInjector(new AccessLogModule)
      .getInstance(classOf[LogAnalyzer])

    logAnalyzer.createStream()
  }

  def setLogLevel(level: Level): Unit = {
    org.apache.log4j.Logger.getLogger("org").setLevel(level)
  }

  def kafkaProduce(): Unit = {
    val config = Config.getConfigDetail
    val producer = new KafkaProducer[String, String](config.toProperties)
    for (i <- 0 to 10) {
      val value = "v2" + Integer.toString(i)
      val topic = config.topics.headOption.getOrElse("")

      producer.send(new ProducerRecord[String, String](topic, value))
    }

    System.out.println("Message sent successfully")
    producer.close()
  }

}