this is a proof of concept high rate processing project
# Components
|component|description| 
|---------|-----------|
|receiver|receives http post requests with jsons. Stores requests into cassandra and forwards them to queue for processing|
|processor|processes request from queue and calculates statistics. Statistics are published to fanout queue for publishing to the clients|
|web|web endpoint which server static html resources and publishes statistics to the cliens (WebSocket with STOMP)
|reader|helper component which reads messages from cassandra and publishes them for processing|
|sender|helper component that creates random messages and sends them to receiver via HTTP|

# Tech stack
* Netty - used by receiver to accept requests
* Cassandra - used to store messages
* RabbitMQ - used to distribute messages across components
* c3js - used to display processing statistics
* java 7 
* spring framework
* gradle

# Local setup
* install jdk 7
* install cassandra 2.1.2
* install RabbitMQ
* git clone git@github.com:zeljkokunica/highrate.git
* cd highrate
* using cassandras cqlsh tool run common/src/main/resources/create_cassandra_keyspace.cql to create keyspace and table to store messages
* gradle build
* java -jar receiver/build/libs/receiver-1.0.0.jar - run receiver on port 9090
* java -jar processor/build/libs/processor-1.0.0.jar - run processor
* java -jar web/build/libs/web-1.0.0.jar --server.port=9091 - run web on port 9091
* navigate to http://localhost:9091 - to see current statistics
* java -jar sender/build/libs/sender-1.0.0.jar http://localhost:9090 1000 - to send 10000 random messages (10 sender threads * 1000 requests)
