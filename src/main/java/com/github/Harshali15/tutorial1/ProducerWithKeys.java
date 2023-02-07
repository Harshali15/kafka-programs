package com.github.Harshali15.tutorial1;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProducerWithKeys {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Logger logger = LoggerFactory.getLogger(ProducerWithKeys.class);

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
            String value = "hello world"+ Integer.toString(i);
            String topic = "topic1";
            String key = "id_" + Integer.toString(i);
            logger.info("key: "+ i);
            //create a producer record
            ProducerRecord<String,String> record = new ProducerRecord<String,String>(topic,key,value);

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
            }).get(); //force the send to be synchronous by using get() it will add throws etc., DONT DO THIS IN PRODUCTION

            //data with follwing keys will always go to same partiton now
            // id 0,1,2,3,5,7,8 -->partition 1
            //id 4,6,9 -->partition 0
            
        }
        // flush data
        producer.flush();
        //flush and close producer
        producer.close();

    }
}
