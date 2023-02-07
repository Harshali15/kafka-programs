package com.github.Harshali15.tutorial1;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerGroups {
    public static void main(String[] args) {
        
        String bootstrap_server = "localhost:9092";
        String groupId = "my-fifth-application";
        String topic = "topic1";
        
        //Logger logger = LoggerFactory.getLogger(Consumer.class.getName());

        //1. create consumer configs/properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_server);
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); //earliest/latest/none

        //2. create consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(properties);
        
        //3. subscribe consumer to topic
        //consumer.subscribe(Collections.singleton("topic1"));   //to subscribe to single topic 
        //consumer.subscribe(Arrays.asList("topic1","topic2")); //subscribe to mutiple topics
        consumer.subscribe(Arrays.asList(topic));


        //4. poll for data
        //consumer does not get data until it asks for data
        while(true){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String,String> record : records) {
                System.out.println("Key" + record.key() +
                "\n" + "Value : "+ record.value() +
                "\n"+ "Offset : " + record.offset() + 
                "\n"+ "Partiton : "+ record.partition() 
                );
            }   
        }

        //note : consumer will read partition wise means read all data from each partiton at once 
        //before moving to next partition
        //when new data comes it is read in the order it arrives

    }
}
