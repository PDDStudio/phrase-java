package com.pddstudio.phrase.java;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by pddstudio on 15/10/2016.
 */
public class PhraseTest {

	private  int methodNameCount = 20;

	private void printResult(String methodName, String result) {
		StringBuilder message = new StringBuilder();
		int remainingWhitespaces = methodNameCount - methodName.length();
		if(remainingWhitespaces < 0) {
			remainingWhitespaces = 0;
			message.append("[RESULT] : ").append(methodName.substring(0, methodNameCount -3)).append("...");
		} else {
			message.append("[RESULT] : ").append(methodName);
		}
		for(int i = 0; i < remainingWhitespaces; i++) {
			message.append(" ");
		}
		message.append(" > ").append(result);
		System.out.println(message.toString());
	}

	@Test
	public void helloWorldTest() {
		String greeting = "Hello";
		String greetingTarget = "World";
		String phrase = Phrase.from("{greeting} {who}!").put("greeting", greeting).put("who", greetingTarget).formatString();
		printResult("helloWorldTest()", phrase);
		assertEquals(phrase, "Hello World!");
	}

	@Test
	public void bigDecimalTest() {
		BigDecimal firstNumber = new BigDecimal(500);
		BigDecimal secondNumber = new BigDecimal(3);
		String phrase = Phrase.from("{first} / {second} = {result}")
							  .put("first", firstNumber)
							  .put("second", secondNumber)
							  .put("result", firstNumber.divide(secondNumber, BigDecimal.ROUND_HALF_DOWN)).formatString();
		printResult("bigDecimalTest()", phrase);
		assertEquals(phrase, "500 / 3 = 167");
	}

	@Test
	public void dummyRestUrlTest() {
		String host = "https://github.com";
		String name = "pddstudio";
		String action = "isAwesome";
		boolean actionValue = true;
		String phrase = Phrase.from("{host}/{name}/?{action}={action_value}")
							  .put("host", host)
							  .put("name", name)
							  .put("action", action)
							  .put("action_value", actionValue).formatString();
		printResult("dummyRestUrlTest()", phrase);
		assertEquals(phrase, "https://github.com/pddstudio/?isAwesome=true");
	}

	@Test
	public void digitsTest() {
		int intValue = 471;
		float floatValue = 21.9F;
		double doubleValue = 1921.2;
		String phrase = Phrase.from("Integer: {integer} | Float: {float} | Double: {double}")
							  .put("integer", intValue)
							  .put("float", floatValue)
							  .put("double", doubleValue)
							  .formatString();
		printResult("digitsTest()", phrase);
		assertEquals(phrase, "Integer: 471 | Float: 21.9 | Double: 1921.2");
	}

}
