package com.likecms.analytics.accesslog

import com.google.inject.AbstractModule
import com.likecms.analytics.accesslog.spark.SparkLogAnalyzer

class AccessLogModule extends AbstractModule {
  @Override
  protected def configure() {
    bind(classOf[LogAnalyzer]).to(classOf[SparkLogAnalyzer])
  }
}