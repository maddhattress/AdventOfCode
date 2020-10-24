package com.wonderland.projects.AdventOfCode2015;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	// Sue 4: goldfish: 10, akitas: 2, perfumes: 9
	private static final String INPUT_REGEX = "^" + AUNT + "(\\d+): ";
	private static final Pattern INPUT_PATTERN = Pattern.compile(INPUT_REGEX);
	
	enum Attribute{
		CHILDREN, CATS, SAMOYEDS, POMERANIANS, AKITAS, VIZSLAS, GOLDFISH, TREES, CARS, PERFUMES
	}

	private void run() {
		
	}
	
	class Aunt {
		public Aunt(String instruction) {
			// Matcher m = INPUT_PATTERN.matcher(input);
			// if (m.find() && m.groupCount() == 3) {
			// group(0) = entire group
			// this.length = Integer.parseInt(m.group(1));
			// this.width = Integer.parseInt(m.group(2));
			// }
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
