<%
    def dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy")
    def timeFormat = new java.text.SimpleDateFormat("HH:mm")
%>

<script type="text/javascript">
    breadcrumbs.push({ label: "${ui.message("emr.patientDashBoard.visits")}" , link:'${ui.pageLink("emr", "patient", [patientId: patient.id])}'});
</script>

<script type="text/template" id="visitDetailsTemplate">
    <p>
        {{ if (stopDatetime) { }}
            ${ ui.message("emr.visitDetails", '{{- startDatetime }}', '{{- stopDatetime }}', '{{- location }}') }
        {{ } else { }}
            ${ ui.message("emr.activeVisit", '{{- startDatetime }}', '{{- location }}') }
        {{ } }}
    </p>

    <h4>Encounters</h4>
    <ul id="encountersList">
        {{ _.each(encounters, function(encounter) { }}
            <li>
                <span class="encounter-date">
                    <i class="icon-time"></i>
                    {{- encounter.encounterTime }}
                    {{- encounter.encounterDate }}
                </span>
                <span class="encounter-details">
                    <span class="encounter-type"> 
                        {{- encounter.encounterType }}
                    </span>
                    by
                    {{- encounter.encounterProviders[0].provider }}
                    in
                    {{- encounter.location }}
                </span>
            </li>
        {{ }); }}
    </ul>
</script>

<script type="text/javascript">
    jq(function() {
        function loadVisit(visitElement) {
            jq.getJSON(
                emr.fragmentActionLink("emr", "visit/visitDetails", "getVisitDetails", {
                    visitId: visitElement.attr('visitId')
                }) 
            ).success(function(data) {
                jq('.viewVisitDetails').removeClass('selected');
                visitElement.addClass('selected');
                visitDetailsSection.html(visitDetailsTemplate(data));
                visitDetailsSection.show();
            }).error(function(err) {
                emr.errorMessage(err);
            });
        }

        var visitDetailsTemplate = _.template(jq('#visitDetailsTemplate').html());
        var visitsSection = jq("#visits-list");
        var visitDetailsSection = jq("#visit-details");

        //load first visit
        loadVisit(jq('.viewVisitDetails').first());

        jq('.viewVisitDetails').click(function() {
            loadVisit(jq(this));
            return false;
        });
    });
</script>

<ul id="visits-list">
    <% patient.allVisitsUsingWrappers.each { wrapper -> %>
        <li class="viewVisitDetails" visitId="${wrapper.visit.visitId}">
            <span class="visit-date">
                <i class="icon-time"></i>
                ${dateFormat.format(wrapper.visit.startDatetime)}
                <% if(wrapper.visit.stopDatetime != null) { %>
                    - ${dateFormat.format(wrapper.visit.stopDatetime)}
                <% } else { %>
                    (active since ${timeFormat.format(wrapper.visit.startDatetime)})
                <% } %>
            </span>
            <span class="visit-primary-diagnosis">
                No diagnosis yet.
            </span>
            <span class="arrow-border"></span>
            <span class="arrow"></span>
        </li>
    <% } %>
    <% if(patient.allVisitsUsingWrappers.size == 0) { %>
        No visits yet.
    <% } %>
</ul>
<div id="visit-details">
</div>