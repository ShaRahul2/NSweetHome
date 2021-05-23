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
