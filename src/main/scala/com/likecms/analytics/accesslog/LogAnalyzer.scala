package com.likecms.analytics.accesslog

trait LogAnalyzer {
  type T

  def createStream(): Unit

  def getPageView(accessLogRecord: T): Long

  def getUniqueVisitor(accessLogRecord: T): Long

}
