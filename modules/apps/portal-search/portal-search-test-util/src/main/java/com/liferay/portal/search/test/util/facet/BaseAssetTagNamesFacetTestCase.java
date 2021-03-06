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

package com.liferay.portal.search.test.util.facet;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.search.facet.tag.AssetTagNamesFacetFactory;
import com.liferay.portal.search.internal.facet.tag.AssetTagNamesFacetFactoryImpl;
import com.liferay.portal.search.test.util.indexing.QueryContributors;

import java.util.Arrays;

import org.junit.Test;

/**
 * @author André de Oliveira
 */
public abstract class BaseAssetTagNamesFacetTestCase extends BaseFacetTestCase {

	@Test
	public void testCaseInsensitiveSearchCaseSensitiveGrouping()
		throws Exception {

		addDocument("tag");
		addDocument("tAg");
		addDocument("TAG");
		addDocument(RandomTestUtil.randomString());

		assertSearch(
			helper -> {
				Facet facet = helper.addFacet(this::createFacet);

				helper.search(QueryContributors.mustMatch(getField(), "tag"));

				helper.assertFrequencies(
					facet, Arrays.asList("TAG=1", "tAg=1", "tag=1"));
			});
	}

	@Test
	public void testKeysWithSpaces() throws Exception {
		addDocument("Green-Blue Tag");
		addDocument("Green-Blue Tag", "Red Tag");
		addDocument("Tag");

		assertSearch(
			helper -> {
				Facet facet = helper.addFacet(this::createFacet);

				helper.search();

				helper.assertFrequencies(
					facet,
					Arrays.asList("Green-Blue Tag=2", "Red Tag=1", "Tag=1"));
			});
	}

	protected Facet createFacet(SearchContext searchContext) {
		return initFacet(assetTagNamesFacetFactory.newInstance(searchContext));
	}

	@Override
	protected String getField() {
		return Field.ASSET_TAG_NAMES;
	}

	protected AssetTagNamesFacetFactory assetTagNamesFacetFactory =
		new AssetTagNamesFacetFactoryImpl();

}