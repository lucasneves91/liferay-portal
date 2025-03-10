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

package com.liferay.commerce.channel.web.internal.frontend;

import com.liferay.commerce.channel.web.internal.model.Channel;
import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.permission.CommerceChannelPermission;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.frontend.taglib.clay.data.Filter;
import com.liferay.frontend.taglib.clay.data.Pagination;
import com.liferay.frontend.taglib.clay.data.set.ClayDataSetActionProvider;
import com.liferay.frontend.taglib.clay.data.set.ClayDataSetDisplayView;
import com.liferay.frontend.taglib.clay.data.set.provider.ClayDataSetDataProvider;
import com.liferay.frontend.taglib.clay.data.set.view.table.BaseTableClayDataSetDisplayView;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchema;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchemaBuilder;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchemaBuilderFactory;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchemaField;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletQName;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(
	enabled = false, immediate = true,
	property = {
		"clay.data.provider.key=" + CommerceChannelClayTable.NAME,
		"clay.data.set.display.name=" + CommerceChannelClayTable.NAME
	},
	service = {
		ClayDataSetActionProvider.class, ClayDataSetDataProvider.class,
		ClayDataSetDisplayView.class
	}
)
public class CommerceChannelClayTable
	extends BaseTableClayDataSetDisplayView
	implements ClayDataSetActionProvider, ClayDataSetDataProvider<Channel> {

	public static final String NAME = "channels";

	@Override
	public ClayTableSchema getClayTableSchema() {
		ClayTableSchemaBuilder clayTableSchemaBuilder =
			_clayTableSchemaBuilderFactory.create();

		ClayTableSchemaField nameField =
			clayTableSchemaBuilder.addClayTableSchemaField("name", "name");

		nameField.setContentRenderer("actionLink");

		clayTableSchemaBuilder.addClayTableSchemaField("type", "type");

		return clayTableSchemaBuilder.build();
	}

	@Override
	public List<DropdownItem> getDropdownItems(
			HttpServletRequest httpServletRequest, long groupId, Object model)
		throws PortalException {

		Channel channel = (Channel)model;

		return DropdownItemListBuilder.add(
			() -> _commerceChannelPermission.contains(
				PermissionThreadLocal.getPermissionChecker(),
				channel.getChannelId(), ActionKeys.UPDATE),
			dropdownItem -> {
				PortletURL portletURL = _portal.getControlPanelPortletURL(
					httpServletRequest, CPPortletKeys.COMMERCE_CHANNELS,
					PortletRequest.RENDER_PHASE);

				portletURL.setParameter("backURL", portletURL.toString());

				portletURL.setParameter(
					"commerceChannelId",
					String.valueOf(channel.getChannelId()));
				portletURL.setParameter(
					"mvcRenderCommandName", "editCommerceChannel");

				dropdownItem.setHref(portletURL.toString());

				dropdownItem.setLabel(
					LanguageUtil.get(httpServletRequest, "edit"));
			}
		).add(
			() -> _commerceChannelPermission.contains(
				PermissionThreadLocal.getPermissionChecker(),
				channel.getChannelId(), ActionKeys.PERMISSIONS),
			dropdownItem -> {
				dropdownItem.setHref(
					_getManageChannelPermissionsURL(
						channel, httpServletRequest));
				dropdownItem.setLabel(
					LanguageUtil.get(httpServletRequest, "permissions"));
				dropdownItem.setTarget("modal-permissions");
			}
		).add(
			() -> _commerceChannelPermission.contains(
				PermissionThreadLocal.getPermissionChecker(),
				channel.getChannelId(), ActionKeys.DELETE),
			dropdownItem -> {
				PortletURL deleteURL = _portal.getControlPanelPortletURL(
					httpServletRequest, CPPortletKeys.COMMERCE_CHANNELS,
					PortletRequest.ACTION_PHASE);

				deleteURL.setParameter(
					ActionRequest.ACTION_NAME, "editCommerceChannel");
				deleteURL.setParameter(Constants.CMD, Constants.DELETE);

				String redirect = ParamUtil.getString(
					httpServletRequest, "currentUrl",
					_portal.getCurrentURL(httpServletRequest));

				deleteURL.setParameter("redirect", redirect);

				deleteURL.setParameter(
					"commerceChannelId",
					String.valueOf(channel.getChannelId()));

				dropdownItem.setHref(deleteURL);
				dropdownItem.setLabel(
					LanguageUtil.get(httpServletRequest, "delete"));
			}
		).build();
	}

	@Override
	public List<Channel> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		List<Channel> channels = new ArrayList<>();

		List<CommerceChannel> commerceChannels =
			_commerceChannelService.searchCommerceChannels(
				_portal.getCompanyId(httpServletRequest), filter.getKeywords(),
				pagination.getStartPosition(), pagination.getEndPosition(),
				sort);

		for (CommerceChannel commerceChannel : commerceChannels) {
			channels.add(
				new Channel(
					commerceChannel.getCommerceChannelId(),
					commerceChannel.getName(),
					LanguageUtil.get(
						httpServletRequest, commerceChannel.getType())));
		}

		return channels;
	}

	@Override
	public int getItemsCount(
			HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		return _commerceChannelService.searchCommerceChannelsCount(
			_portal.getCompanyId(httpServletRequest), filter.getKeywords());
	}

	private PortletURL _getManageChannelPermissionsURL(
			Channel channel, HttpServletRequest httpServletRequest)
		throws PortalException {

		PortletURL portletURL = _portal.getControlPanelPortletURL(
			httpServletRequest,
			"com_liferay_portlet_configuration_web_portlet_" +
				"PortletConfigurationPortlet",
			ActionRequest.RENDER_PHASE);

		String redirect = ParamUtil.getString(
			httpServletRequest, "currentUrl",
			_portal.getCurrentURL(httpServletRequest));

		portletURL.setParameter("mvcPath", "/edit_permissions.jsp");
		portletURL.setParameter(
			PortletQName.PUBLIC_RENDER_PARAMETER_NAMESPACE + "backURL",
			redirect);
		portletURL.setParameter(
			"modelResource", CommerceChannel.class.getName());
		portletURL.setParameter("modelResourceDescription", channel.getName());
		portletURL.setParameter(
			"resourcePrimKey", String.valueOf(channel.getChannelId()));

		try {
			portletURL.setWindowState(LiferayWindowState.POP_UP);
		}
		catch (WindowStateException windowStateException) {
			throw new PortalException(windowStateException);
		}

		return portletURL;
	}

	@Reference
	private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;

	@Reference
	private CommerceChannelPermission _commerceChannelPermission;

	@Reference
	private CommerceChannelService _commerceChannelService;

	@Reference
	private Portal _portal;

}