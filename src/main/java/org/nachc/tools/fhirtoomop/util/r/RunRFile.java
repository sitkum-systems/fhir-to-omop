package org.nachc.tools.fhirtoomop.util.r;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.lang3.SystemUtils;

import org.yaorma.util.time.Timer;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RunRFile {

	private static String DIR = "/temp/ponos";
    private static String RFILENAME = "ponos.r";
	
	public static void run(String rScript) {
        // Write the R script into a file
		writeFile(rScript);

        // If OS is Windows, run the R script via a Bat file
		if (SystemUtils.IS_OS_WINDOWS) {
            File bat = writeBatFile();
		    runFile(bat);
        }
        // Otherwise, run the R script via a bash command
        else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC) {
            File bash = writeBashFile();

            String fileName = FileUtil.getCanonicalPath(bash);
		    log.info("File: " + fileName);
            runProcess("bash",fileName);
        }
//		deleteFiles();
	}
	
	private static void writeFile(String rScript) {
		log.info("Writing source file to "+DIR);
		File dir = new File(DIR);
		FileUtil.rmdir(dir);
		FileUtil.mkdirs(dir);
		File file = new File(dir,RFILENAME);
		FileUtil.write(rScript, file);
		log.info("Done writing file: " + FileUtil.getCanonicalPath(file));
	}
	
    private static File writeBashFile() {
		log.info("Writing .sh file...");
		File file = new File(DIR+"/ponos.sh");
		FileUtil.write("Rscript "+DIR+"/"+RFILENAME, file);
		log.info("Done writing .sh file.");
		return file;
	}

	private static File writeBatFile() {
		log.info("Writing bat...");
		File file = new File(DIR+"/ponos.bat");
		FileUtil.write("rscript "+DIR+"/"+RFILENAME, file);
		log.info("Done writing bat.");
		return file;
	}
	
    private static void runProcess(String... command) {
        Timer timer = new Timer();
		timer.start();

        try {
			log.info("RUNNING PROCESS...");
			// start the process
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			// redirect error to ouput to stdio
			processBuilder.redirectErrorStream(true);

			final Process process = processBuilder.start();
			// create the reader
			InputStream is = process.getInputStream();
			InputStreamReader isReader = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isReader);
			String line;
			// read the output
			log.info("Script is running...\n\n");
			System.out.println("--- OUTPUT FROM SCRIPT -----------------------------------------\n\n");
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			// wait for the process to finish
			int exitCode = process.waitFor();
			System.out.println("\n");
			System.out.println("--- END OUTPUT FROM SCRIPT -------------------------------------\n\n");
			// done
			log.info("EXIT CODE: " + exitCode);
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}

		timer.stop();
		log.info("TIME TO RUN PROCESS: " + timer.getElapsedString());
		log.info("Done running process.");
    }

	private static void runFile(File file) {
		String fileName = FileUtil.getCanonicalPath(file);
		log.info("File: " + fileName);
        runProcess(fileName);
	}

	private static void deleteFiles() {
		File dir = new File(DIR);
		FileUtil.rmdir(dir);
	}
	
}
