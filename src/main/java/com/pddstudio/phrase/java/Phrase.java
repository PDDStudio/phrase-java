/*
 * Copyright (C) 2016 Patrick J
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pddstudio.phrase.java;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.util.Pair;

/**
 * A fluent API for formatting Strings. Canonical usage:
 * <pre>
 *   CharSequence formatted = Phrase.from("Hi {first_name}, you are {age} years old.")
 *       .put("first_name", firstName)
 *       .put("age", age)
 *       .format();
 * </pre>
 * <ul>
 * <li>Surround keys with curly braces; use two {{ to escape.</li>
 * <li>Keys start with lowercase letters followed by lowercase letters and underscores.</li>
 * <li>Spans are preserved, such as simple HTML tags found in strings.xml.</li>
 * <li>Fails fast on any mismatched keys.</li>
 * </ul>
 * The constructor parses the original pattern into a doubly-linked list of {@link Token}s.
 * These tokens do not modify the original pattern, thus preserving any spans.
 * <p>
 * The {@link #format()} method iterates over the tokens, replacing text as it iterates. The
 * doubly-linked list allows each token to ask its predecessor for the expanded length.
 */
public final class Phrase {

	private static final String DEFAULT_SEPARATOR = " ";

	/** The unmodified original pattern. */
	private final CharSequence pattern;

	/** The identification type for keys */
	private final KeyIdentifier keyIdentifier;

	/** All keys parsed from the original pattern, sans braces. */
	private final Set<String> keys = new HashSet<String>();
	private final Map<String, CharSequence> keysToValues = new HashMap<String, CharSequence>();

	/** Cached result after replacing all keys with corresponding values. */
	private CharSequence formatted;

	/** The constructor parses the original pattern into this doubly-linked list of tokens. */
	private Token head;

	/** When parsing, this is the current character. */
	private char curChar;
	private int curCharIndex;

	/** Indicates parsing is complete. */
	private static final int EOF = 0;

	/**
	 * Entry point into this API; pattern must be non-null.
	 *
	 * @throws IllegalArgumentException if pattern contains any syntax errors.
	 */
	public static Phrase from(CharSequence pattern) {
		return new Phrase(pattern, KeyIdentifier.CURLY_BRACKETS);
	}

	/**
	 * Entry point into this API; pattern must be non-null.
	 *
	 * @throws IllegalArgumentException if pattern contains any syntax errors.
	 */
	public static Phrase from(CharSequence pattern, KeyIdentifier keyIdentifier) {
		return new Phrase(pattern, keyIdentifier);
	}

	/**
	 * Replaces the given key with a non-null value. You may reuse Phrase instances and replace
	 * keys with new values.
	 *
	 * @throws IllegalArgumentException if the key is not in the pattern.
	 */
	public Phrase put(String key, CharSequence value) {
		if (!keys.contains(key)) {
			throw new IllegalArgumentException("Invalid key: " + key);
		}
		if (value == null) {
			throw new IllegalArgumentException("Null value for '" + key + "'");
		}
		keysToValues.put(key, value);

		// Invalidate the cached formatted text.
		formatted = null;
		return this;
	}

	/**
	 * Replaces the given key with the {@link Integer#toString(int)} value for the given int.
	 *
	 * @see #put(String, CharSequence)
	 */
	public Phrase put(String key, int value) {
		return put(key, Integer.toString(value));
	}

	/**
	 * Replaces the given key with the {@link Boolean#toString(boolean)} value for the given boolean.
	 *
	 * @see #put(String, CharSequence)
	 */
	public Phrase put(String key, boolean value) {
		return put(key, Boolean.toString(value));
	}

	/**
	 * Replaces the given key with the {@link String#valueOf(double)} value for the given double.
	 *
	 * @see #put(String, CharSequence)
	 */
	public Phrase put(String key, double value) {
		return put(key, String.valueOf(value));
	}

	/**
	 * Replaces the given key with the {@link String#valueOf(float)} value for the given float.
	 *
	 * @see #put(String, CharSequence)
	 */
	public Phrase put(String key, float value) {
		return put(key, String.valueOf(value));
	}

	/**
	 * Replaces the given key with the {@link String#valueOf(Object)} value for the given object.
	 *
	 * @see #put(String, CharSequence)
	 */
	public <T> Phrase put(String key, T value) {
		return put(key, String.valueOf(value));
	}

	/**
	 * Replaces the given key with the created String of each item's {@link String#valueOf(Object)}
	 * value for the given array item - chained with the provided separator.
	 *
	 * @param key - The key for which should be replaced with this array.
	 * @param values - The array which should be used for this replacement.
	 * @param separator - The separator which should be used to chain several items together.
	 *
	 * @see #putArray(String, Object[], String)
	 */
	public Phrase putArray(String key, int[] values, String separator) {
		Integer[] integers = new Integer[values.length];
		for(int i = 0; i < integers.length; i++) {
			integers[i] = values[i];
		}
		return putArray(key, integers, separator);
	}

	/**
	 * Replaces the given key with the created String of each item's {@link String#valueOf(Object)}
	 * value for the given array item - chained with the provided separator.
	 *
	 * @param key - The key for which should be replaced with this array.
	 * @param values - The array which should be used for this replacement.
	 * @param separator - The separator which should be used to chain several items together.
	 *
	 * @see #putArray(String, Object[], String)
	 */
	public Phrase putArray(String key, boolean[] values, String separator) {
		Boolean[] bools = new Boolean[values.length];
		for(int i = 0; i < bools.length; i++) {
			bools[i] = values[i];
		}
		return putArray(key, bools, separator);
	}

	/**
	 * Replaces the given key with the created String of each item's {@link String#valueOf(Object)}
	 * value for the given array item - chained with the provided separator.
	 *
	 * @param key - The key for which should be replaced with this array.
	 * @param values - The array which should be used for this replacement.
	 * @param separator - The separator which should be used to chain several items together.
	 *
	 * @see #putArray(String, Object[], String)
	 */
	public Phrase putArray(String key, float[] values, String separator) {
		Float[] floats = new Float[values.length];
		for(int i = 0; i < floats.length; i++) {
			floats[i] = values[i];
		}
		return putArray(key, floats, separator);
	}

	/**
	 * Replaces the given key with the created String of each item's {@link String#valueOf(Object)}
	 * value for the given array item - chained with the provided separator.
	 *
	 * @param key - The key for which should be replaced with this array.
	 * @param values - The array which should be used for this replacement.
	 * @param separator - The separator which should be used to chain several items together.
	 *
	 * @see #putArray(String, Object[], String)
	 */
	public Phrase putArray(String key, double[] values, String separator) {
		Double[] doubles = new Double[values.length];
		for(int i = 0; i < doubles.length; i++) {
			doubles[i] = values[i];
		}
		return putArray(key, doubles, separator);
	}

	/**
	 * Replaces the given key with the created String of each item's {@link String#valueOf(Object)}
	 * value for the given array item - chained with the provided separator.
	 *
	 * @param key - The key for which should be replaced with this array.
	 * @param values - The array which should be used for this replacement.
	 * @param separator - The separator which should be used to chain several items together.
	 *
	 * @see #put(String, CharSequence)
	 */
	public <T> Phrase putArray(String key, T[] values, String separator) {
		separator = validateSeparator(separator);
		StringBuilder chainedValues = new StringBuilder();
		List<T> typeList = Arrays.asList(values);
		Iterator<T> iterator = typeList.iterator();
		while (iterator.hasNext()) {
			T object = iterator.next();
			chainedValues.append(String.valueOf(object));
			if(iterator.hasNext()) {
				chainedValues.append(separator);
			}
		}
		return put(key, chainedValues.toString());
	}

	/**
	 * Silently ignored if the key is not in the pattern.
	 *
	 * @see #put(String, CharSequence)
	 */
	public Phrase putOptional(String key, CharSequence value) {
		return keys.contains(key) ? put(key, value) : this;
	}

	/**
	 * Replaces the given key, if it exists, with the {@link Integer#toString(int)} value
	 * for the given int.
	 *
	 * @see #putOptional(String, CharSequence)
	 */
	public Phrase putOptional(String key, int value) {
		return keys.contains(key) ? put(key, value) : this;
	}

	public Phrase putOptional(String key, boolean value) {
		return keys.contains(key) ? put(key, value) : this;
	}

	public Phrase putOptional(String key, float value) {
		return keys.contains(key) ? put(key, value) : this;
	}

	public Phrase putOptional(String key, double value) {
		return keys.contains(key) ? put(key, value) : this;
	}

	public <T> Phrase putOptional(String key, T value) {
		return keys.contains(key) ? put(key, value) : this;
	}

	/**
	 * Returns the text after replacing all keys with values.
	 *
	 * @throws IllegalArgumentException if any keys are not replaced.
	 */
	public CharSequence format() {
		if (formatted == null) {
			if (!keysToValues.keySet().containsAll(keys)) {
				Set<String> missingKeys = new HashSet<String>(keys);
				missingKeys.removeAll(keysToValues.keySet());
				throw new IllegalArgumentException("Missing keys: " + missingKeys);
			}

			// Copy the original pattern to preserve all spans, such as bold, italic, etc.
			StringBuilder sb = new StringBuilder(pattern);
			for (Token t = head; t != null; t = t.next) {
				t.expand(sb, keysToValues);
			}

			formatted = sb;
		}
		return formatted;
	}

	/**
	 * Returns the text after replacing all keys with values.
	 *
	 * @throws IllegalArgumentException if any keys are not replaced.
	 */
	public String formatString() {
		return format().toString();
	}

	/**
	 * Returns the raw pattern without expanding keys; only useful for debugging. Does not pass
	 * through to {@link #format()} because doing so would drop all spans.
	 */
	@Override
	public String toString() {
		return pattern.toString();
	}

	private Phrase(CharSequence pattern, KeyIdentifier keyIdentifier) {
		curChar = (pattern.length() > 0) ? pattern.charAt(0) : EOF;

		this.pattern = pattern;
		this.keyIdentifier = keyIdentifier;

		// A hand-coded lexer based on the idioms in "Building Recognizers By Hand".
		// http://www.antlr2.org/book/byhand.pdf.
		Token prev = null;
		Token next;
		while ((next = token(prev)) != null) {
			// Creates a doubly-linked list of tokens starting with head.
			if (head == null) head = next;
			prev = next;
		}
	}

	/** Makes sure that the given separator is not null or empty - if so the default separator is used. **/
	private String validateSeparator(String separator) {
		if(separator == null || separator.length() == 0) {
			separator = DEFAULT_SEPARATOR;
		}
		return separator;
	}

	/** Returns the next token from the input pattern, or null when finished parsing. */
	private Token token(Token prev) {
		if (curChar == EOF) {
			return null;
		}
		if (curChar == keyIdentifier.getOpenCharacter()) {
			char nextChar = lookahead();
			if (nextChar == keyIdentifier.getOpenCharacter()) {
				return leftCurlyBracket(prev);
			} else if (nextChar >= 'a' && nextChar <= 'z') {
				return key(prev);
			} else {
				throw new IllegalArgumentException(
						"Unexpected character '" + nextChar + "'; expected key.");
			}
		}
		return text(prev);
	}

	/** Parses a key: "{some_key}". */
	private KeyToken key(Token prev) {

		// Store keys as normal Strings; we don't want keys to contain spans.
		StringBuilder sb = new StringBuilder();

		// Consume the opening '{'.
		consume();
		while ((curChar >= 'a' && curChar <= 'z') || curChar == '_') {
			sb.append(curChar);
			consume();
		}

		// Consume the closing '}'.
		if (curChar != keyIdentifier.getCloseCharacter()) {
			throw new IllegalArgumentException("Missing closing brace: " + keyIdentifier.getCloseCharString());
		}
		consume();

		// Disallow empty keys: {}.
		if (sb.length() == 0) {
			throw new IllegalArgumentException("Empty key: " + keyIdentifier.getOpenCharString() + keyIdentifier.getCloseCharString());
		}

		String key = sb.toString();
		keys.add(key);
		return new KeyToken(prev, key);
	}

	/** Consumes and returns a token for a sequence of text. */
	private TextToken text(Token prev) {
		int startIndex = curCharIndex;

		while (curChar != keyIdentifier.getOpenCharacter() && curChar != EOF) {
			consume();
		}
		return new TextToken(prev, curCharIndex - startIndex);
	}

	/** Consumes and returns a token representing two consecutive curly brackets. */
	private LeftCurlyBracketToken leftCurlyBracket(Token prev) {
		consume();
		consume();
		return new LeftCurlyBracketToken(prev);
	}

	/** Returns the next character in the input pattern without advancing. */
	private char lookahead() {
		return curCharIndex < pattern.length() - 1 ? pattern.charAt(curCharIndex + 1) : EOF;
	}

	/**
	 * Advances the current character position without any error checking. Consuming beyond the
	 * end of the string can only happen if this parser contains a bug.
	 */
	private void consume() {
		curCharIndex++;
		curChar = (curCharIndex == pattern.length()) ? EOF : pattern.charAt(curCharIndex);
	}

	private abstract static class Token {
		private final Token prev;
		private Token next;

		protected Token(Token prev) {
			this.prev = prev;
			if (prev != null) prev.next = this;
		}

		/** Replace text in {@code target} with this token's associated value. */
		abstract void expand(StringBuilder target, Map<String, CharSequence> data);

		/** Returns the number of characters after expansion. */
		abstract int getFormattedLength();

		/** Returns the character index after expansion. */
		final int getFormattedStart() {
			if (prev == null) {
				// The first token.
				return 0;
			} else {
				// Recursively ask the predecessor node for the starting index.
				return prev.getFormattedStart() + prev.getFormattedLength();
			}
		}
	}

	/** Ordinary text between tokens. */
	private static class TextToken extends Token {
		private final int textLength;

		TextToken(Token prev, int textLength) {
			super(prev);
			this.textLength = textLength;
		}

		@Override
		void expand(StringBuilder target, Map<String, CharSequence> data) {
			// Don't alter spans in the target.
		}

		@Override
		int getFormattedLength() {
			return textLength;
		}
	}

	/** A sequence of two curly brackets. */
	private class LeftCurlyBracketToken extends Token {
		LeftCurlyBracketToken(Token prev) {
			super(prev);
		}

		@Override
		void expand(StringBuilder target, Map<String, CharSequence> data) {
			int start = getFormattedStart();
			target.replace(start, start + 2, keyIdentifier.getOpenCharString());
		}

		@Override
		int getFormattedLength() {
			// Replace {{ with {.
			return 1;
		}
	}

	private static class KeyToken extends Token {
		/** The key without { and }. */
		private final String key;

		private CharSequence value;

		KeyToken(Token prev, String key) {
			super(prev);
			this.key = key;
		}

		@Override
		void expand(StringBuilder target, Map<String, CharSequence> data) {
			value = data.get(key);

			int replaceFrom = getFormattedStart();
			// Add 2 to account for the opening and closing brackets.
			int replaceTo = replaceFrom + key.length() + 2;
			target.replace(replaceFrom, replaceTo, value.toString());
		}

		@Override
		int getFormattedLength() {
			// Note that value is only present after expand. Don't error check because this is all
			// private code.
			return value.length();
		}
	}

	public enum KeyIdentifier {
		CURLY_BRACKETS('{', '}'),
		ROUND_BRACKETS('(', ')'),
		ANGLE_BRACKETS('<', '>'),
		SQUARE_BRACKETS('[', ']');

		private final Pair<Character, Character> identifiers;

		KeyIdentifier(char open, char close) {
			this.identifiers = new Pair<>(open, close);
		}

		public char getOpenCharacter() {
			return identifiers.getKey();
		}

		public char getCloseCharacter() {
			return identifiers.getValue();
		}

		public String getOpenCharString() {
			return String.valueOf(getOpenCharacter());
		}

		public String getCloseCharString() {
			return String.valueOf(getCloseCharacter());
		}

	}

}