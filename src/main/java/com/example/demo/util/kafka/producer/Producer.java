package com.example.demo.util.kafka.producer;

import com.example.demo.transport.StackOverFlowSurvey;
import com.example.demo.util.csv.CsvRunner;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Created by user on 12.10.2018.
 */
public class Producer {

    static Logger logger = LoggerFactory.getLogger(Producer.class);

    private final static String bootstrap = "127.0.0.1:9092";

    public static void main(String[] args) throws InterruptedException {

        CsvRunner csvRunner = new CsvRunner();

        BlockingQueue<StackOverFlowSurvey> surveys = csvRunner.getSurveyData();

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        Random random = new Random();

//        int a = 0;
//        while (a++<100) {
//
//            Thread.sleep(1000);
//            ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic", "key"+random.nextBoolean(), "generated value"+a);
//
//            producer.send(record, (metadata, exception) -> {
//                logger.info("partition: {}", metadata.partition());
//                logger.info("offset: {}", metadata.offset());
//                logger.info("timestamp: {}", metadata.timestamp());
//            });
//
//            producer.flush();
//
//        }

        while (true) {

            StackOverFlowSurvey survey = surveys.take();

            if (survey != null) {

                System.out.println("ready to send survey, queue size: " + (surveys.size() + 1));
                ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic", "key" + random.nextBoolean(), survey.toString());

                producer.send(record);

                producer.flush();

            } else {
                logger.error("survey is null!!!!!!!");
            }


        }
        // producer.close();

    }
}
