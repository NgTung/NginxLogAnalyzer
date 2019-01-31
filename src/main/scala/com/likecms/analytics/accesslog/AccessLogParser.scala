package com.likecms.analytics.accesslog

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern

import scala.util.control.Exception._

class AccessLogParser extends Serializable {
  private val ddd = "\\d{1,3}"                      // at least 1 but not more than 3 times (possessive)
  private val ip = s"($ddd\\.$ddd\\.$ddd\\.$ddd)?"  // eg: `123.456.7.89`
  private val forwardIp = "\"(.*?)\""               // eg: `"123.456.7.89"`
  private val user = "(\\S+)"
  private val dateTime = "(\\[.+?\\])"              // like `[21/Jul/2009:02:48:13 -0700]`
  private val request = "\"(.*?)\""                 // any number of any character, reluctant
  private val status = "(\\d{3})"
  private val bytes = "(\\S+)"                      // this can be a "-"
  private val referer = "\"(.*?)\""
  private val agent = "\"(.*?)\""
  private val regex = s"$ip - $user $dateTime $request $status $bytes $referer $agent $forwardIp"
  private val p = Pattern.compile(regex)
  private val (
    remoteIpIdx,
    remoteUserIdx,
    dateTimeIdx,
    requestUriIdx,
    statusCodeIdx,
    bytesSentIdx,
    refererIdx,
    userAgentIdx,
    httpXForwardedForIdx
  ) = (1, 2, 3, 4, 5, 6, 7, 8, 9)

  def parseRecord(record: String): Option[AccessLogRecord] = {
    val matcher = p.matcher(record)
    if (matcher.find) {
      Some(buildAccessLogRecord(matcher))
    } else {
      None
    }
  }

  def parseRecordReturningNullObjectOnFailure(record: String): AccessLogRecord = {
    val matcher = p.matcher(record)
    if (matcher.find) {
      buildAccessLogRecord(matcher)
    } else {
      AccessLogParser.nullObject
    }
  }

  private def buildAccessLogRecord(matcher: Matcher): AccessLogRecord = AccessLogRecord(
    remoteIpAddress = matcher.group(remoteIpIdx),
    remoteUser = matcher.group(remoteUserIdx),
    dateTime = AccessLogParser.parseDateField(matcher.group(dateTimeIdx)),
    request = RequestLog(matcher.group(requestUriIdx)),
    httpStatusCode = matcher.group(statusCodeIdx),
    bytesSent = matcher.group(bytesSentIdx),
    referer = matcher.group(refererIdx),
    userAgent = matcher.group(userAgentIdx),
    httpXForwardedFor = matcher.group(httpXForwardedForIdx)
  )
}

object AccessLogParser {
  val nullObject = AccessLogRecord(
    remoteIpAddress = "",
    remoteUser = "",
    dateTime = None,
    request = None,
    httpStatusCode = "",
    bytesSent = "",
    referer = "",
    userAgent = "",
    httpXForwardedFor = ""
  )

  def parseDateField(field: String): Option[Date] = {
    val dateRegex = "\\[(.*?) .+]"
    val datePattern = Pattern.compile(dateRegex)
    val dateMatcher = datePattern.matcher(field)

    if (dateMatcher.find) {
      val dateString = dateMatcher.group(1)
      val dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH)
      allCatch.opt(dateFormat.parse(dateString))
    } else None
  }

}