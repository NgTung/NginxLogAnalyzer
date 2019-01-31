package com.likecms.analytics.accesslog.spark

case class SparkConfig(
  appName: String,
  master: String,
  batchDuration: Int
)
