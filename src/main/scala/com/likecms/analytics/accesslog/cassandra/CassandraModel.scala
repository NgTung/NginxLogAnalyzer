package com.likecms.analytics.accesslog.cassandra

import com.datastax.spark.connector.SomeColumns
import org.apache.spark.rdd.RDD

trait CassandraModel[T] {
  protected val keyspace: String
  protected val tableName: String
  protected val columns: SomeColumns

  def load: T

  def save(rdd: RDD[T]): Unit

}
