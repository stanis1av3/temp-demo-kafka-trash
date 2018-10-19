package com.example.demo.util.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by user on 13.10.2018.
 */
public class Consumer {

    private final static String bootstrap = "127.0.0.1:9092";
    private final static String topic = "first_topic";

    public static void main(String[] args) throws InterruptedException {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,"first_group");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(topic));


        while (true) {
            System.out.printf("GETTING DATA");

            //Thread.sleep(3000);
            ConsumerRecords<String, String> records = consumer.poll(100);
            records.forEach(r -> {
                System.out.println("partition " + r.partition());
                System.out.println("key" + r.key());
                System.out.println("value" + r.value());
            });
        }

    }
}
