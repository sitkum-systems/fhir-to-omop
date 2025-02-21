package org.nachc.tools.fhirtoomop.omop.person.factory.builder.observation.translate;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.nachc.tools.fhirtoomop.fhir.parser.procedure.ProcedureParser;
import org.nachc.tools.fhirtoomop.fhir.patient.FhirPatient;
import org.nachc.tools.fhirtoomop.omop.person.OmopPerson;
import org.nachc.tools.fhirtoomop.omop.util.constants.OmopConceptConstants;
import org.nachc.tools.fhirtoomop.omop.util.id.FhirToOmopIdGenerator;
import org.nachc.tools.omop.yaorma.dvo.ConceptDvo;
import org.nachc.tools.omop.yaorma.dvo.MeasurementDvo;
import org.nachc.tools.omop.yaorma.dvo.ObservationDvo;

public class OmopMeasurementFromProcedure {

	//
	// instance variables
	//

	private FhirPatient fhirPatient;

	private ProcedureParser parser;

	private OmopPerson omopPerson;

	private Connection conn;

	private ConceptDvo conceptDvo;

	private List<ObservationDvo> observationList = new ArrayList<ObservationDvo>();

	private List<ObservationDvo> measurementObsList = new ArrayList<ObservationDvo>();

	private List<MeasurementDvo> measurementList = new ArrayList<MeasurementDvo>();

	//
	// constructor
	//

	public OmopMeasurementFromProcedure(FhirPatient fhirPatient, ProcedureParser parser, ConceptDvo measConcept, OmopPerson omopPerson, Connection conn) {
		this.fhirPatient = fhirPatient;
		this.parser = parser;
		this.conceptDvo = measConcept;
		this.omopPerson = omopPerson;
		this.conn = conn;
	}

	public MeasurementDvo build() {
		MeasurementDvo dvo = new MeasurementDvo();
		int measurementId = FhirToOmopIdGenerator.getId("measurement", "measurement_id");
		dvo.setMeasurementId(measurementId);
		dvo.setPersonId(omopPerson.getPersonId());
		dvo.setMeasurementConceptId(conceptDvo.getConceptId());
		dvo.setMeasurementDate(parser.getStartDate());
		dvo.setMeasurementTypeConceptId(0);
		String fhirEncounterId = parser.getEncounterId();
		Integer omopVisitId = this.omopPerson.getOmopEncounterId(fhirEncounterId);
		dvo.setVisitOccurrenceId(omopVisitId);
		// measuerment type
		if("Procedure".equals(conceptDvo.getConceptClassId())) {
			dvo.setMeasurementTypeConceptId(OmopConceptConstants.getObsIsLabResultMeasurementConceptId());
		} else if ("Observable Entity".equals(conceptDvo.getConceptClassId())) {
			dvo.setMeasurementTypeConceptId(OmopConceptConstants.getObsIsLabResultMeasurementConceptId());
		} else {
			dvo.setMeasurementTypeConceptId(OmopConceptConstants.getObsIsFromPhysicalExaminationConceptId());
		}
		// measurment units
		dvo.setUnitConceptId(OmopConceptConstants.getIsScalarMeasurementUnitsConceptId());
		// done
		return dvo;
	}

}
