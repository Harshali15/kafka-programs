**Java consumer with threads**
1. Go through he ConsumerWithThreads.java code / video for the same

**Assign seek**
1. assign and seek are used to replay data or fetch a speicifc message

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