package com.wonderland.projects.AdventOfCode2019;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2019, Day 9">https://adventofcode.com/2019</a>
 *
 */
public class IntCodeProgram<K, V> extends HashMap<K, V> {
	protected V defaultValue;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8332240810019110832L;
	private static final Logger log = LogManager.getLogger();

	public IntCodeProgram(V defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public V get(Object key) {
		if ((int) key < 0) {
			log.error("NegativeKeyException for key[" + key + "].");
			// TODO: Make a new for of exception to throw for Negative Numbers
			throw new NumberFormatException();
		}
		V value = containsKey(key) ? super.get(key) : defaultValue;
		log.debug("Getting key[" + key + "]: " + value);

		return value;
	}

	@Override
	public V put(K key, V value) {
		log.debug("Putting key[" + key + "] with value[" + value + "].");
		return super.put(key, value);
	}

}
