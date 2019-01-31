name := "access_log_analytics"

version := "0.1"

scalaVersion := "2.11.7"

val sparkVersion = "2.4.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-streaming-kafka" % "1.6.3",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,
  "datastax" % "spark-cassandra-connector" % "2.4.0-s_2.11",
  "com.typesafe" % "config" % "1.3.1",
  "com.google.inject" % "guice" % "2.0",
  "org.apache.kafka" % "kafka-clients" % "0.10.0.0",
  "com.github.pureconfig" %% "pureconfig" % "0.10.1"
)

fork in run := true