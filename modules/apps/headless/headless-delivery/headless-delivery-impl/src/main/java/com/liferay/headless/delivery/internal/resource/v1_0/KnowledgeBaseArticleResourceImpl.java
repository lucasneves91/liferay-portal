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

package com.liferay.headless.delivery.internal.resource.v1_0;

import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.headless.common.spi.resource.SPIRatingResource;
import com.liferay.headless.common.spi.service.context.ServiceContextRequestUtil;
import com.liferay.headless.delivery.dto.v1_0.KnowledgeBaseArticle;
import com.liferay.headless.delivery.dto.v1_0.Rating;
import com.liferay.headless.delivery.internal.dto.v1_0.converter.KnowledgeBaseArticleDTOConverter;
import com.liferay.headless.delivery.internal.dto.v1_0.util.CustomFieldsUtil;
import com.liferay.headless.delivery.internal.dto.v1_0.util.EntityFieldsUtil;
import com.liferay.headless.delivery.internal.dto.v1_0.util.RatingUtil;
import com.liferay.headless.delivery.internal.odata.entity.v1_0.KnowledgeBaseArticleEntityModel;
import com.liferay.headless.delivery.internal.search.filter.FilterUtil;
import com.liferay.headless.delivery.internal.search.sort.SortUtil;
import com.liferay.headless.delivery.resource.v1_0.KnowledgeBaseArticleResource;
import com.liferay.knowledge.base.constants.KBPortletKeys;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.model.KBFolder;
import com.liferay.knowledge.base.service.KBArticleService;
import com.liferay.knowledge.base.service.KBFolderService;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.portal.search.sort.Sorts;
import com.liferay.portal.vulcan.aggregation.Aggregation;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.SearchUtil;
import com.liferay.ratings.kernel.service.RatingsEntryLocalService;

import java.io.Serializable;

import java.util.Map;
import java.util.Optional;

import javax.ws.rs.core.MultivaluedMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/knowledge-base-article.properties",
	scope = ServiceScope.PROTOTYPE, service = KnowledgeBaseArticleResource.class
)
public class KnowledgeBaseArticleResourceImpl
	extends BaseKnowledgeBaseArticleResourceImpl
	implements EntityModelResource {

	@Override
	public void deleteKnowledgeBaseArticle(Long knowledgeBaseArticleId)
		throws Exception {

		_kbArticleService.deleteKBArticle(knowledgeBaseArticleId);
	}

	@Override
	public void deleteKnowledgeBaseArticleMyRating(Long knowledgeBaseArticleId)
		throws Exception {

		SPIRatingResource<Rating> spiRatingResource = _getSPIRatingResource();

		spiRatingResource.deleteRating(knowledgeBaseArticleId);
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap) {
		return new KnowledgeBaseArticleEntityModel(
			EntityFieldsUtil.getEntityFields(
				_portal.getClassNameId(KBArticle.class.getName()),
				contextCompany.getCompanyId(), _expandoColumnLocalService,
				_expandoTableLocalService));
	}

	@Override
	public KnowledgeBaseArticle getKnowledgeBaseArticle(
			Long knowledgeBaseArticleId)
		throws Exception {

		return _toKnowledgeBaseArticle(
			_kbArticleService.getLatestKBArticle(
				knowledgeBaseArticleId, WorkflowConstants.STATUS_APPROVED));
	}

	@Override
	public Page<KnowledgeBaseArticle>
			getKnowledgeBaseArticleKnowledgeBaseArticlesPage(
				Long parentKnowledgeBaseArticleId, Boolean flatten,
				String search, Aggregation aggregation, Filter filter,
				Pagination pagination, Sort[] sorts)
		throws Exception {

		KBArticle kbArticle = _kbArticleService.getLatestKBArticle(
			parentKnowledgeBaseArticleId, WorkflowConstants.STATUS_APPROVED);

		return _getKnowledgeBaseArticlesPage(
			HashMapBuilder.put(
				"create",
				addAction(
					"ADD_KB_ARTICLE",
					"postKnowledgeBaseArticleKnowledgeBaseArticle",
					"com.liferay.knowledge.base.admin", kbArticle.getGroupId())
			).put(
				"get",
				addAction(
					"VIEW", "getKnowledgeBaseArticleKnowledgeBaseArticlesPage",
					"com.liferay.knowledge.base.admin", kbArticle.getGroupId())
			).build(),
			booleanQuery -> {
				BooleanFilter booleanFilter =
					booleanQuery.getPreBooleanFilter();

				booleanFilter.add(
					new TermFilter(
						"parentMessageId",
						String.valueOf(kbArticle.getResourcePrimKey())),
					BooleanClauseOccur.MUST);
			},
			kbArticle.getGroupId(), search, aggregation, filter, pagination,
			sorts);
	}

	@Override
	public Rating getKnowledgeBaseArticleMyRating(Long knowledgeBaseArticleId)
		throws Exception {

		SPIRatingResource<Rating> spiRatingResource = _getSPIRatingResource();

		return spiRatingResource.getRating(knowledgeBaseArticleId);
	}

	@Override
	public Page<KnowledgeBaseArticle>
			getKnowledgeBaseFolderKnowledgeBaseArticlesPage(
				Long knowledgeBaseFolderId, Boolean flatten, String search,
				Aggregation aggregation, Filter filter, Pagination pagination,
				Sort[] sorts)
		throws Exception {

		KBFolder kbFolder = _kbFolderService.getKBFolder(knowledgeBaseFolderId);

		return _getKnowledgeBaseArticlesPage(
			HashMapBuilder.put(
				"create",
				addAction(
					"ADD_KB_ARTICLE",
					"postKnowledgeBaseFolderKnowledgeBaseArticle",
					"com.liferay.knowledge.base.admin", kbFolder.getGroupId())
			).put(
				"get",
				addAction(
					"VIEW", "getKnowledgeBaseFolderKnowledgeBaseArticlesPage",
					"com.liferay.knowledge.base.admin", kbFolder.getGroupId())
			).build(),
			booleanQuery -> {
				BooleanFilter booleanFilter =
					booleanQuery.getPreBooleanFilter();

				booleanFilter.add(
					new TermFilter(
						Field.FOLDER_ID,
						String.valueOf(kbFolder.getKbFolderId())),
					BooleanClauseOccur.MUST);

				if (!GetterUtil.getBoolean(flatten)) {
					booleanFilter.add(
						new TermFilter(
							"parentMessageId",
							String.valueOf(kbFolder.getKbFolderId())),
						BooleanClauseOccur.MUST);
				}
			},
			kbFolder.getGroupId(), search, aggregation, filter, pagination,
			sorts);
	}

	@Override
	public Page<KnowledgeBaseArticle> getSiteKnowledgeBaseArticlesPage(
			Long siteId, Boolean flatten, String search,
			Aggregation aggregation, Filter filter, Pagination pagination,
			Sort[] sorts)
		throws Exception {

		return _getKnowledgeBaseArticlesPage(
			HashMapBuilder.put(
				"create",
				addAction(
					"ADD_KB_ARTICLE", "postSiteKnowledgeBaseArticle",
					"com.liferay.knowledge.base.admin", siteId)
			).put(
				"get",
				addAction(
					"VIEW", "getSiteKnowledgeBaseArticlesPage",
					"com.liferay.knowledge.base.admin", siteId)
			).put(
				"subscribe",
				addAction(
					"SUBSCRIBE", "putSiteKnowledgeBaseArticleSubscribe",
					"com.liferay.knowledge.base.admin", siteId)
			).put(
				"unsubscribe",
				addAction(
					"SUBSCRIBE", "putSiteKnowledgeBaseArticleUnsubscribe",
					"com.liferay.knowledge.base.admin", siteId)
			).build(),
			booleanQuery -> {
				if (!GetterUtil.getBoolean(flatten)) {
					BooleanFilter booleanFilter =
						booleanQuery.getPreBooleanFilter();

					booleanFilter.add(
						new TermFilter(Field.FOLDER_ID, "0"),
						BooleanClauseOccur.MUST);
					booleanFilter.add(
						new TermFilter("parentMessageId", "0"),
						BooleanClauseOccur.MUST);
				}
			},
			siteId, search, aggregation, filter, pagination, sorts);
	}

	@Override
	public KnowledgeBaseArticle postKnowledgeBaseArticleKnowledgeBaseArticle(
			Long parentKnowledgeBaseArticleId,
			KnowledgeBaseArticle knowledgeBaseArticle)
		throws Exception {

		KBArticle kbArticle = _kbArticleService.getLatestKBArticle(
			parentKnowledgeBaseArticleId, WorkflowConstants.STATUS_APPROVED);

		return _getKnowledgeBaseArticle(
			kbArticle.getGroupId(),
			_portal.getClassNameId(KBArticle.class.getName()),
			parentKnowledgeBaseArticleId, knowledgeBaseArticle);
	}

	@Override
	public Rating postKnowledgeBaseArticleMyRating(
			Long knowledgeBaseArticleId, Rating rating)
		throws Exception {

		SPIRatingResource<Rating> spiRatingResource = _getSPIRatingResource();

		return spiRatingResource.addOrUpdateRating(
			rating.getRatingValue(), knowledgeBaseArticleId);
	}

	@Override
	public KnowledgeBaseArticle postKnowledgeBaseFolderKnowledgeBaseArticle(
			Long knowledgeBaseFolderId,
			KnowledgeBaseArticle knowledgeBaseArticle)
		throws Exception {

		KBFolder kbFolder = _kbFolderService.getKBFolder(knowledgeBaseFolderId);

		return _getKnowledgeBaseArticle(
			kbFolder.getGroupId(),
			_portal.getClassNameId(KBFolder.class.getName()),
			knowledgeBaseFolderId, knowledgeBaseArticle);
	}

	@Override
	public KnowledgeBaseArticle postSiteKnowledgeBaseArticle(
			Long siteId, KnowledgeBaseArticle knowledgeBaseArticle)
		throws Exception {

		return _getKnowledgeBaseArticle(
			siteId, _portal.getClassNameId(KBFolder.class.getName()), 0L,
			knowledgeBaseArticle);
	}

	@Override
	public KnowledgeBaseArticle putKnowledgeBaseArticle(
			Long knowledgeBaseArticleId,
			KnowledgeBaseArticle knowledgeBaseArticle)
		throws Exception {

		return _toKnowledgeBaseArticle(
			_kbArticleService.updateKBArticle(
				knowledgeBaseArticleId, knowledgeBaseArticle.getTitle(),
				knowledgeBaseArticle.getArticleBody(),
				knowledgeBaseArticle.getDescription(), null, null, null, null,
				ServiceContextRequestUtil.createServiceContext(
					Optional.ofNullable(
						knowledgeBaseArticle.getTaxonomyCategoryIds()
					).orElse(
						new Long[0]
					),
					Optional.ofNullable(
						knowledgeBaseArticle.getKeywords()
					).orElse(
						new String[0]
					),
					_getExpandoBridgeAttributes(knowledgeBaseArticle),
					knowledgeBaseArticle.getSiteId(), contextHttpServletRequest,
					knowledgeBaseArticle.getViewableByAsString())));
	}

	@Override
	public Rating putKnowledgeBaseArticleMyRating(
			Long knowledgeBaseArticleId, Rating rating)
		throws Exception {

		SPIRatingResource<Rating> spiRatingResource = _getSPIRatingResource();

		return spiRatingResource.addOrUpdateRating(
			rating.getRatingValue(), knowledgeBaseArticleId);
	}

	@Override
	public void putKnowledgeBaseArticleSubscribe(Long knowledgeBaseArticleId)
		throws Exception {

		KBArticle kbArticle = _kbArticleService.getLatestKBArticle(
			knowledgeBaseArticleId, WorkflowConstants.STATUS_APPROVED);

		_kbArticleService.subscribeKBArticle(
			kbArticle.getGroupId(), kbArticle.getResourcePrimKey());
	}

	@Override
	public void putKnowledgeBaseArticleUnsubscribe(Long knowledgeBaseArticleId)
		throws Exception {

		_kbArticleService.unsubscribeKBArticle(knowledgeBaseArticleId);
	}

	@Override
	public void putSiteKnowledgeBaseArticleSubscribe(Long siteId)
		throws Exception {

		_kbArticleService.subscribeGroupKBArticles(
			siteId, KBPortletKeys.KNOWLEDGE_BASE_DISPLAY);
	}

	@Override
	public void putSiteKnowledgeBaseArticleUnsubscribe(Long siteId)
		throws Exception {

		_kbArticleService.unsubscribeGroupKBArticles(
			siteId, KBPortletKeys.KNOWLEDGE_BASE_DISPLAY);
	}

	private Map<String, Serializable> _getExpandoBridgeAttributes(
		KnowledgeBaseArticle knowledgeBaseArticle) {

		return CustomFieldsUtil.toMap(
			KBArticle.class.getName(), contextCompany.getCompanyId(),
			knowledgeBaseArticle.getCustomFields(),
			contextAcceptLanguage.getPreferredLocale());
	}

	private KnowledgeBaseArticle _getKnowledgeBaseArticle(
			Long siteId, long parentResourceClassNameId,
			Long parentResourcePrimaryKey,
			KnowledgeBaseArticle knowledgeBaseArticle)
		throws Exception {

		return _toKnowledgeBaseArticle(
			_kbArticleService.addKBArticle(
				KBPortletKeys.KNOWLEDGE_BASE_DISPLAY, parentResourceClassNameId,
				parentResourcePrimaryKey, knowledgeBaseArticle.getTitle(),
				knowledgeBaseArticle.getFriendlyUrlPath(),
				knowledgeBaseArticle.getArticleBody(),
				knowledgeBaseArticle.getDescription(), null, null, null,
				ServiceContextRequestUtil.createServiceContext(
					knowledgeBaseArticle.getTaxonomyCategoryIds(),
					knowledgeBaseArticle.getKeywords(),
					_getExpandoBridgeAttributes(knowledgeBaseArticle), siteId,
					contextHttpServletRequest,
					knowledgeBaseArticle.getViewableByAsString())));
	}

	private Page<KnowledgeBaseArticle> _getKnowledgeBaseArticlesPage(
			Map<String, Map<String, String>> actions,
			UnsafeConsumer<BooleanQuery, Exception> booleanQueryUnsafeConsumer,
			Long siteId, String keywords, Aggregation aggregation,
			Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception {

		return SearchUtil.search(
			actions, booleanQueryUnsafeConsumer,
			FilterUtil.processFilter(_ddmIndexer, filter), KBArticle.class,
			keywords, pagination,
			queryConfig -> queryConfig.setSelectedFieldNames(
				Field.ENTRY_CLASS_PK),
			searchContext -> {
				searchContext.addVulcanAggregation(aggregation);
				searchContext.setAttribute(
					Field.STATUS, WorkflowConstants.STATUS_APPROVED);
				searchContext.setCompanyId(contextCompany.getCompanyId());
				searchContext.setGroupIds(new long[] {siteId});

				if (keywords == null) {
					searchContext.setKeywords("");
				}

				SearchRequestBuilder searchRequestBuilder =
					_searchRequestBuilderFactory.builder(searchContext);

				SortUtil.processSorts(
					_ddmIndexer, searchRequestBuilder, searchContext.getSorts(),
					_queries, _sorts);
			},
			sorts,
			document -> _toKnowledgeBaseArticle(
				_kbArticleService.getLatestKBArticle(
					GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK)),
					WorkflowConstants.STATUS_APPROVED)));
	}

	private SPIRatingResource<Rating> _getSPIRatingResource() {
		return new SPIRatingResource<>(
			KBArticle.class.getName(), _ratingsEntryLocalService,
			ratingsEntry -> {
				KBArticle kbArticle = _kbArticleService.getLatestKBArticle(
					ratingsEntry.getClassPK(),
					WorkflowConstants.STATUS_APPROVED);

				return RatingUtil.toRating(
					HashMapBuilder.put(
						"create",
						addAction(
							"VIEW", kbArticle.getResourcePrimKey(),
							"postKnowledgeBaseArticleMyRating",
							kbArticle.getUserId(),
							"com.liferay.knowledge.base.model.KBArticle",
							kbArticle.getGroupId())
					).put(
						"delete",
						addAction(
							"VIEW", kbArticle.getResourcePrimKey(),
							"deleteKnowledgeBaseArticleMyRating",
							kbArticle.getUserId(),
							"com.liferay.knowledge.base.model.KBArticle",
							kbArticle.getGroupId())
					).put(
						"get",
						addAction(
							"VIEW", kbArticle.getResourcePrimKey(),
							"getKnowledgeBaseArticleMyRating",
							kbArticle.getUserId(),
							"com.liferay.knowledge.base.model.KBArticle",
							kbArticle.getGroupId())
					).put(
						"replace",
						addAction(
							"VIEW", kbArticle.getResourcePrimKey(),
							"putKnowledgeBaseArticleMyRating",
							kbArticle.getUserId(),
							"com.liferay.knowledge.base.model.KBArticle",
							kbArticle.getGroupId())
					).build(),
					_portal, ratingsEntry, _userLocalService);
			},
			contextUser);
	}

	private KnowledgeBaseArticle _toKnowledgeBaseArticle(KBArticle kbArticle)
		throws Exception {

		if (kbArticle == null) {
			return null;
		}

		return _knowledgeBaseArticleDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				contextAcceptLanguage.isAcceptAllLanguages(),
				HashMapBuilder.put(
					"delete",
					addAction(
						"DELETE", kbArticle.getResourcePrimKey(),
						"deleteKnowledgeBaseArticle", kbArticle.getUserId(),
						"com.liferay.knowledge.base.model.KBArticle",
						kbArticle.getGroupId())
				).put(
					"get",
					addAction(
						"VIEW", kbArticle.getResourcePrimKey(),
						"getKnowledgeBaseArticle", kbArticle.getUserId(),
						"com.liferay.knowledge.base.model.KBArticle",
						kbArticle.getGroupId())
				).put(
					"replace",
					addAction(
						"UPDATE", kbArticle.getResourcePrimKey(),
						"putKnowledgeBaseArticle", kbArticle.getUserId(),
						"com.liferay.knowledge.base.model.KBArticle",
						kbArticle.getGroupId())
				).put(
					"subscribe",
					addAction(
						"SUBSCRIBE", kbArticle.getResourcePrimKey(),
						"putKnowledgeBaseArticleSubscribe",
						kbArticle.getUserId(),
						"com.liferay.knowledge.base.model.KBArticle",
						kbArticle.getGroupId())
				).put(
					"unsubscribe",
					addAction(
						"SUBSCRIBE", kbArticle.getResourcePrimKey(),
						"putKnowledgeBaseArticleUnsubscribe",
						kbArticle.getUserId(),
						"com.liferay.knowledge.base.model.KBArticle",
						kbArticle.getGroupId())
				).build(),
				_dtoConverterRegistry, kbArticle.getResourcePrimKey(),
				contextAcceptLanguage.getPreferredLocale(), contextUriInfo,
				contextUser));
	}

	@Reference
	private DDMIndexer _ddmIndexer;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

	@Reference
	private ExpandoColumnLocalService _expandoColumnLocalService;

	@Reference
	private ExpandoTableLocalService _expandoTableLocalService;

	@Reference
	private KBArticleService _kbArticleService;

	@Reference
	private KBFolderService _kbFolderService;

	@Reference
	private KnowledgeBaseArticleDTOConverter _knowledgeBaseArticleDTOConverter;

	@Reference
	private Portal _portal;

	@Reference
	private Queries _queries;

	@Reference
	private RatingsEntryLocalService _ratingsEntryLocalService;

	@Reference
	private SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Reference
	private Sorts _sorts;

	@Reference
	private UserLocalService _userLocalService;

}