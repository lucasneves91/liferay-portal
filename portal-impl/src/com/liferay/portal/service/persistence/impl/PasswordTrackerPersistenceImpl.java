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

package com.liferay.portal.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.NoSuchPasswordTrackerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.PasswordTracker;
import com.liferay.portal.kernel.model.PasswordTrackerTable;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.persistence.PasswordTrackerPersistence;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.model.impl.PasswordTrackerImpl;
import com.liferay.portal.model.impl.PasswordTrackerModelImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The persistence implementation for the password tracker service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class PasswordTrackerPersistenceImpl
	extends BasePersistenceImpl<PasswordTracker>
	implements PasswordTrackerPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>PasswordTrackerUtil</code> to access the password tracker persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		PasswordTrackerImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByUserId;
	private FinderPath _finderPathWithoutPaginationFindByUserId;
	private FinderPath _finderPathCountByUserId;

	/**
	 * Returns all the password trackers where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the matching password trackers
	 */
	@Override
	public List<PasswordTracker> findByUserId(long userId) {
		return findByUserId(userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the password trackers where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordTrackerModelImpl</code>.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of password trackers
	 * @param end the upper bound of the range of password trackers (not inclusive)
	 * @return the range of matching password trackers
	 */
	@Override
	public List<PasswordTracker> findByUserId(long userId, int start, int end) {
		return findByUserId(userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the password trackers where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordTrackerModelImpl</code>.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of password trackers
	 * @param end the upper bound of the range of password trackers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password trackers
	 */
	@Override
	public List<PasswordTracker> findByUserId(
		long userId, int start, int end,
		OrderByComparator<PasswordTracker> orderByComparator) {

		return findByUserId(userId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the password trackers where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordTrackerModelImpl</code>.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of password trackers
	 * @param end the upper bound of the range of password trackers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password trackers
	 */
	@Override
	public List<PasswordTracker> findByUserId(
		long userId, int start, int end,
		OrderByComparator<PasswordTracker> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUserId;
				finderArgs = new Object[] {userId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUserId;
			finderArgs = new Object[] {userId, start, end, orderByComparator};
		}

		List<PasswordTracker> list = null;

		if (useFinderCache) {
			list = (List<PasswordTracker>)FinderCacheUtil.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (PasswordTracker passwordTracker : list) {
					if (userId != passwordTracker.getUserId()) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_PASSWORDTRACKER_WHERE);

			sb.append(_FINDER_COLUMN_USERID_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(PasswordTrackerModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(userId);

				list = (List<PasswordTracker>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first password tracker in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password tracker
	 * @throws NoSuchPasswordTrackerException if a matching password tracker could not be found
	 */
	@Override
	public PasswordTracker findByUserId_First(
			long userId, OrderByComparator<PasswordTracker> orderByComparator)
		throws NoSuchPasswordTrackerException {

		PasswordTracker passwordTracker = fetchByUserId_First(
			userId, orderByComparator);

		if (passwordTracker != null) {
			return passwordTracker;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("userId=");
		sb.append(userId);

		sb.append("}");

		throw new NoSuchPasswordTrackerException(sb.toString());
	}

	/**
	 * Returns the first password tracker in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password tracker, or <code>null</code> if a matching password tracker could not be found
	 */
	@Override
	public PasswordTracker fetchByUserId_First(
		long userId, OrderByComparator<PasswordTracker> orderByComparator) {

		List<PasswordTracker> list = findByUserId(
			userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last password tracker in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password tracker
	 * @throws NoSuchPasswordTrackerException if a matching password tracker could not be found
	 */
	@Override
	public PasswordTracker findByUserId_Last(
			long userId, OrderByComparator<PasswordTracker> orderByComparator)
		throws NoSuchPasswordTrackerException {

		PasswordTracker passwordTracker = fetchByUserId_Last(
			userId, orderByComparator);

		if (passwordTracker != null) {
			return passwordTracker;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("userId=");
		sb.append(userId);

		sb.append("}");

		throw new NoSuchPasswordTrackerException(sb.toString());
	}

	/**
	 * Returns the last password tracker in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password tracker, or <code>null</code> if a matching password tracker could not be found
	 */
	@Override
	public PasswordTracker fetchByUserId_Last(
		long userId, OrderByComparator<PasswordTracker> orderByComparator) {

		int count = countByUserId(userId);

		if (count == 0) {
			return null;
		}

		List<PasswordTracker> list = findByUserId(
			userId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the password trackers before and after the current password tracker in the ordered set where userId = &#63;.
	 *
	 * @param passwordTrackerId the primary key of the current password tracker
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password tracker
	 * @throws NoSuchPasswordTrackerException if a password tracker with the primary key could not be found
	 */
	@Override
	public PasswordTracker[] findByUserId_PrevAndNext(
			long passwordTrackerId, long userId,
			OrderByComparator<PasswordTracker> orderByComparator)
		throws NoSuchPasswordTrackerException {

		PasswordTracker passwordTracker = findByPrimaryKey(passwordTrackerId);

		Session session = null;

		try {
			session = openSession();

			PasswordTracker[] array = new PasswordTrackerImpl[3];

			array[0] = getByUserId_PrevAndNext(
				session, passwordTracker, userId, orderByComparator, true);

			array[1] = passwordTracker;

			array[2] = getByUserId_PrevAndNext(
				session, passwordTracker, userId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected PasswordTracker getByUserId_PrevAndNext(
		Session session, PasswordTracker passwordTracker, long userId,
		OrderByComparator<PasswordTracker> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_PASSWORDTRACKER_WHERE);

		sb.append(_FINDER_COLUMN_USERID_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(PasswordTrackerModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(userId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						passwordTracker)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<PasswordTracker> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the password trackers where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 */
	@Override
	public void removeByUserId(long userId) {
		for (PasswordTracker passwordTracker :
				findByUserId(
					userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(passwordTracker);
		}
	}

	/**
	 * Returns the number of password trackers where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching password trackers
	 */
	@Override
	public int countByUserId(long userId) {
		FinderPath finderPath = _finderPathCountByUserId;

		Object[] finderArgs = new Object[] {userId};

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_PASSWORDTRACKER_WHERE);

			sb.append(_FINDER_COLUMN_USERID_USERID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(userId);

				count = (Long)query.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_USERID_USERID_2 =
		"passwordTracker.userId = ?";

	public PasswordTrackerPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("password", "password_");

		setDBColumnNames(dbColumnNames);

		setModelClass(PasswordTracker.class);

		setModelImplClass(PasswordTrackerImpl.class);
		setModelPKClass(long.class);

		setTable(PasswordTrackerTable.INSTANCE);
	}

	/**
	 * Caches the password tracker in the entity cache if it is enabled.
	 *
	 * @param passwordTracker the password tracker
	 */
	@Override
	public void cacheResult(PasswordTracker passwordTracker) {
		EntityCacheUtil.putResult(
			PasswordTrackerImpl.class, passwordTracker.getPrimaryKey(),
			passwordTracker);
	}

	/**
	 * Caches the password trackers in the entity cache if it is enabled.
	 *
	 * @param passwordTrackers the password trackers
	 */
	@Override
	public void cacheResult(List<PasswordTracker> passwordTrackers) {
		for (PasswordTracker passwordTracker : passwordTrackers) {
			if (EntityCacheUtil.getResult(
					PasswordTrackerImpl.class,
					passwordTracker.getPrimaryKey()) == null) {

				cacheResult(passwordTracker);
			}
		}
	}

	/**
	 * Clears the cache for all password trackers.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		EntityCacheUtil.clearCache(PasswordTrackerImpl.class);

		FinderCacheUtil.clearCache(PasswordTrackerImpl.class);
	}

	/**
	 * Clears the cache for the password tracker.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(PasswordTracker passwordTracker) {
		EntityCacheUtil.removeResult(
			PasswordTrackerImpl.class, passwordTracker);
	}

	@Override
	public void clearCache(List<PasswordTracker> passwordTrackers) {
		for (PasswordTracker passwordTracker : passwordTrackers) {
			EntityCacheUtil.removeResult(
				PasswordTrackerImpl.class, passwordTracker);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		FinderCacheUtil.clearCache(PasswordTrackerImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			EntityCacheUtil.removeResult(PasswordTrackerImpl.class, primaryKey);
		}
	}

	/**
	 * Creates a new password tracker with the primary key. Does not add the password tracker to the database.
	 *
	 * @param passwordTrackerId the primary key for the new password tracker
	 * @return the new password tracker
	 */
	@Override
	public PasswordTracker create(long passwordTrackerId) {
		PasswordTracker passwordTracker = new PasswordTrackerImpl();

		passwordTracker.setNew(true);
		passwordTracker.setPrimaryKey(passwordTrackerId);

		passwordTracker.setCompanyId(CompanyThreadLocal.getCompanyId());

		return passwordTracker;
	}

	/**
	 * Removes the password tracker with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordTrackerId the primary key of the password tracker
	 * @return the password tracker that was removed
	 * @throws NoSuchPasswordTrackerException if a password tracker with the primary key could not be found
	 */
	@Override
	public PasswordTracker remove(long passwordTrackerId)
		throws NoSuchPasswordTrackerException {

		return remove((Serializable)passwordTrackerId);
	}

	/**
	 * Removes the password tracker with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the password tracker
	 * @return the password tracker that was removed
	 * @throws NoSuchPasswordTrackerException if a password tracker with the primary key could not be found
	 */
	@Override
	public PasswordTracker remove(Serializable primaryKey)
		throws NoSuchPasswordTrackerException {

		Session session = null;

		try {
			session = openSession();

			PasswordTracker passwordTracker = (PasswordTracker)session.get(
				PasswordTrackerImpl.class, primaryKey);

			if (passwordTracker == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchPasswordTrackerException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(passwordTracker);
		}
		catch (NoSuchPasswordTrackerException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected PasswordTracker removeImpl(PasswordTracker passwordTracker) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(passwordTracker)) {
				passwordTracker = (PasswordTracker)session.get(
					PasswordTrackerImpl.class,
					passwordTracker.getPrimaryKeyObj());
			}

			if (passwordTracker != null) {
				session.delete(passwordTracker);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (passwordTracker != null) {
			clearCache(passwordTracker);
		}

		return passwordTracker;
	}

	@Override
	public PasswordTracker updateImpl(PasswordTracker passwordTracker) {
		boolean isNew = passwordTracker.isNew();

		if (!(passwordTracker instanceof PasswordTrackerModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(passwordTracker.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					passwordTracker);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in passwordTracker proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom PasswordTracker implementation " +
					passwordTracker.getClass());
		}

		PasswordTrackerModelImpl passwordTrackerModelImpl =
			(PasswordTrackerModelImpl)passwordTracker;

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(passwordTracker);
			}
			else {
				passwordTracker = (PasswordTracker)session.merge(
					passwordTracker);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		EntityCacheUtil.putResult(
			PasswordTrackerImpl.class, passwordTrackerModelImpl, false, true);

		if (isNew) {
			passwordTracker.setNew(false);
		}

		passwordTracker.resetOriginalValues();

		return passwordTracker;
	}

	/**
	 * Returns the password tracker with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the password tracker
	 * @return the password tracker
	 * @throws NoSuchPasswordTrackerException if a password tracker with the primary key could not be found
	 */
	@Override
	public PasswordTracker findByPrimaryKey(Serializable primaryKey)
		throws NoSuchPasswordTrackerException {

		PasswordTracker passwordTracker = fetchByPrimaryKey(primaryKey);

		if (passwordTracker == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchPasswordTrackerException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return passwordTracker;
	}

	/**
	 * Returns the password tracker with the primary key or throws a <code>NoSuchPasswordTrackerException</code> if it could not be found.
	 *
	 * @param passwordTrackerId the primary key of the password tracker
	 * @return the password tracker
	 * @throws NoSuchPasswordTrackerException if a password tracker with the primary key could not be found
	 */
	@Override
	public PasswordTracker findByPrimaryKey(long passwordTrackerId)
		throws NoSuchPasswordTrackerException {

		return findByPrimaryKey((Serializable)passwordTrackerId);
	}

	/**
	 * Returns the password tracker with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param passwordTrackerId the primary key of the password tracker
	 * @return the password tracker, or <code>null</code> if a password tracker with the primary key could not be found
	 */
	@Override
	public PasswordTracker fetchByPrimaryKey(long passwordTrackerId) {
		return fetchByPrimaryKey((Serializable)passwordTrackerId);
	}

	/**
	 * Returns all the password trackers.
	 *
	 * @return the password trackers
	 */
	@Override
	public List<PasswordTracker> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the password trackers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordTrackerModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password trackers
	 * @param end the upper bound of the range of password trackers (not inclusive)
	 * @return the range of password trackers
	 */
	@Override
	public List<PasswordTracker> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the password trackers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordTrackerModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password trackers
	 * @param end the upper bound of the range of password trackers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of password trackers
	 */
	@Override
	public List<PasswordTracker> findAll(
		int start, int end,
		OrderByComparator<PasswordTracker> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the password trackers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordTrackerModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password trackers
	 * @param end the upper bound of the range of password trackers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of password trackers
	 */
	@Override
	public List<PasswordTracker> findAll(
		int start, int end,
		OrderByComparator<PasswordTracker> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<PasswordTracker> list = null;

		if (useFinderCache) {
			list = (List<PasswordTracker>)FinderCacheUtil.getResult(
				finderPath, finderArgs);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_PASSWORDTRACKER);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_PASSWORDTRACKER;

				sql = sql.concat(PasswordTrackerModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<PasswordTracker>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the password trackers from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (PasswordTracker passwordTracker : findAll()) {
			remove(passwordTracker);
		}
	}

	/**
	 * Returns the number of password trackers.
	 *
	 * @return the number of password trackers
	 */
	@Override
	public int countAll() {
		Long count = (Long)FinderCacheUtil.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_PASSWORDTRACKER);

				count = (Long)query.uniqueResult();

				FinderCacheUtil.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return EntityCacheUtil.getEntityCache();
	}

	@Override
	protected String getPKDBName() {
		return "passwordTrackerId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_PASSWORDTRACKER;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return PasswordTrackerModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the password tracker persistence.
	 */
	public void afterPropertiesSet() {
		Registry registry = RegistryUtil.getRegistry();

		_argumentsResolverServiceRegistration = registry.registerService(
			ArgumentsResolver.class,
			new PasswordTrackerModelArgumentsResolver());

		_finderPathWithPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByUserId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUserId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"userId"}, true);

		_finderPathWithoutPaginationFindByUserId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUserId",
			new String[] {Long.class.getName()}, new String[] {"userId"}, true);

		_finderPathCountByUserId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUserId",
			new String[] {Long.class.getName()}, new String[] {"userId"},
			false);
	}

	public void destroy() {
		EntityCacheUtil.removeCache(PasswordTrackerImpl.class.getName());

		_argumentsResolverServiceRegistration.unregister();
	}

	private static final String _SQL_SELECT_PASSWORDTRACKER =
		"SELECT passwordTracker FROM PasswordTracker passwordTracker";

	private static final String _SQL_SELECT_PASSWORDTRACKER_WHERE =
		"SELECT passwordTracker FROM PasswordTracker passwordTracker WHERE ";

	private static final String _SQL_COUNT_PASSWORDTRACKER =
		"SELECT COUNT(passwordTracker) FROM PasswordTracker passwordTracker";

	private static final String _SQL_COUNT_PASSWORDTRACKER_WHERE =
		"SELECT COUNT(passwordTracker) FROM PasswordTracker passwordTracker WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "passwordTracker.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No PasswordTracker exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No PasswordTracker exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		PasswordTrackerPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"password"});

	@Override
	protected FinderCache getFinderCache() {
		return FinderCacheUtil.getFinderCache();
	}

	private ServiceRegistration<ArgumentsResolver>
		_argumentsResolverServiceRegistration;

	private static class PasswordTrackerModelArgumentsResolver
		implements ArgumentsResolver {

		@Override
		public Object[] getArguments(
			FinderPath finderPath, BaseModel<?> baseModel, boolean checkColumn,
			boolean original) {

			String[] columnNames = finderPath.getColumnNames();

			if ((columnNames == null) || (columnNames.length == 0)) {
				if (baseModel.isNew()) {
					return FINDER_ARGS_EMPTY;
				}

				return null;
			}

			PasswordTrackerModelImpl passwordTrackerModelImpl =
				(PasswordTrackerModelImpl)baseModel;

			long columnBitmask = passwordTrackerModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(
					passwordTrackerModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |=
						passwordTrackerModelImpl.getColumnBitmask(columnName);
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(
					passwordTrackerModelImpl, columnNames, original);
			}

			return null;
		}

		@Override
		public String getClassName() {
			return PasswordTrackerImpl.class.getName();
		}

		@Override
		public String getTableName() {
			return PasswordTrackerTable.INSTANCE.getTableName();
		}

		private Object[] _getValue(
			PasswordTrackerModelImpl passwordTrackerModelImpl,
			String[] columnNames, boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] =
						passwordTrackerModelImpl.getColumnOriginalValue(
							columnName);
				}
				else {
					arguments[i] = passwordTrackerModelImpl.getColumnValue(
						columnName);
				}
			}

			return arguments;
		}

		private static Map<FinderPath, Long> _finderPathColumnBitmasksCache =
			new ConcurrentHashMap<>();

	}

}