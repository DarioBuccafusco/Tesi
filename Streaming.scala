package com.dbuccafusco.streaming

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._

object Streaming{

  val kafkaParams = Map[String,Object](
    "bootstrap.servers" -> "http://tesi-worker-02.northeurope.cloudapp.azure.com:9092,http://tesi-worker-03.northeurope.cloudapp.azure.com:9092, http://tesi-worker-04.northeurope.cloudapp.azure.com",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "use_a_separate_group_id_for_each_stream",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )
  val topics: Array[String] = Array("jsontest")

  val conf = new SparkConf().setAppName("Streaming").setMaster("http://tesi-worker-04.northeurope.cloudapp.azure.com")
  val ssc = new StreamingContext(conf,Seconds(1))

  val stream = KafkaUtils.createDirectStream[String, String](
    ssc,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )

  stream.map(record => (record.key, record.value))

}
