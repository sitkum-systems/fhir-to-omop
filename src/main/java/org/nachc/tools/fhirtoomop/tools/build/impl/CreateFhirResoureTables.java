package org.nachc.tools.fhirtoomop.tools.build.impl;

import java.io.InputStream;
import java.sql.Connection;

import org.nachc.tools.fhirtoomop.util.params.AppParams;
import org.yaorma.database.Database;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateFhirResoureTables {

	private static final InputStream IS = FileUtil.getInputStream("/sqlserver/fhir/fhir-resources-ddl.sql");
	
	public static void exec(Connection conn) {
		String dbName = AppParams.getSchemaName();
		log.info("Using: " + dbName);
		Database.update("use " + dbName, conn);
		log.info("Running script...");
		Database.executeSqlScript(IS, conn);
		log.info("Done running script.");
		log.info("Done creating database tables.");		
	}
	
}
