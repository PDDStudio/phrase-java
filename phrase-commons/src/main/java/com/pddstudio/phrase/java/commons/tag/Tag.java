package com.pddstudio.phrase.java.commons.tag;

/**
 * Created by pddstudio on 16/10/2016.
 */
public class Tag extends BaseTag {

	private final CharSequence tagStart;
	private final CharSequence tagEnd;

	public Tag(String identifier, String startTag, String endTag) {
		super(identifier);
		this.tagStart = startTag;
		this.tagEnd = endTag;
	}

	@Override
	public Pair<CharSequence, CharSequence> getTagTypes() {
		return new Pair<>(tagStart, tagEnd);
	}

	@Override
	public CharSequence getStartTag() {
		return tagStart;
	}

	@Override
	public CharSequence getEndTag() {
		return tagEnd;
	}

}
