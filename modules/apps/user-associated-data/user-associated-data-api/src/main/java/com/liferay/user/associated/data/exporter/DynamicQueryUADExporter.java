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

package com.liferay.user.associated.data.exporter;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.xml.XMLUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactoryUtil;
import com.liferay.user.associated.data.util.UADDynamicQueryUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * The base implementation of {@link UADExporter} for entities generated with
 * ServiceBuilder. The count and batch actions are based on
 * {@link ActionableDynamicQuery}, which is available in the LocalService
 * generated by ServiceBuilder.
 *
 * @author William Newbury
 */
public abstract class DynamicQueryUADExporter<T extends BaseModel>
	implements UADExporter<T> {

	@Override
	public long count(long userId) throws PortalException {
		return getActionableDynamicQuery(userId).performCount();
	}

	@Override
	public byte[] export(T baseModel) throws PortalException {
		String xml = toXmlString(baseModel);

		xml = formatXML(xml);

		try {
			return xml.getBytes(StringPool.UTF8);
		}
		catch (UnsupportedEncodingException uee) {
			throw new PortalException(uee);
		}
	}

	@Override
	public File exportAll(long userId) throws PortalException {
		ActionableDynamicQuery actionableDynamicQuery =
			getActionableDynamicQuery(userId);

		Class<T> clazz = getTypeClass();

		ZipWriter zipWriter = getZipWriter(userId, clazz.getName());

		actionableDynamicQuery.setPerformActionMethod(
			(T baseModel) -> {
				try {
					writeToZip(baseModel, zipWriter);
				}
				catch (Exception e) {
					throw new PortalException(e);
				}
			});

		actionableDynamicQuery.performActions();

		return zipWriter.getFile();
	}

	protected File createFolder(long userId) {
		StringBundler sb = new StringBundler(3);

		sb.append(SystemProperties.get(SystemProperties.TMP_DIR));
		sb.append("/liferay/uad/");
		sb.append(userId);

		File file = new File(sb.toString());

		file.mkdirs();

		return file;
	}

	/**
	 * Returns an {@link ActionableDynamicQuery} for type {@code T}. This can be
	 * retrieved from the service.
	 *
	 * @return an {@link ActionableDynamicQuery} for type {@code T}
	 */
	protected abstract ActionableDynamicQuery doGetActionableDynamicQuery();

	/**
	 * Returns an array of names identifying fields on the entity of type
	 * {@code T} that contain a userId.
	 *
	 * @return an array of strings identifying fields that may contain a userId
	 */
	protected abstract String[] doGetUserIdFieldNames();

	protected String formatXML(String xml) {
		return XMLUtil.formatXML(xml);
	}

	/**
	 * Returns an {@link ActionableDynamicQuery} for type {@code T}. It should
	 * be populated with criterion and ready for use by the service.
	 *
	 * @param userId the userId to pre-filter the {@link ActionableDynamicQuery}
	 * @return a pre-filtered {@link ActionableDynamicQuery}
	 */
	protected ActionableDynamicQuery getActionableDynamicQuery(long userId) {
		return UADDynamicQueryUtil.addActionableDynamicQueryCriteria(
			doGetActionableDynamicQuery(), doGetUserIdFieldNames(), userId);
	}

	/**
	 * Returns a {@link ZipWriter} to write the data to. Each individual entity
	 * of type {@code T} will be written as a file in the resulting zip file.
	 *
	 * @param userId the userId of the user whose data is being exported
	 * @param modelClassName the string representation of the model class name
	 * @return a {@link ZipWriter} where each piece of data will be written
	 */
	protected ZipWriter getZipWriter(long userId, String modelClassName) {
		File file = createFolder(userId);

		StringBundler sb = new StringBundler(6);

		sb.append(file.getAbsolutePath());
		sb.append(StringPool.SLASH);
		sb.append(modelClassName);
		sb.append(StringPool.UNDERLINE);
		sb.append(Time.getShortTimestamp());
		sb.append(".zip");

		return ZipWriterFactoryUtil.getZipWriter(new File(sb.toString()));
	}

	/**
	 * Converts the baseModel of type {@code T} to an XML string to be written
	 * to a file.
	 *
	 * @param baseModel the baseModel to be converted into an XML string
	 * @return and XML string representation of the base model
	 */
	protected String toXmlString(T baseModel) {
		return baseModel.toXmlString();
	}

	/**
	 * Converts the baseModel of type {@code T} to a byte array and writes it to
	 * the zipWriter.
	 *
	 * @param baseModel the baseModel to be written to the zip
	 * @param zipWriter the {@link ZipWriter} to write to
	 * @throws Exception
	 */
	protected void writeToZip(T baseModel, ZipWriter zipWriter)
		throws Exception {

		byte[] data = export(baseModel);

		zipWriter.addEntry(baseModel.getPrimaryKeyObj() + ".xml", data);
	}

}