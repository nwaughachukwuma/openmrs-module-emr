/*
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

package org.openmrs.module.emr.utils;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflow;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.WebUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GeneralUtils {


    /**
     * Get the concept by id, the id can either be 1)an integer id like 5090 or 2)mapping type id
     * like "XYZ:HT" or 3)uuid like "a3e12268-74bf-11df-9768-17cfc9833272"
     *
     * @param id
     * @return the concept if exist, else null
     * @should find a concept by its conceptId
     * @should find a concept by its mapping
     * @should find a concept by its uuid
     * @should return null otherwise
     * @should find a concept by its mapping with a space in between
     */
    public static Concept getConcept(String id) {
        Concept cpt = null;

        if (id != null) {

            // see if this is a parseable int; if so, try looking up concept by id
            try { //handle integer: id
                int conceptId = Integer.parseInt(id);
                cpt = Context.getConceptService().getConcept(conceptId);

                if (cpt != null) {
                    return cpt;
                }
            } catch (Exception ex) {
                //do nothing
            }

            // handle  mapping id: xyz:ht
            int index = id.indexOf(":");
            if (index != -1) {
                String mappingCode = id.substring(0, index).trim();
                String conceptCode = id.substring(index + 1, id.length()).trim();
                cpt = Context.getConceptService().getConceptByMapping(conceptCode, mappingCode);

                if (cpt != null) {
                    return cpt;
                }
            }

            //handle uuid id: "a3e1302b-74bf-11df-9768-17cfc9833272", if the id matches a uuid format
            if (org.openmrs.module.emrapi.utils.GeneralUtils.isValidUuidFormat(id)) {
                cpt = Context.getConceptService().getConceptByUuid(id);
            }
        }

        return cpt;
    }

    /**
     * Get the location by: 1)an integer id like 5090 or 2) uuid like
     * "a3e12268-74bf-11df-9768-17cfc9833272" or 3) location name like "Boston" or 4) an id/name
     * pair like "501 - Boston" (this format is used when saving a location on a obs as a value
     * text) or 5) "GlobalProperty:property.name" or 6) "UserProperty:propertyName"
     *
     * @param id
     * @return the location if exist, else null
     * @should find a location by its locationId
     * @should find a location by name
     * @should find a location by its uuid
     * @should find a location by global property
     * @should find a location by user property
     * @should return null otherwise
     */
    public static Location getLocation(String id) {

        Location location = null;

        if (id != null) {

            // handle GlobalProperty:property.name
            if (id.startsWith("GlobalProperty:")) {
                String gpName = id.substring("GlobalProperty:".length());
                String gpValue = Context.getAdministrationService().getGlobalProperty(gpName);
                if (StringUtils.isNotEmpty(gpValue)) {
                    return getLocation(gpValue);
                }
            }

            // handle UserProperty:propName
            if (id.startsWith("UserProperty:")) {
                String upName = id.substring("UserProperty:".length());
                String upValue = Context.getAuthenticatedUser().getUserProperty(upName);
                if (StringUtils.isNotEmpty(upValue)) {
                    return getLocation(upValue);
                }
            }

            // see if this is parseable int; if so, try looking up by id
            try { //handle integer: id
                int locationId = Integer.parseInt(id);
                location = Context.getLocationService().getLocation(locationId);

                if (location != null) {
                    return location;
                }
            } catch (Exception ex) {
                //do nothing
            }

            // handle uuid id: "a3e1302b-74bf-11df-9768-17cfc9833272" if id matches a uuid format
            if (org.openmrs.module.emrapi.utils.GeneralUtils.isValidUuidFormat(id)) {
                location = Context.getLocationService().getLocationByUuid(id);

                if (location != null) {
                    return location;
                }
            }

            // if it's neither a uuid or id, try location name
            location = Context.getLocationService().getLocation(id);

            if (location != null) {
                return location;
            }

            // try the "101 - Cange" case
            if (id.contains(" ")) {
                String[] values = id.split(" ");
                try {
                    int locationId = Integer.parseInt(values[0]);
                    location = Context.getLocationService().getLocation(locationId);

                    if (location != null) {
                        return location;
                    }
                } catch (Exception ex) {
                    //do nothing
                }
            }
        }

        // no match found, so return null
        return null;
    }

    /**
     * Get the program by: 1)an integer id like 5090 or 2) uuid like
     * "a3e12268-74bf-11df-9768-17cfc9833272" or 3) name of *associated concept* (not name of
     * program), like "MDR-TB Program"
     *
     * @param id
     * @return the program if exist, else null
     * @should find a program by its id
     * @should find a program by name of associated concept
     * @should find a program by its uuid
     * @should return null otherwise
     */
    public static Program getProgram(String id) {

        Program program = null;

        if (id != null) {

            // see if this is parseable int; if so, try looking up by id
            try {//handle integer: id
                int programId = Integer.parseInt(id);
                program = Context.getProgramWorkflowService().getProgram(programId);

                if (program != null) {
                    return program;
                }
            } catch (Exception ex) {
                //do nothing
            }

            //handle uuid id: "a3e1302b-74bf-11df-9768-17cfc9833272", if id matches uuid format
            if (org.openmrs.module.emrapi.utils.GeneralUtils.isValidUuidFormat(id)) {
                program = Context.getProgramWorkflowService().getProgramByUuid(id);

                if (program != null) {
                    return program;
                }
            } else {
                // if it's neither a uuid or id, try program name
                // (note that this API method actually checks based on name of the associated concept, not the name of the program itself)
                program = Context.getProgramWorkflowService().getProgramByName(id);
            }

        }
        return program;
    }

    /**
     * Get the person by: 1)an integer id like 5090 or 2) uuid like
     * "a3e12268-74bf-11df-9768-17cfc9833272" or 3) a username like "mgoodrich" or 4) an id/name
     * pair like "5090 - Bob Jones" (this format is used when saving a person on a obs as a value
     * text)
     *
     * @param id
     * @return the person if exist, else null
     * @should find a person by its id
     * @should find a person by its uuid
     * @should find a person by username of corresponding user
     * @should return null otherwise
     */
    public static Person getPerson(String id) {

        Person person = null;

        if (id != null) {

            // see if this is parseable int; if so, try looking up by id
            try { //handle integer: id
                int personId = Integer.parseInt(id);
                person = Context.getPersonService().getPerson(personId);

                if (person != null) {
                    return person;
                }
            } catch (Exception ex) {
                //do nothing
            }

            // handle uuid id: "a3e1302b-74bf-11df-9768-17cfc9833272", if id matches uuid format
            if (org.openmrs.module.emrapi.utils.GeneralUtils.isValidUuidFormat(id)) {
                person = Context.getPersonService().getPersonByUuid(id);

                if (person != null) {
                    return person;
                }
            }

            // handle username
            User personByUsername = Context.getUserService().getUserByUsername(id);
            if (personByUsername != null) {
                return personByUsername.getPerson();
            }

            // try the "5090 - Bob Jones" case
            if (id.contains(" ")) {
                String[] values = id.split(" ");
                try {
                    int personId = Integer.parseInt(values[0]);
                    person = Context.getPersonService().getPerson(personId);

                    if (person != null) {
                        return person;
                    }
                } catch (Exception ex) {
                    //do nothing
                }
            }
        }

        // no match found, so return null
        return null;
    }

    /**
     * Looks up a {@link org.openmrs.ProgramWorkflow} by id, uuid or by concept map of the underlying concept
     */
    @SuppressWarnings("deprecation")
    public static ProgramWorkflow getWorkflow(String identifier) {
        ProgramWorkflow workflow = null;

        if (identifier != null) {
            // first try to fetch by id
            try {
                Integer id = Integer.valueOf(identifier);
                workflow = Context.getProgramWorkflowService().getWorkflow(id);

                if (workflow != null) {
                    return workflow;
                }
            } catch (NumberFormatException e) {
            }

            // if not, try to fetch by uuid
            if (org.openmrs.module.emrapi.utils.GeneralUtils.isValidUuidFormat(identifier)) {
                workflow = Context.getProgramWorkflowService().getWorkflowByUuid(identifier);

                if (workflow != null) {
                    return workflow;
                }
            }

            // finally, try to fetch by concept map
            // handle  mapping id: xyz:ht
            int index = identifier.indexOf(":");
            if (index != -1) {
                Concept concept = getConcept(identifier);

                // iterate through workflows until we see if we find a match
                if (concept != null) {
                    for (Program program : Context.getProgramWorkflowService().getAllPrograms(false)) {
                        for (ProgramWorkflow w : program.getAllWorkflows()) {
                            if (w.getConcept().equals(concept)) {
                                return w;
                            }
                        }
                    }
                }
            }
        }

        return workflow;
    }

    /**
     * Looks up a {@link org.openmrs.ProgramWorkflowState} from the specified program by programWorkflowStateId,
     * uuid, or by a concept map to the the underlying concept (Note that if there are multiple
     * states associated with the same concept in the program, this method will return an arbitrary
     * one if fetched by concept mapping)
     *
     * @param identifier the programWorkflowStateId, uuid or the concept name to match against
     * @param program
     * @return
     * @should return the state with the matching id
     * @should return the state with the matching uuid
     * @should return the state associated with a concept that matches the passed concept map
     */
    public static ProgramWorkflowState getState(String identifier, Program program) {
        if (identifier == null) {
            return null;
        }

        // first try to fetch by id or uuid
        ProgramWorkflowState state = getState(identifier);

        if (state != null) {
            return state;
        }
        // if we didn't find a match, see if this is a concept mapping
        else {
            int index = identifier.indexOf(":");
            if (index != -1) {
                Concept concept = getConcept(identifier);
                if (concept != null) {
                    for (ProgramWorkflow workflow : program.getAllWorkflows()) {
                        for (ProgramWorkflowState s : workflow.getStates(false)) {
                            if (s.getConcept().equals(concept)) {
                                return s;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Looks up a {@link ProgramWorkflowState} from the specified workflow by
     * programWorkflowStateId, uuid, or by a concept map to the the underlying concept (Note that if
     * there are multiple states associated with the same concept in the workflow, this method will
     * return an arbitrary one if fetched by concept mapping)
     *
     * @param identifier the programWorkflowStateId, uuid or the concept name to match against
     * @param workflow
     * @return
     * @should return the state with the matching id
     * @should return the state with the matching uuid
     * @should return the state associated with a concept that matches the passed concept map
     */
    public static ProgramWorkflowState getState(String identifier, ProgramWorkflow workflow) {
        if (identifier == null) {
            return null;
        }

        // first try to fetch by id or uuid
        ProgramWorkflowState state = getState(identifier);

        if (state != null && state.getProgramWorkflow().equals(workflow)) {
            return state;
        }

        // if we didn't find a match, see if this is a concept mapping
        else {
            int index = identifier.indexOf(":");
            if (index != -1) {
                Concept concept = getConcept(identifier);
                if (concept != null) {
                    for (ProgramWorkflowState s : workflow.getStates(false)) {
                        if (s.getConcept().equals(concept)) {
                            return s;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Looks up a {@link ProgramWorkflowState} from the specified workflow by
     * programWorkflowStateId, or uuid
     *
     * @param identifier the programWorkflowStateId or uuid to match against
     * @param
     * @return
     * @should return the state with the matching id
     * @should return the state with the matching uuid
     */
    @SuppressWarnings("deprecation")
    public static ProgramWorkflowState getState(String identifier) {
        ProgramWorkflowState state = null;

        if (identifier != null) {
            try {
                Integer id = Integer.valueOf(identifier);
                state = Context.getProgramWorkflowService().getState(id);

                if (state != null) {
                    return state;
                }
            } catch (NumberFormatException e) {
            }

            if (org.openmrs.module.emrapi.utils.GeneralUtils.isValidUuidFormat(identifier)) {
                state = Context.getProgramWorkflowService().getStateByUuid(identifier);

                if (state != null) {
                    return state;
                }
            }
        }
        return null;
    }

    /**
     * Utility method to fetch the patient identifier for a patient of a certain type at a certain location
     * <p/>
     * Returns null if no identifiers found for that patient of that type at that location
     * If the patient has multiple identifiers for that type/location, it returns the first preferred one
     * If no preferred identifiers, returns first non-preferred one
     *
     * @param patient
     * @param patientIdentifierType
     * @param location
     * @return
     */
    public static PatientIdentifier getPatientIdentifier(Patient patient, PatientIdentifierType patientIdentifierType, Location location) {

        // TODO: add some sort of data quality flag if there are two or more identifiers of the same type and location?

        List<PatientIdentifier> patientIdentifiers = patient.getPatientIdentifiers(patientIdentifierType);

        if (patientIdentifiers == null || patientIdentifiers.size() == 0) {
            return null;
        }

        for (PatientIdentifier patientIdentifer : patientIdentifiers) {
            if (patientIdentifer.getLocation().equals(location) && patientIdentifer.isPreferred()) {
                return patientIdentifer;
            }
        }

        for (PatientIdentifier patientIdentifer : patientIdentifiers) {
            if (patientIdentifer.getLocation().equals(location)) {
                return patientIdentifer;
            }
        }

        return null;
    }

    /**
     * Gets the specified property (referenced by string) off of a person address
     * Returns null if the underlying property is null
     */
    public static String getPersonAddressProperty(PersonAddress address, String property) {
        try {
            Class<?> personAddressClass = Context.loadClass("org.openmrs.PersonAddress");
            Method getPersonAddressProperty;
            getPersonAddressProperty = personAddressClass.getMethod("get" + property.substring(0, 1).toUpperCase() + property.substring(1));
            return (String) getPersonAddressProperty.invoke(address);
        } catch (Exception e) {
            throw new APIException("Invalid property name " + property + " passed to getPersonAddressProperty");
        }
    }

    public static List<String> getAddressHierarchyLevels() {
        List<String> l = new ArrayList<String>();

        try {
            Class<?> svcClass = Context.loadClass("org.openmrs.module.addresshierarchy.service.AddressHierarchyService");
            Object svc = Context.getService(svcClass);
            List<Object> levels = (List<Object>) svcClass.getMethod("getOrderedAddressHierarchyLevels", Boolean.class, Boolean.class).invoke(svc, true, true);
            Class<?> levelClass = Context.loadClass("org.openmrs.module.addresshierarchy.AddressHierarchyLevel");
            Class<?> fieldClass = Context.loadClass("org.openmrs.module.addresshierarchy.AddressField");
            for (Object o : levels) {
                Object addressField = levelClass.getMethod("getAddressField").invoke(o);
                String fieldName = (String) fieldClass.getMethod("getName").invoke(addressField);
                l.add(fieldName);
            }
            if (l.size() > 1) {
                Collections.reverse(l);
            }
        } catch (Exception e) {
            throw new APIException("Error obtaining address hierarchy levels", e);
        }

        return l;
    }

    /**
     * Given a user, returns the default locale (if any) for that user
     * Returns null if no default locate
     */
    public static Locale getDefaultLocale(User user) {

        if (user != null && user.getUserProperties() != null
                && user.getUserProperties().containsKey(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCALE)) {
            String localeString = user.getUserProperty(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCALE);
            Locale locale = WebUtil.normalizeLocale(localeString);
            return locale;
        }

        return null;
    }

}
