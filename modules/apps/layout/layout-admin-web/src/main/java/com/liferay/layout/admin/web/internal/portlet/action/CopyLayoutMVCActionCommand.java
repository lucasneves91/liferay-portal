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

package com.liferay.layout.admin.web.internal.portlet.action;

import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.admin.web.internal.handler.LayoutExceptionRequestHandler;
import com.liferay.layout.util.LayoutCopyHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + LayoutAdminPortletKeys.GROUP_PAGES,
		"mvc.command.name=/layout_admin/copy_layout"
	},
	service = MVCActionCommand.class
)
public class CopyLayoutMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		UploadPortletRequest uploadPortletRequest =
			_portal.getUploadPortletRequest(actionRequest);

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long sourcePlid = ParamUtil.getLong(uploadPortletRequest, "sourcePlid");
		long groupId = ParamUtil.getLong(actionRequest, "groupId");
		boolean privateLayout = ParamUtil.getBoolean(
			actionRequest, "privateLayout");

		String name = ParamUtil.getString(actionRequest, "name");

		Map<Locale, String> nameMap = HashMapBuilder.put(
			themeDisplay.getLocale(), name
		).build();

		if (!Objects.equals(
				themeDisplay.getLocale(), LocaleUtil.getSiteDefault())) {

			nameMap.put(LocaleUtil.getSiteDefault(), name);
		}

		UnicodeProperties typeSettingsUnicodeProperties =
			PropertiesParamUtil.getProperties(
				actionRequest, "TypeSettingsProperties--");

		Layout sourceLayout = _layoutLocalService.fetchLayout(sourcePlid);

		UnicodeProperties sourceTypeSettingsUnicodeProperties =
			sourceLayout.getTypeSettingsProperties();

		sourceTypeSettingsUnicodeProperties.putAll(
			typeSettingsUnicodeProperties);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			Layout.class.getName(), actionRequest);

		try {
			Layout targetLayout = _layoutService.addLayout(
				groupId, privateLayout, sourceLayout.getParentLayoutId(), 0, 0,
				nameMap, sourceLayout.getTitleMap(),
				sourceLayout.getDescriptionMap(), sourceLayout.getKeywordsMap(),
				sourceLayout.getRobotsMap(), sourceLayout.getType(),
				sourceTypeSettingsUnicodeProperties.toString(), false, false,
				new HashMap<>(), serviceContext);

			Layout draftLayout = targetLayout.fetchDraftLayout();

			if (draftLayout != null) {
				targetLayout = draftLayout;
			}

			targetLayout = _layoutCopyHelper.copyLayout(
				sourceLayout, targetLayout);

			targetLayout.setNameMap(nameMap);

			UnicodeProperties unicodeProperties =
				targetLayout.getTypeSettingsProperties();

			unicodeProperties.put("published", Boolean.FALSE.toString());

			_layoutLocalService.updateLayout(targetLayout);

			LiferayPortletResponse liferayPortletResponse =
				_portal.getLiferayPortletResponse(actionResponse);

			PortletURL redirectURL = liferayPortletResponse.createRenderURL();

			redirectURL.setParameter(
				"navigation", privateLayout ? "private-pages" : "public-pages");
			redirectURL.setParameter(
				"selPlid", String.valueOf(sourceLayout.getParentPlid()));
			redirectURL.setParameter(
				"privateLayout", String.valueOf(privateLayout));

			JSONPortletResponseUtil.writeJSON(
				actionRequest, actionResponse,
				JSONUtil.put("redirectURL", redirectURL.toString()));
		}
		catch (PortalException portalException) {
			SessionErrors.add(actionRequest, "layoutNameInvalid");

			hideDefaultErrorMessage(actionRequest);

			_layoutExceptionRequestHandler.handlePortalException(
				actionRequest, actionResponse, portalException);
		}
	}

	@Reference
	private LayoutCopyHelper _layoutCopyHelper;

	@Reference
	private LayoutExceptionRequestHandler _layoutExceptionRequestHandler;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutService _layoutService;

	@Reference
	private Portal _portal;

}