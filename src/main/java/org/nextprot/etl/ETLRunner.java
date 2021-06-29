package org.nextprot.etl;

import org.nextprot.etl.kafka.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Base ETL Runner, which spawns up a kafka consumer & producer accordingly, which runs E.T.L portions of the pipeline
 */
@ComponentScan(basePackages = "org.nextprot.etl")
public class ETLRunner {

    public static void main(String args[]) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ETLRunner.class);
        ETLRunner etlRunner = applicationContext.getBean(ETLRunner.class);
        etlRunner.start();
    }

    @Autowired
    Consumer consumer;
    public void start() {
        consumer.doSomething();
    }

}
