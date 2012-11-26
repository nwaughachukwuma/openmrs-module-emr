<%
    ui.decorateWith("emr", "standardEmrPage")
    ui.includeJavascript("emr", "knockout-2.1.0.js")
    ui.includeJavascript("emr", "custom/retrospectiveCheckin.js")
    ui.includeCss("emr", "retrospectiveCheckin.css")
%>

<script type="text/javascript">
    jQuery(function() {
        ko.applyBindings(RetrospectiveCheckinViewModel());
        FormNavigator(jQuery('#submitButton'), jQuery('#cancelButton'));
    });
</script>

<h1>Retrospective Check-in</h1>
<section id="checkinInformation">
    <img class="field_check" src=${ui.resourceLink("emr", "images/checked.png")}
        data-bind="style: {visibility: checkinInfoIsValid() ? 'visible':'hidden'}"/>
    <span class="label">Check-in information</span>
    <span class="value" data-bind="text:patientIdentifier"></span> -
    <span class="value" data-bind="text:location() ? location().name : ''"></span> -
    <span class="value" data-bind="text:checkinDate"></span>

    <ul>
        <li>
            <label for="patientIdentifier">Patient identifier</label>
            <input id="patientIdentifier" type="text" data-bind="value:patientIdentifier" />
        </li>
        <li>
            <label for="location">Location</label>
            <select id="location" data-bind="value:location, options:locations, optionsText:'name', optionsCaption:'Choose location ...'">
            </select>
        </li>
        <li>
            <label for="checkinDate">Check-in date</label>
            <input id="checkinDate" type="date" data-bind="value:checkinDate" placeholder="  /  /  "/>
        </li>
    </ul>
</section>

<section id="paymentInformation">
    <img class="field_check" src=${ui.resourceLink("emr", "images/checked.png")}
        data-bind="style: {visibility: paymentInfoIsValid() ? 'visible':'hidden'}" />
    <span class="label">Payment</span>
    <span class="value" data-bind="text:paymentReason() ? paymentReason().name : ''"></span>
    <span class="value" data-bind="text:amountPaid() ? amountPaid().name : ''"></span>

    <ul>
        <li>
            <label for="paymentReason">Reason</label>
            <select id="paymentReason" data-bind="value:paymentReason, options:paymentReasons, optionsText:'name', optionsCaption:'Choose reason ...'">
            </select>
        </li>
        <li>
            <label for="paymentAmount">Amount</label>
            <select id="paymentAmount" data-bind="value:amountPaid, options:paymentAmounts, optionsText:'name', optionsCaption:'Choose amount ...'" >
            </select>
        </li>
    </ul>
</section>

<section id="encountersInformation">
    <img class="field_check" src=${ui.resourceLink("emr", "images/checked.png")}
         data-bind="style: {visibility: amountPaid() ? 'visible':'hidden'}"/>
    <span class="label">Encounters</span>

    <ul>
        <li>
            <label for="radiologyEncounter">Radiology</label>
            <input id="radiologyEncounter" type="checkbox" data-bind="checked:radiologyEncounter" />
        </li>
        <li>
            <label for="diagnosesEncounter">Diagnostics</label>
            <input id="diagnosesEncounter" type="checkbox" data-bind="checked:diagnosticsEncounter" />
            <div data-bind="visible: diagnosticsEncounter">
                <label for="diagnose">Diagnostic</label>
                <input id="diagnose" type="text" data-bind="value:diagnostic"/>
            </div>
            <div data-bind="visible: diagnosticsEncounter">
                <label for="diagnosticPhisician">Phisician</label>
                <input id="diagnosticPhisician" type="text" data-bind="value:diagnosticPhisician" />
            </div>
        </li>
        <li>
            <label for="medicationAdministration">Medication administration</label>
            <input id="medicationAdministration" type="checkbox" data-bind="checked:medicationEncounter"/>
        </li>
        <li>
            <label for="admissionEncounter">Admision</label>
            <input id="admissionEncounter" type="checkbox" data-bind="checked:admissionEncounter" />
            <div style="display: block" data-bind="visible: admissionEncounter">
                <label for="admissionDate">Admission date</label>
                <input id="admissionDate" type="date" data-bind="value:admissionDate" />
            </div>
        </li>
        <li>
            <label for="dischargeEncounter">Discharge</label>
            <input id="dischargeEncounter" type="checkbox" data-bind="checked:dischargeEncounter" />

            <div style="display: block" data-bind="visible: dischargeEncounter">
                <label for="dischargeDate">Discharge date</label>
                <input id="dischargeDate" type="date" data-bind="value:dischargeDate" />
            </div>
        </li>
    </ul>
</section>

<div class="actions">
    <a id="cancelButton" href="#" class="cancel">Cancel</a>
    <input id="submitButton" type="button" class="submit" value="Submit" />
</div>