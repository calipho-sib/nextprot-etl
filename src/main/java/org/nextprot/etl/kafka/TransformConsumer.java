package org.nextprot.etl.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.nextprot.api.etl.service.transform.StatementTransformerService;
import org.nextprot.commons.statements.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"org.nextprot.api.etl.service.transform.*", "org.nextprot.api.core.*", "org.nextprot.api.commons.*", "org.nextprot.api.isoform.*"})
@Component
public class TransformConsumer {

    @Autowired
    StatementTransformerService statementTransformerService;

    public void doSomething() {
        System.out.println("At consumer");
    }

    private Consumer<String, Statement> consumer;

    public TransformConsumer() {


        // Subscribe to the topic.
        //consumer.subscribe(Collections.singletonList(topic));
    }

    public void init() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.KAFKA_BROKERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConfig.GROUP_ID_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Create the consumer using props.
        consumer = new KafkaConsumer<>(props);
    }

    public void run() {
        System.out.println("Starting the consumer and subscribed to topic ");
        long startTime = System.currentTimeMillis();
        final int giveUp = 10;   int noRecordsCount = 0;

        int count = 0;
        while (true) {
            final ConsumerRecords<String, Statement> consumerRecords = consumer.poll(1000);

            if (consumerRecords.count()==0) {
                System.out.println("No statements");
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

            for(ConsumerRecord record: consumerRecords) {
                System.out.println("Reading statement" + (++count) + " " + record.value().toString());
            }

            consumer.commitAsync();
        }
        consumer.close();
        long endTime = System.currentTimeMillis();
        System.out.println("Elapsed time " + (endTime - startTime) + " milliseconds");
    }
}
