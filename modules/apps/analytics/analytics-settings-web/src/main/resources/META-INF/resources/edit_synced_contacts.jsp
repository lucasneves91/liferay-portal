<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("mvcRenderCommandName", "/configuration_admin/view_configuration_screen");

boolean includeSyncContactsFields = ParamUtil.getBoolean(request, "includeSyncContactsFields");

if (includeSyncContactsFields) {
	portletURL.setParameter("configurationScreenKey", "2-synced-contact-data");
}
else {
	portletURL.setParameter("configurationScreenKey", "2-synced-contacts");
}

String redirect = portletURL.toString();

AnalyticsConfiguration analyticsConfiguration = (AnalyticsConfiguration)request.getAttribute(AnalyticsSettingsWebKeys.ANALYTICS_CONFIGURATION);
AnalyticsUsersManager analyticsUsersManager = (AnalyticsUsersManager)request.getAttribute(AnalyticsSettingsWebKeys.ANALYTICS_USERS_MANAGER);

boolean connected = false;

if (!Validator.isBlank(analyticsConfiguration.token())) {
	connected = true;
}

boolean syncAllContacts = analyticsConfiguration.syncAllContacts();
Set<String> syncedOrganizationIds = SetUtil.fromArray(analyticsConfiguration.syncedOrganizationIds());
Set<String> syncedUserGroupIds = SetUtil.fromArray(analyticsConfiguration.syncedUserGroupIds());

if (includeSyncContactsFields) {
	portletDisplay.setShowBackIcon(true);
	portletDisplay.setURLBack(ParamUtil.getString(request, "backURL", redirect));

	PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(resourceBundle, "select-contact-data"), redirect);
	PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(resourceBundle, "select-contacts"), currentURL);
}
%>

<portlet:actionURL name="/analytics_settings/edit_synced_contacts" var="editSyncedContactsURL" />

<portlet:renderURL var="editSyncedContactsFieldsURL">
	<portlet:param name="mvcRenderCommandName" value="/analytics_settings/edit_synced_contacts_fields" />
</portlet:renderURL>

<c:if test="<%= includeSyncContactsFields %>">
	<clay:container-fluid>
		<clay:row>
			<clay:col
				size="12"
			>
				<div id="breadcrumb">
					<liferay-ui:breadcrumb
						showCurrentGroup="<%= false %>"
						showGuestGroup="<%= false %>"
						showLayout="<%= false %>"
						showPortletBreadcrumb="<%= true %>"
					/>
				</div>
			</clay:col>
		</clay:row>
	</clay:container-fluid>
</c:if>

<clay:sheet
	cssClass="portlet-analytics-settings"
>
	<h2>
		<liferay-ui:message key="contact-data" />
	</h2>

	<div class="c-pb-3 form-text">
		<liferay-ui:message key="contact-data-help" />
	</div>

	<aui:form action="<%= includeSyncContactsFields ? editSyncedContactsFieldsURL : editSyncedContactsURL %>" method="post" name="fm">
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="includeSyncContactsFields" type="hidden" value="<%= String.valueOf(includeSyncContactsFields) %>" />

		<fieldset <%= connected ? "" : "disabled" %>>
			<label class="control-label">
				<liferay-ui:message key="sync-all-contacts" />
			</label>

			<br />

			<label class="mb-5 mt-3 toggle-switch">
				<input class="toggle-switch-check" name="<portlet:namespace />syncAllContacts" type="checkbox" <%= syncAllContacts ? "checked" : "" %> />

				<span aria-hidden="true" class="toggle-switch-bar">
					<span class="toggle-switch-handle" />
				</span>
				<span class="toggle-switch-text toggle-switch-text-right">
					<liferay-ui:message arguments="<%= analyticsUsersManager.getCompanyUsersCount(themeDisplay.getCompanyId()) %>" key="sync-all-x-contacts" />
				</span>
			</label>
		</fieldset>

		<fieldset <%= connected ? "" : "disabled" %>>
			<label class="control-label">
				<liferay-ui:message key="sync-by-user-groups-and-organizations" />
			</label>

			<c:choose>
				<c:when test="<%= connected %>">
					<portlet:renderURL var="createUserGroupURL">
						<portlet:param name="mvcRenderCommandName" value="/analytics_settings/edit_synced_contacts_groups" />
						<portlet:param name="redirect" value="<%= currentURL %>" />
						<portlet:param name="includeSyncContactsFields" value="<%= String.valueOf(includeSyncContactsFields) %>" />
					</portlet:renderURL>

					<a class="d-flex m-4 p-2 text-decoration-none" href=<%= createUserGroupURL %>>
				</c:when>
				<c:otherwise>
					<span class="contacts-link-disabled d-flex m-4 p-2">
				</c:otherwise>
			</c:choose>

				<div class="pr-3">
					<clay:sticker
						cssClass="sticker-light"
						displayType="light"
						icon="users"
					/>
				</div>

				<div>
					<p class="list-group-title">
						<liferay-ui:message key="sync-by-user-groups" />
					</p>

					<small class="list-group-subtext">
						<liferay-ui:message arguments='<%= syncAllContacts ? "all" : syncedUserGroupIds.size() %>' key="x-user-groups-selected" />
					</small>
				</div>

			<c:choose>
				<c:when test="<%= connected %>">
					</a>
				</c:when>
				<c:otherwise>
					</span>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="<%= connected %>">
					<portlet:renderURL var="createOrganizationsURL">
						<portlet:param name="mvcRenderCommandName" value="/analytics_settings/edit_synced_contacts_organizations" />
						<portlet:param name="redirect" value="<%= currentURL %>" />
						<portlet:param name="includeSyncContactsFields" value="<%= String.valueOf(includeSyncContactsFields) %>" />
					</portlet:renderURL>

					<a class="d-flex m-4 p-2 text-decoration-none" href=<%= createOrganizationsURL %>>
				</c:when>
				<c:otherwise>
					<span class="contacts-link-disabled d-flex m-4 p-2">
				</c:otherwise>
			</c:choose>

				<div class="pr-3">
					<clay:sticker
						cssClass="sticker-light"
						displayType="light"
						icon="organizations"
					/>
				</div>

				<div>
					<p class="list-group-title">
						<liferay-ui:message key="sync-by-organizations" />
					</p>

					<small class="list-group-subtext">
						<liferay-ui:message arguments='<%= syncAllContacts ? "all" : syncedOrganizationIds.size() %>' key="x-organizations-selected" />
					</small>
				</div>

			<c:choose>
				<c:when test="<%= connected %>">
					</a>
				</c:when>
				<c:otherwise>
					</span>
				</c:otherwise>
			</c:choose>
		<fieldset>

		<aui:button-row>
			<aui:button disabled="<%= !connected %>" type="submit" value='<%= includeSyncContactsFields ? "save-and-next" : "save" %>' />
		</aui:button-row>
	</aui:form>
</clay:sheet>