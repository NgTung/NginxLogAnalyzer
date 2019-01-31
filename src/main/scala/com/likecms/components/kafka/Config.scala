package com.likecms.components.kafka

import java.util.Properties

case class Config(kafka: KafkaConfigDetail)
case class KafkaConfigDetail(
  topics: Set[String],
  bootstrap: Bootstrap,
  acks: String,
  retries: String,
  batch: Batch,
  linger: Linger,
  buffer: Buffer,
  key: Key,
  value: Value
)
case class Bootstrap(servers: String)
case class Batch(size: Int)
case class Linger(ms: Int)
case class Buffer(memory: Long)
case class Key(serializer: String)
case class Value(serializer: String)

object Config {
  import pureconfig.generic.auto._

  private val kafkaConfig = pureconfig.loadConfig[Config] match {
    case Left(e) => throw new Exception(e.head.description)
    case Right(config) => config
  }

  implicit class KafkaConfigToProperties[A](config: KafkaConfigDetail) {
    def toProperties: Properties = {
      val props = new Properties()
      props.put("bootstrap.servers", config.bootstrap.servers)
      props.put("topic", config.topics.headOption.getOrElse(""))
      props.put("acks", config.acks)
      props.put("retries", config.retries)
      props.put("batch.size", config.batch.size.toString)
      props.put("linger.ms", config.linger.ms.toString)
      props.put("buffer.memory", config.buffer.memory.toString)
      props.put("key.serializer", config.key.serializer)
      props.put("value.serializer", config.value.serializer)
      props
    }
  }

  def getConfigDetail: KafkaConfigDetail = kafkaConfig.kafka
}