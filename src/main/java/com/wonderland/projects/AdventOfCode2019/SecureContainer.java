package com.wonderland.projects.AdventOfCode2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2019, Day 4">https://adventofcode.com/2019/day/4</a>
 *
 */
public class SecureContainer {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/day4.txt";
	
	private int MIN;
	private int MAX; 


	private static final int PASSWORD_LENGTH = 6;
	// input: 136760-595730
	// 6 digit number
	// within range
	// two adjacent digits
	// only increase left to right

	// valid input: 136,777 - 589,999

	/**
	 * read input file and set min/max values. 
	 */
	public SecureContainer() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
			String line = reader.readLine();
			MIN = Integer.parseInt(StringUtils.substringBefore(line, "-"));
			MAX = Integer.parseInt(StringUtils.substringAfter(line, "-"));
		} catch (IOException ioe) {
			log.error("IOException while reading iput file " + INPUT, ioe);
		}
	}
	
	/**
	 * calculate all the possible values for the password
	 * @return
	 */
	private int calculatePossiblePasswords() {
		int val = MIN;
		int counter = 0;
		List<Integer> possibilities = new ArrayList<Integer>();

		while (val <= MAX) {
			String strVal = Integer.toString(val);
			char[] charVal = strVal.toCharArray();

			for (int c = 1; c < PASSWORD_LENGTH; c++) {
				if (charVal[c] < charVal[c - 1]) {
					charVal[c] = charVal[c - 1];
				}
			}
			val = Integer.parseInt(new String(charVal));
			if (this.checkDuplicates(charVal) && val <= MAX && this.checkStrictDuplicates(charVal)) {
				log.debug("Valid possible value: " + val);
				possibilities.add(val);
				counter++;
			}
			val++;
		}
		return counter;
	}

	/**
	 * check to see if there exists a duplicate
	 * @param charVal
	 * @return
	 */
	public boolean checkDuplicates(char[] charVal) {
		for (int c = 1; c < PASSWORD_LENGTH; c++) {
			if (charVal[c] == charVal[c - 1]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * check to see if there is at least 1 duplicate and note a triple
	 * @param charVal
	 * @return
	 */
	public boolean checkStrictDuplicates(char[] charVal) {
		Map<Integer, Integer> frequency = new HashMap<Integer, Integer>();
		for (int c = 1; c < PASSWORD_LENGTH; c++) {
			if (charVal[c] == charVal[c - 1]) {
				int key = Character.getNumericValue(charVal[c]);
				if (frequency.containsKey(key)) {
					frequency.replace(key, (frequency.get(key)) + 1);
				} else {
					frequency.put(key, 2);
				}
			}
		}
		if (frequency.containsValue(2)) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SecureContainer sc = new SecureContainer();
		log.info("Total Passwords Possible: " + sc.calculatePossiblePasswords());

	}

}
