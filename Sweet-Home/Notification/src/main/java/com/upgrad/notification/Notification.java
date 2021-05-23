package com.upgrad.notification;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.upgrad.notification.kafka.Consumerloop;

public class Notification {

//	 public static void main(String[] args) {
		 
//		 	        Properties properties = new Properties();
//	        properties.put("bootstrap.servers", "54.242.164.154:9092");
//	        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//	        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//	        properties.put("group.id", "test-group");
//
//	        KafkaConsumer kafkaConsumer = new KafkaConsumer(properties);
//	        List topics = new ArrayList();
//	        topics.add("message");
//	        kafkaConsumer.subscribe(topics);
//	        try{
//	        	while (true) {
//					ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(10));
//					for (ConsumerRecord<String, String> record : records) {
////		          Map<String, Object> data = new HashMap<>();
////		          data.put("partition", record.partition());
////		          data.put("offset", record.offset());
////		          data.put("value", record.value());
//						System.out.println(record.value());
//						kafkaConsumer.commitAsync();
//					}
////	            while (true){
////	                ConsumerRecords records = kafkaConsumer.poll(Duration.ofSeconds(10));
////	                for (ConsumerRecord record: records){
////	                    System.out.println(String.format("Topic - %s, Partition - %d, Value: %s", record.topic(), record.partition(), record.value()));
////	                }
//	            }
//	        }catch (Exception e){
//	            System.out.println(e.getMessage());
//	        }finally {
//	            kafkaConsumer.close();
//	        }
//	    }
	
	public static void main(String[] args) { 
		  int numConsumers = 1;
		  String groupId = "upgrad-group";
		  List<String> topics = Arrays.asList("message");
		  ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

		  final List<Consumerloop> consumers = new ArrayList<>();
		  for (int i = 0; i < numConsumers; i++) {
			  Consumerloop consumer = new Consumerloop(i, groupId, topics);
		    consumers.add(consumer);
		    executor.submit(consumer);
		  }

		  Runtime.getRuntime().addShutdownHook(new Thread() {
		    @Override
		    public void run() {
		      for (Consumerloop consumer : consumers) {
		    	  consumer.shutdown();
		      } 
		      executor.shutdown();
		      try {
		        executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
		      } catch (InterruptedException e) {
		       System.out.println(e.getMessage());
		       e.getMessage();
		      }
		    }
		  });
		}
}
