package com.pddstudio.phrase.java;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by pddstudio on 15/10/2016.
 */
public class PhraseTest {

	private void printResult(String methodName, String result) {
		System.out.println("[RESULT] : " + methodName + " > " + result);
	}

	@Test
	public void helloWorldTest() {
		String greeting = "Hello";
		String greetingTarget = "World";
		String phrase = Phrase.from("{greeting} {who}!").put("greeting", greeting).put("who", greetingTarget).formatString();
		printResult("helloWorldTest()", phrase);
		assertEquals(phrase, "Hello World!");
	}

}
