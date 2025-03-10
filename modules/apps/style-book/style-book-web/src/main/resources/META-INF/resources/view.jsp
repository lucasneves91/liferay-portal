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
StyleBookDisplayContext styleBookDisplayContext = new StyleBookDisplayContext(request, liferayPortletRequest, liferayPortletResponse);

StyleBookManagementToolbarDisplayContext styleBookManagementToolbarDisplayContext = new StyleBookManagementToolbarDisplayContext(request, liferayPortletRequest, liferayPortletResponse, styleBookDisplayContext.getStyleBookEntriesSearchContainer());
%>

<clay:management-toolbar-v2
	displayContext="<%= styleBookManagementToolbarDisplayContext %>"
/>

<portlet:actionURL name="/style_book/delete_style_book_entry" var="deleteStyleBookEntryURL">
	<portlet:param name="redirect" value="<%= currentURL %>" />
</portlet:actionURL>

<clay:container-fluid>
	<aui:form action="<%= deleteStyleBookEntryURL %>" name="fm">
		<liferay-ui:search-container
			searchContainer="<%= styleBookDisplayContext.getStyleBookEntriesSearchContainer() %>"
		>
			<liferay-ui:search-container-row
				className="com.liferay.style.book.model.StyleBookEntry"
				keyProperty="styleBookEntryId"
				modelVar="styleBookEntry"
			>

				<%
				row.setCssClass("entry-card lfr-asset-item " + row.getCssClass());
				%>

				<liferay-ui:search-container-column-text>
					<clay:vertical-card-v2
						verticalCard="<%= new StyleBookVerticalCard(styleBookEntry, renderRequest, renderResponse, searchContainer.getRowChecker()) %>"
					/>
				</liferay-ui:search-container-column-text>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				displayStyle="icon"
				markupView="lexicon"
			/>
		</liferay-ui:search-container>
	</aui:form>
</clay:container-fluid>

<aui:form name="styleBookEntryFm">
	<aui:input name="styleBookEntryIds" type="hidden" />
</aui:form>

<portlet:actionURL name="/style_book/update_style_book_entry_preview" var="styleBookEntryPreviewURL">
	<portlet:param name="redirect" value="<%= currentURL %>" />
</portlet:actionURL>

<aui:form action="<%= styleBookEntryPreviewURL %>" name="styleBookEntryPreviewFm">
	<aui:input name="styleBookEntryId" type="hidden" />
	<aui:input name="fileEntryId" type="hidden" />
</aui:form>

<liferay-frontend:component
	componentId="<%= StyleBookWebKeys.STYLE_BOOK_ENTRY_DROPDOWN_DEFAULT_EVENT_HANDLER %>"
	module="js/StyleBookEntryDropdownDefaultEventHandler.es"
/>

<liferay-frontend:component
	componentId="<%= styleBookManagementToolbarDisplayContext.getDefaultEventHandler() %>"
	context="<%= styleBookManagementToolbarDisplayContext.getComponentContext() %>"
	module="js/ManagementToolbarDefaultEventHandler.es"
/>