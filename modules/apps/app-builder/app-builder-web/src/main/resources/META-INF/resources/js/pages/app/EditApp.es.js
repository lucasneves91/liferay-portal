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

import React, {useState} from 'react';
import EditAppFooter from './EditAppFooter.es';
import MultiStepNav from './MultiStepNav.es';
import ControlMenu from '../../components/control-menu/ControlMenu.es';
import {addItem, updateItem} from '../../utils/client.es';
import EditAppHeader from './EditAppHeader.es';
import SelectFormView from './SelectFormView.es';

export default ({
	history,
	match: {
		params: {dataDefinitionId, appId}
	}
}) => {
	const [app, setApp] = useState({
		dataLayoutId: null,
		name: {
			en_US: ''
		}
	});

	const [currentStep, setCurrentStep] = useState(0);

	let title = Liferay.Language.get('new-app');

	if (appId) {
		title = Liferay.Language.get('edit-app');
	}

	const onAppNameChange = event => {
		const name = event.target.value;

		setApp(prevApp => ({
			...prevApp,
			name: {
				en_US: name
			}
		}));
	};

	const onCancel = () => {
		history.push(`/custom-object/${dataDefinitionId}/apps`);
	};

	const onDeploy = () => {
		if (app.name.en_US === '') {
			return;
		}

		if (appId) {
			updateItem(`/o/app-builder/v1.0/apps/${appId}`, app).then(onCancel);
		} else {
			addItem(
				`/o/app-builder/v1.0/data-definitions/${dataDefinitionId}/apps`,
				app
			).then(onCancel);
		}
	};

	const onFormViewSelect = formViewId => {
		setApp(prevApp => ({
			...prevApp,
			dataLayoutId: formViewId
		}));
	};

	return (
		<>
			<ControlMenu backURL="../" title={title} />

			<div className="container-fluid container-fluid-max-lg mt-4">
				<div className="card card-root">
					<EditAppHeader
						app={app}
						onAppNameChange={onAppNameChange}
					/>

					<h4 className="card-divider mb-4"></h4>

					<div className="card-body p-0">
						<div className="autofit-row">
							<div className="col-md-12">
								<MultiStepNav currentStep={currentStep} />
							</div>
						</div>

						{currentStep == 0 && (
							<SelectFormView
								dataLayoutId={app.dataLayoutId}
								onFormViewSelect={onFormViewSelect}
							/>
						)}
						{currentStep == 1 && (
							<div className="autofit-row">
								<div className="col-md-12">
									Choose Table View
								</div>
							</div>
						)}
						{currentStep == 2 && (
							<div className="autofit-row">
								<div className="col-md-12">Deploy</div>
							</div>
						)}
					</div>

					<EditAppFooter
						currentStep={currentStep}
						onCancel={onCancel}
						onDeploy={onDeploy}
						onStepChange={step => setCurrentStep(step)}
					/>
				</div>
			</div>
		</>
	);
};
