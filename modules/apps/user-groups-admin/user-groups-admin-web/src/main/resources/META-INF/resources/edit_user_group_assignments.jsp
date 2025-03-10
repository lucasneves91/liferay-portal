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
String backURL = ParamUtil.getString(request, "backURL");

PortletURL homeURL = renderResponse.createRenderURL();

homeURL.setParameter("mvcPath", "/view.jsp");

if (Validator.isNull(backURL)) {
	backURL = homeURL.toString();
}

long userGroupId = ParamUtil.getLong(request, "userGroupId");

UserGroup userGroup = UserGroupServiceUtil.fetchUserGroup(userGroupId);

String displayStyle = ParamUtil.getString(request, "displayStyle");

if (Validator.isNull(displayStyle)) {
	displayStyle = portalPreferences.getValue(UserGroupsAdminPortletKeys.USER_GROUPS_ADMIN, "users-display-style", "list");
}
else {
	portalPreferences.setValue(UserGroupsAdminPortletKeys.USER_GROUPS_ADMIN, "users-display-style", displayStyle);

	request.setAttribute(WebKeys.SINGLE_PAGE_APPLICATION_CLEAR_CACHE, Boolean.TRUE);
}

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(backURL);

renderResponse.setTitle(userGroup.getName());

PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(request, "user-groups"), homeURL.toString());
PortalUtil.addPortletBreadcrumbEntry(request, userGroup.getName(), null);

EditUserGroupAssignmentsManagementToolbarDisplayContext editUserGroupAssignmentsManagementToolbarDisplayContext = new EditUserGroupAssignmentsManagementToolbarDisplayContext(request, renderRequest, renderResponse, displayStyle, "/edit_user_group_assignments.jsp");

LinkedHashMap<String, Object> userParams = new LinkedHashMap<String, Object>();

if (filterManageableOrganizations) {
	userParams.put("usersOrgsTree", user.getOrganizations());
}

userParams.put("usersUserGroups", Long.valueOf(userGroup.getUserGroupId()));

SearchContainer<User> searchContainer = editUserGroupAssignmentsManagementToolbarDisplayContext.getSearchContainer(userParams);

PortletURL portletURL = editUserGroupAssignmentsManagementToolbarDisplayContext.getPortletURL();
%>

<clay:management-toolbar-v2
	actionDropdownItems="<%= editUserGroupAssignmentsManagementToolbarDisplayContext.getActionDropdownItems() %>"
	clearResultsURL="<%= editUserGroupAssignmentsManagementToolbarDisplayContext.getClearResultsURL() %>"
	componentId="editUserGroupAssignmentsManagementToolbar"
	filterDropdownItems="<%= editUserGroupAssignmentsManagementToolbarDisplayContext.getFilterDropdownItems() %>"
	itemsTotal="<%= searchContainer.getTotal() %>"
	searchActionURL="<%= editUserGroupAssignmentsManagementToolbarDisplayContext.getSearchActionURL() %>"
	searchContainerId="users"
	searchFormName="searchFm"
	selectable="<%= true %>"
	showCreationMenu="<%= true %>"
	showSearch="<%= true %>"
	sortingOrder="<%= searchContainer.getOrderByType() %>"
	sortingURL="<%= editUserGroupAssignmentsManagementToolbarDisplayContext.getSortingURL() %>"
	viewTypeItems="<%= editUserGroupAssignmentsManagementToolbarDisplayContext.getViewTypeItems() %>"
/>

<aui:form action="<%= portletURL.toString() %>" cssClass="container-fluid-1280" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="userGroupId" type="hidden" value="<%= userGroup.getUserGroupId() %>" />
	<aui:input name="deleteUserGroupIds" type="hidden" />
	<aui:input name="addUserIds" type="hidden" />
	<aui:input name="removeUserIds" type="hidden" />

	<div id="breadcrumb">
		<liferay-ui:breadcrumb
			showCurrentGroup="<%= false %>"
			showGuestGroup="<%= false %>"
			showLayout="<%= false %>"
			showPortletBreadcrumb="<%= true %>"
		/>
	</div>

	<liferay-ui:search-container
		id="users"
		searchContainer="<%= searchContainer %>"
		var="userSearchContainer"
	>
		<liferay-ui:search-container-row
			className="com.liferay.portal.kernel.model.User"
			escapedModel="<%= true %>"
			keyProperty="userId"
			modelVar="user2"
			rowIdProperty="screenName"
		>

			<%
			boolean showActions = true;
			%>

			<%@ include file="/user_search_columns.jspf" %>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="<%= displayStyle %>"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>

<aui:script sandbox="<%= true %>">
	var form = document.<portlet:namespace />fm;

	<portlet:renderURL var="selectUsersURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
		<portlet:param name="mvcPath" value="/select_user_group_users.jsp" />
		<portlet:param name="userGroupId" value="<%= String.valueOf(userGroupId) %>" />
	</portlet:renderURL>

	function <portlet:namespace />addUsers(event) {
		Liferay.Util.openSelectionModal({
			onSelect: function (selectedItem) {
				if (selectedItem) {
					Liferay.Util.postForm(form, {
						data: {
							addUserIds: selectedItem,
						},
						url:
							'<portlet:actionURL name="editUserGroupAssignments" />',
					});
				}
			},
			multiple: true,
			selectEventName: '<portlet:namespace />selectUsers',
			title:
				'<liferay-ui:message arguments="<%= HtmlUtil.escape(userGroup.getName()) %>" key="add-users-to-x" />',
			url: '<%= selectUsersURL %>',
		});
	}

	function <portlet:namespace />removeUsers() {
		Liferay.Util.postForm(form, {
			data: {
				redirect: '<%= portletURL.toString() %>',
				removeUserIds: Liferay.Util.listCheckedExcept(
					form,
					'<portlet:namespace />allRowIds'
				),
			},
			url: '<portlet:actionURL name="editUserGroupAssignments" />',
		});
	}

	Liferay.componentReady('editUserGroupAssignmentsManagementToolbar').then(
		function (managementToolbar) {
			managementToolbar.on(
				'actionItemClicked',
				<portlet:namespace />removeUsers
			);
			managementToolbar.on(
				'creationButtonClicked',
				<portlet:namespace />addUsers
			);
		}
	);
</aui:script>