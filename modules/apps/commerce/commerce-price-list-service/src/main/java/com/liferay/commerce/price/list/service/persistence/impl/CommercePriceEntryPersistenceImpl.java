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

package com.liferay.commerce.price.list.service.persistence.impl;

import com.liferay.commerce.price.list.exception.NoSuchPriceEntryException;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.model.CommercePriceEntryTable;
import com.liferay.commerce.price.list.model.impl.CommercePriceEntryImpl;
import com.liferay.commerce.price.list.model.impl.CommercePriceEntryModelImpl;
import com.liferay.commerce.price.list.service.persistence.CommercePriceEntryPersistence;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * The persistence implementation for the commerce price entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @generated
 */
public class CommercePriceEntryPersistenceImpl
	extends BasePersistenceImpl<CommercePriceEntry>
	implements CommercePriceEntryPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>CommercePriceEntryUtil</code> to access the commerce price entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		CommercePriceEntryImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByUuid;
	private FinderPath _finderPathWithoutPaginationFindByUuid;
	private FinderPath _finderPathCountByUuid;

	/**
	 * Returns all the commerce price entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @return the range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByUuid(
		String uuid, int start, int end) {

		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<CommercePriceEntry> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid;
				finderArgs = new Object[] {uuid};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid;
			finderArgs = new Object[] {uuid, start, end, orderByComparator};
		}

		List<CommercePriceEntry> list = null;

		if (useFinderCache) {
			list = (List<CommercePriceEntry>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceEntry commercePriceEntry : list) {
					if (!uuid.equals(commercePriceEntry.getUuid())) {
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

			sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				list = (List<CommercePriceEntry>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
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
	 * Returns the first commerce price entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByUuid_First(
			String uuid,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByUuid_First(
			uuid, orderByComparator);

		if (commercePriceEntry != null) {
			return commercePriceEntry;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchPriceEntryException(sb.toString());
	}

	/**
	 * Returns the first commerce price entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByUuid_First(
		String uuid, OrderByComparator<CommercePriceEntry> orderByComparator) {

		List<CommercePriceEntry> list = findByUuid(
			uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByUuid_Last(
			String uuid,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByUuid_Last(
			uuid, orderByComparator);

		if (commercePriceEntry != null) {
			return commercePriceEntry;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchPriceEntryException(sb.toString());
	}

	/**
	 * Returns the last commerce price entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByUuid_Last(
		String uuid, OrderByComparator<CommercePriceEntry> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<CommercePriceEntry> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price entries before and after the current commerce price entry in the ordered set where uuid = &#63;.
	 *
	 * @param commercePriceEntryId the primary key of the current commerce price entry
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price entry
	 * @throws NoSuchPriceEntryException if a commerce price entry with the primary key could not be found
	 */
	@Override
	public CommercePriceEntry[] findByUuid_PrevAndNext(
			long commercePriceEntryId, String uuid,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		uuid = Objects.toString(uuid, "");

		CommercePriceEntry commercePriceEntry = findByPrimaryKey(
			commercePriceEntryId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceEntry[] array = new CommercePriceEntryImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, commercePriceEntry, uuid, orderByComparator, true);

			array[1] = commercePriceEntry;

			array[2] = getByUuid_PrevAndNext(
				session, commercePriceEntry, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommercePriceEntry getByUuid_PrevAndNext(
		Session session, CommercePriceEntry commercePriceEntry, String uuid,
		OrderByComparator<CommercePriceEntry> orderByComparator,
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

		sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2);
		}

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
			sb.append(CommercePriceEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceEntry)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceEntry> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price entries where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (CommercePriceEntry commercePriceEntry :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(commercePriceEntry);
		}
	}

	/**
	 * Returns the number of commerce price entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching commerce price entries
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMERCEPRICEENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
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

	private static final String _FINDER_COLUMN_UUID_UUID_2 =
		"commercePriceEntry.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(commercePriceEntry.uuid IS NULL OR commercePriceEntry.uuid = '')";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the commerce price entries where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByUuid_C(String uuid, long companyId) {
		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @return the range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<CommercePriceEntry> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid_C;
				finderArgs = new Object[] {uuid, companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid_C;
			finderArgs = new Object[] {
				uuid, companyId, start, end, orderByComparator
			};
		}

		List<CommercePriceEntry> list = null;

		if (useFinderCache) {
			list = (List<CommercePriceEntry>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceEntry commercePriceEntry : list) {
					if (!uuid.equals(commercePriceEntry.getUuid()) ||
						(companyId != commercePriceEntry.getCompanyId())) {

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
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				list = (List<CommercePriceEntry>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
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
	 * Returns the first commerce price entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByUuid_C_First(
			uuid, companyId, orderByComparator);

		if (commercePriceEntry != null) {
			return commercePriceEntry;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchPriceEntryException(sb.toString());
	}

	/**
	 * Returns the first commerce price entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		List<CommercePriceEntry> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);

		if (commercePriceEntry != null) {
			return commercePriceEntry;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchPriceEntryException(sb.toString());
	}

	/**
	 * Returns the last commerce price entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<CommercePriceEntry> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price entries before and after the current commerce price entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param commercePriceEntryId the primary key of the current commerce price entry
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price entry
	 * @throws NoSuchPriceEntryException if a commerce price entry with the primary key could not be found
	 */
	@Override
	public CommercePriceEntry[] findByUuid_C_PrevAndNext(
			long commercePriceEntryId, String uuid, long companyId,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		uuid = Objects.toString(uuid, "");

		CommercePriceEntry commercePriceEntry = findByPrimaryKey(
			commercePriceEntryId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceEntry[] array = new CommercePriceEntryImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, commercePriceEntry, uuid, companyId, orderByComparator,
				true);

			array[1] = commercePriceEntry;

			array[2] = getByUuid_C_PrevAndNext(
				session, commercePriceEntry, uuid, companyId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommercePriceEntry getByUuid_C_PrevAndNext(
		Session session, CommercePriceEntry commercePriceEntry, String uuid,
		long companyId, OrderByComparator<CommercePriceEntry> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

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
			sb.append(CommercePriceEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceEntry)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceEntry> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price entries where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (CommercePriceEntry commercePriceEntry :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(commercePriceEntry);
		}
	}

	/**
	 * Returns the number of commerce price entries where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching commerce price entries
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid_C;

		Object[] finderArgs = new Object[] {uuid, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMERCEPRICEENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
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

	private static final String _FINDER_COLUMN_UUID_C_UUID_2 =
		"commercePriceEntry.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(commercePriceEntry.uuid IS NULL OR commercePriceEntry.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"commercePriceEntry.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByCompanyId;
	private FinderPath _finderPathWithoutPaginationFindByCompanyId;
	private FinderPath _finderPathCountByCompanyId;

	/**
	 * Returns all the commerce price entries where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByCompanyId(long companyId) {
		return findByCompanyId(
			companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price entries where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @return the range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByCompanyId(
		long companyId, int start, int end) {

		return findByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price entries where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		return findByCompanyId(companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price entries where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<CommercePriceEntry> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByCompanyId;
				finderArgs = new Object[] {companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCompanyId;
			finderArgs = new Object[] {
				companyId, start, end, orderByComparator
			};
		}

		List<CommercePriceEntry> list = null;

		if (useFinderCache) {
			list = (List<CommercePriceEntry>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceEntry commercePriceEntry : list) {
					if (companyId != commercePriceEntry.getCompanyId()) {
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

			sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				list = (List<CommercePriceEntry>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
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
	 * Returns the first commerce price entry in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByCompanyId_First(
			long companyId,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByCompanyId_First(
			companyId, orderByComparator);

		if (commercePriceEntry != null) {
			return commercePriceEntry;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchPriceEntryException(sb.toString());
	}

	/**
	 * Returns the first commerce price entry in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByCompanyId_First(
		long companyId,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		List<CommercePriceEntry> list = findByCompanyId(
			companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price entry in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByCompanyId_Last(
			long companyId,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByCompanyId_Last(
			companyId, orderByComparator);

		if (commercePriceEntry != null) {
			return commercePriceEntry;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchPriceEntryException(sb.toString());
	}

	/**
	 * Returns the last commerce price entry in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByCompanyId_Last(
		long companyId,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		int count = countByCompanyId(companyId);

		if (count == 0) {
			return null;
		}

		List<CommercePriceEntry> list = findByCompanyId(
			companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price entries before and after the current commerce price entry in the ordered set where companyId = &#63;.
	 *
	 * @param commercePriceEntryId the primary key of the current commerce price entry
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price entry
	 * @throws NoSuchPriceEntryException if a commerce price entry with the primary key could not be found
	 */
	@Override
	public CommercePriceEntry[] findByCompanyId_PrevAndNext(
			long commercePriceEntryId, long companyId,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = findByPrimaryKey(
			commercePriceEntryId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceEntry[] array = new CommercePriceEntryImpl[3];

			array[0] = getByCompanyId_PrevAndNext(
				session, commercePriceEntry, companyId, orderByComparator,
				true);

			array[1] = commercePriceEntry;

			array[2] = getByCompanyId_PrevAndNext(
				session, commercePriceEntry, companyId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommercePriceEntry getByCompanyId_PrevAndNext(
		Session session, CommercePriceEntry commercePriceEntry, long companyId,
		OrderByComparator<CommercePriceEntry> orderByComparator,
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

		sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

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
			sb.append(CommercePriceEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceEntry)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceEntry> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price entries where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	@Override
	public void removeByCompanyId(long companyId) {
		for (CommercePriceEntry commercePriceEntry :
				findByCompanyId(
					companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(commercePriceEntry);
		}
	}

	/**
	 * Returns the number of commerce price entries where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching commerce price entries
	 */
	@Override
	public int countByCompanyId(long companyId) {
		FinderPath finderPath = _finderPathCountByCompanyId;

		Object[] finderArgs = new Object[] {companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMERCEPRICEENTRY_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
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

	private static final String _FINDER_COLUMN_COMPANYID_COMPANYID_2 =
		"commercePriceEntry.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByCommercePriceListId;
	private FinderPath _finderPathWithoutPaginationFindByCommercePriceListId;
	private FinderPath _finderPathCountByCommercePriceListId;

	/**
	 * Returns all the commerce price entries where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @return the matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByCommercePriceListId(
		long commercePriceListId) {

		return findByCommercePriceListId(
			commercePriceListId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price entries where commercePriceListId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @return the range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByCommercePriceListId(
		long commercePriceListId, int start, int end) {

		return findByCommercePriceListId(commercePriceListId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price entries where commercePriceListId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByCommercePriceListId(
		long commercePriceListId, int start, int end,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		return findByCommercePriceListId(
			commercePriceListId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price entries where commercePriceListId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByCommercePriceListId(
		long commercePriceListId, int start, int end,
		OrderByComparator<CommercePriceEntry> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByCommercePriceListId;
				finderArgs = new Object[] {commercePriceListId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCommercePriceListId;
			finderArgs = new Object[] {
				commercePriceListId, start, end, orderByComparator
			};
		}

		List<CommercePriceEntry> list = null;

		if (useFinderCache) {
			list = (List<CommercePriceEntry>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceEntry commercePriceEntry : list) {
					if (commercePriceListId !=
							commercePriceEntry.getCommercePriceListId()) {

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

			sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

			sb.append(_FINDER_COLUMN_COMMERCEPRICELISTID_COMMERCEPRICELISTID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commercePriceListId);

				list = (List<CommercePriceEntry>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
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
	 * Returns the first commerce price entry in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByCommercePriceListId_First(
			long commercePriceListId,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry =
			fetchByCommercePriceListId_First(
				commercePriceListId, orderByComparator);

		if (commercePriceEntry != null) {
			return commercePriceEntry;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("commercePriceListId=");
		sb.append(commercePriceListId);

		sb.append("}");

		throw new NoSuchPriceEntryException(sb.toString());
	}

	/**
	 * Returns the first commerce price entry in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByCommercePriceListId_First(
		long commercePriceListId,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		List<CommercePriceEntry> list = findByCommercePriceListId(
			commercePriceListId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price entry in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByCommercePriceListId_Last(
			long commercePriceListId,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByCommercePriceListId_Last(
			commercePriceListId, orderByComparator);

		if (commercePriceEntry != null) {
			return commercePriceEntry;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("commercePriceListId=");
		sb.append(commercePriceListId);

		sb.append("}");

		throw new NoSuchPriceEntryException(sb.toString());
	}

	/**
	 * Returns the last commerce price entry in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByCommercePriceListId_Last(
		long commercePriceListId,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		int count = countByCommercePriceListId(commercePriceListId);

		if (count == 0) {
			return null;
		}

		List<CommercePriceEntry> list = findByCommercePriceListId(
			commercePriceListId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price entries before and after the current commerce price entry in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceEntryId the primary key of the current commerce price entry
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price entry
	 * @throws NoSuchPriceEntryException if a commerce price entry with the primary key could not be found
	 */
	@Override
	public CommercePriceEntry[] findByCommercePriceListId_PrevAndNext(
			long commercePriceEntryId, long commercePriceListId,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = findByPrimaryKey(
			commercePriceEntryId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceEntry[] array = new CommercePriceEntryImpl[3];

			array[0] = getByCommercePriceListId_PrevAndNext(
				session, commercePriceEntry, commercePriceListId,
				orderByComparator, true);

			array[1] = commercePriceEntry;

			array[2] = getByCommercePriceListId_PrevAndNext(
				session, commercePriceEntry, commercePriceListId,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommercePriceEntry getByCommercePriceListId_PrevAndNext(
		Session session, CommercePriceEntry commercePriceEntry,
		long commercePriceListId,
		OrderByComparator<CommercePriceEntry> orderByComparator,
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

		sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

		sb.append(_FINDER_COLUMN_COMMERCEPRICELISTID_COMMERCEPRICELISTID_2);

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
			sb.append(CommercePriceEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(commercePriceListId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceEntry)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceEntry> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price entries where commercePriceListId = &#63; from the database.
	 *
	 * @param commercePriceListId the commerce price list ID
	 */
	@Override
	public void removeByCommercePriceListId(long commercePriceListId) {
		for (CommercePriceEntry commercePriceEntry :
				findByCommercePriceListId(
					commercePriceListId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(commercePriceEntry);
		}
	}

	/**
	 * Returns the number of commerce price entries where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @return the number of matching commerce price entries
	 */
	@Override
	public int countByCommercePriceListId(long commercePriceListId) {
		FinderPath finderPath = _finderPathCountByCommercePriceListId;

		Object[] finderArgs = new Object[] {commercePriceListId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMERCEPRICEENTRY_WHERE);

			sb.append(_FINDER_COLUMN_COMMERCEPRICELISTID_COMMERCEPRICELISTID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commercePriceListId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
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

	private static final String
		_FINDER_COLUMN_COMMERCEPRICELISTID_COMMERCEPRICELISTID_2 =
			"commercePriceEntry.commercePriceListId = ?";

	private FinderPath _finderPathWithPaginationFindByCPInstanceUuid;
	private FinderPath _finderPathWithoutPaginationFindByCPInstanceUuid;
	private FinderPath _finderPathCountByCPInstanceUuid;

	/**
	 * Returns all the commerce price entries where CPInstanceUuid = &#63;.
	 *
	 * @param CPInstanceUuid the cp instance uuid
	 * @return the matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByCPInstanceUuid(
		String CPInstanceUuid) {

		return findByCPInstanceUuid(
			CPInstanceUuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price entries where CPInstanceUuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param CPInstanceUuid the cp instance uuid
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @return the range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByCPInstanceUuid(
		String CPInstanceUuid, int start, int end) {

		return findByCPInstanceUuid(CPInstanceUuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price entries where CPInstanceUuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param CPInstanceUuid the cp instance uuid
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByCPInstanceUuid(
		String CPInstanceUuid, int start, int end,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		return findByCPInstanceUuid(
			CPInstanceUuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price entries where CPInstanceUuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param CPInstanceUuid the cp instance uuid
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findByCPInstanceUuid(
		String CPInstanceUuid, int start, int end,
		OrderByComparator<CommercePriceEntry> orderByComparator,
		boolean useFinderCache) {

		CPInstanceUuid = Objects.toString(CPInstanceUuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByCPInstanceUuid;
				finderArgs = new Object[] {CPInstanceUuid};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCPInstanceUuid;
			finderArgs = new Object[] {
				CPInstanceUuid, start, end, orderByComparator
			};
		}

		List<CommercePriceEntry> list = null;

		if (useFinderCache) {
			list = (List<CommercePriceEntry>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceEntry commercePriceEntry : list) {
					if (!CPInstanceUuid.equals(
							commercePriceEntry.getCPInstanceUuid())) {

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

			sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

			boolean bindCPInstanceUuid = false;

			if (CPInstanceUuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_CPINSTANCEUUID_CPINSTANCEUUID_3);
			}
			else {
				bindCPInstanceUuid = true;

				sb.append(_FINDER_COLUMN_CPINSTANCEUUID_CPINSTANCEUUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(CommercePriceEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindCPInstanceUuid) {
					queryPos.add(CPInstanceUuid);
				}

				list = (List<CommercePriceEntry>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
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
	 * Returns the first commerce price entry in the ordered set where CPInstanceUuid = &#63;.
	 *
	 * @param CPInstanceUuid the cp instance uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByCPInstanceUuid_First(
			String CPInstanceUuid,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByCPInstanceUuid_First(
			CPInstanceUuid, orderByComparator);

		if (commercePriceEntry != null) {
			return commercePriceEntry;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("CPInstanceUuid=");
		sb.append(CPInstanceUuid);

		sb.append("}");

		throw new NoSuchPriceEntryException(sb.toString());
	}

	/**
	 * Returns the first commerce price entry in the ordered set where CPInstanceUuid = &#63;.
	 *
	 * @param CPInstanceUuid the cp instance uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByCPInstanceUuid_First(
		String CPInstanceUuid,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		List<CommercePriceEntry> list = findByCPInstanceUuid(
			CPInstanceUuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price entry in the ordered set where CPInstanceUuid = &#63;.
	 *
	 * @param CPInstanceUuid the cp instance uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByCPInstanceUuid_Last(
			String CPInstanceUuid,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByCPInstanceUuid_Last(
			CPInstanceUuid, orderByComparator);

		if (commercePriceEntry != null) {
			return commercePriceEntry;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("CPInstanceUuid=");
		sb.append(CPInstanceUuid);

		sb.append("}");

		throw new NoSuchPriceEntryException(sb.toString());
	}

	/**
	 * Returns the last commerce price entry in the ordered set where CPInstanceUuid = &#63;.
	 *
	 * @param CPInstanceUuid the cp instance uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByCPInstanceUuid_Last(
		String CPInstanceUuid,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		int count = countByCPInstanceUuid(CPInstanceUuid);

		if (count == 0) {
			return null;
		}

		List<CommercePriceEntry> list = findByCPInstanceUuid(
			CPInstanceUuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price entries before and after the current commerce price entry in the ordered set where CPInstanceUuid = &#63;.
	 *
	 * @param commercePriceEntryId the primary key of the current commerce price entry
	 * @param CPInstanceUuid the cp instance uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price entry
	 * @throws NoSuchPriceEntryException if a commerce price entry with the primary key could not be found
	 */
	@Override
	public CommercePriceEntry[] findByCPInstanceUuid_PrevAndNext(
			long commercePriceEntryId, String CPInstanceUuid,
			OrderByComparator<CommercePriceEntry> orderByComparator)
		throws NoSuchPriceEntryException {

		CPInstanceUuid = Objects.toString(CPInstanceUuid, "");

		CommercePriceEntry commercePriceEntry = findByPrimaryKey(
			commercePriceEntryId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceEntry[] array = new CommercePriceEntryImpl[3];

			array[0] = getByCPInstanceUuid_PrevAndNext(
				session, commercePriceEntry, CPInstanceUuid, orderByComparator,
				true);

			array[1] = commercePriceEntry;

			array[2] = getByCPInstanceUuid_PrevAndNext(
				session, commercePriceEntry, CPInstanceUuid, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommercePriceEntry getByCPInstanceUuid_PrevAndNext(
		Session session, CommercePriceEntry commercePriceEntry,
		String CPInstanceUuid,
		OrderByComparator<CommercePriceEntry> orderByComparator,
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

		sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

		boolean bindCPInstanceUuid = false;

		if (CPInstanceUuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_CPINSTANCEUUID_CPINSTANCEUUID_3);
		}
		else {
			bindCPInstanceUuid = true;

			sb.append(_FINDER_COLUMN_CPINSTANCEUUID_CPINSTANCEUUID_2);
		}

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
			sb.append(CommercePriceEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindCPInstanceUuid) {
			queryPos.add(CPInstanceUuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceEntry)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceEntry> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price entries where CPInstanceUuid = &#63; from the database.
	 *
	 * @param CPInstanceUuid the cp instance uuid
	 */
	@Override
	public void removeByCPInstanceUuid(String CPInstanceUuid) {
		for (CommercePriceEntry commercePriceEntry :
				findByCPInstanceUuid(
					CPInstanceUuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(commercePriceEntry);
		}
	}

	/**
	 * Returns the number of commerce price entries where CPInstanceUuid = &#63;.
	 *
	 * @param CPInstanceUuid the cp instance uuid
	 * @return the number of matching commerce price entries
	 */
	@Override
	public int countByCPInstanceUuid(String CPInstanceUuid) {
		CPInstanceUuid = Objects.toString(CPInstanceUuid, "");

		FinderPath finderPath = _finderPathCountByCPInstanceUuid;

		Object[] finderArgs = new Object[] {CPInstanceUuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_COMMERCEPRICEENTRY_WHERE);

			boolean bindCPInstanceUuid = false;

			if (CPInstanceUuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_CPINSTANCEUUID_CPINSTANCEUUID_3);
			}
			else {
				bindCPInstanceUuid = true;

				sb.append(_FINDER_COLUMN_CPINSTANCEUUID_CPINSTANCEUUID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindCPInstanceUuid) {
					queryPos.add(CPInstanceUuid);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
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

	private static final String _FINDER_COLUMN_CPINSTANCEUUID_CPINSTANCEUUID_2 =
		"commercePriceEntry.CPInstanceUuid = ?";

	private static final String _FINDER_COLUMN_CPINSTANCEUUID_CPINSTANCEUUID_3 =
		"(commercePriceEntry.CPInstanceUuid IS NULL OR commercePriceEntry.CPInstanceUuid = '')";

	private FinderPath _finderPathFetchByC_C;
	private FinderPath _finderPathCountByC_C;

	/**
	 * Returns the commerce price entry where commercePriceListId = &#63; and CPInstanceUuid = &#63; or throws a <code>NoSuchPriceEntryException</code> if it could not be found.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param CPInstanceUuid the cp instance uuid
	 * @return the matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByC_C(
			long commercePriceListId, String CPInstanceUuid)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByC_C(
			commercePriceListId, CPInstanceUuid);

		if (commercePriceEntry == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("commercePriceListId=");
			sb.append(commercePriceListId);

			sb.append(", CPInstanceUuid=");
			sb.append(CPInstanceUuid);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchPriceEntryException(sb.toString());
		}

		return commercePriceEntry;
	}

	/**
	 * Returns the commerce price entry where commercePriceListId = &#63; and CPInstanceUuid = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param CPInstanceUuid the cp instance uuid
	 * @return the matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByC_C(
		long commercePriceListId, String CPInstanceUuid) {

		return fetchByC_C(commercePriceListId, CPInstanceUuid, true);
	}

	/**
	 * Returns the commerce price entry where commercePriceListId = &#63; and CPInstanceUuid = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param CPInstanceUuid the cp instance uuid
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByC_C(
		long commercePriceListId, String CPInstanceUuid,
		boolean useFinderCache) {

		CPInstanceUuid = Objects.toString(CPInstanceUuid, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {commercePriceListId, CPInstanceUuid};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(_finderPathFetchByC_C, finderArgs);
		}

		if (result instanceof CommercePriceEntry) {
			CommercePriceEntry commercePriceEntry = (CommercePriceEntry)result;

			if ((commercePriceListId !=
					commercePriceEntry.getCommercePriceListId()) ||
				!Objects.equals(
					CPInstanceUuid, commercePriceEntry.getCPInstanceUuid())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

			sb.append(_FINDER_COLUMN_C_C_COMMERCEPRICELISTID_2);

			boolean bindCPInstanceUuid = false;

			if (CPInstanceUuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_C_CPINSTANCEUUID_3);
			}
			else {
				bindCPInstanceUuid = true;

				sb.append(_FINDER_COLUMN_C_C_CPINSTANCEUUID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commercePriceListId);

				if (bindCPInstanceUuid) {
					queryPos.add(CPInstanceUuid);
				}

				List<CommercePriceEntry> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByC_C, finderArgs, list);
					}
				}
				else {
					CommercePriceEntry commercePriceEntry = list.get(0);

					result = commercePriceEntry;

					cacheResult(commercePriceEntry);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (CommercePriceEntry)result;
		}
	}

	/**
	 * Removes the commerce price entry where commercePriceListId = &#63; and CPInstanceUuid = &#63; from the database.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param CPInstanceUuid the cp instance uuid
	 * @return the commerce price entry that was removed
	 */
	@Override
	public CommercePriceEntry removeByC_C(
			long commercePriceListId, String CPInstanceUuid)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = findByC_C(
			commercePriceListId, CPInstanceUuid);

		return remove(commercePriceEntry);
	}

	/**
	 * Returns the number of commerce price entries where commercePriceListId = &#63; and CPInstanceUuid = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param CPInstanceUuid the cp instance uuid
	 * @return the number of matching commerce price entries
	 */
	@Override
	public int countByC_C(long commercePriceListId, String CPInstanceUuid) {
		CPInstanceUuid = Objects.toString(CPInstanceUuid, "");

		FinderPath finderPath = _finderPathCountByC_C;

		Object[] finderArgs = new Object[] {
			commercePriceListId, CPInstanceUuid
		};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMERCEPRICEENTRY_WHERE);

			sb.append(_FINDER_COLUMN_C_C_COMMERCEPRICELISTID_2);

			boolean bindCPInstanceUuid = false;

			if (CPInstanceUuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_C_CPINSTANCEUUID_3);
			}
			else {
				bindCPInstanceUuid = true;

				sb.append(_FINDER_COLUMN_C_C_CPINSTANCEUUID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commercePriceListId);

				if (bindCPInstanceUuid) {
					queryPos.add(CPInstanceUuid);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
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

	private static final String _FINDER_COLUMN_C_C_COMMERCEPRICELISTID_2 =
		"commercePriceEntry.commercePriceListId = ? AND ";

	private static final String _FINDER_COLUMN_C_C_CPINSTANCEUUID_2 =
		"commercePriceEntry.CPInstanceUuid = ?";

	private static final String _FINDER_COLUMN_C_C_CPINSTANCEUUID_3 =
		"(commercePriceEntry.CPInstanceUuid IS NULL OR commercePriceEntry.CPInstanceUuid = '')";

	private FinderPath _finderPathFetchByC_C_S;
	private FinderPath _finderPathCountByC_C_S;

	/**
	 * Returns the commerce price entry where commercePriceListId = &#63; and CPInstanceUuid = &#63; and status = &#63; or throws a <code>NoSuchPriceEntryException</code> if it could not be found.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param CPInstanceUuid the cp instance uuid
	 * @param status the status
	 * @return the matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByC_C_S(
			long commercePriceListId, String CPInstanceUuid, int status)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByC_C_S(
			commercePriceListId, CPInstanceUuid, status);

		if (commercePriceEntry == null) {
			StringBundler sb = new StringBundler(8);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("commercePriceListId=");
			sb.append(commercePriceListId);

			sb.append(", CPInstanceUuid=");
			sb.append(CPInstanceUuid);

			sb.append(", status=");
			sb.append(status);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchPriceEntryException(sb.toString());
		}

		return commercePriceEntry;
	}

	/**
	 * Returns the commerce price entry where commercePriceListId = &#63; and CPInstanceUuid = &#63; and status = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param CPInstanceUuid the cp instance uuid
	 * @param status the status
	 * @return the matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByC_C_S(
		long commercePriceListId, String CPInstanceUuid, int status) {

		return fetchByC_C_S(commercePriceListId, CPInstanceUuid, status, true);
	}

	/**
	 * Returns the commerce price entry where commercePriceListId = &#63; and CPInstanceUuid = &#63; and status = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param CPInstanceUuid the cp instance uuid
	 * @param status the status
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByC_C_S(
		long commercePriceListId, String CPInstanceUuid, int status,
		boolean useFinderCache) {

		CPInstanceUuid = Objects.toString(CPInstanceUuid, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {
				commercePriceListId, CPInstanceUuid, status
			};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(_finderPathFetchByC_C_S, finderArgs);
		}

		if (result instanceof CommercePriceEntry) {
			CommercePriceEntry commercePriceEntry = (CommercePriceEntry)result;

			if ((commercePriceListId !=
					commercePriceEntry.getCommercePriceListId()) ||
				!Objects.equals(
					CPInstanceUuid, commercePriceEntry.getCPInstanceUuid()) ||
				(status != commercePriceEntry.getStatus())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(5);

			sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

			sb.append(_FINDER_COLUMN_C_C_S_COMMERCEPRICELISTID_2);

			boolean bindCPInstanceUuid = false;

			if (CPInstanceUuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_C_S_CPINSTANCEUUID_3);
			}
			else {
				bindCPInstanceUuid = true;

				sb.append(_FINDER_COLUMN_C_C_S_CPINSTANCEUUID_2);
			}

			sb.append(_FINDER_COLUMN_C_C_S_STATUS_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commercePriceListId);

				if (bindCPInstanceUuid) {
					queryPos.add(CPInstanceUuid);
				}

				queryPos.add(status);

				List<CommercePriceEntry> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByC_C_S, finderArgs, list);
					}
				}
				else {
					CommercePriceEntry commercePriceEntry = list.get(0);

					result = commercePriceEntry;

					cacheResult(commercePriceEntry);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (CommercePriceEntry)result;
		}
	}

	/**
	 * Removes the commerce price entry where commercePriceListId = &#63; and CPInstanceUuid = &#63; and status = &#63; from the database.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param CPInstanceUuid the cp instance uuid
	 * @param status the status
	 * @return the commerce price entry that was removed
	 */
	@Override
	public CommercePriceEntry removeByC_C_S(
			long commercePriceListId, String CPInstanceUuid, int status)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = findByC_C_S(
			commercePriceListId, CPInstanceUuid, status);

		return remove(commercePriceEntry);
	}

	/**
	 * Returns the number of commerce price entries where commercePriceListId = &#63; and CPInstanceUuid = &#63; and status = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param CPInstanceUuid the cp instance uuid
	 * @param status the status
	 * @return the number of matching commerce price entries
	 */
	@Override
	public int countByC_C_S(
		long commercePriceListId, String CPInstanceUuid, int status) {

		CPInstanceUuid = Objects.toString(CPInstanceUuid, "");

		FinderPath finderPath = _finderPathCountByC_C_S;

		Object[] finderArgs = new Object[] {
			commercePriceListId, CPInstanceUuid, status
		};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_COMMERCEPRICEENTRY_WHERE);

			sb.append(_FINDER_COLUMN_C_C_S_COMMERCEPRICELISTID_2);

			boolean bindCPInstanceUuid = false;

			if (CPInstanceUuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_C_S_CPINSTANCEUUID_3);
			}
			else {
				bindCPInstanceUuid = true;

				sb.append(_FINDER_COLUMN_C_C_S_CPINSTANCEUUID_2);
			}

			sb.append(_FINDER_COLUMN_C_C_S_STATUS_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(commercePriceListId);

				if (bindCPInstanceUuid) {
					queryPos.add(CPInstanceUuid);
				}

				queryPos.add(status);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
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

	private static final String _FINDER_COLUMN_C_C_S_COMMERCEPRICELISTID_2 =
		"commercePriceEntry.commercePriceListId = ? AND ";

	private static final String _FINDER_COLUMN_C_C_S_CPINSTANCEUUID_2 =
		"commercePriceEntry.CPInstanceUuid = ? AND ";

	private static final String _FINDER_COLUMN_C_C_S_CPINSTANCEUUID_3 =
		"(commercePriceEntry.CPInstanceUuid IS NULL OR commercePriceEntry.CPInstanceUuid = '') AND ";

	private static final String _FINDER_COLUMN_C_C_S_STATUS_2 =
		"commercePriceEntry.status = ?";

	private FinderPath _finderPathFetchByC_ERC;
	private FinderPath _finderPathCountByC_ERC;

	/**
	 * Returns the commerce price entry where companyId = &#63; and externalReferenceCode = &#63; or throws a <code>NoSuchPriceEntryException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching commerce price entry
	 * @throws NoSuchPriceEntryException if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry findByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByC_ERC(
			companyId, externalReferenceCode);

		if (commercePriceEntry == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("companyId=");
			sb.append(companyId);

			sb.append(", externalReferenceCode=");
			sb.append(externalReferenceCode);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchPriceEntryException(sb.toString());
		}

		return commercePriceEntry;
	}

	/**
	 * Returns the commerce price entry where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByC_ERC(
		long companyId, String externalReferenceCode) {

		return fetchByC_ERC(companyId, externalReferenceCode, true);
	}

	/**
	 * Returns the commerce price entry where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce price entry, or <code>null</code> if a matching commerce price entry could not be found
	 */
	@Override
	public CommercePriceEntry fetchByC_ERC(
		long companyId, String externalReferenceCode, boolean useFinderCache) {

		externalReferenceCode = Objects.toString(externalReferenceCode, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {companyId, externalReferenceCode};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(_finderPathFetchByC_ERC, finderArgs);
		}

		if (result instanceof CommercePriceEntry) {
			CommercePriceEntry commercePriceEntry = (CommercePriceEntry)result;

			if ((companyId != commercePriceEntry.getCompanyId()) ||
				!Objects.equals(
					externalReferenceCode,
					commercePriceEntry.getExternalReferenceCode())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_COMMERCEPRICEENTRY_WHERE);

			sb.append(_FINDER_COLUMN_C_ERC_COMPANYID_2);

			boolean bindExternalReferenceCode = false;

			if (externalReferenceCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_3);
			}
			else {
				bindExternalReferenceCode = true;

				sb.append(_FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindExternalReferenceCode) {
					queryPos.add(externalReferenceCode);
				}

				List<CommercePriceEntry> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByC_ERC, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {
									companyId, externalReferenceCode
								};
							}

							_log.warn(
								"CommercePriceEntryPersistenceImpl.fetchByC_ERC(long, String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					CommercePriceEntry commercePriceEntry = list.get(0);

					result = commercePriceEntry;

					cacheResult(commercePriceEntry);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (CommercePriceEntry)result;
		}
	}

	/**
	 * Removes the commerce price entry where companyId = &#63; and externalReferenceCode = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the commerce price entry that was removed
	 */
	@Override
	public CommercePriceEntry removeByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = findByC_ERC(
			companyId, externalReferenceCode);

		return remove(commercePriceEntry);
	}

	/**
	 * Returns the number of commerce price entries where companyId = &#63; and externalReferenceCode = &#63;.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the number of matching commerce price entries
	 */
	@Override
	public int countByC_ERC(long companyId, String externalReferenceCode) {
		externalReferenceCode = Objects.toString(externalReferenceCode, "");

		FinderPath finderPath = _finderPathCountByC_ERC;

		Object[] finderArgs = new Object[] {companyId, externalReferenceCode};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_COMMERCEPRICEENTRY_WHERE);

			sb.append(_FINDER_COLUMN_C_ERC_COMPANYID_2);

			boolean bindExternalReferenceCode = false;

			if (externalReferenceCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_3);
			}
			else {
				bindExternalReferenceCode = true;

				sb.append(_FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindExternalReferenceCode) {
					queryPos.add(externalReferenceCode);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
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

	private static final String _FINDER_COLUMN_C_ERC_COMPANYID_2 =
		"commercePriceEntry.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_2 =
		"commercePriceEntry.externalReferenceCode = ?";

	private static final String _FINDER_COLUMN_C_ERC_EXTERNALREFERENCECODE_3 =
		"(commercePriceEntry.externalReferenceCode IS NULL OR commercePriceEntry.externalReferenceCode = '')";

	public CommercePriceEntryPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

		setDBColumnNames(dbColumnNames);

		setModelClass(CommercePriceEntry.class);

		setModelImplClass(CommercePriceEntryImpl.class);
		setModelPKClass(long.class);

		setTable(CommercePriceEntryTable.INSTANCE);
	}

	/**
	 * Caches the commerce price entry in the entity cache if it is enabled.
	 *
	 * @param commercePriceEntry the commerce price entry
	 */
	@Override
	public void cacheResult(CommercePriceEntry commercePriceEntry) {
		entityCache.putResult(
			CommercePriceEntryImpl.class, commercePriceEntry.getPrimaryKey(),
			commercePriceEntry);

		finderCache.putResult(
			_finderPathFetchByC_C,
			new Object[] {
				commercePriceEntry.getCommercePriceListId(),
				commercePriceEntry.getCPInstanceUuid()
			},
			commercePriceEntry);

		finderCache.putResult(
			_finderPathFetchByC_C_S,
			new Object[] {
				commercePriceEntry.getCommercePriceListId(),
				commercePriceEntry.getCPInstanceUuid(),
				commercePriceEntry.getStatus()
			},
			commercePriceEntry);

		finderCache.putResult(
			_finderPathFetchByC_ERC,
			new Object[] {
				commercePriceEntry.getCompanyId(),
				commercePriceEntry.getExternalReferenceCode()
			},
			commercePriceEntry);
	}

	/**
	 * Caches the commerce price entries in the entity cache if it is enabled.
	 *
	 * @param commercePriceEntries the commerce price entries
	 */
	@Override
	public void cacheResult(List<CommercePriceEntry> commercePriceEntries) {
		for (CommercePriceEntry commercePriceEntry : commercePriceEntries) {
			if (entityCache.getResult(
					CommercePriceEntryImpl.class,
					commercePriceEntry.getPrimaryKey()) == null) {

				cacheResult(commercePriceEntry);
			}
		}
	}

	/**
	 * Clears the cache for all commerce price entries.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(CommercePriceEntryImpl.class);

		finderCache.clearCache(CommercePriceEntryImpl.class);
	}

	/**
	 * Clears the cache for the commerce price entry.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(CommercePriceEntry commercePriceEntry) {
		entityCache.removeResult(
			CommercePriceEntryImpl.class, commercePriceEntry);
	}

	@Override
	public void clearCache(List<CommercePriceEntry> commercePriceEntries) {
		for (CommercePriceEntry commercePriceEntry : commercePriceEntries) {
			entityCache.removeResult(
				CommercePriceEntryImpl.class, commercePriceEntry);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(CommercePriceEntryImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(CommercePriceEntryImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		CommercePriceEntryModelImpl commercePriceEntryModelImpl) {

		Object[] args = new Object[] {
			commercePriceEntryModelImpl.getCommercePriceListId(),
			commercePriceEntryModelImpl.getCPInstanceUuid()
		};

		finderCache.putResult(_finderPathCountByC_C, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByC_C, args, commercePriceEntryModelImpl);

		args = new Object[] {
			commercePriceEntryModelImpl.getCommercePriceListId(),
			commercePriceEntryModelImpl.getCPInstanceUuid(),
			commercePriceEntryModelImpl.getStatus()
		};

		finderCache.putResult(_finderPathCountByC_C_S, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByC_C_S, args, commercePriceEntryModelImpl);

		args = new Object[] {
			commercePriceEntryModelImpl.getCompanyId(),
			commercePriceEntryModelImpl.getExternalReferenceCode()
		};

		finderCache.putResult(_finderPathCountByC_ERC, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByC_ERC, args, commercePriceEntryModelImpl);
	}

	/**
	 * Creates a new commerce price entry with the primary key. Does not add the commerce price entry to the database.
	 *
	 * @param commercePriceEntryId the primary key for the new commerce price entry
	 * @return the new commerce price entry
	 */
	@Override
	public CommercePriceEntry create(long commercePriceEntryId) {
		CommercePriceEntry commercePriceEntry = new CommercePriceEntryImpl();

		commercePriceEntry.setNew(true);
		commercePriceEntry.setPrimaryKey(commercePriceEntryId);

		String uuid = PortalUUIDUtil.generate();

		commercePriceEntry.setUuid(uuid);

		commercePriceEntry.setCompanyId(CompanyThreadLocal.getCompanyId());

		return commercePriceEntry;
	}

	/**
	 * Removes the commerce price entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePriceEntryId the primary key of the commerce price entry
	 * @return the commerce price entry that was removed
	 * @throws NoSuchPriceEntryException if a commerce price entry with the primary key could not be found
	 */
	@Override
	public CommercePriceEntry remove(long commercePriceEntryId)
		throws NoSuchPriceEntryException {

		return remove((Serializable)commercePriceEntryId);
	}

	/**
	 * Removes the commerce price entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the commerce price entry
	 * @return the commerce price entry that was removed
	 * @throws NoSuchPriceEntryException if a commerce price entry with the primary key could not be found
	 */
	@Override
	public CommercePriceEntry remove(Serializable primaryKey)
		throws NoSuchPriceEntryException {

		Session session = null;

		try {
			session = openSession();

			CommercePriceEntry commercePriceEntry =
				(CommercePriceEntry)session.get(
					CommercePriceEntryImpl.class, primaryKey);

			if (commercePriceEntry == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchPriceEntryException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(commercePriceEntry);
		}
		catch (NoSuchPriceEntryException noSuchEntityException) {
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
	protected CommercePriceEntry removeImpl(
		CommercePriceEntry commercePriceEntry) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(commercePriceEntry)) {
				commercePriceEntry = (CommercePriceEntry)session.get(
					CommercePriceEntryImpl.class,
					commercePriceEntry.getPrimaryKeyObj());
			}

			if (commercePriceEntry != null) {
				session.delete(commercePriceEntry);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (commercePriceEntry != null) {
			clearCache(commercePriceEntry);
		}

		return commercePriceEntry;
	}

	@Override
	public CommercePriceEntry updateImpl(
		CommercePriceEntry commercePriceEntry) {

		boolean isNew = commercePriceEntry.isNew();

		if (!(commercePriceEntry instanceof CommercePriceEntryModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(commercePriceEntry.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					commercePriceEntry);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in commercePriceEntry proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom CommercePriceEntry implementation " +
					commercePriceEntry.getClass());
		}

		CommercePriceEntryModelImpl commercePriceEntryModelImpl =
			(CommercePriceEntryModelImpl)commercePriceEntry;

		if (Validator.isNull(commercePriceEntry.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			commercePriceEntry.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (commercePriceEntry.getCreateDate() == null)) {
			if (serviceContext == null) {
				commercePriceEntry.setCreateDate(now);
			}
			else {
				commercePriceEntry.setCreateDate(
					serviceContext.getCreateDate(now));
			}
		}

		if (!commercePriceEntryModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				commercePriceEntry.setModifiedDate(now);
			}
			else {
				commercePriceEntry.setModifiedDate(
					serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(commercePriceEntry);
			}
			else {
				commercePriceEntry = (CommercePriceEntry)session.merge(
					commercePriceEntry);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			CommercePriceEntryImpl.class, commercePriceEntryModelImpl, false,
			true);

		cacheUniqueFindersCache(commercePriceEntryModelImpl);

		if (isNew) {
			commercePriceEntry.setNew(false);
		}

		commercePriceEntry.resetOriginalValues();

		return commercePriceEntry;
	}

	/**
	 * Returns the commerce price entry with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce price entry
	 * @return the commerce price entry
	 * @throws NoSuchPriceEntryException if a commerce price entry with the primary key could not be found
	 */
	@Override
	public CommercePriceEntry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchPriceEntryException {

		CommercePriceEntry commercePriceEntry = fetchByPrimaryKey(primaryKey);

		if (commercePriceEntry == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchPriceEntryException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return commercePriceEntry;
	}

	/**
	 * Returns the commerce price entry with the primary key or throws a <code>NoSuchPriceEntryException</code> if it could not be found.
	 *
	 * @param commercePriceEntryId the primary key of the commerce price entry
	 * @return the commerce price entry
	 * @throws NoSuchPriceEntryException if a commerce price entry with the primary key could not be found
	 */
	@Override
	public CommercePriceEntry findByPrimaryKey(long commercePriceEntryId)
		throws NoSuchPriceEntryException {

		return findByPrimaryKey((Serializable)commercePriceEntryId);
	}

	/**
	 * Returns the commerce price entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commercePriceEntryId the primary key of the commerce price entry
	 * @return the commerce price entry, or <code>null</code> if a commerce price entry with the primary key could not be found
	 */
	@Override
	public CommercePriceEntry fetchByPrimaryKey(long commercePriceEntryId) {
		return fetchByPrimaryKey((Serializable)commercePriceEntryId);
	}

	/**
	 * Returns all the commerce price entries.
	 *
	 * @return the commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the commerce price entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @return the range of commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the commerce price entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findAll(
		int start, int end,
		OrderByComparator<CommercePriceEntry> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the commerce price entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce price entries
	 * @param end the upper bound of the range of commerce price entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of commerce price entries
	 */
	@Override
	public List<CommercePriceEntry> findAll(
		int start, int end,
		OrderByComparator<CommercePriceEntry> orderByComparator,
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

		List<CommercePriceEntry> list = null;

		if (useFinderCache) {
			list = (List<CommercePriceEntry>)finderCache.getResult(
				finderPath, finderArgs);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_COMMERCEPRICEENTRY);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_COMMERCEPRICEENTRY;

				sql = sql.concat(CommercePriceEntryModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<CommercePriceEntry>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
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
	 * Removes all the commerce price entries from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (CommercePriceEntry commercePriceEntry : findAll()) {
			remove(commercePriceEntry);
		}
	}

	/**
	 * Returns the number of commerce price entries.
	 *
	 * @return the number of commerce price entries
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(
					_SQL_COUNT_COMMERCEPRICEENTRY);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
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
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "commercePriceEntryId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_COMMERCEPRICEENTRY;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return CommercePriceEntryModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the commerce price entry persistence.
	 */
	public void afterPropertiesSet() {
		Bundle bundle = FrameworkUtil.getBundle(
			CommercePriceEntryPersistenceImpl.class);

		_bundleContext = bundle.getBundleContext();

		_argumentsResolverServiceRegistration = _bundleContext.registerService(
			ArgumentsResolver.class,
			new CommercePriceEntryModelArgumentsResolver(),
			new HashMapDictionary<>());

		_finderPathWithPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"uuid_"}, true);

		_finderPathWithoutPaginationFindByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] {String.class.getName()}, new String[] {"uuid_"},
			true);

		_finderPathCountByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] {String.class.getName()}, new String[] {"uuid_"},
			false);

		_finderPathWithPaginationFindByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C",
			new String[] {
				String.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"uuid_", "companyId"}, true);

		_finderPathWithoutPaginationFindByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "companyId"}, true);

		_finderPathCountByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "companyId"}, false);

		_finderPathWithPaginationFindByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCompanyId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"companyId"}, true);

		_finderPathWithoutPaginationFindByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCompanyId",
			new String[] {Long.class.getName()}, new String[] {"companyId"},
			true);

		_finderPathCountByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCompanyId",
			new String[] {Long.class.getName()}, new String[] {"companyId"},
			false);

		_finderPathWithPaginationFindByCommercePriceListId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCommercePriceListId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"commercePriceListId"}, true);

		_finderPathWithoutPaginationFindByCommercePriceListId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByCommercePriceListId", new String[] {Long.class.getName()},
			new String[] {"commercePriceListId"}, true);

		_finderPathCountByCommercePriceListId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByCommercePriceListId", new String[] {Long.class.getName()},
			new String[] {"commercePriceListId"}, false);

		_finderPathWithPaginationFindByCPInstanceUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCPInstanceUuid",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"CPInstanceUuid"}, true);

		_finderPathWithoutPaginationFindByCPInstanceUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCPInstanceUuid",
			new String[] {String.class.getName()},
			new String[] {"CPInstanceUuid"}, true);

		_finderPathCountByCPInstanceUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCPInstanceUuid",
			new String[] {String.class.getName()},
			new String[] {"CPInstanceUuid"}, false);

		_finderPathFetchByC_C = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByC_C",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"commercePriceListId", "CPInstanceUuid"}, true);

		_finderPathCountByC_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_C",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"commercePriceListId", "CPInstanceUuid"}, false);

		_finderPathFetchByC_C_S = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByC_C_S",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName()
			},
			new String[] {"commercePriceListId", "CPInstanceUuid", "status"},
			true);

		_finderPathCountByC_C_S = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_C_S",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName()
			},
			new String[] {"commercePriceListId", "CPInstanceUuid", "status"},
			false);

		_finderPathFetchByC_ERC = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByC_ERC",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"companyId", "externalReferenceCode"}, true);

		_finderPathCountByC_ERC = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_ERC",
			new String[] {Long.class.getName(), String.class.getName()},
			new String[] {"companyId", "externalReferenceCode"}, false);
	}

	public void destroy() {
		entityCache.removeCache(CommercePriceEntryImpl.class.getName());

		_argumentsResolverServiceRegistration.unregister();
	}

	private BundleContext _bundleContext;

	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;

	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_COMMERCEPRICEENTRY =
		"SELECT commercePriceEntry FROM CommercePriceEntry commercePriceEntry";

	private static final String _SQL_SELECT_COMMERCEPRICEENTRY_WHERE =
		"SELECT commercePriceEntry FROM CommercePriceEntry commercePriceEntry WHERE ";

	private static final String _SQL_COUNT_COMMERCEPRICEENTRY =
		"SELECT COUNT(commercePriceEntry) FROM CommercePriceEntry commercePriceEntry";

	private static final String _SQL_COUNT_COMMERCEPRICEENTRY_WHERE =
		"SELECT COUNT(commercePriceEntry) FROM CommercePriceEntry commercePriceEntry WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "commercePriceEntry.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No CommercePriceEntry exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No CommercePriceEntry exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		CommercePriceEntryPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid"});

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

	private ServiceRegistration<ArgumentsResolver>
		_argumentsResolverServiceRegistration;

	private static class CommercePriceEntryModelArgumentsResolver
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

			CommercePriceEntryModelImpl commercePriceEntryModelImpl =
				(CommercePriceEntryModelImpl)baseModel;

			long columnBitmask = commercePriceEntryModelImpl.getColumnBitmask();

			if (!checkColumn || (columnBitmask == 0)) {
				return _getValue(
					commercePriceEntryModelImpl, columnNames, original);
			}

			Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
				finderPath);

			if (finderPathColumnBitmask == null) {
				finderPathColumnBitmask = 0L;

				for (String columnName : columnNames) {
					finderPathColumnBitmask |=
						commercePriceEntryModelImpl.getColumnBitmask(
							columnName);
				}

				_finderPathColumnBitmasksCache.put(
					finderPath, finderPathColumnBitmask);
			}

			if ((columnBitmask & finderPathColumnBitmask) != 0) {
				return _getValue(
					commercePriceEntryModelImpl, columnNames, original);
			}

			return null;
		}

		@Override
		public String getClassName() {
			return CommercePriceEntryImpl.class.getName();
		}

		@Override
		public String getTableName() {
			return CommercePriceEntryTable.INSTANCE.getTableName();
		}

		private Object[] _getValue(
			CommercePriceEntryModelImpl commercePriceEntryModelImpl,
			String[] columnNames, boolean original) {

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < arguments.length; i++) {
				String columnName = columnNames[i];

				if (original) {
					arguments[i] =
						commercePriceEntryModelImpl.getColumnOriginalValue(
							columnName);
				}
				else {
					arguments[i] = commercePriceEntryModelImpl.getColumnValue(
						columnName);
				}
			}

			return arguments;
		}

		private static Map<FinderPath, Long> _finderPathColumnBitmasksCache =
			new ConcurrentHashMap<>();

	}

}