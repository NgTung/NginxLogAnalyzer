package com.likecms.analytics.accesslog

import com.google.inject.Guice
import org.apache.log4j.Level

object AccessLogAnalyzer {

  def main(args: Array[String]): Unit = {
    setLogLevel(Level.ERROR) // Disable info logging

    val logAnalyzer = Guice
      .createInjector(new AccessLogModule)
      .getInstance(classOf[LogAnalyzer])

    val accessLogRecords = logAnalyzer.setLogFile(args(0))

    println("Total page views: " + logAnalyzer.getPageView(accessLogRecords))
    println("Total unique visitors: " + logAnalyzer.getUniqueVisitor(accessLogRecords))
  }

  def setLogLevel(level: Level): Unit = {
    org.apache.log4j.Logger.getLogger("org").setLevel(level)
  }

}