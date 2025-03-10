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
ViewDisplayContext viewDisplayContext = (ViewDisplayContext)request.getAttribute(ViewDisplayContext.class.getName());
%>

<clay:management-toolbar
	displayContext="<%= viewDisplayContext.getTranslationEntryManagementToolbarDisplayContext() %>"
/>

<clay:container-fluid>
	<aui:form action="<%= viewDisplayContext.getActionURL() %>" cssClass="container-fluid-1280" name="fm">
		<liferay-ui:search-container
			id="searchContainer"
			searchContainer="<%= viewDisplayContext.getSearchContainer() %>"
		>
			<liferay-ui:search-container-row
				className="com.liferay.translation.model.TranslationEntry"
				keyProperty="translationEntryId"
				modelVar="translationEntry"
			>

				<%
				row.setData(
					HashMapBuilder.<String, Object>put(
						"actions", viewDisplayContext.getAvailableActions(translationEntry)
					).build());
				%>

				<liferay-ui:search-container-column-text
					cssClass="table-cell-expand table-title"
					href="<%= viewDisplayContext.getTranslatePortletURL(translationEntry) %>"
					name="title"
					value="<%= viewDisplayContext.getTitle(translationEntry) %>"
				/>

				<liferay-ui:search-container-column-text
					cssClass="table-cell-expand"
					name="type"
					value="<%= viewDisplayContext.getModelName(translationEntry) %>"
				/>

				<liferay-ui:search-container-column-text
					name="language"
					property="languageId"
				/>

				<liferay-ui:search-container-column-status
					cssClass="table-cell-expand-smallest"
					name="status"
					property="status"
				/>

				<liferay-ui:search-container-column-date
					cssClass="table-cell-expand-smallest"
					name="modified-date"
					property="modifiedDate"
				/>

				<liferay-ui:search-container-column-text>
					<clay:dropdown-actions
						dropdownItems="<%= viewDisplayContext.getActionDropdownItems(translationEntry) %>"
					/>
				</liferay-ui:search-container-column-text>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				markupView="lexicon"
			/>
		</liferay-ui:search-container>
	</aui:form>
</clay:container-fluid>

<liferay-frontend:component
	componentId="<%= viewDisplayContext.getDefaultEventHandler() %>"
	context="<%= viewDisplayContext.getComponentContext() %>"
	module="js/translate/TranslationManagementToolbarDefaultEventHandler.es"
/>