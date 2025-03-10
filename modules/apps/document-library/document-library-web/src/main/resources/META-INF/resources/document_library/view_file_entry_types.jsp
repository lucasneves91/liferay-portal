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

<%@ include file="/document_library/init.jsp" %>

<%
DLViewFileEntryTypesDisplayContext dlViewFileEntryTypesDisplayContext = new DLViewFileEntryTypesDisplayContext(request, renderRequest, renderResponse);
%>

<liferay-util:include page="/document_library/navigation.jsp" servletContext="<%= application %>" />

<clay:management-toolbar-v2
	clearResultsURL="<%= dlViewFileEntryTypesDisplayContext.getClearResultsURL() %>"
	creationMenu="<%= dlViewFileEntryTypesDisplayContext.getCreationMenu() %>"
	disabled="<%= dlViewFileEntryTypesDisplayContext.getTotalItems() == 0 %>"
	itemsTotal="<%= dlViewFileEntryTypesDisplayContext.getTotalItems() %>"
	searchActionURL="<%= dlViewFileEntryTypesDisplayContext.getSearchActionURL() %>"
	searchFormName="fm"
	selectable="<%= false %>"
/>

<clay:container-fluid
	cssClass="main-content-body"
>
	<liferay-ui:breadcrumb
		showLayout="<%= false %>"
	/>

	<liferay-ui:error exception="<%= RequiredFileEntryTypeException.class %>" message="cannot-delete-a-document-type-that-is-presently-used-by-one-or-more-documents" />

	<liferay-ui:search-container
		searchContainer="<%= dlViewFileEntryTypesDisplayContext.getSearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.document.library.kernel.model.DLFileEntryType"
			escapedModel="<%= true %>"
			keyProperty="fileEntryTypeId"
			modelVar="fileEntryType"
		>

			<%
			PortletURL rowURL = liferayPortletResponse.createRenderURL();

			if (dlViewFileEntryTypesDisplayContext.useDataEngineEditor()) {
				rowURL.setParameter("mvcRenderCommandName", "/document_library/edit_file_entry_type_data_definition");
			}
			else {
				rowURL.setParameter("mvcRenderCommandName", "/document_library/edit_file_entry_type");
			}

			rowURL.setParameter("redirect", currentURL);
			rowURL.setParameter("fileEntryTypeId", String.valueOf(fileEntryType.getFileEntryTypeId()));
			%>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand table-cell-minw-200 table-title"
				href="<%= DLFileEntryTypePermission.contains(permissionChecker, fileEntryType, ActionKeys.UPDATE) ? rowURL : null %>"
				name="name"
				value="<%= fileEntryType.getName(locale) %>"
			/>

			<%
			Group group = GroupLocalServiceUtil.getGroup(fileEntryType.getGroupId());
			%>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand-small table-cell-minw-150"
				name="scope"
				value="<%= LanguageUtil.get(request, group.getScopeLabel(themeDisplay)) %>"
			/>

			<liferay-ui:search-container-column-date
				cssClass="table-cell-expand-small table-cell-ws-nowrap"
				name="modified-date"
				value="<%= fileEntryType.getModifiedDate() %>"
			/>

			<c:choose>
				<c:when test="<%= dlViewFileEntryTypesDisplayContext.useDataEngineEditor() %>">
					<liferay-ui:search-container-column-jsp
						cssClass="entry-action"
						path="/document_library/file_entry_type_action_data_definition.jsp"
					/>
				</c:when>
				<c:otherwise>
					<liferay-ui:search-container-column-jsp
						cssClass="entry-action"
						path="/document_library/file_entry_type_action.jsp"
					/>
				</c:otherwise>
			</c:choose>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</clay:container-fluid>