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

package com.liferay.asset.list.web.internal.portlet;

import com.liferay.asset.list.constants.AssetListPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.BasePortletProvider;
import com.liferay.portal.kernel.portlet.EditPortletProvider;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jürgen Kappler
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.asset.list.model.AssetListEntry",
	service = EditPortletProvider.class
)
public class AssetListEditPortletProvider
	extends BasePortletProvider implements EditPortletProvider {

	@Override
	public String getPortletName() {
		return AssetListPortletKeys.ASSET_LIST;
	}

	@Override
	public PortletURL getPortletURL(HttpServletRequest httpServletRequest)
		throws PortalException {

		PortletURL portletURL = super.getPortletURL(httpServletRequest);

		portletURL.setParameter("mvcPath", "/edit_asset_list_entry.jsp");

		return portletURL;
	}

}