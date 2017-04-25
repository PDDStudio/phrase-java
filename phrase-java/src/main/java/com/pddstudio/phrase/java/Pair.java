package com.pddstudio.phrase.java;

import java.io.Serializable;

/**
 * Created by pddstudio on 29/11/2016.
 */
public class Pair<K, V> implements Serializable {

	private final K key;
	private final V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof Pair) {
			Pair pair = (Pair) o;
			if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
			if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return key + "=" + value;
	}

}
