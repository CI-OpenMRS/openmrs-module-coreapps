<%
    ui.decorateWith("appui", "standardEmrPage")

	ui.includeCss("coreapps", "patientdashboard/patientDashboard.css")

    ui.includeJavascript("uicommons", "bootstrap-collapse.js")
    ui.includeJavascript("uicommons", "bootstrap-transition.js")


    def tabs = [
        [ id: "visits", label: ui.message("coreapps.patientDashBoard.visits"), provider: "coreapps", fragment: "patientdashboard/visits" ],
        patientTabs.collect{
            [id: it.id, label: ui.message(it.label), provider: it.extensionParams.provider, fragment: it.extensionParams.fragment]
        }
    ]

    if (!isNewPatientHeaderEnabled) {
      tabs.add([ id: "contactInfo", label: ui.message("coreapps.patientDashBoard.contactinfo"), provider: "coreapps", fragment: "patientdashboard/contactInfo" ])
    }
    tabs = tabs.flatten()

%>
<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.format(patient.patient.familyName) }, ${ ui.format(patient.patient.givenName) }" , link: '${ui.pageLink("coreapps", "patientdashboard/patientDashboard", [patientId: patient.patient.id])}'}
    ];

    jq(function(){
        jq(".tabs").tabs();
        visit.createQuickVisitCreationDialog(${patient.id});
    });

    var patient = { id: ${ patient.id } };
</script>

<% if(includes){
     includes.each(){ %>
         ${ ui.includeFragment(it.extensionParams.resource, it.extensionParams.fragment)}
<%   }
} %>


${ ui.includeFragment("coreapps", "patientHeader", [ patient: patient.patient, activeVisit: activeVisit ]) }
<div class="actions dropdown">
    <span class="dropdown-name"><i class="icon-cog"></i>${ ui.message("coreapps.actions") }<i class="icon-sort-down"></i></span>
    <ul>
        <% if (!activeVisit && !patient.patient.dead) { %>
        <li>
            <a href="javascript:visit.showQuickVisitCreationDialog()">
                <i class="icon-check-in"></i>${ ui.message("coreapps.task.startVisit.label") }
            </a>
        </li>
        <% } %>
        <% overallActions.each {
            def url = it.url
            url = it.url(contextPath, actionBindings)
        %>
            <li>
                <a href="${ url }"><i class="${ it.icon }"></i>${ ui.message(it.label) }</a>
            </li>
        <% } %>
    </ul>
</div>

<div class="tabs" xmlns="http://www.w3.org/1999/html">
    <div class="dashboard-container">

        <ul>
            <% tabs.each { %>
            <li>
                <a href="#${ it.id }">
                    ${ it.label }
                </a>
            </li>
            <% } %>

        </ul>

        <% tabs.each { %>
        <div id="${it.id}">
            ${ ui.includeFragment(it.provider, it.fragment, [ patient: patient ]) }
        </div>
        <% } %>

    </div>
</div>

<div id="quick-visit-creation-dialog" class="dialog">
    <div class="dialog-header">
        <h3>
            <i class="icon-check-in"></i>
            ${ ui.message("coreapps.visit.createQuickVisit.title") }</h3>
    </div>
    <div class="dialog-content">
        <p class="dialog-instructions">${ ui.message("coreapps.task.startVisit.message", ui.format(patient.patient)) }</p>

        <button class="confirm right">${ ui.message("coreapps.confirm") }</button>
        <button class="cancel">${ ui.message("coreapps.cancel") }</button>
    </div>
</div>
