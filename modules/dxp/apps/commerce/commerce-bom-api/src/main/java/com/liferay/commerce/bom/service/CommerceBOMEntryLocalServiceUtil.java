/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.bom.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for CommerceBOMEntry. This utility wraps
 * <code>com.liferay.commerce.bom.service.impl.CommerceBOMEntryLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Luca Pellizzon
 * @see CommerceBOMEntryLocalService
 * @generated
 */
public class CommerceBOMEntryLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.bom.service.impl.CommerceBOMEntryLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the commerce bom entry to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceBOMEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceBOMEntry the commerce bom entry
	 * @return the commerce bom entry that was added
	 */
	public static com.liferay.commerce.bom.model.CommerceBOMEntry
		addCommerceBOMEntry(
			com.liferay.commerce.bom.model.CommerceBOMEntry commerceBOMEntry) {

		return getService().addCommerceBOMEntry(commerceBOMEntry);
	}

	public static com.liferay.commerce.bom.model.CommerceBOMEntry
			addCommerceBOMEntry(
				long userId, int number, String cpInstanceUuid, long cProductId,
				long commerceBOMDefinitionId, double positionX,
				double positionY, double radius)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addCommerceBOMEntry(
			userId, number, cpInstanceUuid, cProductId, commerceBOMDefinitionId,
			positionX, positionY, radius);
	}

	/**
	 * Creates a new commerce bom entry with the primary key. Does not add the commerce bom entry to the database.
	 *
	 * @param commerceBOMEntryId the primary key for the new commerce bom entry
	 * @return the new commerce bom entry
	 */
	public static com.liferay.commerce.bom.model.CommerceBOMEntry
		createCommerceBOMEntry(long commerceBOMEntryId) {

		return getService().createCommerceBOMEntry(commerceBOMEntryId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			createPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Deletes the commerce bom entry from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceBOMEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceBOMEntry the commerce bom entry
	 * @return the commerce bom entry that was removed
	 */
	public static com.liferay.commerce.bom.model.CommerceBOMEntry
		deleteCommerceBOMEntry(
			com.liferay.commerce.bom.model.CommerceBOMEntry commerceBOMEntry) {

		return getService().deleteCommerceBOMEntry(commerceBOMEntry);
	}

	/**
	 * Deletes the commerce bom entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceBOMEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceBOMEntryId the primary key of the commerce bom entry
	 * @return the commerce bom entry that was removed
	 * @throws PortalException if a commerce bom entry with the primary key could not be found
	 */
	public static com.liferay.commerce.bom.model.CommerceBOMEntry
			deleteCommerceBOMEntry(long commerceBOMEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteCommerceBOMEntry(commerceBOMEntryId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			deletePersistedModel(
				com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static <T> T dslQuery(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return getService().dslQuery(dslQuery);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.bom.model.impl.CommerceBOMEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.bom.model.impl.CommerceBOMEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.commerce.bom.model.CommerceBOMEntry
		fetchCommerceBOMEntry(long commerceBOMEntryId) {

		return getService().fetchCommerceBOMEntry(commerceBOMEntryId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	/**
	 * Returns a range of all the commerce bom entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.bom.model.impl.CommerceBOMEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce bom entries
	 * @param end the upper bound of the range of commerce bom entries (not inclusive)
	 * @return the range of commerce bom entries
	 */
	public static java.util.List
		<com.liferay.commerce.bom.model.CommerceBOMEntry> getCommerceBOMEntries(
			int start, int end) {

		return getService().getCommerceBOMEntries(start, end);
	}

	public static java.util.List
		<com.liferay.commerce.bom.model.CommerceBOMEntry> getCommerceBOMEntries(
			long commerceBOMDefinitionId, int start, int end) {

		return getService().getCommerceBOMEntries(
			commerceBOMDefinitionId, start, end);
	}

	/**
	 * Returns the number of commerce bom entries.
	 *
	 * @return the number of commerce bom entries
	 */
	public static int getCommerceBOMEntriesCount() {
		return getService().getCommerceBOMEntriesCount();
	}

	public static int getCommerceBOMEntriesCount(long commerceBOMDefinitionId) {
		return getService().getCommerceBOMEntriesCount(commerceBOMDefinitionId);
	}

	/**
	 * Returns the commerce bom entry with the primary key.
	 *
	 * @param commerceBOMEntryId the primary key of the commerce bom entry
	 * @return the commerce bom entry
	 * @throws PortalException if a commerce bom entry with the primary key could not be found
	 */
	public static com.liferay.commerce.bom.model.CommerceBOMEntry
			getCommerceBOMEntry(long commerceBOMEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCommerceBOMEntry(commerceBOMEntryId);
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the commerce bom entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceBOMEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceBOMEntry the commerce bom entry
	 * @return the commerce bom entry that was updated
	 */
	public static com.liferay.commerce.bom.model.CommerceBOMEntry
		updateCommerceBOMEntry(
			com.liferay.commerce.bom.model.CommerceBOMEntry commerceBOMEntry) {

		return getService().updateCommerceBOMEntry(commerceBOMEntry);
	}

	public static com.liferay.commerce.bom.model.CommerceBOMEntry
			updateCommerceBOMEntry(
				long commerceBOMEntryId, int number, String cpInstanceUuid,
				long cProductId, double positionX, double positionY,
				double radius)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateCommerceBOMEntry(
			commerceBOMEntryId, number, cpInstanceUuid, cProductId, positionX,
			positionY, radius);
	}

	public static CommerceBOMEntryLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<CommerceBOMEntryLocalService, CommerceBOMEntryLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			CommerceBOMEntryLocalService.class);

		ServiceTracker
			<CommerceBOMEntryLocalService, CommerceBOMEntryLocalService>
				serviceTracker =
					new ServiceTracker
						<CommerceBOMEntryLocalService,
						 CommerceBOMEntryLocalService>(
							 bundle.getBundleContext(),
							 CommerceBOMEntryLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}