***Creating Java Consumer**

1. create consumer configs/properties
Properties properties = new Properties();
properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_server);
properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); //earliest/latest/none

2. create consumer
KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(properties);

3. subscribe consumer to topic
//consumer.subscribe(Collections.singleton("topic1"));   //to subscribe to single topic 
//consumer.subscribe(Arrays.asList("topic1","topic2")); //subscribe to mutiple topics
consumer.subscribe(Arrays.asList(topic));

4. poll for data
   consumer does not get data until it asks for data

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

*note : consumer will read partition wise means read all data from each partiton at once before moving to next partition*
*when new data comes it is read in the order it arrives*

**Consumer groups**

*By changing group id you basically reset the application*

To run multiple consumers start multiple instances of the consumergroup.java. Start prodcuergroup.java. You will notice that each consumer will read specific partitions only. If one oro other consumer goes down then it is rebalanced amongst other consumers.