package com.github.Harshali15.tutorial1;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerAssignSeek {
    public static void main(String[] args) {
        
        String bootstrap_server = "localhost:9092";
        String topic = "topic1";
        
        //1. create consumer configs/properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_server);
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); //earliest/latest/none

        //2. create consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(properties);
        
        //assign and seek are used to replay data or fetch a speicifc message
        //assign
        TopicPartition partitionToReadFrom = new TopicPartition(topic, 0);
        long offsetToReadFrom = 15L;
        consumer.assign(Arrays.asList(partitionToReadFrom));

        //seek
        consumer.seek(partitionToReadFrom,offsetToReadFrom);

        int numMessagesToRead = 6;
        boolean keepReading = true;
        int numOfMessagesReadAlready = 0;
        while(keepReading){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String,String> record : records) {
                numOfMessagesReadAlready++;
                System.out.println("Key" + record.key() +
                "\n" + "Value : "+ record.value() +
                "\n"+ "Offset : " + record.offset() + 
                "\n"+ "Partiton : "+ record.partition() 
                );
                if(numOfMessagesReadAlready>numMessagesToRead){
                    keepReading=false;
                    break;
                }
            }   
        }

    }
}
