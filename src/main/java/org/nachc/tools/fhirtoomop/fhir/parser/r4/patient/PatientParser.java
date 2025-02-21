package org.nachc.tools.fhirtoomop.fhir.parser.r4.patient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.nachc.tools.fhirtoomop.fhir.parser.r4.coding.CodingParser;
import org.nachc.tools.fhirtoomop.fhir.parser.r4.extension.ExtensionParser;
import org.yaorma.util.time.TimeUtil;

import com.nach.core.util.string.StringUtil;

public class PatientParser {

	private Patient patient;

	public PatientParser(Patient patient) {
		this.patient = patient;
	}

	public Patient getPatient() {
		return this.patient;
	}
	
	public String getId() {
		try {
			String patientId = patient.getIdElement().getIdPart();
			return patientId;
		} catch (Exception exp) {
			return null;
		}
	}

	public String getFirstName() {
		List<HumanName> nameList = patient.getName();
		if(nameList == null || nameList.size() == 0) {
			return null;
		}
		HumanName name = nameList.get(0);
		if(name == null) {
			return null;
		}
		String rtn = name.getGivenAsSingleString();
		return rtn;
	}
	
	public String getLastName() {
		List<HumanName> nameList = patient.getName();
		if(nameList == null || nameList.size() == 0) {
			return null;
		}
		HumanName name = nameList.get(0);
		if(name == null) {
			return null;
		}
		String rtn = name.getFamily();
		return rtn;
	}
	
	public Coding getRace() {
		ExtensionParser ex = getExtension("http://hl7.org/fhir/us/core/StructureDefinition/us-core-race");
		Coding rtn = ex.getCoding();
		return rtn;
	}

	public String getRaceSystem() {
		try {
			return getRace().getSystem();
		} catch(Exception exp) {
			return null;
		}
	}
	
	public String getRaceCode() {
		try {
			return getRace().getCode();
		} catch(Exception exp) {
			return null;
		}
	}
	
	public String getRaceDisplay() {
		try {
			return getRace().getDisplay();
		} catch(Exception exp) {
			return null;
		}
	}
	
	public Coding getEthnicity() {
		ExtensionParser ex = getExtension("http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity");
		Coding rtn = ex.getCoding();
		return rtn;
	}

	public String getEthnicitySystem() {
		try {
			return getEthnicity().getSystem();
		} catch(Exception exp) {
			return null;
		}
	}
	
	public String getEthnicityCode() {
		try {
			return getEthnicity().getCode();
		} catch(Exception exp) {
			return null;
		}
	}
	
	public String getEthnicityDisplay() {
		try {
			return getEthnicity().getDisplay();
		} catch(Exception exp) {
			return null;
		}
	}
	
	public String getEthnicityStr() {
		return CodingParser.getAsPipeDelimited(getEthnicity());
	}
	
	public String getRaceStr() {
		return CodingParser.getAsPipeDelimited(getRace());
	}
	
	public AdministrativeGender getGender() {
		AdministrativeGender ag = this.patient.getGender();
		return ag;
	}

	public String getGenderDisplay() {
		AdministrativeGender ag = this.patient.getGender();
		if(ag == null) {
			return "<null>";
		}
		return ag.getDisplay();
	}

	public Date getBirthDate() {
		return this.patient.getBirthDate();
	}

	public String getBirthDateAsString() {
		Date date = this.getBirthDate();
		String rtn = TimeUtil.format(date, "yyyy-MM-dd");
		return rtn;
	}

	public Integer getBirthYear() {
		Date date = this.getBirthDate();
		if (date != null) {
			String yearString = TimeUtil.format(date, "yyyy");
			Integer rtn = StringUtil.parseInt(yearString);
			return rtn;
		} else {
			return null;
		}
	}

	public Integer getBirthMonth() {
		Date date = this.getBirthDate();
		if (date != null) {
			String yearString = TimeUtil.format(date, "MM");
			Integer rtn = StringUtil.parseInt(yearString);
			return rtn;
		} else {
			return null;
		}
	}

	public Integer getBirthDay() {
		Date date = this.getBirthDate();
		if (date != null) {
			String yearString = TimeUtil.format(date, "dd");
			Integer rtn = StringUtil.parseInt(yearString);
			return rtn;
		} else {
			return null;
		}
	}

	//
	// methods for extensions
	//

	public List<ExtensionParser> getExtensions() {
		List<ExtensionParser> rtn = new ArrayList<ExtensionParser>();
		List<Extension> extensions = this.patient.getExtension();
		for (Extension ex : extensions) {
			ExtensionParser parser = new ExtensionParser(ex);
			rtn.add(parser);
		}
		return rtn;
	}

	public ExtensionParser getExtension(String url) {
		List<ExtensionParser> extensions = getExtensions();
		for (ExtensionParser ex : extensions) {
			if (url != null && url.equals(ex.getUrl())) {
				return ex;
			}
		}
		return null;
	}

	public String getExtensionAsPipeDelimited(String url) {
		ExtensionParser ex = getExtension(url);
		String rtn = ex.getAsPipeDelimited();
		return rtn;
	}

}
