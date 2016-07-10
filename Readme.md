# Kafka Connect Random Connector

This is a Kafka Connector to send random generated values.

## Overview

Kafka Connect Random Connector is Kafka Connector to send random values.
This is useful to test of streaming application reading data from Apache Kafka(such as Kafka Streams, Spark Streaming, and so on).
You can see [here](http://kafka.apache.org/) about Apache Kafka and Kafka Connect.

## Building

You can build this connector with Apache Maven 3.3.3+.

    $ mvn clean
    $ mvn package -DskipTests


## Usage

You should add created (and depended) jar file to class path when you run Kafka Connect.

For example(Using Kafka Connect standalone mode), 
   
    $ CLASSPATH=target/random-connector-1.0-SNAPSHOT.jar:commons-lang.jar \
    connect-standalone.sh config/connect-standalone.properties

This connector depends on Commons Lang([detail](https://commons.apache.org/proper/commons-lang/)).
If you use Random String Generator, you should also add class path this library.


## Generators

Generator is the module to generate random values.

### Prepared Generators

This connector has 2 generators __Random Int Generator__ and __Random String Generator__.

* Random Int Generator: send random integer and you can set range of value with configs.

* Random String Generator: send random strings of alphabets(a-z, A-Z)

### Implements your Generators

In addition to these generator, you can use your original generator. 

You should new class extended Generator and implements a few methods as below.

* setConfigDef
    
* setTaskConfigs
    
* getKeySchema
    
* getValueSchema

* generate

In addition to these methods, you can overwrite 2 methods, if you need.

* start

* stop
    
If you don't overwrite these, nothing to do for initialize and finalize.

You can see details of these in Generator class's comments.

## Configurations

### Commons

| Name                  | Description                                   | Default value |
|:----------------------|:----------------------------------------------|:--------------|
| connector.class       | A connector class to use <br> (Usually, you need not to change this) | sasakitoa.kafka.connect.random.RandomSourceConnector |
| generator.class       | A generator class to use. <br> You want to use Random Int Generator or Random String Generator, should set _sasakitoa.kafka.connect.random.generator.{RandomInt, RandomString}_ | (none) |
| name                  | A name of this connector                                       | random-connector |
| tasks.max             | Number of tasks                                                | 1     | 
| messages.per.second   | Number of messages will send in a second<br> -1 is unlimited   | -1    | 
| topic                 | Topic name will send generated random value                    | topic |


### Random Int Generator

| Name                  | Description                              | Default value |
|:----------------------|:-----------------------------------------|:--------------|
| random.int.key.min    | Minimum value which generates for Key    | 0             |
| random.int.key.max    | Maximum value which generates for Key    | 100           |
| random.int.value.min  | Minimum value which generates for Value  | 0             |
| random.int.value.max  | Maximum value which generates for Value  | 100           |


### Random String Generator

| Name                        | Description                                 | Default value |
|:----------------------------|:--------------------------------------------|:--------------|
| random.string.key.length    | Length of strings which generates for Key   | 10            |
| random.string.key.length    | Length of strings which generates for Value | 10            |

