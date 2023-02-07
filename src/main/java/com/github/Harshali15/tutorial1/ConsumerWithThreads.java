package com.github.Harshali15.tutorial1;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerWithThreads {

    private ConsumerWithThreads(){

    }
    public static void main(String[] args) {
        
        new ConsumerWithThreads().run();

    }
    
    private void run(){
        String groupId = "my-sixth-application";
        String topic = "topic1";
        String bootstrap_server = "localhost:9092";

        //latch for dealing with multiple threads
        CountDownLatch latch = new CountDownLatch(1);
        Logger logger = LoggerFactory.getLogger(ConsumerWithThreads.class.getName());
        
        //create consumer runnable
        logger.info("Creating consumer thread");
        Runnable myConsumerRunnable = new ConsumerRunnable(bootstrap_server, groupId, topic, latch);
        
        //start the thread
        Thread myThread = new Thread(myConsumerRunnable);
        myThread.start();

        //add a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            logger.info("SHutdown hook");
            ((ConsumerRunnable)myConsumerRunnable).shutdown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("Application has exited");
        }
        ));

        try {
            latch.await();
        } catch (InterruptedException e) {
            //e.printStackTrace();
            logger.error("Application interrupted", e);
        }finally{
            logger.info("Application is closing");
        }
    }
  

    public class ConsumerRunnable implements Runnable{
        // to deal with concurrency in java
        private CountDownLatch latch;
        private KafkaConsumer<String,String> consumer;
        private Logger logger = LoggerFactory.getLogger(ConsumerRunnable.class.getName());

        ConsumerRunnable(String bootstrap_server, String groupId,String topic,CountDownLatch latch){
            this.latch=latch;
            
            //1. create consumer configs/properties
            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_server);
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); //earliest/latest/none
        
            //2. create consumer
            consumer = new KafkaConsumer<String,String>(properties);
        
            //3. subscribe consumer to topic
            consumer.subscribe(Arrays.asList(topic));
        }

        @Override
        public void run(){
            //4. poll for data
            try{
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
            }catch(WakeupException w){
                logger.info("Received shutdown signal.");
            }finally{
                consumer.close();
                latch.countDown();  //tell our main code we are done with consumer
            }
        }

        //shutdown our consumer threads
        public void shutdown(){
            //wakeup()is a special method t interrupt consumer.poll() 
            // it will throw exception WakeUpException
            consumer.wakeup();
        }
    }
}
