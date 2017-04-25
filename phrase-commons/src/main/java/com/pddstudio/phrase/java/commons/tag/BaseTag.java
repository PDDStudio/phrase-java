package com.pddstudio.phrase.java.commons.tag;

/**
 * Created by pddstudio on 16/10/2016.
 */
public abstract class BaseTag implements ITag {

	private final CharSequence tagId;

	public BaseTag(String tagName) {
		this.tagId = tagName;
	}

	@Override
	public CharSequence getTagName() {
		return tagId;
	}
}
