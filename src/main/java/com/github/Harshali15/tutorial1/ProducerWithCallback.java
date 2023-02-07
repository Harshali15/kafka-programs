package com.github.Harshali15.tutorial1;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProducerWithCallback {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ProducerWithCallback.class);

        String bootstrap_server = "localhost:9092";

        //1. Create producer properties
        Properties properties = new Properties();

        //newer better way to user
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_server);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //2. Create producer
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        for(int i =0;i<10;i++){
        //create a producer record
            ProducerRecord<String,String> record = new ProducerRecord<String,String>("topic1","hello world"+ Integer.toString(i));

            //3.send data --asynchronous- means it is happening in the background so you have to wait, flush()
            producer.send(record, new Callback() {
                //executes every time a record is successfully sent or error is thrown
                public void onCompletion(RecordMetadata recordMetaData, Exception e){
                    if(e == null){  //suucessfully produced
                        System.out.println("Received new metadata - " +
                        "\n"+ "Topic : " +recordMetaData.topic() +
                        "\n"+ "Partiton : "+ recordMetaData.partition() +
                        "\n"+ "Offset : " + recordMetaData.offset() +
                        "\n"+ "Timestamp : "+ recordMetaData.timestamp()
                        );
                    }
                    else{  //exception occured 
                        logger.error("Error occured", e);
                    }
                }
            });
        }
        // flush data
        producer.flush();
        //flush and close producer
        producer.close();

        //Note there is no ordering of messages received in consumer because we are not passing any key
        //messages are being produced to 2 partitons and are ordered within partition
    }
}
