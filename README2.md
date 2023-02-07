*****Creating Java producer****

There are 3 steps in creating a producer:
1. Create producer properties
2. Create producer
3. Send data
4. flush data

1. Create producer properties

String bootstrap_server = "localhost:9092";

Properties properties = new Properties();
properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_server);
properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

2. Create producer
KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

//create a producer record
ProducerRecord<String,String> record = new ProducerRecord<String,String>("topic1","trying java kafka 2");

3. send data --asynchronous- means it is happening in the background so you have to wait, flush()
producer.send(record);

4. flush data
producer.flush();

5. flush and close producer
producer.close();

5. Now connect to your kafka zookeper and server and open a consumer which listens to topic 1. When you run App.java now you will see the message appearing in the consumer

*****Producer with callback****
To understand where the message was produced, partiton no, timestamp, offset etc. 
In the producer.send function add a callback and print out reqd info
*Note there is no ordering of messages received in consumer because we are not passing any key*
*messages are being produced to 2 partitons and are ordered within partition*

****Producer with keys****
Add topic,key,value instead of topic,value in ProducerRecord
*ProducerRecord<String,String> record = new ProducerRecord<String,String>(topic,key,value);*
Use .get() with send to make the send synchronous. **Not to be used in Production at all ***

// data with specific keys will always go to same partiton now
// eg: id 0,1,2,3,5,7,8 -->partition 1
// eg: id 4,6,9 -->partition 0