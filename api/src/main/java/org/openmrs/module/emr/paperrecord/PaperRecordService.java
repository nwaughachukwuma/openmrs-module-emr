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

package org.openmrs.module.emr.paperrecord;

import java.util.List;
import java.util.Map;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.Person;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.emr.EmrConstants;
import org.openmrs.module.emr.printer.PrinterService;

/**
 * Public API for functionality relating to paper medical records
 */
public interface PaperRecordService extends OpenmrsService {

    /**
     * Returns true/false if a paper medical record exist at the specified location with the specified identifier
     * (We assume a record exists if the specified identifier has been assigned to a patient at the
     * specified location)
     *
     * @param identifier
     * @param paperMedicalRecordLocation
     * @return
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_REQUEST_RECORDS)
    boolean paperRecordExists(String identifier, Location location);

    /**
     * Fetches the Paper Record Request with the specified id
     *
     * @param id primary key of the paper record request to retrieve
     * @return the patient record request with the specified id
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    PaperRecordRequest getPaperRecordRequestById(Integer id);

    /**
     * Fetches the Paper Record Merge Request with the specified id
     *
     * @param id
     * @return
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    PaperRecordMergeRequest getPaperRecordMergeRequestById(Integer id);

    /**
     * Fetches all Paper Record Requests for the specified Patient
     * 
     * @param patient a Patient
     * @return a List<PaperRecordRequest>
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    List<PaperRecordRequest> getPaperRecordRequestsByPatient(Patient patient);

    /**
     * Requests the paper record for the specified patient for the specified location
     *
     * @param patient the patient whose record we are requesting
     * @param recordLocation the location of the record (ie, "Mirebalais Hospital"); if the specified location is not
     *                       a medical record location, will search up the location hierarchy for a valid medical
     *                       record location
     * @param requestLocation the location where the record is to be sent
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_REQUEST_RECORDS)
    PaperRecordRequest requestPaperRecord(Patient patient, Location recordLocation, Location requestLocation);

    /**
     * Retrieves all records that are open (ie, have yet to be assigned to an archivist for retrieval)
     * and need to be pulled (ie, already exist and just need to be pulled, not created)
     *
     * @return the list of all open paper record requests that need to be pulled
     */
    // TODO: once we have multiple medical record locations, we will need to add location as a criteria (see paperRecordExists)
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    List<PaperRecordRequest> getOpenPaperRecordRequestsToPull();

    /**
     * Retrieves all records that are open (ie, have yet to be assigned to an archivist for retrieval)
     * and need to be created (ie, do not yet exist)
     *
     * @return the list of all open paper record requests that need to be created
     */
    // TODO: once we have multiple medical record locations, we will need to add location as a criteria (see paperRecordExists)
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    List<PaperRecordRequest> getOpenPaperRecordRequestsToCreate();

    /**
     * 
     * Creates or updates a Paper Record Request
     * 
     * @return
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    PaperRecordRequest savePaperRecordRequest(PaperRecordRequest paperRecordRequest);

    /**
     * Sets the status to ASSIGNED_TO_PULL and the assignee to the given value, for the given requests.
     *
     * @param requests
     * @param assignee
     * @param location the location to print any required registration labels at
     * @return the list that was passed in, but with assignees and status set
     * @throws IllegalStateException if any of the requests are not in the OPEN status
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    Map<String, List<String>> assignRequests(List<PaperRecordRequest> requests, Person assignee, Location location) throws UnableToPrintPaperRecordLabelException;

    /**
     * This internal method should not be invoked directly!
     *
     * Workaround because we need this method to be @Transactional and Spring won't handle calling a @Transactional
     * method from within the same class:
     * http://stackoverflow.com/questions/3423972/spring-transaction-method-call-by-the-method-within-the-same-class-does-not-wo
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    Map<String, List<String>>  assignRequestsInternal(List<PaperRecordRequest> requests, Person assignee, Location location) throws UnableToPrintPaperRecordLabelException;

    /**
     * Retrieves all records that have been assigned and need to be pulled
     *
     * @return the list of all assigned paper record requests that need to be pulled
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    List<PaperRecordRequest> getAssignedPaperRecordRequestsToPull();

    /**
     * Retrieves all records that have been assigned and need to be created
     *
     * @return
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    List<PaperRecordRequest> getAssignedPaperRecordRequestsToCreate();

    /**
     * Returns the pending (ie, open or assigned) paper record request (if any) for the record with the specified identifier
     * (there should only be one pending request per identifier & location)
     *
     * @param identifier
     * @return the pending (ie, open or assigned) paper record request with the specified identifier (returns null if no request found)
     * @throws IllegalStateException if more than one request is found
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    // TODO: once we have multiple medical record locations, we will need to add location as a criteria (see paperRecordExists)
    PaperRecordRequest getPendingPaperRecordRequestByIdentifier(String identifier);


    /**
     * Returns the "sent" paper record request (if any) for the record with specified identifier
     * (there should only be one sent request per identifier & location)
     *
     * @param identifier
     * @return returns the "sent" paper record request (if any) for the record with specified identifier
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    // TODO: once we have multiple medical record locations, we will need to add location as a criteria (see paperRecordExists)
    PaperRecordRequest getSentPaperRecordRequestByIdentifier(String identifier);


    /**
     * Marks the specified paper record request as "sent"
     *
     * @param request
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    void markPaperRecordRequestAsSent(PaperRecordRequest request);

    /**
     * Marks the specified paper record request as "cancelled"
     *
     * @param request
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    void markPaperRecordRequestAsCancelled(PaperRecordRequest request);

    /**
     * Marks all active requests with the specified identifier as returned
     */
    // TODO: once we have multiple medical record locations, we will need to add location as a criteria (see paperRecordExists)
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    void markPaperRecordRequestsAsReturned(String identifier)
        throws NoMatchingPaperMedicalRequestException;

    /**
     * Prints a label for the paper record associated wth the request
     * at the selected location
     *
     * @param request
     * @param location
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_REQUEST_RECORDS)
    void printPaperRecordLabel(PaperRecordRequest request, Location location) throws UnableToPrintPaperRecordLabelException;


    /**
     * Prints x numbers of labels for the paper record associated with the request
     * at the default location
     *
     * @param request
     * @param location
     * @param count the number of labels to print
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_REQUEST_RECORDS)
    void printPaperRecordLabels(PaperRecordRequest request, Location location, Integer count) throws UnableToPrintPaperRecordLabelException;

    /**
     * Prints x numbers of labels for the paper record associated with the patient
     *
     * @param patient the patient we want to print the label for
     * @param location the location where the record should be printed
     * @param count the of labels to print
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_REQUEST_RECORDS)
    void printPaperRecordLabels(Patient patient, Location location, Integer count) throws UnableToPrintPaperRecordLabelException;


    /**
     * Creates a request to merge two paper records
     *
     * @param preferredIdentifier the identifier of the preferred paper record
     * @param notPreferredIdentifier the identifier of the non-preferred paper record
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_REQUEST_RECORDS)
    void markPaperRecordsForMerge(PatientIdentifier preferredIdentifier, PatientIdentifier notPreferredIdentifier);

    /**
     * Marks that the paper record merge request has been completed
     *
     * @param mergeRequest
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    void markPaperRecordsAsMerged(PaperRecordMergeRequest mergeRequest);

    /**
     * Returns all merge requets with status = OPEN
     */
    @Authorized(EmrConstants.PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS)
    List<PaperRecordMergeRequest> getOpenPaperRecordMergeRequests();

    /**
     * Hack to bring this up to the interface level to allow us to stub out the template when printing
     */
    void setPrinterService(PrinterService printerService);

}

