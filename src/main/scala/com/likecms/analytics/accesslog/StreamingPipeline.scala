package com.likecms.analytics.accesslog

trait StreamingPipeline[T] {

  def getStream: T

}
