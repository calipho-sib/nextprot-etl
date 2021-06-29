package org.nextprot.etl.kafka;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@ComponentScan(basePackages = "org.nextprot.api.etl.service")
@Component
public class Consumer {

    public void doSomething() {
       /*try {
            etlService.extractTransformLoadStatements(null,"", false, false);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println("At consumer");
    }
}
