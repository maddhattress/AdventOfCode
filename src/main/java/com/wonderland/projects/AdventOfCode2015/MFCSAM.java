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
 
	private static final Map<String,Integer> TICKER_TAPE = new HashMap<String, Integer>(){/**
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
	}};
	

	private void run() {
		Map<Integer, Aunt> aunts = this.readAndProcessInput();
		aunts.values().forEach(aunt->log.debug(aunt));
		
		log.info("The Aunt that bought the presents is #: " + aunts.get(Collections.max(aunts.keySet())));
	}

	/**
	 * reads the input from the input file
	 * 
	 * @return
	 */
	private Map<Integer, Aunt> readAndProcessInput() {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
		List<String> inputs = reader.lines().collect(Collectors.toList());
		IOUtils.closeQuietly(reader);

		Map<Integer, Aunt> aunts = new TreeMap<Integer, Aunt>();
		for (String input : inputs) {
			Aunt a = new Aunt(input);
			aunts.put(a.getScore(), a);
		}
		return aunts;
	}

	class Aunt {
		int number = 0;
		Map<String, Integer> attributes = new HashMap<String, Integer>();
		int score = 0; //score for Part 1
		int score2 = 0; //score for Part 2

		public Aunt(String input) {
			Matcher m = INPUT_PATTERN.matcher(input);
			if (m.find()) {
				this.number = Integer.parseInt(m.group(1));
				int index = 2;
				while(index < m.groupCount()) {
					String attribute = m.group(index);
					index++;
					int amount = Integer.parseInt(m.group(index));
					index++;
					this.calculateScore(attribute, amount);
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

		public void calculateScore(String attribute, int amount) {
			this.score = (TICKER_TAPE.containsKey(attribute) && TICKER_TAPE.get(attribute).equals(amount)) ? score + 5 : score; 
		}
		
		public void calculateScore2(String attribute, int amount) {
			
		}

		@Override
		public String toString() {
			return "Aunt [number=" + number + ", attributes=" + attributes + ", score=" + score + "]";
		}

	}

	public static void main(String[] args) {
		MFCSAM mfcsam = new MFCSAM();
		mfcsam.run();
	}

}
