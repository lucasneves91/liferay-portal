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

import {getByTestId} from '@testing-library/dom';
import userEvent from '@testing-library/user-event';

import delegate from '../../../src/main/resources/META-INF/resources/liferay/delegate/delegate.es';

describe('delegate', () => {
	afterEach(() => {
		document.body.innerHTML = '';
	});

	it('triggers delegate listener for matched elements', () => {
		document.body.innerHTML =
			'<div class="nomatch" data-testid="nomatch"></div>' +
			'<div class="match" data-testid="match"></div>';

		const listener = jest.fn();

		delegate(document, 'click', '.match', listener);

		userEvent.click(getByTestId(document, 'nomatch'));

		expect(listener).not.toHaveBeenCalled();

		userEvent.click(getByTestId(document, 'match'));

		expect(listener).toHaveBeenCalled();
	});

	it('triggers event on the parent if target is the child of the selector element', () => {
		document.body.innerHTML = `<div class="match" data-testid="match">
				<div>
					<div data-testid="most-inner-match"></div>
				</div>
			</div >`;

		const listener = jest.fn();

		delegate(document, 'click', '.match', listener);

		userEvent.click(getByTestId(document, 'most-inner-match'));

		expect(listener).toHaveBeenCalled();
	});

	it('stops triggering event if stopPropagation is called', () => {
		document.body.innerHTML = `<div class="match" data-testid="match"></div>`;

		const listener = jest.fn();

		delegate(document, 'click', '.match', listener);

		expect(listener).not.toHaveBeenCalled();

		userEvent.click(getByTestId(document, 'match'));

		expect(listener).toHaveBeenCalledTimes(1);

		document
			.querySelector('.match')
			.addEventListener('click', (e) => e.stopPropagation());

		userEvent.click(getByTestId(document, 'match'));

		expect(listener).toHaveBeenCalledTimes(1);
	});

	it('only triggers delegate event at initial target', () => {
		document.body.innerHTML = `<div>
			<div class="match" data-testid="match">
				<div class="match" data-testid="match2"></div>
			</div>
		</div>`;

		const listener = jest.fn();

		delegate(document, 'click', '.match', listener);

		userEvent.click(getByTestId(document, 'match'));

		expect(listener).toHaveBeenCalledTimes(1);

		userEvent.click(getByTestId(document, 'match2'));

		expect(listener).toHaveBeenCalledTimes(2);
	});

	it('triggers listener twice when two ancestors are delegating', () => {
		document.body.innerHTML = `<div>
			<div>
				<div class="match" data-testid="match"></div>
			</div>
		</div>`;

		const listener = jest.fn();

		delegate(document, 'click', '.match', listener);
		delegate(document.body, 'click', '.match', listener);

		userEvent.click(getByTestId(document, 'match'));

		expect(listener).toHaveBeenCalledTimes(2);
	});

	it('removes listener through via `.dispose`', () => {
		document.body.innerHTML = `<div class="nomatch" data-testid="nomatch"></div>
			<div class="match" data-testid="match"></div>`;

		const listener = jest.fn();

		const eventHandler = delegate(document, 'click', '.match', listener);

		userEvent.click(getByTestId(document, 'match'));

		expect(listener).toHaveBeenCalledTimes(1);

		eventHandler.dispose();

		userEvent.click(getByTestId(document, 'match'));

		expect(listener).toHaveBeenCalledTimes(1);
	});

	it("doesn't run click event listeners for disabled elements", () => {
		document.body.innerHTML = `<div class="root">
			<button disabled class="match" data-testid="match"></button>
		</div>`;

		const listener = jest.fn();

		delegate(document, 'click', '.match', listener);

		userEvent.click(getByTestId(document, 'match'));

		expect(listener).not.toHaveBeenCalled();
	});

	it("doesn't run click event listeners for elements with a disabled parent", () => {
		document.body.innerHTML = `<button disabled class="root">
			<div class="match" data-testid="match"></div>
		</button>`;

		const listener = jest.fn();

		delegate(document, 'click', '.match', listener);

		userEvent.click(getByTestId(document, 'match'));

		expect(listener).not.toHaveBeenCalled();
	});
});
