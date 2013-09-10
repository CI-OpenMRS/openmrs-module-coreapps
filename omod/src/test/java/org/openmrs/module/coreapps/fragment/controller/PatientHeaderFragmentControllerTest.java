package org.openmrs.module.coreapps.fragment.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.feature.FeatureToggleProperties;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.coreapps.CoreAppsConstants;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.emrapi.adt.AdtService;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;



public class PatientHeaderFragmentControllerTest {

	Patient mockedPatient;
	PatientHeaderFragmentController controller;
	FragmentConfiguration mockedFragmentConfiguration;
	UiSessionContext mockedUiSessionContext;
	AdtService mockedAdtService;
	EmrApiProperties mockedEmrApiProperties;
	FeatureToggleProperties mockedFeatureToggleProperties;
	Location unknownLocation;

	@Test
	public void shouldReturnAnImage() {
		Object patient = new Patient();
		//when(Context.getAdministrationService().getGlobalProperty(CoreAppsConstants.GP_PHOTO_PATIENT_CONCEPT_UUID)).thenReturn(anyString());
		new PatientHeaderFragmentController().getPatientImage(patient);
	}
}







