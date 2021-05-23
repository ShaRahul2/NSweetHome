package com.upgrad.notification.kafka;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

public class Consumerloop implements Runnable {
	private final KafkaConsumer<String, String> consumer;
	private final List<String> topics;
	private final int id;

	public Consumerloop(int id, String groupId, List<String> topics) {
		this.id = id;
		this.topics = topics;
		Properties props = new Properties();
		props.put("bootstrap.servers", "54.242.164.154:9092");
		props.put("group.id", groupId);
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		this.consumer = new KafkaConsumer<>(props);
	}

	@Override
	public void run() {
		try {
			consumer.subscribe(topics);

			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
				for (ConsumerRecord<String, String> record : records) {
//	          Map<String, Object> data = new HashMap<>();
//	          data.put("partition", record.partition());
//	          data.put("offset", record.offset());
//	          data.put("value", record.value());
					System.out.println(record.value());
					consumer.commitAsync();
				}
			}
		} catch (WakeupException e) {
			// ignore for shutdown
		} finally {
			consumer.close();
		}
	}

	public void shutdown() {
		consumer.wakeup();
	}
}