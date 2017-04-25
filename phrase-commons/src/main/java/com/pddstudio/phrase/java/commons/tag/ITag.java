package com.pddstudio.phrase.java.commons.tag;

/**
 * Created by pddstudio on 16/10/2016.
 */
public interface ITag {
	CharSequence getTagName();
	Pair<CharSequence, CharSequence> getTagTypes();
	CharSequence getStartTag();
	CharSequence getEndTag();
}
