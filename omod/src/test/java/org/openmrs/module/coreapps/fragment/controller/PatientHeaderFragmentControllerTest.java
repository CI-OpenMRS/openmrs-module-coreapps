package org.openmrs.module.coreapps.fragment.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import javassist.expr.Instanceof;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.module.appframework.feature.FeatureToggleProperties;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.emrapi.adt.AdtService;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;


public class PatientHeaderFragmentControllerTest {

	
	PatientHeaderFragmentController controller;
	FragmentConfiguration mockedFragmentConfiguration;
	UiSessionContext mockedUiSessionContext;
	AdtService mockedAdtService;
	EmrApiProperties mockedEmrApiProperties;
	FeatureToggleProperties mockedFeatureToggleProperties;
	Location unknownLocation;
	
	@Before
	public void setUp(){
		controller = new PatientHeaderFragmentController();
		mockedFragmentConfiguration = mock(FragmentConfiguration.class);
		mockedUiSessionContext = mock(UiSessionContext.class);
		mockedAdtService = mock(AdtService.class);
		mockedEmrApiProperties = mock(EmrApiProperties.class);
		mockedFeatureToggleProperties = mock(FeatureToggleProperties.class);
		unknownLocation = new Location(Location.LOCATION_UNKNOWN);
		when(mockedUiSessionContext.getSessionLocation()).thenReturn(unknownLocation);
		when(mockedAdtService.getLocationThatSupportsVisits((Location) any())).thenReturn(unknownLocation);
		when(mockedFeatureToggleProperties.isFeatureEnabled("hideEditPatientDemographicsButton")).thenReturn(true);
		when(mockedFeatureToggleProperties.isFeatureEnabled("enableNewPatientHeader")).thenReturn(true);
	}
	
	
	@Test
	public void shouldAddAttributePatientImageHtml() {
		controller.controller(mockedFragmentConfiguration, mockedEmrApiProperties, null, null, null, mockedAdtService, mockedUiSessionContext, mockedFeatureToggleProperties);
		verify(mockedFragmentConfiguration).addAttribute(eq("patientImageHtml"), anyString());
	}
	
	@Test
	public void shouldGetTheConceptPhoto(){
		controller.controller(mockedFragmentConfiguration, mockedEmrApiProperties, null, null, null, mockedAdtService, mockedUiSessionContext, mockedFeatureToggleProperties);
		
	}
}
