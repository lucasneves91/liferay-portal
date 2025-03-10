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

package com.liferay.commerce.product.internal.importer;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.commerce.product.importer.CPFileImporter;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.model.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalArticleConstants;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.PortletPreferencesIds;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.model.ThemeSetting;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryConstants;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.resource.bundle.AggregateResourceBundleLoader;
import com.liferay.portal.kernel.resource.bundle.ClassResourceBundleLoader;
import com.liferay.portal.kernel.resource.bundle.ResourceBundleLoader;
import com.liferay.portal.kernel.resource.bundle.ResourceBundleLoaderUtil;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ThemeLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.impl.ThemeSettingImpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(enabled = false, immediate = true, service = CPFileImporter.class)
public class CPFileImporterImpl implements CPFileImporter {

	public static final String GROUP_ID_PLACEHOLDER = "[$groupId$]";

	public static final String IMG_TAG =
		"<img alt='' src='%s' data-fileentryid='%s' />";

	public static final String LOCALE_PLACEHOLDER = "[$LOCALE$]";

	@Override
	public void cleanLayouts(ServiceContext serviceContext)
		throws PortalException {

		_layoutLocalService.deleteLayouts(
			serviceContext.getScopeGroupId(), true, serviceContext);

		_layoutLocalService.deleteLayouts(
			serviceContext.getScopeGroupId(), false, serviceContext);
	}

	@Override
	public void createJournalArticles(
			JSONArray journalArticleJSONArray, ClassLoader classLoader,
			String dependenciesFilePath, ServiceContext serviceContext)
		throws Exception {

		for (int i = 0; i < journalArticleJSONArray.length(); i++) {
			JSONObject journalArticleJSONObject =
				journalArticleJSONArray.getJSONObject(i);

			createJournalArticle(
				journalArticleJSONObject, classLoader, dependenciesFilePath,
				serviceContext);
		}
	}

	@Override
	public void createLayouts(
			JSONArray jsonArray, ClassLoader classLoader,
			String dependenciesFilePath, ServiceContext serviceContext)
		throws Exception {

		createLayouts(
			jsonArray, null, classLoader, dependenciesFilePath, serviceContext);
	}

	@Override
	public void createRoles(JSONArray jsonArray, ServiceContext serviceContext)
		throws PortalException {

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			JSONObject actionsJSONObject = jsonObject.getJSONObject("actions");
			String name = jsonObject.getString("name");
			int scope = jsonObject.getInt("scope");
			int type = jsonObject.getInt("type");

			Role role = getRole(name, type, serviceContext);

			if (actionsJSONObject != null) {
				updateActions(role, actionsJSONObject, scope, serviceContext);
			}
			else {
				JSONArray actionsJSONArray = jsonObject.getJSONArray("actions");

				for (int j = 0; j < actionsJSONArray.length(); j++) {
					updateActions(
						role, actionsJSONArray.getJSONObject(j), scope,
						serviceContext);
				}
			}
		}
	}

	@Override
	public DDMTemplate getDDMTemplate(
			File file, long classNameId, long classPK, long resourceClassNameId,
			String name, String type, String mode, String language,
			ServiceContext serviceContext)
		throws Exception {

		if (file == null) {
			return _ddmTemplateLocalService.fetchTemplate(
				serviceContext.getScopeGroupId(), classNameId, getKey(name));
		}

		FileInputStream fileInputStream = new FileInputStream(file);

		BufferedInputStream bufferedInputStream = new BufferedInputStream(
			fileInputStream);

		String script = StringUtil.read(bufferedInputStream);

		return fetchOrAddDDMTemplate(
			classNameId, classPK, resourceClassNameId, name, type, mode,
			language, script, serviceContext);
	}

	@Override
	public void updateLogo(
			File file, boolean privateLayout, boolean logo,
			ServiceContext serviceContext)
		throws PortalException {

		_layoutSetLocalService.updateLogo(
			serviceContext.getScopeGroupId(), privateLayout, logo, file);
	}

	@Override
	public void updateLookAndFeel(
			String themeId, boolean privateLayout,
			ServiceContext serviceContext)
		throws PortalException {

		Theme theme = _themeLocalService.fetchTheme(
			serviceContext.getCompanyId(), themeId);

		if (theme == null) {
			if (_log.isInfoEnabled()) {
				_log.info("No Theme registered with themeId: " + themeId);
			}

			return;
		}

		LayoutSet layoutSet = _layoutSetLocalService.getLayoutSet(
			serviceContext.getScopeGroupId(), privateLayout);

		UnicodeProperties typeSettingUnicodeProperties =
			layoutSet.getSettingsProperties();

		setThemeSettingProperties(theme, typeSettingUnicodeProperties);

		_layoutSetLocalService.updateLookAndFeel(
			serviceContext.getScopeGroupId(), privateLayout, themeId,
			StringPool.BLANK, StringPool.BLANK);
	}

	protected void addLayoutPortlets(
			JSONArray jsonArray, Layout layout, String layoutTemplateId,
			ClassLoader classLoader, ServiceContext serviceContext)
		throws Exception {

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		layoutTypePortlet.setLayoutTemplateId(
			UserConstants.USER_ID_DEFAULT, layoutTemplateId, false);

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject portletJSONObject = jsonArray.getJSONObject(i);

			String portletId = addPortletId(
				portletJSONObject, layoutTypePortlet, serviceContext);

			setPortletPreferences(
				portletJSONObject.getJSONObject("portletPreferences"), layout,
				portletId, serviceContext);

			setPortletLookAndFeel(
				portletJSONObject.getJSONObject("lookAndFeel"), layout,
				portletId, classLoader);
		}
	}

	protected String addPortletId(
			JSONObject jsonObject, LayoutTypePortlet layoutTypePortlet,
			ServiceContext serviceContext)
		throws Exception {

		String layoutColumnId = jsonObject.getString("layoutColumnId");
		int layoutColumnPos = jsonObject.getInt("layoutColumnPos");
		String portletName = jsonObject.getString("portletName");

		return layoutTypePortlet.addPortletId(
			serviceContext.getUserId(), portletName, layoutColumnId,
			layoutColumnPos, false);
	}

	protected JournalArticle createJournalArticle(
			JSONObject jsonObject, ClassLoader classLoader,
			String dependenciesFilePath, ServiceContext serviceContext)
		throws Exception {

		String articleId = jsonObject.getString("articleId");

		articleId = StringUtil.toUpperCase(StringUtil.trim(articleId));

		JournalArticle journalArticle =
			_journalArticleLocalService.fetchArticle(
				serviceContext.getScopeGroupId(), articleId);

		if (journalArticle != null) {
			return journalArticle;
		}

		String ddmStructureKey = jsonObject.getString("ddmStructureKey");
		String ddmTemplateKey = jsonObject.getString("ddmTemplateKey");

		DDMStructure ddmStructure = fetchOrAddDDMStructure(
			ddmStructureKey, classLoader, dependenciesFilePath,
			ddmStructureKey + ".json", serviceContext);

		InputStream inputStream = classLoader.getResourceAsStream(
			dependenciesFilePath + ddmTemplateKey + ".ftl");

		if (inputStream != null) {
			fetchOrAddDDMTemplate(
				_portal.getClassNameId(DDMStructure.class),
				ddmStructure.getStructureId(),
				_portal.getClassNameId(JournalArticle.class), ddmTemplateKey,
				DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, null, "ftl",
				StringUtil.read(inputStream), serviceContext);
		}

		Locale locale = serviceContext.getLocale();

		Map<Locale, String> titleMap = HashMapBuilder.put(
			locale, jsonObject.getString("title")
		).build();

		Map<Locale, String> descriptionMap = HashMapBuilder.put(
			locale, jsonObject.getString("description")
		).build();

		String content = StringUtil.read(
			classLoader, dependenciesFilePath + articleId + ".xml");

		content = getNormalizedContent(
			content, classLoader, dependenciesFilePath, serviceContext);

		Calendar displayCalendar = CalendarFactoryUtil.getCalendar(
			serviceContext.getTimeZone());

		int displayDateMonth = displayCalendar.get(Calendar.MONTH);
		int displayDateDay = displayCalendar.get(Calendar.DAY_OF_MONTH);
		int displayDateYear = displayCalendar.get(Calendar.YEAR);
		int displayDateHour = displayCalendar.get(Calendar.HOUR);
		int displayDateMinute = displayCalendar.get(Calendar.MINUTE);
		int displayDateAmPm = displayCalendar.get(Calendar.AM_PM);

		if (displayDateAmPm == Calendar.PM) {
			displayDateHour += 12;
		}

		journalArticle = _journalArticleLocalService.addArticle(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(), 0L,
			JournalArticleConstants.CLASSNAME_ID_DEFAULT, 0L, articleId, false,
			1, titleMap, descriptionMap, content, ddmStructureKey,
			ddmTemplateKey, StringPool.BLANK, displayDateMonth, displayDateDay,
			displayDateYear, displayDateHour, displayDateMinute, 0, 0, 0, 0, 0,
			true, 0, 0, 0, 0, 0, true, true, false, StringPool.BLANK, null,
			null, StringPool.BLANK, serviceContext);

		JSONArray permissionsJSONArray = jsonObject.getJSONArray("permissions");

		if ((permissionsJSONArray != null) &&
			(permissionsJSONArray.length() > 0)) {

			updatePermissions(
				journalArticle.getCompanyId(),
				journalArticle.getModelClassName(),
				String.valueOf(journalArticle.getResourcePrimKey()),
				permissionsJSONArray);
		}
		else {

			// Give site members view permissions

			updatePermissions(
				journalArticle.getCompanyId(),
				journalArticle.getModelClassName(),
				String.valueOf(journalArticle.getResourcePrimKey()), null);
		}

		return journalArticle;
	}

	protected void createLayout(
			JSONObject jsonObject, Layout parentLayout, ClassLoader classLoader,
			String dependenciesFilePath, ServiceContext serviceContext)
		throws Exception {

		boolean hidden = jsonObject.getBoolean("hidden");
		String icon = jsonObject.getString("icon");
		String layoutTemplateId = jsonObject.getString("layoutTemplateId");
		String layoutType = jsonObject.getString(
			"layoutType", LayoutConstants.TYPE_PORTLET);
		String name = jsonObject.getString("name");
		boolean privateLayout = jsonObject.getBoolean("privateLayout");

		long parentLayoutId = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;

		if (parentLayout != null) {
			parentLayoutId = parentLayout.getLayoutId();
		}

		String friendlyURL = StringUtil.toLowerCase(name);

		friendlyURL = friendlyURL.trim();

		friendlyURL = FriendlyURLNormalizerUtil.normalize(friendlyURL);

		friendlyURL = CharPool.SLASH + friendlyURL;

		Layout layout = _layoutLocalService.addLayout(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			privateLayout, parentLayoutId, name, name, StringPool.BLANK,
			layoutType, hidden, friendlyURL, serviceContext);

		if (Validator.isNotNull(icon)) {
			String filePath = dependenciesFilePath + "layout_icons/" + icon;

			InputStream inputStream = classLoader.getResourceAsStream(filePath);

			byte[] byteArray = FileUtil.getBytes(inputStream);

			layout = _layoutLocalService.updateIconImage(
				layout.getPlid(), byteArray);
		}

		JSONObject lookAndFeelJSONObject = jsonObject.getJSONObject(
			"lookAndFeel");

		if (lookAndFeelJSONObject != null) {
			layout = updateLayoutLookAndFeel(lookAndFeelJSONObject, layout);
		}

		JSONArray portletsJSONArray = jsonObject.getJSONArray("portlets");

		if ((portletsJSONArray != null) && (portletsJSONArray.length() > 0)) {
			addLayoutPortlets(
				portletsJSONArray, layout, layoutTemplateId, classLoader,
				serviceContext);
		}

		layout = _layoutLocalService.updateLayout(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			layout.getTypeSettings());

		JSONArray permissionsJSONArray = jsonObject.getJSONArray("permissions");

		if ((permissionsJSONArray != null) &&
			(permissionsJSONArray.length() > 0)) {

			updatePermissions(
				layout.getCompanyId(), layout.getModelClassName(),
				String.valueOf(layout.getPlid()), permissionsJSONArray);
		}

		JSONArray sublayoutsJSONArray = jsonObject.getJSONArray("subLayouts");

		if ((sublayoutsJSONArray != null) &&
			(sublayoutsJSONArray.length() > 0)) {

			createLayouts(
				sublayoutsJSONArray, layout, classLoader, dependenciesFilePath,
				serviceContext);
		}
	}

	protected void createLayouts(
			JSONArray jsonArray, Layout parentLayout, ClassLoader classLoader,
			String dependenciesFilePath, ServiceContext serviceContext)
		throws Exception {

		for (int i = 0; i < jsonArray.length(); i++) {
			createLayout(
				jsonArray.getJSONObject(i), parentLayout, classLoader,
				dependenciesFilePath, serviceContext);
		}
	}

	protected void deleteThemeSettingsProperties(
		UnicodeProperties typeSettingsUnicodeProperties, String device) {

		String keyPrefix = ThemeSettingImpl.namespaceProperty(device);

		Set<String> keys = typeSettingsUnicodeProperties.keySet();

		Iterator<String> iterator = keys.iterator();

		while (iterator.hasNext()) {
			String key = iterator.next();

			if (key.startsWith(keyPrefix)) {
				iterator.remove();
			}
		}
	}

	protected DDMStructure fetchOrAddDDMStructure(
			String ddmStructureKey, ClassLoader classLoader,
			String dependencyFilePath, String ddmStructureFileName,
			ServiceContext serviceContext)
		throws Exception {

		long classNameId = _portal.getClassNameId(JournalArticle.class);

		DDMStructure ddmStructure = _ddmStructureLocalService.fetchStructure(
			serviceContext.getScopeGroupId(), classNameId, ddmStructureKey,
			true);

		if (ddmStructure != null) {
			return ddmStructure;
		}

		Map<Locale, String> nameMap = Collections.singletonMap(
			serviceContext.getLocale(),
			TextFormatter.format(ddmStructureKey, TextFormatter.J));

		String json = StringUtil.read(
			classLoader, dependencyFilePath + ddmStructureFileName);

		json = getNormalizedContent(
			json, classLoader, dependencyFilePath, serviceContext);

		DDMFormDeserializerDeserializeRequest
			ddmFormDeserializerDeserializeRequest =
				DDMFormDeserializerDeserializeRequest.Builder.newBuilder(
					json
				).build();

		DDMFormDeserializerDeserializeResponse
			ddmFormDeserializerDeserializeResponse =
				_ddmFormDeserializer.deserialize(
					ddmFormDeserializerDeserializeRequest);

		DDMForm ddmForm = ddmFormDeserializerDeserializeResponse.getDDMForm();

		ddmForm = _updateDDMFormAvailableLocales(
			ddmForm, serviceContext.getLocale());

		DDMFormLayout ddmFormLayout = _ddm.getDefaultDDMFormLayout(ddmForm);

		return _ddmStructureLocalService.addStructure(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(), 0,
			classNameId, ddmStructureKey, nameMap, null, ddmForm, ddmFormLayout,
			"json", DDMStructureConstants.TYPE_DEFAULT, serviceContext);
	}

	protected DDMTemplate fetchOrAddDDMTemplate(
			long classNameId, long classPK, long resourceClassNameId,
			String name, String type, String mode, String language,
			String script, ServiceContext serviceContext)
		throws PortalException {

		Map<Locale, String> nameMap = HashMapBuilder.put(
			LocaleUtil.getSiteDefault(), name
		).build();

		DDMTemplate ddmTemplate = _ddmTemplateLocalService.fetchTemplate(
			serviceContext.getScopeGroupId(), classNameId, getKey(name));

		if (ddmTemplate == null) {
			ddmTemplate = _ddmTemplateLocalService.addTemplate(
				serviceContext.getUserId(), serviceContext.getScopeGroupId(),
				classNameId, classPK, resourceClassNameId, getKey(name),
				nameMap, null, type, mode, language, script, true, false,
				StringPool.BLANK, null, serviceContext);
		}
		else {
			ddmTemplate = _ddmTemplateLocalService.updateTemplate(
				serviceContext.getUserId(), ddmTemplate.getTemplateId(),
				classPK, nameMap, null, type, mode, language, script, true,
				serviceContext);
		}

		return ddmTemplate;
	}

	protected FileEntry fetchOrAddFileEntry(
			ClassLoader classLoader, String dependenciesFilePath,
			String fileName, ServiceContext serviceContext)
		throws Exception {

		FileEntry fileEntry = null;

		try {
			fileEntry = _dlAppLocalService.getFileEntry(
				serviceContext.getScopeGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, fileName);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception, exception);
			}
		}

		if (fileEntry != null) {
			return fileEntry;
		}

		String filePath = dependenciesFilePath + fileName;

		InputStream inputStream = classLoader.getResourceAsStream(filePath);

		String mimeType = MimeTypesUtil.getContentType(fileName);

		byte[] byteArray = FileUtil.getBytes(inputStream);

		return _dlAppLocalService.addFileEntry(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, fileName, mimeType,
			fileName, StringPool.BLANK, StringPool.BLANK, byteArray,
			serviceContext);
	}

	protected long getAssetEntryId(
			String articleId, ServiceContext serviceContext)
		throws PortalException {

		if (Validator.isNull(articleId)) {
			return 0;
		}

		JournalArticle journalArticle =
			_journalArticleLocalService.fetchArticle(
				serviceContext.getScopeGroupId(), articleId);

		if (journalArticle == null) {
			return 0;
		}

		AssetEntry assetEntry = _assetEntryLocalService.fetchEntry(
			JournalArticle.class.getName(),
			journalArticle.getResourcePrimKey());

		if (assetEntry == null) {
			return 0;
		}

		return assetEntry.getEntryId();
	}

	protected String getKey(String name) {
		name = StringUtil.replace(name, CharPool.SPACE, CharPool.DASH);

		name = StringUtil.toUpperCase(name);

		return name;
	}

	protected String getNormalizedContent(
			String content, ClassLoader classLoader,
			String dependenciesFilePath, ServiceContext serviceContext)
		throws Exception {

		content = StringUtil.replace(
			content, GROUP_ID_PLACEHOLDER,
			String.valueOf(serviceContext.getScopeGroupId()));

		content = StringUtil.replace(
			content, LOCALE_PLACEHOLDER,
			LocaleUtil.toLanguageId(serviceContext.getLocale()));

		content = _replaceJournalArticleImages(
			content, _journalArticleHTMLImagePattern,
			fileEntry -> {
				String previewURL = DLUtil.getDownloadURL(
					fileEntry, fileEntry.getLatestFileVersion(), null,
					StringPool.BLANK, false, false);

				return String.format(
					IMG_TAG, previewURL,
					String.valueOf(fileEntry.getFileEntryId()));
			},
			classLoader, dependenciesFilePath, serviceContext);

		content = _replaceJournalArticleImages(
			content, _journalArticleJSONImagePattern,
			fileEntry -> {
				JSONObject jsonObject = _jsonFactory.createJSONObject();

				jsonObject.put(
					"alt", fileEntry.getTitle()
				).put(
					"groupId", fileEntry.getGroupId()
				).put(
					"name", fileEntry.getFileName()
				).put(
					"title", fileEntry.getTitle()
				).put(
					"type", "document"
				).put(
					"uuid", fileEntry.getUuid()
				);

				return jsonObject.toJSONString();
			},
			classLoader, dependenciesFilePath, serviceContext);

		return content;
	}

	protected Role getRole(String name, int type, ServiceContext serviceContext)
		throws PortalException {

		Role role = _roleLocalService.fetchRole(
			serviceContext.getCompanyId(), name);

		if (role == null) {
			role = _roleLocalService.addRole(
				serviceContext.getUserId(), null, 0, name,
				HashMapBuilder.put(
					serviceContext.getLocale(), name
				).build(),
				null, type, null, serviceContext);
		}

		return role;
	}

	protected void setLocalizedValues(
			PortletPreferences portletPreferences,
			ResourceBundleLoader resourceBundleLoader, long groupId, String key,
			String value)
		throws PortletException {

		for (Locale locale : LanguageUtil.getAvailableLocales(groupId)) {
			ResourceBundle resourceBundle =
				resourceBundleLoader.loadResourceBundle(locale);

			portletPreferences.setValue(
				key + StringPool.UNDERLINE + LanguageUtil.getLanguageId(locale),
				LanguageUtil.get(resourceBundle, value));
		}
	}

	protected void setPortletLookAndFeel(
			JSONObject jsonObject, Layout layout, String portletId,
			ClassLoader classLoader)
		throws Exception {

		if (jsonObject == null) {
			return;
		}

		PortletPreferencesIds portletPreferencesIds =
			PortletPreferencesFactoryUtil.getPortletPreferencesIds(
				layout.getCompanyId(), layout.getGroupId(), layout.getPlid(),
				portletId,
				PortletPreferencesFactoryConstants.
					SETTINGS_SCOPE_PORTLET_INSTANCE);

		PortletPreferences portletPreferences =
			_portletPreferencesLocalService.getPreferences(
				portletPreferencesIds);

		ResourceBundleLoader resourceBundleLoader =
			new AggregateResourceBundleLoader(
				new ClassResourceBundleLoader("content.Language", classLoader),
				ResourceBundleLoaderUtil.getPortalResourceBundleLoader());

		Iterator<String> iterator = jsonObject.keys();

		while (iterator.hasNext()) {
			String key = iterator.next();

			String value = jsonObject.getString(key);

			if (key.equals("portletSetupTitle")) {
				setLocalizedValues(
					portletPreferences, resourceBundleLoader,
					layout.getGroupId(), key, value);
			}
			else {
				portletPreferences.setValue(key, value);
			}
		}

		portletPreferences.store();
	}

	protected void setPortletPreferences(
			JSONObject jsonObject, Layout layout, String portletId,
			ServiceContext serviceContext)
		throws Exception {

		if (jsonObject == null) {
			return;
		}

		PortletPreferencesIds portletPreferencesIds =
			PortletPreferencesFactoryUtil.getPortletPreferencesIds(
				layout.getGroupId(), 0, layout, portletId, false);

		PortletPreferences portletPreferences =
			_portletPreferencesLocalService.getPreferences(
				portletPreferencesIds);

		Iterator<String> iterator = jsonObject.keys();

		while (iterator.hasNext()) {
			String key = iterator.next();

			String value = jsonObject.getString(key);

			if (key.equals("assetEntryId")) {
				String articleId = jsonObject.getString("articleId");

				long assetEntryId = getAssetEntryId(articleId, serviceContext);

				value = String.valueOf(assetEntryId);
			}
			else if (key.equals("groupId")) {
				value = String.valueOf(serviceContext.getScopeGroupId());
			}

			portletPreferences.setValue(key, value);
		}

		portletPreferences.store();
	}

	protected void setThemeSettingProperties(
		Theme theme, UnicodeProperties typeSettingUnicodeProperties) {

		String device = "regular";

		deleteThemeSettingsProperties(typeSettingUnicodeProperties, device);

		Map<String, ThemeSetting> themeSettings =
			theme.getConfigurableSettings();

		for (Map.Entry<String, ThemeSetting> entry : themeSettings.entrySet()) {
			String key = entry.getKey();

			ThemeSetting themeSetting = entry.getValue();

			String value = themeSetting.getValue();

			if (!value.equals(themeSetting.getValue())) {
				typeSettingUnicodeProperties.setProperty(
					ThemeSettingImpl.namespaceProperty(device, key), value);
			}
		}
	}

	protected void updateAction(
			Role role, String resource, String actionId, int scope,
			ServiceContext serviceContext)
		throws PortalException {

		if (scope == ResourceConstants.SCOPE_COMPANY) {
			_resourcePermissionLocalService.addResourcePermission(
				serviceContext.getCompanyId(), resource, scope,
				String.valueOf(role.getCompanyId()), role.getRoleId(),
				actionId);
		}
		else if (scope == ResourceConstants.SCOPE_GROUP_TEMPLATE) {
			_resourcePermissionLocalService.addResourcePermission(
				serviceContext.getCompanyId(), resource,
				ResourceConstants.SCOPE_GROUP_TEMPLATE,
				String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
				role.getRoleId(), actionId);
		}
		else if (scope == ResourceConstants.SCOPE_GROUP) {
			_resourcePermissionLocalService.removeResourcePermissions(
				serviceContext.getCompanyId(), resource,
				ResourceConstants.SCOPE_GROUP, role.getRoleId(), actionId);

			_resourcePermissionLocalService.addResourcePermission(
				serviceContext.getCompanyId(), resource,
				ResourceConstants.SCOPE_GROUP,
				String.valueOf(serviceContext.getScopeGroupId()),
				role.getRoleId(), actionId);
		}
	}

	protected void updateActions(
			Role role, JSONObject jsonObject, int scope,
			ServiceContext serviceContext)
		throws PortalException {

		String resource = jsonObject.getString("resource");
		JSONArray actionIdsJSONArray = jsonObject.getJSONArray("actionIds");

		for (int i = 0; i < actionIdsJSONArray.length(); i++) {
			String actionId = actionIdsJSONArray.getString(i);

			updateAction(role, resource, actionId, scope, serviceContext);
		}
	}

	protected Layout updateLayoutLookAndFeel(
		JSONObject jsonObject, Layout layout) {

		UnicodeProperties typeSettingsUnicodeProperties =
			layout.getTypeSettingsProperties();

		Iterator<String> iterator = jsonObject.keys();

		while (iterator.hasNext()) {
			String key = iterator.next();

			Set<String> typeSettingPropertiesKeys =
				typeSettingsUnicodeProperties.keySet();

			if (typeSettingPropertiesKeys.contains(key)) {
				typeSettingsUnicodeProperties.replace(
					key, jsonObject.getString(key));
			}
			else {
				typeSettingsUnicodeProperties.put(
					key, jsonObject.getString(key));
			}
		}

		layout.setTypeSettingsProperties(typeSettingsUnicodeProperties);

		return layout;
	}

	protected void updatePermissions(
			long companyId, String name, String primKey, JSONArray jsonArray)
		throws PortalException {

		if (jsonArray == null) {
			jsonArray = JSONFactoryUtil.createJSONArray(
				"[{\"actionIds\": [\"VIEW\"], \"roleName\": \"Site Member\"," +
					"\"scope\": 4}]");
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			int scope = jsonObject.getInt("scope");

			String roleName = jsonObject.getString("roleName");

			Role role = _roleLocalService.getRole(companyId, roleName);

			String[] actionIds = new String[0];

			JSONArray actionIdsJSONArray = jsonObject.getJSONArray("actionIds");

			if (actionIdsJSONArray != null) {
				for (int j = 0; j < actionIdsJSONArray.length(); j++) {
					actionIds = ArrayUtil.append(
						actionIds, actionIdsJSONArray.getString(j));
				}
			}

			_resourcePermissionLocalService.setResourcePermissions(
				companyId, name, scope, primKey, role.getRoleId(), actionIds);
		}
	}

	private String _replaceJournalArticleImages(
			String content, Pattern pattern,
			UnsafeFunction<FileEntry, String, Exception> replacementFunction,
			ClassLoader classLoader, String dependenciesFilePath,
			ServiceContext serviceContext)
		throws Exception {

		StringBuffer sb = new StringBuffer();

		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			String fileName = matcher.group(1);

			FileEntry fileEntry = fetchOrAddFileEntry(
				classLoader, dependenciesFilePath, fileName, serviceContext);

			String replacement = replacementFunction.apply(fileEntry);

			matcher.appendReplacement(sb, replacement);
		}

		matcher.appendTail(sb);

		return sb.toString();
	}

	private DDMForm _updateDDMFormAvailableLocales(
		DDMForm ddmForm, Locale locale) {

		Set<Locale> availableLocales = ddmForm.getAvailableLocales();

		availableLocales.add(locale);

		ddmForm.setAvailableLocales(availableLocales);

		ddmForm.setDefaultLocale(locale);

		return ddmForm;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CPFileImporterImpl.class);

	private static final Pattern _journalArticleHTMLImagePattern =
		Pattern.compile("\\[%([^\\[%]+)%\\]");
	private static final Pattern _journalArticleJSONImagePattern =
		Pattern.compile("\\[\\$([^\\[%]+)\\$\\]");

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private DDM _ddm;

	@Reference
	private DDMFormDeserializer _ddmFormDeserializer;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private DDMTemplateLocalService _ddmTemplateLocalService;

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutSetLocalService _layoutSetLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private PortletPreferencesLocalService _portletPreferencesLocalService;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private ThemeLocalService _themeLocalService;

}