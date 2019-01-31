package com.likecms.analytics.accesslog.cassandra

import com.datastax.spark.connector.SomeColumns
import org.apache.spark.rdd.RDD
import com.datastax.spark.connector._

case class Pageviews(id: String, views: Long)

object Pageviews extends CassandraModel[Pageviews] {
  override protected val keyspace: String = "test"
  override protected val tableName: String = "pageviews"
  override protected val columns: SomeColumns = SomeColumns("key", "value")

  override def load: Pageviews = ???

  override def save(rdd: RDD[Pageviews]): Unit = {
    rdd.saveToCassandra(keyspace, tableName, columns)
  }
}
