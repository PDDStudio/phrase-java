package com.pddstudio.phrase.java.commons.tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pddstudio on 16/10/2016.
 */
public class TagResult {

	private final ITag               targetTag;
	private final List<CharSequence> resultTexts;

	TagResult(ITag target) {
		this.targetTag = target;
		this.resultTexts = new ArrayList<>();
	}

	void addResult(CharSequence result) {
		resultTexts.add(result);
	}

	public boolean isPresent() {
		return !resultTexts.isEmpty();
	}

	public List<CharSequence> getResults() {
		return resultTexts;
	}

	public ITag getTargetTag() {
		return targetTag;
	}

	public int getResultCount() {
		return resultTexts.size();
	}

}
