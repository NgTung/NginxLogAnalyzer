package com.likecms.analytics.accesslog

trait LogAnalyzer {
  type T

  def setLogFile(path: String): T

  def getPageView(accessLogRecord: T): Long

  def getUniqueVisitor(accessLogRecord: T): Long

}
