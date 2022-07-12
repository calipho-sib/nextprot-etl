package org.nextprot.etl.kafka;

import org.nextprot.api.commons.exception.NextProtException;
import org.nextprot.api.etl.domain.IsoformPositions;
import org.nextprot.api.etl.service.transform.StatementIsoformPositionService;
import org.nextprot.commons.statements.Statement;
import org.nextprot.commons.statements.StatementBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.nextprot.api.commons.constants.AnnotationCategory.DISEASE_RELATED_VARIANT;
import static org.nextprot.api.commons.constants.AnnotationCategory.PHENOTYPIC_VARIATION;
import static org.nextprot.commons.statements.specs.CoreStatementField.*;
import static org.nextprot.commons.statements.specs.CoreStatementField.TARGET_ISOFORMS;

@Configuration
@ComponentScan(basePackages = {"org.nextprot.api.etl.service.transform.*", "org.nextprot.api.core.*", "org.nextprot.api.commons.*", "org.nextprot.api.isoform.*"})
@Component
public class Consumer {

    @Autowired
    StatementIsoformPositionService statementIsoformPositionService;

    public void doSomething() {
       /*try {
            etlService.extractTransformLoadStatements(null,"", false, false);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println("At consumer");
    }

    private Optional<Statement> transform(Statement simpleStatement) {
        String category = simpleStatement.getValue(ANNOTATION_CATEGORY);

        if (category.equals(PHENOTYPIC_VARIATION.getDbAnnotationTypeName())
                || category.equals(DISEASE_RELATED_VARIANT.getDbAnnotationTypeName())) {
            throw new NextProtException("Not expecting phenotypic variation at this stage.");
        }

        IsoformPositions isoformPositions =
                statementIsoformPositionService.computeIsoformPositionsForNormalAnnotation(simpleStatement);

        if (isoformPositions == null | !isoformPositions.hasTargetIsoforms()) {

            //LOGGER.warn("Skipping statement " + simpleStatement.getValue(ANNOTATION_NAME) + " (source=" + simpleStatement.getValue(ASSIGNED_BY) + ")");
            return Optional.empty();
        }

        StatementBuilder builder = new StatementBuilder(simpleStatement)
                .addField(RAW_STATEMENT_ID, simpleStatement.getStatementId());

        if (isoformPositions.hasExactPositions()) {
            builder.addField(LOCATION_BEGIN, String.valueOf(isoformPositions.getBeginPositionOfCanonicalOrIsoSpec()))
                    .addField(LOCATION_END, String.valueOf(isoformPositions.getEndPositionOfCanonicalOrIsoSpec()))
                    .addField(LOCATION_BEGIN_MASTER, String.valueOf(isoformPositions.getMasterBeginPosition()))
                    .addField(LOCATION_END_MASTER, String.valueOf(isoformPositions.getMasterEndPosition()));
        }

        return Optional.of(builder
                .addField(ISOFORM_CANONICAL, isoformPositions.getCanonicalIsoform())
                .addField(TARGET_ISOFORMS, isoformPositions.getTargetIsoformSet().serializeToJsonString())
                .withAnnotationHash()
                .build());
    }
}
