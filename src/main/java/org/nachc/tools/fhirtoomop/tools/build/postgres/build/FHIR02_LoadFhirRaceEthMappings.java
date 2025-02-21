package org.nachc.tools.fhirtoomop.tools.build.postgres.build;

import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.util.List;

import org.nachc.tools.fhirtoomop.tools.build.impl.LoadMappingTables;
import org.nachc.tools.fhirtoomop.util.db.connection.postgres.PostgresDatabaseConnectionFactory;
import org.nachc.tools.fhirtoomop.util.params.AppParams;
import org.yaorma.database.Database;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FHIR02_LoadFhirRaceEthMappings {

	private File sqlFile;

	private static final String DST_DIR = "/temp/fhir-to-omop";

	private static final String SQL_FILE = "/postgres/build/FHIR02_LoadFhirRaceEthMappings.sql";

	private static final String DATA_DIR = "/postgres/build/fhir/mappings/race-eth";

	public File getSqlFile() {
		return this.sqlFile;
	}

	public static void main(String[] args) {
		exec();
	}

	public static void exec() {
		log.info("Creating FHIR race/eth mappings table.");
		Connection conn = PostgresDatabaseConnectionFactory.getCdmConnection();
		try {
			FHIR02_LoadFhirRaceEthMappings loadRaceFiles = new FHIR02_LoadFhirRaceEthMappings();
			loadRaceFiles.createMappingSqlFile();
			loadRaceFiles.loadMappings(loadRaceFiles.getSqlFile(), conn);
		} finally {
			Database.close(conn);
		}
		log.info("Done creating FHIR race/eth mappings table.");
	}
	
	public void createMappingSqlFile() {
		List<String> fileNames = FileUtil.listResources(DATA_DIR, getClass());
		String sql = getMappingDataSql();
		log.info("Got sqlString: \n" + sql);
		for (String fileName : fileNames) {
			log.info(fileName);
			try {
				String sep = FileSystems.getDefault().getSeparator();
				int start = fileName.lastIndexOf(sep);
				if (start == -1) {
					start = fileName.lastIndexOf("/");
				}
				int end = fileName.length();
				String dstFileName = fileName.substring(start, end);
				File file = new File(DST_DIR, dstFileName);
				if (fileName.endsWith("Eth.txt")) {
					if("true".equals(AppParams.get("runningFromDocker"))) {
						String filePath = file.getPath();
						filePath = filePath.replace("\\", "/");
						sql = sql.replace("<ETH_FILE>", filePath);
					} else {
						sql = sql.replace("<ETH_FILE>", FileUtil.getCanonicalPath(file));
					}
				}
				if (fileName.endsWith("Race.txt")) {
					if("true".equals(AppParams.get("runningFromDocker"))) {
						String filePath = file.getPath();
						filePath = filePath.replace("\\", "/");
						sql = sql.replace("<RACE_FILE>", filePath);
					} else {
						sql = sql.replace("<RACE_FILE>", FileUtil.getCanonicalPath(file));
					}
				}
				String txt = FileUtil.getAsString(fileName);
				log.info("Writing to: \n" + FileUtil.getCanonicalPath(file));
				FileUtil.write(txt, file);
			} catch (Exception exp) {
				log.info("Skipping: " + fileName);
			}
		}
		this.sqlFile = new File(DST_DIR, "load-mapping-data.sql");
		FileUtil.write(sql, this.sqlFile);
		log.info("------------------------");
		log.info("SQL FILE WRITTEN TO: " + FileUtil.getCanonicalPath(this.sqlFile));
		log.info("------------------------");
		log.info("Sql String: \n" + FileUtil.getAsString(sqlFile));
		log.info("------------------------");
	}

	private String getMappingDataSql() {
		String sqlString = FileUtil.getAsString(SQL_FILE);
		sqlString = sqlString.replace("@cdmDatabaseSchema", "public");
		return sqlString;
	}

	private void loadMappings(File sqlFile, Connection conn) {
		log.info("-----------------------");
		log.info("LOAD RACE/ETH SQL FILE: " + FileUtil.getCanonicalPath(sqlFile));
		log.info("-----------------------");
		String sqlString = getSqlString(sqlFile);
		log.info("Running script...");
		Database.executeSqlScript(sqlString, conn);
		log.info("Done running script.");
		log.info("Done creating database tables.");
	}

	private static String getSqlString(File sqlFile) {
		String sqlString = FileUtil.getAsString(sqlFile);
		sqlString = sqlString.replace("<ohdsiDbName>", AppParams.getSchemaName());
		return sqlString;
	}

	
	
}
