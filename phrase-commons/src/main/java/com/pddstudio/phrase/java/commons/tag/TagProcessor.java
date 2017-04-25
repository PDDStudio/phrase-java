package com.pddstudio.phrase.java.commons.tag;

import com.pddstudio.phrase.java.commons.log.Logger;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by pddstudio on 16/10/2016.
 */
public class TagProcessor {

	static TagProcessor create(ITag tag) {
		return new TagProcessor(tag);
	}

	private final ITag tag;
	private final TagResult tagResult;
	private final Logger logger;

	private CharSequence target;

	private TagProcessor(ITag tag) {
		this.tag = tag;
		this.tagResult = new TagResult(tag);
		this.logger = Logger.getLogger(TagProcessor.class);
	}

	public TagResult execute(CharSequence target) {
		logger.log("execute() called => target : %s", target);
		this.target = target;
		findTagContent();
		return tagResult;
	}

	private void findTagContent() {
		int startTags = StringUtils.countMatches(target, tag.getStartTag());
		int endTags = StringUtils.countMatches(target, tag.getEndTag());
		//make sure the amount of start and end tags are equal
		if(startTags == endTags) {
			String[] results = StringUtils.substringsBetween(target.toString(), tag.getStartTag().toString(), tag.getEndTag().toString());
			for(String result : results) {
				tagResult.addResult(result);
				logger.log("Found Tag: %s", result);
			}
		} else {
			//TODO: throw exception or something
			logger.log("Tag counts don't match. Found %s start tags and %s end tags!", startTags, endTags);
		}
	}


}
