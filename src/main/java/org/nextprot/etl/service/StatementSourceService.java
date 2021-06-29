package org.nextprot.etl.service;

import org.nextprot.api.core.app.StatementSource;

import java.io.IOException;
import java.util.Set;

/**
 * Provide statements from Json files for a specific source and release
 */
public interface StatementSourceService {

	Set<String> getJsonFilenamesForRelease(StatementSource source, String release) throws IOException;

	String getStatementsAsJsonArray(StatementSource source, String release, String jsonFileName) throws IOException;
}
