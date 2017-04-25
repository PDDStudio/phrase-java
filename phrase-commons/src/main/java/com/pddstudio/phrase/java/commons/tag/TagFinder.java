package com.pddstudio.phrase.java.commons.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pddstudio on 16/10/2016.
 */
public class TagFinder {

	public static TagFinder in(CharSequence targetCharSequence) {
		return new TagFinder(targetCharSequence);
	}

	private final CharSequence target;
	private final List<ITag> tagList;
	private final Map<CharSequence, TagResult> resultMap;
	private TagProcessor tagProcessor;

	private TagFinder(CharSequence target) {
		this.target = target;
		this.tagList = new ArrayList<>();
		this.resultMap = new HashMap<>();
	}

	public TagFinder find(ITag... tags) {
		tagList.addAll(Arrays.asList(tags));
		return this;
	}

	public TagResult getResult(ITag forTag) {
		return getResult(forTag.getTagName());
	}

	public TagResult getResult(CharSequence forTagName) {
		return resultMap.get(forTagName);
	}

	public void execute() {
		for(ITag tag : tagList) {
			tagProcessor = TagProcessor.create(tag);
			TagResult result = tagProcessor.execute(target);
			resultMap.put(tag.getTagName(), result);
		}
	}

}
