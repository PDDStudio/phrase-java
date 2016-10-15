package com.pddstudio.phrase.java;

import com.pddstudio.phrase.java.Phrase.KeyIdentifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by pddstudio on 15/10/2016.
 */
@RunWith(Parameterized.class)
public class PhraseTest extends BaseUtilityTest {

	@Parameters
	public static Collection<KeyIdentifier> data() {
		return Arrays.asList(KeyIdentifier.values());
	}

	private final String separator = " | ";

	public PhraseTest(KeyIdentifier keyIdentifier) {
		super(keyIdentifier);
	}

	private String getPhraseForIdentifier(String phrase) {
		return phrase.replaceAll("\\{", phraseKeyIdentifier.getOpenCharString()).replaceAll("\\}", phraseKeyIdentifier.getCloseCharString());
	}

	private Phrase phraseFrom(String phrase) {
		return Phrase.from(getPhraseForIdentifier(phrase), phraseKeyIdentifier);
	}

	@Test
	public void helloWorldTest() {
		String greeting = "Hello";
		String greetingTarget = "World";
		String expected = "Hello World!";
		String phrase = phraseFrom("{greeting} {who}!").put("greeting", greeting).put("who", greetingTarget).formatString();
		printResult("helloWorldTest()", phrase, expected);
		assertEquals(phrase, expected);
	}

	@Test
	public void bigDecimalTest() {
		BigDecimal firstNumber = new BigDecimal(500);
		BigDecimal secondNumber = new BigDecimal(3);
		String expected = "500 / 3 = 167";
		String phrase = phraseFrom("{first} / {second} = {result}")
							  .put("first", firstNumber)
							  .put("second", secondNumber)
							  .put("result", firstNumber.divide(secondNumber, BigDecimal.ROUND_HALF_DOWN)).formatString();
		printResult("bigDecimalTest()", phrase, expected);
		assertEquals(phrase, expected);
	}

	@Test
	public void dummyRestUrlTest() {
		String host = "https://github.com";
		String name = "pddstudio";
		String action = "isAwesome";
		boolean actionValue = true;
		String expected = "https://github.com/pddstudio/?isAwesome=true";
		String phrase = phraseFrom("{host}/{name}/?{action}={action_value}")
							  .put("host", host)
							  .put("name", name)
							  .put("action", action)
							  .put("action_value", actionValue).formatString();
		printResult("dummyRestUrlTest()", phrase, expected);
		assertEquals(phrase, expected);
	}

	@Test
	public void digitsTest() {
		int intValue = 471;
		float floatValue = 21.9F;
		double doubleValue = 1921.2;
		String expected = "Integer: 471 | Float: 21.9 | Double: 1921.2";
		String phrase = phraseFrom("Integer: {integer} | Float: {float} | Double: {double}")
							  .put("integer", intValue)
							  .put("float", floatValue)
							  .put("double", doubleValue)
							  .formatString();
		printResult("digitsTest()", phrase, expected);
		assertEquals(phrase, expected);
	}

	@Test
	public void intArrayTest() {
		int[] ints = getRandomIntArray();
		String expected = getExpectedIntArrayResult(ints, separator);
		String phrase = phraseFrom("{integer_array}").putArray("integer_array", ints, separator).formatString();
		printResult("intArrayTest()", phrase, expected);
		assertEquals(phrase, expected);
	}

	@Test
	public void booleanArrayTest() {
		boolean[] bools = getRandomBooleanArray();
		String expected = getExpectedBooleanArrayResult(bools, separator);
		String phrase = phraseFrom("{boolean_array}").putArray("boolean_array", bools, separator).formatString();
		printResult("booleanArrayTest()", phrase, expected);
		assertEquals(phrase, expected);
	}

	@Test
	public void floatArrayTest() {
		float[] floats = getRandomFloatArray();
		String expected = getExpectedFloatArrayResult(floats, separator);
		String phrase = phraseFrom("{float_array}").putArray("float_array", floats, separator).formatString();
		printResult("floatArrayTest()", phrase, expected);
		assertEquals(phrase, expected);
	}

	@Test
	public void doubleArrayTest() {
		double[] doubles = getRandomDoubleArray();
		String expected = getExpectedDoubleArrayResult(doubles, separator);
		String phrase = phraseFrom("{double_array}").putArray("double_array", doubles, separator).formatString();
		printResult("doubleArrayTest()", phrase, expected);
		assertEquals(phrase, expected);
	}

}
