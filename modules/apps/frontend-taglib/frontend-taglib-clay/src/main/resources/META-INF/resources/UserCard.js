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

import {ClayCardWithUser} from '@clayui/card';
import React, {useState} from 'react';

const getDataAttributes = (data) => {
	return data
		? Object.entries(data).reduce((acc, [key, value]) => {
				acc[`data-${key}`] = value;

				return acc;
		  }, {})
		: {};
};

export default function UserCard({
	actions = [],
	componentId: _componentId,
	cssClass,
	inputName,
	inputValue,
	labels = [],
	locale: _locale,
	portletId: _portletId,
	portletNamespace: _portletNamespace,
	selected: initialSelected,
	selectable,
	...otherProps
}) {
	const [selected, setSelected] = useState(initialSelected);

	return (
		<ClayCardWithUser
			actions={actions?.map(({data, ...rest}) => {
				const dataAttributes = getDataAttributes(data);

				return {
					...dataAttributes,
					...rest,
				};
			})}
			checkboxProps={{
				name: inputName,
				value: inputValue,
			}}
			className={cssClass}
			labels={labels?.map(({data, label, style: _style, ...rest}) => {
				const dataAttributes = getDataAttributes(data);

				return {
					value: label,
					...dataAttributes,
					...rest,
				};
			})}
			onSelectChange={
				selectable
					? () => {
							setSelected(!selected);
					  }
					: null
			}
			selected={selected}
			{...otherProps}
		/>
	);
}
