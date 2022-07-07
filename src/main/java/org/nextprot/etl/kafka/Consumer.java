package org.nextprot.etl.kafka;

import org.nextprot.api.etl.service.StatementETLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
@ComponentScan(basePackages = {"org.nextprot.api.*", "org.nextprot.commons.*"})
@Component
public class Consumer {

    @Autowired
    StatementETLService etlService;

    public void doSomething() {
       /*try {
            etlService.extractTransformLoadStatements(null,"", false, false);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println("At consumer");
    }
}
