package com.dbuccafusco.streaming

import org.apache.kudu.spark.kudu._
import org.apache.kudu.client._

import collection.JavaConverters._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.ConsumerStrategies._
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.streaming.kafka010._

object Streaming extends App {
  //KAFKA
  val kafkaParams = Map[String,Object](
      "bootstrap.servers" -> "http://tesi-worker-02.northeurope.cloudapp.azure.com:9092,http://tesi-worker-03.northeurope.cloudapp.azure.com:9092, http://tesi-worker-04.northeurope.cloudapp.azure.com",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "1",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)

  )
  val topics = Array("jsontest")

  val conf = new SparkConf().setAppName("Streaming").setMaster("local[2]")
 // val ssc = new StreamingContext(conf,Seconds(1))
  val sc = new SparkContext(conf)
/*
  val stream = KafkaUtils.createDirectStream[String, String](
    ssc,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )
  stream.map(record => (record.key, record.value))

*/
  //KUDU
  val kuduContext = new KuduContext("http://tesi-worker-02.northeurope.cloudapp.azure.com:7051", sc)

}
