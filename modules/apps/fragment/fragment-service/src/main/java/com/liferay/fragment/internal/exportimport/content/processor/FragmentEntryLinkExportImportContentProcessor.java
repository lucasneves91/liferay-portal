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

package com.liferay.fragment.internal.exportimport.content.processor;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.exportimport.content.processor.ExportImportContentProcessor;
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.model.StagedModel;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.staging.StagingGroupHelper;
import com.liferay.staging.StagingGroupHelperUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	property = "model.class.name=com.liferay.fragment.model.FragmentEntryLink",
	service = {
		ExportImportContentProcessor.class,
		FragmentEntryLinkExportImportContentProcessor.class
	}
)
public class FragmentEntryLinkExportImportContentProcessor
	implements ExportImportContentProcessor<String> {

	@Override
	public String replaceExportContentReferences(
			PortletDataContext portletDataContext, StagedModel stagedModel,
			String content, boolean exportReferencedContent,
			boolean escapeContent)
		throws Exception {

		JSONObject editableValuesJSONObject = JSONFactoryUtil.createJSONObject(
			content);

		String portletId = editableValuesJSONObject.getString("portletId");

		if (Validator.isNotNull(portletId)) {
			return content;
		}

		content =
			_dlReferencesExportImportContentProcessor.
				replaceExportContentReferences(
					portletDataContext, stagedModel, content,
					exportReferencedContent, escapeContent);
		content =
			_layoutReferencesExportImportContentProcessor.
				replaceExportContentReferences(
					portletDataContext, stagedModel, content,
					exportReferencedContent, escapeContent);

		JSONObject editableProcessorJSONObject =
			editableValuesJSONObject.getJSONObject(
				_KEY_EDITABLE_FRAGMENT_ENTRY_PROCESSOR);

		if ((editableProcessorJSONObject == null) ||
			(editableProcessorJSONObject.length() <= 0)) {

			return content;
		}

		Iterator<String> editableKeysIterator =
			editableProcessorJSONObject.keys();

		while (editableKeysIterator.hasNext()) {
			String editableKey = editableKeysIterator.next();

			JSONObject editableJSONObject =
				editableProcessorJSONObject.getJSONObject(editableKey);

			_replaceMappedFieldExportContentReferences(
				portletDataContext, stagedModel, editableJSONObject,
				exportReferencedContent);
		}

		return editableValuesJSONObject.toString();
	}

	@Override
	public String replaceImportContentReferences(
			PortletDataContext portletDataContext, StagedModel stagedModel,
			String content)
		throws Exception {

		JSONObject editableValuesJSONObject = JSONFactoryUtil.createJSONObject(
			content);

		String portletId = editableValuesJSONObject.getString("portletId");

		if (Validator.isNotNull(portletId)) {
			return content;
		}

		content =
			_dlReferencesExportImportContentProcessor.
				replaceImportContentReferences(
					portletDataContext, stagedModel, content);
		content =
			_layoutReferencesExportImportContentProcessor.
				replaceImportContentReferences(
					portletDataContext, stagedModel, content);

		JSONObject editableProcessorJSONObject =
			editableValuesJSONObject.getJSONObject(
				_KEY_EDITABLE_FRAGMENT_ENTRY_PROCESSOR);

		if ((editableProcessorJSONObject == null) ||
			(editableProcessorJSONObject.length() <= 0)) {

			return content;
		}

		Iterator<String> editableKeysIterator =
			editableProcessorJSONObject.keys();

		while (editableKeysIterator.hasNext()) {
			String editableKey = editableKeysIterator.next();

			JSONObject editableJSONObject =
				editableProcessorJSONObject.getJSONObject(editableKey);

			_replaceMappedFieldImportContentReferences(
				portletDataContext, editableJSONObject);
		}

		return editableValuesJSONObject.toString();
	}

	@Override
	public void validateContentReferences(long groupId, String content)
		throws PortalException {
	}

	private void _replaceMappedFieldExportContentReferences(
			PortletDataContext portletDataContext, StagedModel stagedModel,
			JSONObject editableJSONObject, boolean exportReferencedContent)
		throws Exception {

		long classNameId = editableJSONObject.getLong("classNameId");
		long classPK = editableJSONObject.getLong("classPK");

		if ((classNameId == 0) || (classPK == 0)) {
			return;
		}

		String mappedField = editableJSONObject.getString(
			"mappedField", editableJSONObject.getString("fieldId"));

		if (mappedField.startsWith(_DDM_TEMPLATE)) {
			String ddmTemplateKey = mappedField.substring(
				_DDM_TEMPLATE.length());

			DDMTemplate ddmTemplate = _ddmTemplateLocalService.fetchTemplate(
				portletDataContext.getScopeGroupId(),
				_portal.getClassNameId(DDMStructure.class), ddmTemplateKey);

			if (ddmTemplate != null) {
				StagedModelDataHandlerUtil.exportReferenceStagedModel(
					portletDataContext, stagedModel, ddmTemplate,
					PortletDataContext.REFERENCE_TYPE_DEPENDENCY);
			}
		}

		// LPS-111037

		String className = _portal.getClassName(classNameId);

		if (Objects.equals(className, FileEntry.class.getName())) {
			className = DLFileEntry.class.getName();
		}

		AssetEntry assetEntry = _assetEntryLocalService.fetchEntry(
			className, classPK);

		if (assetEntry == null) {
			return;
		}

		AssetRenderer<?> assetRenderer = assetEntry.getAssetRenderer();

		if (assetRenderer == null) {
			return;
		}

		AssetRendererFactory<?> assetRendererFactory =
			assetRenderer.getAssetRendererFactory();

		StagingGroupHelper stagingGroupHelper =
			StagingGroupHelperUtil.getStagingGroupHelper();

		if (ExportImportThreadLocal.isStagingInProcess() &&
			!stagingGroupHelper.isStagedPortlet(
				portletDataContext.getScopeGroupId(),
				assetRendererFactory.getPortletId())) {

			return;
		}

		editableJSONObject.put("className", _portal.getClassName(classNameId));

		if (exportReferencedContent) {
			try {
				StagedModelDataHandlerUtil.exportReferenceStagedModel(
					portletDataContext, stagedModel,
					(StagedModel)assetRenderer.getAssetObject(),
					PortletDataContext.REFERENCE_TYPE_DEPENDENCY);
			}
			catch (Exception exception) {
				if (_log.isDebugEnabled()) {
					StringBundler messageSB = new StringBundler(11);

					messageSB.append("Staged model with class name ");
					messageSB.append(stagedModel.getModelClassName());
					messageSB.append(" and primary key ");
					messageSB.append(stagedModel.getPrimaryKeyObj());
					messageSB.append(" references asset entry with class ");
					messageSB.append("primary key ");
					messageSB.append(classPK);
					messageSB.append(" and class name ");
					messageSB.append(_portal.getClassName(classNameId));
					messageSB.append(" that could not be exported due to ");
					messageSB.append(exception);

					String errorMessage = messageSB.toString();

					if (Validator.isNotNull(exception.getMessage())) {
						errorMessage = StringBundler.concat(
							errorMessage, ": ", exception.getMessage());
					}

					_log.debug(errorMessage, exception);
				}
			}
		}
		else {
			Element entityElement = portletDataContext.getExportDataElement(
				stagedModel);

			portletDataContext.addReferenceElement(
				stagedModel, entityElement,
				(ClassedModel)assetRenderer.getAssetObject(),
				PortletDataContext.REFERENCE_TYPE_DEPENDENCY, true);
		}
	}

	private void _replaceMappedFieldImportContentReferences(
		PortletDataContext portletDataContext, JSONObject editableJSONObject) {

		String className = GetterUtil.getString(
			editableJSONObject.remove("className"));

		if (Validator.isNull(className)) {
			return;
		}

		String mappedField = editableJSONObject.getString(
			"mappedField", editableJSONObject.getString("fieldId"));

		if (mappedField.startsWith(_DDM_TEMPLATE)) {
			String ddmTemplateKey = mappedField.substring(
				_DDM_TEMPLATE.length());

			Map<String, String> ddmTemplateKeys =
				(Map<String, String>)portletDataContext.getNewPrimaryKeysMap(
					DDMTemplate.class + ".ddmTemplateKey");

			String importedDDMTemplateKey = MapUtil.getString(
				ddmTemplateKeys, ddmTemplateKey, ddmTemplateKey);

			if (editableJSONObject.has("mappedField")) {
				editableJSONObject.put(
					"mappedField", _DDM_TEMPLATE + importedDDMTemplateKey);
			}
			else {
				editableJSONObject.put(
					"fieldId", _DDM_TEMPLATE + importedDDMTemplateKey);
			}
		}

		// LPS-111037

		String assetRendererFactoryByClassName = className;

		if (Objects.equals(
				assetRendererFactoryByClassName, FileEntry.class.getName())) {

			assetRendererFactoryByClassName = DLFileEntry.class.getName();
		}

		AssetRendererFactory<?> assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				assetRendererFactoryByClassName);

		StagingGroupHelper stagingGroupHelper =
			StagingGroupHelperUtil.getStagingGroupHelper();

		if (ExportImportThreadLocal.isStagingInProcess() &&
			!stagingGroupHelper.isStagedPortlet(
				portletDataContext.getScopeGroupId(),
				assetRendererFactory.getPortletId())) {

			return;
		}

		long classPK = editableJSONObject.getLong("classPK");

		if (classPK == 0) {
			return;
		}

		editableJSONObject.put(
			"classNameId", _portal.getClassNameId(className));

		Map<Long, Long> primaryKeys =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(className);

		classPK = MapUtil.getLong(primaryKeys, classPK, classPK);

		editableJSONObject.put("classPK", classPK);
	}

	private static final String _DDM_TEMPLATE = "ddmTemplate_";

	private static final String _KEY_EDITABLE_FRAGMENT_ENTRY_PROCESSOR =
		"com.liferay.fragment.entry.processor.editable." +
			"EditableFragmentEntryProcessor";

	private static final Log _log = LogFactoryUtil.getLog(
		FragmentEntryLinkExportImportContentProcessor.class);

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private DDMTemplateLocalService _ddmTemplateLocalService;

	@Reference(target = "(content.processor.type=DLReferences)")
	private ExportImportContentProcessor<String>
		_dlReferencesExportImportContentProcessor;

	@Reference(target = "(content.processor.type=LayoutReferences)")
	private ExportImportContentProcessor<String>
		_layoutReferencesExportImportContentProcessor;

	@Reference
	private Portal _portal;

}