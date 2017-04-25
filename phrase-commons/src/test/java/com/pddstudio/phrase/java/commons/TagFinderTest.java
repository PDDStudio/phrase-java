package com.pddstudio.phrase.java.commons;

import com.pddstudio.phrase.java.commons.tag.Tag;
import com.pddstudio.phrase.java.commons.tag.TagResult;
import com.pddstudio.phrase.java.commons.tag.TagFinder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by pddstudio on 16/10/2016.
 */
public class TagFinderTest extends BaseUtilityTest {

	@Test
	public void simpleSingleTagTest() {
		//prepare dummy string and tag
		String targetString = "Hello, this is a simple test <name>String</name>";
		Tag nameTag = new Tag("nameTag", "<name>", "</name>");

		//execute the request
		TagFinder tagFinder = TagFinder.in(targetString).find(nameTag);
		tagFinder.execute();
		TagResult result = tagFinder.getResult(nameTag);

		assertTrue(result.isPresent());
		assertEquals(result.getResults().get(0), "String");
	}

}
