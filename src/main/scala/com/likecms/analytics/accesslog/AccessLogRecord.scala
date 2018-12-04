package com.likecms.analytics.accesslog

import java.util.Date

/**
  * Nginx log format
  *
  * Eg:
  * log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
  *                   '$status $body_bytes_sent "$http_referer" '
  *                   '"$http_user_agent" "$http_x_forwarded_for"';
  *
  * @param remoteIpAddress   String
  * @param remoteUser        String
  * @param dateTime          String
  * @param request           String
  * @param httpStatusCode    String
  * @param bytesSent         String
  * @param referer           String
  * @param userAgent         String
  * @param httpXForwardedFor String
  */
case class AccessLogRecord(
  remoteIpAddress: String,        // should be an ip address, but may also be the hostname if hostname-lookups are enabled
  remoteUser: String,             // typically `-`
  dateTime: Option[Date],         // [day/month/year:hour:minute:second zone]
  request: Option[RequestLog],    // `GET /foo/bar HTTP/1.1`
  httpStatusCode: String,         // 200, 404, etc.
  bytesSent: String,              // may be `-`
  referer: String,                // where the visitor came from
  userAgent: String,              // long string to represent the browser and OS
  httpXForwardedFor: String       // http_x_forwarded_for
)

case class RequestLog(method: String, uri: String, httpVersion: String)

object RequestLog {
  def apply(string: String): Option[RequestLog] = string.split("\\s+") match {
    case Array(m, u, v) => Some(RequestLog(m, u, v))
    case _ => None
  }
}