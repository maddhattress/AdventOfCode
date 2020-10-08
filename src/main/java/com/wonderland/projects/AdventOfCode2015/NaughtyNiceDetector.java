package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2015, Day 5">https://adventofcode.com/2015/day/5</a>
 *
 */
public class NaughtyNiceDetector {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day5.txt";
	// private static final String INPUT = "input/2015/day5.sample.txt";
	// private static final String INPUT = "input/2015/day5Pt2.sample.txt";

	// Part 1 Nice Criteria
	// at least 3 vowels
	private static final String VOWELS_REGEX = "(.*[aeiou].*){3}";
	private static final Pattern VOWEL_PATTERN = Pattern.compile(VOWELS_REGEX);
	// at least 1 letter that appears twice in a row
	private static final String REPEATING_CHAR_REGEX = "(\\w)\\1+";
	private static final Pattern REP_CHAR_PATTERN = Pattern.compile(REPEATING_CHAR_REGEX);
	// does not contain "ab" "cd" "pq" "xy"
	private static final String NON_MATCHING_REGEX = "^((?!ab|cd|pq|xy).)*$";
	private static final Pattern NON_MATCH_PATTERN = Pattern.compile(NON_MATCHING_REGEX);

	// Part 2 Nice Criteria
	// contains 2 consecutive letters that appear at least 2x in the string without overlapping
	private static final String REPEATING_2CHARS_NO_OVERLAP_REGEX = "(\\w\\w).*\\1+";
	private static final Pattern REP_2CHAR_PATTERN = Pattern.compile(REPEATING_2CHARS_NO_OVERLAP_REGEX);
	// contains at least 1 letter which repeats with exactly 1 letter between them
	private static final String REPPEATING_CHAR_ONE_CHAR_BETWEEN_REGEX = "(\\w).{1}\\1+";
	private static final Pattern REP_CHAR_ONE_BET_PATTERN = Pattern.compile(REPPEATING_CHAR_ONE_CHAR_BETWEEN_REGEX);

	/**
	 * main processing unit
	 */
	public void run() {
		List<String> inputs = getInput();
		List<String> niceList = new ArrayList<String>();
		List<String> nice2List = new ArrayList<String>();
		for (String input : inputs) {
			boolean nice = NaughtyNiceDetector.checkNice(input);
			log.debug("String [" + input + "] is " + (nice ? "nice." : "not nice."));
			boolean nice2 = NaughtyNiceDetector.checkNice2(input);
			log.debug("String [" + input + "] is " + (nice ? "nice2." : "not nice2."));
			if (nice) {
				niceList.add(input);
			}
			if (nice2) {
				nice2List.add(input);
			}
		}
		log.info("Nice list count: " + niceList.size());
		log.info("Nice2 list count: " + nice2List.size());
	}

	/**
	 * checks to see if a string is nice (true) based on if it meets the required
	 * criteria
	 * 
	 * @param input
	 * @return
	 */
	public static final boolean checkNice(String input) {
		// at least 3 vowels
		if (!NaughtyNiceDetector.findPattern(VOWEL_PATTERN, input)) {
			log.debug("[" + input + "]: Doesn't contain at least 3 vowels");
			return false;
		}
		// at least one letter that appears twice in a row
		if (!NaughtyNiceDetector.findPattern(REP_CHAR_PATTERN, input)) {
			log.debug("[" + input + "]: Doesn't have at leat 1 repeating letter");
			return false;
		}
		// does not contain "ab" "cd" "pq" "xy"
		if (!NaughtyNiceDetector.findPattern(NON_MATCH_PATTERN, input)) {
			log.debug("[" + input + "]: Does contain ab | cd | pq | xy");
			return false;
		}

		return true;
	}

	/**
	 * checks to see if a string is nice (true) based on if it meets the required
	 * criteria for Part 2
	 * 
	 * @param input
	 * @return
	 */
	public static final boolean checkNice2(String input) {
		// contains 2 consecutive letters that appear at least twice in the string
		// without overlapping
		if (!NaughtyNiceDetector.findPattern(REP_2CHAR_PATTERN, input)) {
			log.debug("[" + input + "]: Doesn't contain 2 consecutive letters that appears 2x in the string without overlapping");
			return false;
		}
		// contains at least one letter which repeats with exactly one letter between
		// them
		if (!NaughtyNiceDetector.findPattern(REP_CHAR_ONE_BET_PATTERN, input)) {
			log.debug("[" + input + "]: Doesn't have 1 letter that repeats with exactly 1 letter between them");
			return false;
		}

		return true;
	}

	/**
	 * returns true if a pattern is found within the input string
	 * 
	 * @param pattern
	 * @param input
	 * @return
	 */
	public static final boolean findPattern(Pattern pattern, String input) {
		Matcher m = pattern.matcher(input);
		// check if instruction is valid.
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * read from the input file and put all the inputs into an ArrayList
	 * 
	 * @return
	 */
	private List<String> getInput() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
		List<String> input = reader.lines().collect(Collectors.toList());

		IOUtils.closeQuietly(reader);
		return input;
	}

	public static void main(String[] args) {
		NaughtyNiceDetector nnd = new NaughtyNiceDetector();
		nnd.run();
	}

}
