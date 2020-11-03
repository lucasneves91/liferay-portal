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

package com.liferay.jenkins.results.parser.test.clazz.group;

import com.liferay.jenkins.results.parser.JenkinsResultsParserUtil;
import com.liferay.jenkins.results.parser.Job;
import com.liferay.jenkins.results.parser.PortalTestClassJob;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Hashimoto
 */
public class TestClassGroupFactory {

	public static AxisTestClassGroup newAxisTestClassGroup(
		BatchTestClassGroup batchTestClassGroup) {

		if (batchTestClassGroup instanceof FunctionalBatchTestClassGroup) {
			return new FunctionalAxisTestClassGroup(
				(FunctionalBatchTestClassGroup)batchTestClassGroup);
		}

		return new AxisTestClassGroup(batchTestClassGroup);
	}

	public static BatchTestClassGroup newBatchTestClassGroup(
		String batchName, BatchTestClassGroup.BuildProfile buildProfile,
		Job job) {

		String key = JenkinsResultsParserUtil.combine(
			batchName, "_", buildProfile.toString(), "_", job.getJobName());

		if (_batchTestClassGroups.containsKey(key)) {
			return _batchTestClassGroups.get(key);
		}

		BatchTestClassGroup batchTestClassGroup = null;

		if (job instanceof PortalTestClassJob) {
			PortalTestClassJob portalTestClassJob = (PortalTestClassJob)job;

			if (batchName.contains("functional-") ||
				batchName.contains("subrepository-functional-")) {

				batchTestClassGroup = new FunctionalBatchTestClassGroup(
					batchName, buildProfile, portalTestClassJob);
			}
			else if (batchName.startsWith("integration-") ||
					 batchName.startsWith("junit-test-") ||
					 batchName.startsWith("subrepository-integration-") ||
					 batchName.startsWith("subrepository-unit-") ||
					 batchName.startsWith("unit-")) {

				batchTestClassGroup = new JUnitBatchTestClassGroup(
					batchName, buildProfile, portalTestClassJob);
			}
			else if (batchName.startsWith("modules-compile-")) {
				batchTestClassGroup = new ModulesCompileBatchTestClassGroup(
					batchName, buildProfile, portalTestClassJob);
			}
			else if (batchName.startsWith("modules-integration-") ||
					 batchName.startsWith("modules-unit-")) {

				batchTestClassGroup = new ModulesJUnitBatchTestClassGroup(
					batchName, buildProfile, portalTestClassJob);
			}
			else if (batchName.startsWith("modules-semantic-versioning-")) {
				batchTestClassGroup = new ModulesSemVerBatchTestClassGroup(
					batchName, buildProfile, portalTestClassJob);
			}
			else if (batchName.startsWith("plugins-compile-")) {
				batchTestClassGroup = new PluginsBatchTestClassGroup(
					batchName, buildProfile, portalTestClassJob);
			}
			else if (batchName.startsWith("js-test-") ||
					 batchName.startsWith("portal-frontend-js-")) {

				batchTestClassGroup = new NPMTestBatchTestClassGroup(
					batchName, buildProfile, portalTestClassJob);
			}
			else if (batchName.startsWith("rest-builder-")) {
				batchTestClassGroup = new RESTBuilderBatchTestClassGroup(
					batchName, buildProfile, portalTestClassJob);
			}
			else if (batchName.startsWith("service-builder-")) {
				batchTestClassGroup = new ServiceBuilderBatchTestClassGroup(
					batchName, buildProfile, portalTestClassJob);
			}
			else if (batchName.startsWith("tck-")) {
				batchTestClassGroup = new TCKJunitBatchTestClassGroup(
					batchName, buildProfile, portalTestClassJob);
			}
			else {
				batchTestClassGroup = new DefaultBatchTestClassGroup(
					batchName, buildProfile, portalTestClassJob);
			}
		}

		if (batchTestClassGroup == null) {
			throw new IllegalArgumentException("Unknown test class group");
		}

		_batchTestClassGroups.put(key, batchTestClassGroup);

		return batchTestClassGroup;
	}

	public static SegmentTestClassGroup newSegmentTestClassGroup(
		BatchTestClassGroup batchTestClassGroup) {

		if (batchTestClassGroup instanceof FunctionalBatchTestClassGroup) {
			return new FunctionalSegmentTestClassGroup(
				(FunctionalBatchTestClassGroup)batchTestClassGroup);
		}

		return new SegmentTestClassGroup(batchTestClassGroup);
	}

	private static final Map<String, BatchTestClassGroup>
		_batchTestClassGroups = new HashMap<>();

}