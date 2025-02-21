package org.nachc.tools.fhirtoomop.tools.build.postgres.build;

import org.nachc.tools.fhirtoomop.util.win.r.RunRFileAsBat;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ACH0_InstallAch {

	public static void main(String[] args) {
		exec();
	}

	public static void exec() {
		// run the install script
		log.info("---------------------");
		log.info("START: Installing Achilles...");
		RunRFileAsBat.run(FileUtil.getAsString("/postgres/build/r/achilles/install-achilles.r"));
		log.info("DONE: Installing Achilles...");
		log.info("---------------------");
	}

}












