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

package com.liferay.commerce.account.model;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link CommerceAccountGroupRel}.
 * </p>
 *
 * @author Marco Leo
 * @see CommerceAccountGroupRel
 * @generated
 */
public class CommerceAccountGroupRelWrapper
	extends BaseModelWrapper<CommerceAccountGroupRel>
	implements CommerceAccountGroupRel, ModelWrapper<CommerceAccountGroupRel> {

	public CommerceAccountGroupRelWrapper(
		CommerceAccountGroupRel commerceAccountGroupRel) {

		super(commerceAccountGroupRel);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put(
			"commerceAccountGroupRelId", getCommerceAccountGroupRelId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("classNameId", getClassNameId());
		attributes.put("classPK", getClassPK());
		attributes.put("commerceAccountGroupId", getCommerceAccountGroupId());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long commerceAccountGroupRelId = (Long)attributes.get(
			"commerceAccountGroupRelId");

		if (commerceAccountGroupRelId != null) {
			setCommerceAccountGroupRelId(commerceAccountGroupRelId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		Long classPK = (Long)attributes.get("classPK");

		if (classPK != null) {
			setClassPK(classPK);
		}

		Long commerceAccountGroupId = (Long)attributes.get(
			"commerceAccountGroupId");

		if (commerceAccountGroupId != null) {
			setCommerceAccountGroupId(commerceAccountGroupId);
		}
	}

	/**
	 * Returns the fully qualified class name of this commerce account group rel.
	 *
	 * @return the fully qualified class name of this commerce account group rel
	 */
	@Override
	public String getClassName() {
		return model.getClassName();
	}

	/**
	 * Returns the class name ID of this commerce account group rel.
	 *
	 * @return the class name ID of this commerce account group rel
	 */
	@Override
	public long getClassNameId() {
		return model.getClassNameId();
	}

	/**
	 * Returns the class pk of this commerce account group rel.
	 *
	 * @return the class pk of this commerce account group rel
	 */
	@Override
	public long getClassPK() {
		return model.getClassPK();
	}

	@Override
	public CommerceAccountGroup getCommerceAccountGroup()
		throws com.liferay.portal.kernel.exception.PortalException {

		return model.getCommerceAccountGroup();
	}

	/**
	 * Returns the commerce account group ID of this commerce account group rel.
	 *
	 * @return the commerce account group ID of this commerce account group rel
	 */
	@Override
	public long getCommerceAccountGroupId() {
		return model.getCommerceAccountGroupId();
	}

	/**
	 * Returns the commerce account group rel ID of this commerce account group rel.
	 *
	 * @return the commerce account group rel ID of this commerce account group rel
	 */
	@Override
	public long getCommerceAccountGroupRelId() {
		return model.getCommerceAccountGroupRelId();
	}

	/**
	 * Returns the company ID of this commerce account group rel.
	 *
	 * @return the company ID of this commerce account group rel
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this commerce account group rel.
	 *
	 * @return the create date of this commerce account group rel
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the modified date of this commerce account group rel.
	 *
	 * @return the modified date of this commerce account group rel
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the primary key of this commerce account group rel.
	 *
	 * @return the primary key of this commerce account group rel
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the user ID of this commerce account group rel.
	 *
	 * @return the user ID of this commerce account group rel
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this commerce account group rel.
	 *
	 * @return the user name of this commerce account group rel
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this commerce account group rel.
	 *
	 * @return the user uuid of this commerce account group rel
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	@Override
	public void persist() {
		model.persist();
	}

	@Override
	public void setClassName(String className) {
		model.setClassName(className);
	}

	/**
	 * Sets the class name ID of this commerce account group rel.
	 *
	 * @param classNameId the class name ID of this commerce account group rel
	 */
	@Override
	public void setClassNameId(long classNameId) {
		model.setClassNameId(classNameId);
	}

	/**
	 * Sets the class pk of this commerce account group rel.
	 *
	 * @param classPK the class pk of this commerce account group rel
	 */
	@Override
	public void setClassPK(long classPK) {
		model.setClassPK(classPK);
	}

	/**
	 * Sets the commerce account group ID of this commerce account group rel.
	 *
	 * @param commerceAccountGroupId the commerce account group ID of this commerce account group rel
	 */
	@Override
	public void setCommerceAccountGroupId(long commerceAccountGroupId) {
		model.setCommerceAccountGroupId(commerceAccountGroupId);
	}

	/**
	 * Sets the commerce account group rel ID of this commerce account group rel.
	 *
	 * @param commerceAccountGroupRelId the commerce account group rel ID of this commerce account group rel
	 */
	@Override
	public void setCommerceAccountGroupRelId(long commerceAccountGroupRelId) {
		model.setCommerceAccountGroupRelId(commerceAccountGroupRelId);
	}

	/**
	 * Sets the company ID of this commerce account group rel.
	 *
	 * @param companyId the company ID of this commerce account group rel
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this commerce account group rel.
	 *
	 * @param createDate the create date of this commerce account group rel
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the modified date of this commerce account group rel.
	 *
	 * @param modifiedDate the modified date of this commerce account group rel
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the primary key of this commerce account group rel.
	 *
	 * @param primaryKey the primary key of this commerce account group rel
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the user ID of this commerce account group rel.
	 *
	 * @param userId the user ID of this commerce account group rel
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this commerce account group rel.
	 *
	 * @param userName the user name of this commerce account group rel
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this commerce account group rel.
	 *
	 * @param userUuid the user uuid of this commerce account group rel
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	@Override
	protected CommerceAccountGroupRelWrapper wrap(
		CommerceAccountGroupRel commerceAccountGroupRel) {

		return new CommerceAccountGroupRelWrapper(commerceAccountGroupRel);
	}

}