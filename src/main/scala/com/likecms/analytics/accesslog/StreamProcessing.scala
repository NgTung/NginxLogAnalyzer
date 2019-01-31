package com.likecms.analytics.accesslog

trait StreamProcessing[T] {

  def getStreamContext: T

  def run(): Unit

}
