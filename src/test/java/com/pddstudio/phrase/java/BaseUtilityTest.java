package com.pddstudio.phrase.java;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by pddstudio on 15/10/2016.
 */
public abstract class BaseUtilityTest {

	private int    methodNameCount = 20;
	private Random random          = new Random();

	protected final Phrase.KeyIdentifier phraseKeyIdentifier;

	public BaseUtilityTest(Phrase.KeyIdentifier keyIdentifier) {
		this.phraseKeyIdentifier = keyIdentifier;
	}

	protected void printResult(String methodName, String result, String expected) {
		System.out.println("==== Test Method: " + methodName + " Test KeyIdentifier: " +
								   phraseKeyIdentifier.name() + " " +
								   phraseKeyIdentifier.getOpenCharString() +
								   phraseKeyIdentifier.getCloseCharString() + " ====");
		StringBuilder message = new StringBuilder();
		int remainingWhitespaces = methodNameCount - methodName.length();
		if(remainingWhitespaces < 0) {
			remainingWhitespaces = 0;
			message.append("[EXPECTED] : ").append(methodName.substring(0, methodNameCount -3)).append("...");
		} else {
			message.append("[EXPECTED] : ").append(methodName);
		}
		for(int i = 0; i < remainingWhitespaces; i++) {
			message.append(" ");
		}
		message.append(" > ").append(expected);
		System.out.println(message.toString());
		//print the actual result too
		printResult(methodName, result);
	}

	private void printResult(String methodName, String result) {
		StringBuilder message = new StringBuilder();
		int remainingWhitespaces = methodNameCount - methodName.length();
		if(remainingWhitespaces < 0) {
			remainingWhitespaces = 0;
			message.append("[RESULT  ] : ").append(methodName.substring(0, methodNameCount -3)).append("...");
		} else {
			message.append("[RESULT  ] : ").append(methodName);
		}
		for(int i = 0; i < remainingWhitespaces; i++) {
			message.append(" ");
		}
		message.append(" > ").append(result);
		System.out.println(message.toString());
	}

	private <T> String getExpectedArrayResult(T[] values, String separator) {
		//falling back to the default separator
		if (separator == null || separator.length() == 0) {
			separator = " ";
		}
		StringBuilder sb = new StringBuilder();
		List<T> list = Arrays.asList(values);
		Iterator<T> iterator = list.iterator();
		while(iterator.hasNext()) {
			sb.append(String.valueOf(iterator.next()));
			if(iterator.hasNext()) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	protected int[] getRandomIntArray() {
		int size = random.nextInt(100);
		int[] ints = new int[size];
		for(int i = 0; i < ints.length; i++) {
			ints[i] = random.nextInt(Integer.MAX_VALUE);
		}
		return ints;
	}

	protected float[] getRandomFloatArray() {
		int size = random.nextInt(100);
		float[] floats = new float[size];
		for(int i = 0; i < floats.length; i++) {
			floats[i] = random.nextFloat();
		}
		return floats;
	}

	protected double[] getRandomDoubleArray() {
		int size = random.nextInt(100);
		double[] doubles = new double[size];
		for(int i = 0; i < doubles.length; i++) {
			doubles[i] = random.nextDouble();
		}
		return doubles;
	}

	protected boolean[] getRandomBooleanArray() {
		int size = random.nextInt(100);
		boolean[] bools = new boolean[size];
		for(int i = 0; i < bools.length; i++) {
			bools[i] = random.nextBoolean();
		}
		return bools;
	}

	protected String getExpectedIntArrayResult(int[] values, String separator) {
		Integer[] integers = new Integer[values.length];
		for(int i = 0; i < integers.length; i++) {
			integers[i] = values[i];
		}
		return getExpectedArrayResult(integers, separator);
	}

	protected String getExpectedFloatArrayResult(float[] values, String separator) {
		Float[] floats = new Float[values.length];
		for(int i = 0; i < floats.length; i++) {
			floats[i] = values[i];
		}
		return getExpectedArrayResult(floats, separator);
	}

	protected String getExpectedDoubleArrayResult(double[] values, String separator) {
		Double[] doubles = new Double[values.length];
		for(int i = 0; i < doubles.length; i++) {
			doubles[i] = values[i];
		}
		return getExpectedArrayResult(doubles, separator);
	}

	protected String getExpectedBooleanArrayResult(boolean[] values, String separator) {
		Boolean[] bools = new Boolean[values.length];
		for(int i = 0; i < bools.length; i++) {
			bools[i] = values[i];
		}
		return getExpectedArrayResult(bools, separator);
	}

}
