package com.github.Harshali15.tutorial1;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public final class Producer {

    public static void main(String[] args) {
        //System.out.println("Hello World!");
        String bootstrap_server = "localhost:9092";

        //1. Create producer properties

        Properties properties = new Properties();
        // properties.setProperty("bootstrap.servers", bootstrap_server);
        // properties.setProperty("key.serializer", StringSerializer.class.getName());
        // properties.setProperty("value.serializer", StringSerializer.class.getName());

        //newer better way to user
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_server);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //2. Create producer
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        //create a producer record
        ProducerRecord<String,String> record = new ProducerRecord<String,String>("topic1","trying java kafka 2");

        //3.send data --asynchronous- means it is happening in the background so you have to wait, flush()
        producer.send(record);

        // flush data
        producer.flush();
        //flush and close producer
        producer.close();

    }
}
