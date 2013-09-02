/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.coreapps.fragment.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.feature.FeatureToggleProperties;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.emrapi.adt.AdtService;
import org.openmrs.module.emrapi.patient.PatientDomainWrapper;
import org.openmrs.module.emrapi.visit.VisitDomainWrapper;
import org.openmrs.module.idgen.AutoGenerationOption;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.registrationapp.RegistrationAppConstants;
import org.openmrs.obs.ComplexObsHandler;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.InjectBeans;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.web.WebConstants;
import org.openmrs.ConceptComplex;

/**
 * Ideally you pass in a PatientDomainWrapper as the "patient" config parameter. But if you pass in
 * a Patient, then this controller will wrap that for you.
 */
public class PatientHeaderFragmentController {
	
	public void controller(FragmentConfiguration config, @SpringBean("emrApiProperties") EmrApiProperties emrApiProperties,
	                       @SpringBean("baseIdentifierSourceService") IdentifierSourceService identifierSourceService,
	                       @FragmentParam("patient") Object patient, @InjectBeans PatientDomainWrapper wrapper,
	                       @SpringBean("adtService") AdtService adtService, UiSessionContext sessionContext,
                           @SpringBean("featureToggles") FeatureToggleProperties featureToggleProperties) {

		
		if (patient instanceof Patient) {
			wrapper.setPatient((Patient) patient);
			config.addAttribute("patient", wrapper);
			
		}
		
		/*
		Obs obs = new Obs(person, concept, new Date(), location);
		String handlerName = ((ConceptComplex)obs.getConcept()).getHandler();
		CompledObsHandler handler = Context.getObsService().getHandler(handlerName);
		String imageTag = handler.getObs(obs, WebConstants.HTML_VIEW).getComplexDate().getData();
		config.addAttribute("patientImageHtml", imageTag);
		*/
		
		String photoConceptUuid = Context.getAdministrationService().getGlobalProperty(RegistrationAppConstants.GP_PHOTO_PATIENT_CONCEPT_UUID);
		Concept concept = Context.getConceptService().getConceptByUuid(photoConceptUuid);
		//Obs obs = new Obs(patient, concept, new Date(), sessionContext.getSessionLocation());
		//String handlerName = ((ConceptComplex)obs.getConcept()).getHandler();
		//ComplexObsHandler handler = Context.getObsService().getHandler(handlerName);
		//String imageTag = handler.getObs(obs, WebConstants.HTML_VIEW).getComplexData().getData();
		//config.addAttribute("patientImageHtml", imageTag);
	
		config.addAttribute("patientImageHtml", "<img src= \"http://t2.gstatic.com/images?q=tbn:ANd9GcQHmyF5yYH5hudnlnxcDjBYuos9FEb8d2LlHvHhrdqr3Dyd7Ni03g\"/>");
		VisitDomainWrapper activeVisit = (VisitDomainWrapper) config.getAttribute("activeVisit");
		if (activeVisit == null) {
            try {
            	Location sessionLocation = sessionContext.getSessionLocation();
                Location visitLocation = adtService.getLocationThatSupportsVisits(sessionLocation);
                activeVisit = adtService.getActiveVisit((Patient) patient, visitLocation);
            } catch (IllegalArgumentException ex) {
                // location does not support visits
            }
		}

        if (activeVisit != null) {
            config.addAttribute("activeVisit", activeVisit);
            config.addAttribute("activeVisitStartDatetime",
                    DateFormatUtils.format(activeVisit.getStartDatetime(), "dd MMM yyyy hh:mm a", Context.getLocale()));
        }
		
		List<ExtraPatientIdentifierType> extraPatientIdentifierTypes = new ArrayList<ExtraPatientIdentifierType>();
		
		for (PatientIdentifierType type : emrApiProperties.getExtraPatientIdentifierTypes()) {
			AutoGenerationOption option = identifierSourceService.getAutoGenerationOption(type);
			extraPatientIdentifierTypes.add(new ExtraPatientIdentifierType(type, option != null ? option
			        .isManualEntryEnabled() : true));
		}
		
		config.addAttribute("extraPatientIdentifierTypes", extraPatientIdentifierTypes);
        config.addAttribute("hideEditDemographicsButton", featureToggleProperties.isFeatureEnabled("hideEditPatientDemographicsButton"));
        config.addAttribute("isNewPatientHeaderEnabled", featureToggleProperties.isFeatureEnabled("enableNewPatientHeader"));
        
        
        
	}
	
	
	public class ExtraPatientIdentifierType {
		
		private PatientIdentifierType patientIdentifierType;
		
		private boolean editable = false;
		
		public ExtraPatientIdentifierType(PatientIdentifierType type, boolean editable) {
			this.patientIdentifierType = type;
			this.editable = editable;
		}
		
		public PatientIdentifierType getPatientIdentifierType() {
			return patientIdentifierType;
		}
		
		public void setPatientIdentifierType(PatientIdentifierType patientIdentifierType) {
			this.patientIdentifierType = patientIdentifierType;
		}
		
		public boolean isEditable() {
			return editable;
		}
		
		public void setEditable(boolean editable) {
			this.editable = editable;
		}
		
	}
	
}
