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

package com.liferay.commerce.data.integration.service.base;

import com.liferay.commerce.data.integration.model.CommerceDataIntegrationProcess;
import com.liferay.commerce.data.integration.service.CommerceDataIntegrationProcessLocalService;
import com.liferay.commerce.data.integration.service.persistence.CommerceDataIntegrationProcessLogPersistence;
import com.liferay.commerce.data.integration.service.persistence.CommerceDataIntegrationProcessPersistence;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.CompanyPersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the commerce data integration process local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.commerce.data.integration.service.impl.CommerceDataIntegrationProcessLocalServiceImpl}.
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @see com.liferay.commerce.data.integration.service.impl.CommerceDataIntegrationProcessLocalServiceImpl
 * @generated
 */
public abstract class CommerceDataIntegrationProcessLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements CommerceDataIntegrationProcessLocalService,
			   IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>CommerceDataIntegrationProcessLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.commerce.data.integration.service.CommerceDataIntegrationProcessLocalServiceUtil</code>.
	 */

	/**
	 * Adds the commerce data integration process to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceDataIntegrationProcessLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceDataIntegrationProcess the commerce data integration process
	 * @return the commerce data integration process that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceDataIntegrationProcess addCommerceDataIntegrationProcess(
		CommerceDataIntegrationProcess commerceDataIntegrationProcess) {

		commerceDataIntegrationProcess.setNew(true);

		return commerceDataIntegrationProcessPersistence.update(
			commerceDataIntegrationProcess);
	}

	/**
	 * Creates a new commerce data integration process with the primary key. Does not add the commerce data integration process to the database.
	 *
	 * @param commerceDataIntegrationProcessId the primary key for the new commerce data integration process
	 * @return the new commerce data integration process
	 */
	@Override
	@Transactional(enabled = false)
	public CommerceDataIntegrationProcess createCommerceDataIntegrationProcess(
		long commerceDataIntegrationProcessId) {

		return commerceDataIntegrationProcessPersistence.create(
			commerceDataIntegrationProcessId);
	}

	/**
	 * Deletes the commerce data integration process with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceDataIntegrationProcessLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceDataIntegrationProcessId the primary key of the commerce data integration process
	 * @return the commerce data integration process that was removed
	 * @throws PortalException if a commerce data integration process with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CommerceDataIntegrationProcess deleteCommerceDataIntegrationProcess(
			long commerceDataIntegrationProcessId)
		throws PortalException {

		return commerceDataIntegrationProcessPersistence.remove(
			commerceDataIntegrationProcessId);
	}

	/**
	 * Deletes the commerce data integration process from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceDataIntegrationProcessLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceDataIntegrationProcess the commerce data integration process
	 * @return the commerce data integration process that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CommerceDataIntegrationProcess deleteCommerceDataIntegrationProcess(
			CommerceDataIntegrationProcess commerceDataIntegrationProcess)
		throws PortalException {

		return commerceDataIntegrationProcessPersistence.remove(
			commerceDataIntegrationProcess);
	}

	@Override
	public <T> T dslQuery(DSLQuery dslQuery) {
		return commerceDataIntegrationProcessPersistence.dslQuery(dslQuery);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			CommerceDataIntegrationProcess.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return commerceDataIntegrationProcessPersistence.findWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.data.integration.model.impl.CommerceDataIntegrationProcessModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return commerceDataIntegrationProcessPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.data.integration.model.impl.CommerceDataIntegrationProcessModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return commerceDataIntegrationProcessPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return commerceDataIntegrationProcessPersistence.countWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection) {

		return commerceDataIntegrationProcessPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public CommerceDataIntegrationProcess fetchCommerceDataIntegrationProcess(
		long commerceDataIntegrationProcessId) {

		return commerceDataIntegrationProcessPersistence.fetchByPrimaryKey(
			commerceDataIntegrationProcessId);
	}

	/**
	 * Returns the commerce data integration process with the primary key.
	 *
	 * @param commerceDataIntegrationProcessId the primary key of the commerce data integration process
	 * @return the commerce data integration process
	 * @throws PortalException if a commerce data integration process with the primary key could not be found
	 */
	@Override
	public CommerceDataIntegrationProcess getCommerceDataIntegrationProcess(
			long commerceDataIntegrationProcessId)
		throws PortalException {

		return commerceDataIntegrationProcessPersistence.findByPrimaryKey(
			commerceDataIntegrationProcessId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(
			commerceDataIntegrationProcessLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(
			CommerceDataIntegrationProcess.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"commerceDataIntegrationProcessId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			commerceDataIntegrationProcessLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(
			CommerceDataIntegrationProcess.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"commerceDataIntegrationProcessId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(
			commerceDataIntegrationProcessLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(
			CommerceDataIntegrationProcess.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"commerceDataIntegrationProcessId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return commerceDataIntegrationProcessPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return commerceDataIntegrationProcessLocalService.
			deleteCommerceDataIntegrationProcess(
				(CommerceDataIntegrationProcess)persistedModel);
	}

	@Override
	public BasePersistence<CommerceDataIntegrationProcess>
		getBasePersistence() {

		return commerceDataIntegrationProcessPersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return commerceDataIntegrationProcessPersistence.findByPrimaryKey(
			primaryKeyObj);
	}

	/**
	 * Returns a range of all the commerce data integration processes.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.data.integration.model.impl.CommerceDataIntegrationProcessModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce data integration processes
	 * @param end the upper bound of the range of commerce data integration processes (not inclusive)
	 * @return the range of commerce data integration processes
	 */
	@Override
	public List<CommerceDataIntegrationProcess>
		getCommerceDataIntegrationProcesses(int start, int end) {

		return commerceDataIntegrationProcessPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of commerce data integration processes.
	 *
	 * @return the number of commerce data integration processes
	 */
	@Override
	public int getCommerceDataIntegrationProcessesCount() {
		return commerceDataIntegrationProcessPersistence.countAll();
	}

	/**
	 * Updates the commerce data integration process in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceDataIntegrationProcessLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceDataIntegrationProcess the commerce data integration process
	 * @return the commerce data integration process that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceDataIntegrationProcess updateCommerceDataIntegrationProcess(
		CommerceDataIntegrationProcess commerceDataIntegrationProcess) {

		return commerceDataIntegrationProcessPersistence.update(
			commerceDataIntegrationProcess);
	}

	/**
	 * Returns the commerce data integration process local service.
	 *
	 * @return the commerce data integration process local service
	 */
	public CommerceDataIntegrationProcessLocalService
		getCommerceDataIntegrationProcessLocalService() {

		return commerceDataIntegrationProcessLocalService;
	}

	/**
	 * Sets the commerce data integration process local service.
	 *
	 * @param commerceDataIntegrationProcessLocalService the commerce data integration process local service
	 */
	public void setCommerceDataIntegrationProcessLocalService(
		CommerceDataIntegrationProcessLocalService
			commerceDataIntegrationProcessLocalService) {

		this.commerceDataIntegrationProcessLocalService =
			commerceDataIntegrationProcessLocalService;
	}

	/**
	 * Returns the commerce data integration process persistence.
	 *
	 * @return the commerce data integration process persistence
	 */
	public CommerceDataIntegrationProcessPersistence
		getCommerceDataIntegrationProcessPersistence() {

		return commerceDataIntegrationProcessPersistence;
	}

	/**
	 * Sets the commerce data integration process persistence.
	 *
	 * @param commerceDataIntegrationProcessPersistence the commerce data integration process persistence
	 */
	public void setCommerceDataIntegrationProcessPersistence(
		CommerceDataIntegrationProcessPersistence
			commerceDataIntegrationProcessPersistence) {

		this.commerceDataIntegrationProcessPersistence =
			commerceDataIntegrationProcessPersistence;
	}

	/**
	 * Returns the commerce data integration process log local service.
	 *
	 * @return the commerce data integration process log local service
	 */
	public com.liferay.commerce.data.integration.service.
		CommerceDataIntegrationProcessLogLocalService
			getCommerceDataIntegrationProcessLogLocalService() {

		return commerceDataIntegrationProcessLogLocalService;
	}

	/**
	 * Sets the commerce data integration process log local service.
	 *
	 * @param commerceDataIntegrationProcessLogLocalService the commerce data integration process log local service
	 */
	public void setCommerceDataIntegrationProcessLogLocalService(
		com.liferay.commerce.data.integration.service.
			CommerceDataIntegrationProcessLogLocalService
				commerceDataIntegrationProcessLogLocalService) {

		this.commerceDataIntegrationProcessLogLocalService =
			commerceDataIntegrationProcessLogLocalService;
	}

	/**
	 * Returns the commerce data integration process log persistence.
	 *
	 * @return the commerce data integration process log persistence
	 */
	public CommerceDataIntegrationProcessLogPersistence
		getCommerceDataIntegrationProcessLogPersistence() {

		return commerceDataIntegrationProcessLogPersistence;
	}

	/**
	 * Sets the commerce data integration process log persistence.
	 *
	 * @param commerceDataIntegrationProcessLogPersistence the commerce data integration process log persistence
	 */
	public void setCommerceDataIntegrationProcessLogPersistence(
		CommerceDataIntegrationProcessLogPersistence
			commerceDataIntegrationProcessLogPersistence) {

		this.commerceDataIntegrationProcessLogPersistence =
			commerceDataIntegrationProcessLogPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService
		getCounterLocalService() {

		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService
			counterLocalService) {

		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.kernel.service.ClassNameLocalService
		getClassNameLocalService() {

		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.kernel.service.ClassNameLocalService
			classNameLocalService) {

		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {

		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the company local service.
	 *
	 * @return the company local service
	 */
	public com.liferay.portal.kernel.service.CompanyLocalService
		getCompanyLocalService() {

		return companyLocalService;
	}

	/**
	 * Sets the company local service.
	 *
	 * @param companyLocalService the company local service
	 */
	public void setCompanyLocalService(
		com.liferay.portal.kernel.service.CompanyLocalService
			companyLocalService) {

		this.companyLocalService = companyLocalService;
	}

	/**
	 * Returns the company persistence.
	 *
	 * @return the company persistence
	 */
	public CompanyPersistence getCompanyPersistence() {
		return companyPersistence;
	}

	/**
	 * Sets the company persistence.
	 *
	 * @param companyPersistence the company persistence
	 */
	public void setCompanyPersistence(CompanyPersistence companyPersistence) {
		this.companyPersistence = companyPersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService
		getResourceLocalService() {

		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService
			resourceLocalService) {

		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService
		getUserLocalService() {

		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {

		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		persistedModelLocalServiceRegistry.register(
			"com.liferay.commerce.data.integration.model.CommerceDataIntegrationProcess",
			commerceDataIntegrationProcessLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.commerce.data.integration.model.CommerceDataIntegrationProcess");
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return CommerceDataIntegrationProcessLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return CommerceDataIntegrationProcess.class;
	}

	protected String getModelClassName() {
		return CommerceDataIntegrationProcess.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource =
				commerceDataIntegrationProcessPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	@BeanReference(type = CommerceDataIntegrationProcessLocalService.class)
	protected CommerceDataIntegrationProcessLocalService
		commerceDataIntegrationProcessLocalService;

	@BeanReference(type = CommerceDataIntegrationProcessPersistence.class)
	protected CommerceDataIntegrationProcessPersistence
		commerceDataIntegrationProcessPersistence;

	@BeanReference(
		type = com.liferay.commerce.data.integration.service.CommerceDataIntegrationProcessLogLocalService.class
	)
	protected com.liferay.commerce.data.integration.service.
		CommerceDataIntegrationProcessLogLocalService
			commerceDataIntegrationProcessLogLocalService;

	@BeanReference(type = CommerceDataIntegrationProcessLogPersistence.class)
	protected CommerceDataIntegrationProcessLogPersistence
		commerceDataIntegrationProcessLogPersistence;

	@ServiceReference(
		type = com.liferay.counter.kernel.service.CounterLocalService.class
	)
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.ClassNameLocalService.class
	)
	protected com.liferay.portal.kernel.service.ClassNameLocalService
		classNameLocalService;

	@ServiceReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.CompanyLocalService.class
	)
	protected com.liferay.portal.kernel.service.CompanyLocalService
		companyLocalService;

	@ServiceReference(type = CompanyPersistence.class)
	protected CompanyPersistence companyPersistence;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.ResourceLocalService.class
	)
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.UserLocalService.class
	)
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;

	@ServiceReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry
		persistedModelLocalServiceRegistry;

}