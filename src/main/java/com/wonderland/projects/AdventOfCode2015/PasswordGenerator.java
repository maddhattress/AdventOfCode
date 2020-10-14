package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2015, Day 11">https://adventofcode.com/2015/day/11</a>
 *
 */
public class PasswordGenerator {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day11.txt";

	// at least 1 letter that appears twice in a row
	private static final String REPEATING_CHAR_REGEX = "(\\w)\\1+";
	private static final Pattern REP_CHAR_PATTERN = Pattern.compile(REPEATING_CHAR_REGEX);

	private static final char[] INVALID_CHARS = { 'i', 'o', 'l' };

	private static final int A = (int) 'a';
	private static final int Z = (int) 'z';

	private char[] currPassArray;
	int rightMost = 0;

	/**
	 * main processing unit
	 */
	private void run() {
		String currentPassword = this.getInput();
		String newPassword = this.findNextPassword(currentPassword);
		log.info("Next available password: " + newPassword);
		newPassword = this.findNextPassword(newPassword);
		log.info("Next available password: " + newPassword);

	}

	/**
	 * finds the next password that meets the required criteria
	 * @param currentPassword
	 * @return
	 */
	private String findNextPassword(String currentPassword) {
		String newPassword = "";
		currPassArray = currentPassword.toCharArray();
		rightMost = currPassArray.length - 1;
		// must have at least 3 sequential letters (e.g. abc, cde, bcd)
		// can't have 'i' 'o' or 'l'
		// must have at least two different, non-overlapping pairs of letters (e.g. aa,
		// bb, zz)

		do {
			this.updateIndex(rightMost);
			newPassword = new String(currPassArray);
		}while (!validatePassword(newPassword));

		return newPassword;
	}

	/**
	 * recursive method to update the index
	 * @param index
	 * @return
	 */
	private int updateIndex(int index) {
		log.debug("index[" + index + "]");
		if(index != 0) {
			int currentChar = (int) currPassArray[index];
			int nextChar = (currentChar == Z) ? A : currentChar + 1;
			currPassArray[index] = (char) nextChar;
			log.debug("Updating next character: " + new String(currPassArray));
			if(nextChar == A) {
				log.debug("Updating index[" + (index-1) + "]");
				return updateIndex(index-1);
			}
			return 0;
		}else {
			return rightMost;
		}
	}

	/**
	 * validates if the password meets the required criteria
	 * @param password
	 * @return
	 */
	public static final boolean validatePassword(String password) {
		log.debug("Checking password[" + password + "]");
		if (!PasswordGenerator.consecutive3LetterCheck(password)) {
			return false;
		}
		if (!PasswordGenerator.doubleChar2xCheck(password)) {
			return false;
		}
		if (!PasswordGenerator.allValidCharCheck(password)) {
			return false;
		}
		return true;
	}

	/**
	 * checks to see if password has 3 consecutive letters
	 * @param password
	 * @return
	 */
	public static final boolean consecutive3LetterCheck(String password) {
		int counter = 1;
		char[] currPassArray = password.toCharArray();
		// starting at 1 so i can grab previous
		for (int c = 1; c < currPassArray.length; c++) {
			char curChar = currPassArray[c];
			char prevChar = currPassArray[c - 1];
			counter = ((int) prevChar + 1 == (int) curChar) ? counter + 1 : 1;
			if (counter == 3) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * checks to see if password contains at least 2 double characters
	 * @param password
	 * @return
	 */
	public static final boolean doubleChar2xCheck(String password) {
		Bag<String> doubles = new HashBag<String>();
		Matcher m = REP_CHAR_PATTERN.matcher(password);
		// check if instruction is valid.
		while (m.find()) {
			// each time it finds a match add it to the bag
			doubles.add(m.group(1));
		}
		// if there are at least 2, password is valid
		if (doubles.uniqueSet().size() >= 2) {
			return true;
		}
		return false;
	}

	/**
	 * checks to see if password contains any all characters
	 * @param password
	 * @return
	 */
	public static final boolean allValidCharCheck(String password) {
		if (StringUtils.containsAny(password, INVALID_CHARS)) {
			return false;
		}
		return true;
	}

	/**
	 * retrieves the password from the input file
	 * 
	 * @return
	 */
	private String getInput() {
		BufferedReader reader = null;
		String line = new String();
		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
			line = reader.readLine();
		} catch (IOException ioe) {
			log.error("IOException while reading iput file " + INPUT, ioe);
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return line;
	}

	public static void main(String[] args) {
		PasswordGenerator pg = new PasswordGenerator();
		pg.run();
	}

}
