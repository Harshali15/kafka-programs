1. Create a new maven project (make sure jdk id 1.8)

2. Inside the pom.xml file add these two dependencies. We are using kafka-cleints 2.0.x and slf4j simple 1.7.25

<!-- https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients -->
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
        <version>2.0.0</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-simple -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>1.7.25</version>
        <!-- <scope>test</scope> -->
    </dependency>

3. Comment the <scope>test</scope> in the dependency for slf4j
4. Create a package inside src/main/java (com.github.Harshali15.tutorial1)
5. Create a java file under this package (App.java)
6. Write a simple hello world code. It should run successfully.