package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2015, Day 16">https://adventofcode.com/2015/day/16</a>
 *
 */
public class MFCSAM {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day16.txt";

	private static final String AUNT = "Sue";

	private static final String INPUT_REGEX = "^" + AUNT + " (\\d+): (\\w+): (\\d+), (\\w+): (\\d+), (\\w+): (\\d+)$";
	private static final Pattern INPUT_PATTERN = Pattern.compile(INPUT_REGEX);

	private static final Map<String, Integer> TICKER_TAPE = new HashMap<String, Integer>() {
		/**
		* 
		*/
		private static final long serialVersionUID = 5587982375618196734L;

		{
			put("children", 3);
			put("cats", 7);
			put("samoyeds", 2);
			put("pomeranians", 3);
			put("akitas", 0);
			put("vizslas", 0);
			put("goldfish", 5);
			put("trees", 3);
			put("cars", 2);
			put("perfumes", 1);
		}
	};

	private static final Map<String, Integer> GREATER_THAN_TICKER_TAPE = new HashMap<String, Integer>() {
		/**
		* 
		*/
		private static final long serialVersionUID = 5804534214884917674L;

		{
			put("cats", TICKER_TAPE.get("cats"));
			put("trees", TICKER_TAPE.get("trees"));
		}
	};

	private static final Map<String, Integer> LESS_THAN_TICKER_TAPE = new HashMap<String, Integer>() {
		/**
		* 
		*/
		private static final long serialVersionUID = -825267466993843265L;

		{
			put("pomeranians", TICKER_TAPE.get("pomeranians"));
			put("goldfish", TICKER_TAPE.get("goldfish"));
		}
	};

	/**
	 * main processing unit
	 */
	private void run() {
		Map<Integer, Aunt> aunts = this.readAndProcessInput(false);
		aunts.values().forEach(aunt -> log.debug(aunt));

		log.info("The Aunt that bought the Part 1 presents is #: " + aunts.get(Collections.max(aunts.keySet())));

		aunts = this.readAndProcessInput(true);
		aunts.values().forEach(aunt -> log.debug(aunt));
		log.info("The Aunt that bought the Part 2 presents is #: " + aunts.get(Collections.max(aunts.keySet())));
	}

	/**
	 * reads the input from the input file and calculates score1 or score2 for the
	 * aunts
	 * 
	 * @return
	 */
	private Map<Integer, Aunt> readAndProcessInput(boolean score2) {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
		List<String> inputs = reader.lines().collect(Collectors.toList());
		IOUtils.closeQuietly(reader);

		Map<Integer, Aunt> aunts = new TreeMap<Integer, Aunt>();
		for (String input : inputs) {
			Aunt a = new Aunt(input);
			if (score2 && a.getScore2() > 0) {
				aunts.put(a.getScore2(), a);
			} else {
				aunts.put(a.getScore(), a);
			}
		}
		return aunts;
	}

	class Aunt {
		int number = 0;
		Map<String, Integer> attributes = new HashMap<String, Integer>();
		int score = 0; // score for Part 1
		int score2 = 0; // score for Part 2

		public Aunt(String input) {
			Matcher m = INPUT_PATTERN.matcher(input);
			if (m.find()) {
				this.number = Integer.parseInt(m.group(1));
				int index = 2;
				while (index < m.groupCount()) {
					String attribute = m.group(index);
					index++;
					int amount = Integer.parseInt(m.group(index));
					index++;
					this.calculateScore(attribute, amount);
					this.calculateScore2(attribute, amount);
					attributes.put(attribute, amount);

				}
			}
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public Map<String, Integer> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, Integer> attributes) {
			this.attributes = attributes;
		}

		public int getScore() {
			return score;
		}

		public int getScore2() {
			return score2;
		}

		public void calculateScore(String attribute, int amount) {
			this.score = (TICKER_TAPE.containsKey(attribute) && TICKER_TAPE.get(attribute).equals(amount)) ? score + 5
					: score;
		}

		public void calculateScore2(String attribute, int amount) {
			if (GREATER_THAN_TICKER_TAPE.containsKey(attribute) && amount > GREATER_THAN_TICKER_TAPE.get(attribute)) {
				this.score2 += 5;
			} else if (LESS_THAN_TICKER_TAPE.containsKey(attribute) && amount < LESS_THAN_TICKER_TAPE.get(attribute)) {
				this.score2 += 5;
			} else {
				this.score2 = (TICKER_TAPE.containsKey(attribute) && TICKER_TAPE.get(attribute).equals(amount))
						? score2 + 5
						: score2;
			}
		}

		@Override
		public String toString() {
			return "Aunt [number=" + number + ", attributes=" + attributes + ", score=" + score + ", score2=" + score2
					+ "]";
		}

	}

	public static void main(String[] args) {
		MFCSAM mfcsam = new MFCSAM();
		mfcsam.run();
	}

}
