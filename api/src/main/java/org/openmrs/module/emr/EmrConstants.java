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

package org.openmrs.module.emr;

import java.util.HashMap;
import java.util.Map;

public class EmrConstants {

    public static final String EMR_MODULE_ID = "emr";

    public final static String GP_RADIOLOGY_ORDER_ENCOUNTER_TYPE = "emr.radiologyOrderEncounterType";

    public static final String GP_CHECK_IN_ENCOUNTER_TYPE = "emr.checkInEncounterType";

    public static final String GP_TRIAGE_ENCOUNTER_TYPE = "emr.triageEncounterType";

    public static final String GP_CONSULT_ENCOUNTER_TYPE = "emr.consultEncounterType";

    public static final String GP_AT_FACILITY_VISIT_TYPE = "emr.atFacilityVisitType";

    public static final String GP_CLINICIAN_ENCOUNTER_ROLE = "emr.clinicianEncounterRole";

    public static final String GP_ORDERING_PROVIDER_ENCOUNTER_ROLE = "emr.orderingProviderEncounterRole";

    public static final String GP_CHECK_IN_CLERK_ENCOUNTER_ROLE = "emr.checkInClerkEncounterRole";

    public static final String GP_RADIOLOGY_TEST_ORDER_TYPE = "emr.radiologyTestOrderType";

    public static final String GP_PAPER_RECORD_IDENTIFIER_TYPE = "emr.paperRecordIdentifierType";

    public static final String GP_EXTRA_PATIENT_IDENTIFIER_TYPES = "emr.extraPatientIdentifierTypes";

    public static final String GP_DIAGNOSIS_SET_OF_SETS = "emr.concept.diagnosisSetOfSets";

    public static final String GP_XRAY_ORDERABLES_CONCEPT = "emr.xrayOrderablesConcept";

    public static final String GP_CT_SCAN_ORDERABLES_CONCEPT = "emr.ctScanOrderablesConcept";

    public static final String GP_ULTRASOUND_ORDERABLES_CONCEPT = "emr.ultrasoundOrderablesConcept";

    public static final String PRIVILEGE_PAPER_RECORDS_MANAGE_REQUESTS = "Paper Records - Manage Requests";

    public static final String PRIVILEGE_PAPER_RECORDS_REQUEST_RECORDS = "Paper Records - Request Records";

    public static final String PRIVILEGE_PRINTERS_ACCESS_PRINTERS = "Printers - Access Printers";

    public static final String PRIVILEGE_PRINTERS_MANAGE_PRINTERS = "Printers - Manage Printers";

    public static final String LOCATION_TAG_SUPPORTS_VISITS = "Visit Location";

    public static final String LOCATION_TAG_SUPPORTS_LOGIN = "Login Location";

    public static final String LOCATION_TAG_MEDICAL_RECORD_LOCATION = "Medical Record Location";

    public static final String ROLE_PREFIX_CAPABILITY = "Application Role: ";

    public static final String ROLE_PREFIX_PRIVILEGE_LEVEL = "Privilege Level: ";

    public static final String PRIVILEGE_LEVEL_FULL_ROLE = ROLE_PREFIX_PRIVILEGE_LEVEL+"Full";

    public static final String PRIVILEGE_LEVEL_FULL_DESCRIPTION = "A role that has all API privileges";

    public static final String PRIVILEGE_LEVEL_FULL_UUID = "ab2160f6-0941-430c-9752-6714353fbd3c";

    public static final String PRIVILEGE_PREFIX_APP = "App: ";

    public static final String PRIVILEGE_PREFIX_TASK = "Task: ";

    public static final String PRIVILEGE_DELETE_ENCOUNTER="Task: emr.patient.encounter.delete";

    public static final String PRIMARY_IDENTIFIER_TYPE = "emr.primaryIdentifierType";

    public static final String DAEMON_USER_UUID = "A4F30A1B-5EB9-11DF-A648-37A07F9C90FB";

    public static final String COOKIE_NAME_LAST_SESSION_LOCATION = "emr.lastSessionLocation";

    public static final String SESSION_ATTRIBUTE_ERROR_MESSAGE = "emr.errorMessage";

    public static final String SESSION_ATTRIBUTE_INFO_MESSAGE = "emr.infoMessage";

    public static final String SESSION_ATTRIBUTE_TOAST_MESSAGE = "emr.toastMessage";

    public static final String TASK_CLOSE_STALE_VISITS_NAME = "EMR module - Close Stale Visits";

    public static final String TASK_CLOSE_STALE_VISITS_DESCRIPTION = "Closes any open visits that are no longer active";

    public static final long TASK_CLOSE_STALE_VISITS_REPEAT_INTERVAL = 5 * 60; // 5 minutes

    public static final String TASK_CLOSE_STALE_PULL_REQUESTS = "EMR module - Close Stale Pull Request";

    public static final String TASK_CLOSE_STALE_PULL_REQUESTS_DESCRIPTION = "Closes any pending pull record requests older than a specified date";

    public static final String UNKNOWN_PATIENT_PERSON_ATTRIBUTE_TYPE_NAME="Unknown patient";

    public static final String TELEPHONE_ATTRIBUTE_TYPE_NAME="Telephone Number";

    public static final String LOCATION_ATTRIBUTE_TYPE_NAME_TO_PRINT_ON_ID_CARD = "a5fb5770-409a-11e2-a25f-0800200c9a66";

    public static final String LOCATION_ATTRIBUTE_TYPE_LOCATION_CODE = "64f01c78-191d-4947-a201-7e0a7f0caf21";

    // remember if we add more types here to also create them in the module activator
    public static final Map<String, String> LOCATION_ATTRIBUTE_TYPE_DEFAULT_PRINTER = new HashMap<String, String>() {{
        put("ID_CARD", "b48ef9a0-38d3-11e2-81c1-0800200c9a66");
        put("LABEL", "bd6c1c10-38d3-11e2-81c1-0800200c9a66");
    }};

    public static final String PAYMENT_AMOUNT_CONCEPT = "emr.paymentAmountConcept";
    public static final String PAYMENT_REASON_CONCEPT = "emr.paymentReasonConcept";
    public static final String PAYMENT_RECEIPT_NUMBER_CONCEPT = "emr.paymentReceiptNumberConcept";
    public static final String PAYMENT_CONSTRUCT_CONCEPT = "emr.paymentConstructConcept";

    public static final String HTMLFORMENTRY_UI_MESSAGE_TAG_NAME = "uimessage";

    public static final String EMR_CONCEPT_SOURCE_NAME = "org.openmrs.module.emr";
    public static final String EMR_CONCEPT_SOURCE_DESCRIPTION = "Source used to tag concepts used in the EMR module";
    public static final String EMR_CONCEPT_SOURCE_UUID = "b5e44116-778f-11e2-ad73-903c7bb72694";

    // codes in the concept source provided by this module
    public static final String CONCEPT_CODE_DIAGNOSIS_CONCEPT_SET = "Diagnosis Concept Set";
    public static final String CONCEPT_CODE_CONSULT_FREE_TEXT_COMMENT = "Consult Free Text Comments";
    public static final String CONCEPT_CODE_CODED_DIAGNOSIS = "Coded Diagnosis";
    public static final String CONCEPT_CODE_NON_CODED_DIAGNOSIS = "Non-Coded Diagnosis";
    public static final String CONCEPT_CODE_DIAGNOSIS_ORDER = "Diagnosis Order"; // e.g. Primary or Secondary
    public static final String CONCEPT_CODE_DIAGNOSIS_ORDER_PRIMARY = "Primary";
    public static final String CONCEPT_CODE_DIAGNOSIS_ORDER_SECONDARY = "Secondary";
    public static final String CONSULTATION_TYPE_UUID = "92fd09b4-5335-4f7e-9f63-b2a663fd09a6";

    // these should be constants in OpenMRS core
    public static final String SAME_AS_CONCEPT_MAP_TYPE_UUID = "35543629-7d8c-11e1-909d-c80aa9edcf4e";
    public static final String NARROWER_THAN_CONCEPT_MAP_TYPE_UUID = "43ac5109-7d8c-11e1-909d-c80aa9edcf4e";

}