this is a proof of concept high rate processing project
# Components
|component|description| 
|---------|-----------|
|receiver|receives http post requests with jsons. Stores requests into cassandra and forwards them to queue for processing|
|processor|processes request from queue and calculates statistics. Statistics are published to fanout queue for publishing to the clients|
|web|web endpoint which server static html resources and publishes statistics to the cliens (WebSocket with STOMP)
|reader|helper component which reads messages from cassandra and publishes them for processing|
|sender|helper component that creates random messages and sends them to receiver via HTTP|

