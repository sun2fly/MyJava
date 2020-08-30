package com.mrfsong.message.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

@Slf4j
public class AppTest {

    private static String topicName1 = "test1_topic_string";
    private static String topicName2 = "test2_topic_string";

    public static void main(String[] args) {
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        for(int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<String, String>(topicName1, Integer.toString(i), Integer.toString(i)));

            producer.send(new ProducerRecord<String, String>(topicName2, Integer.toString(i), Integer.toString(i)));
        }

       log.info("Message sent successfully");
        producer.close();
    }


}
