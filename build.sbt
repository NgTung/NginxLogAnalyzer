name := "access_log_analytics"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.0",
  "com.typesafe" % "config" % "1.3.1",
  "com.google.inject" % "guice" % "2.0"
)

fork in run := true