package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2015, Day 10">https://adventofcode.com/2015/day/10</a>
 *
 */
public class LookAndSayGame {

	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day10.txt";

	private static final int DAY1_ROUNDS = 40;
	private static final int DAY2_ROUNDS = 50;

	/**
	 * main processing unit
	 */
	private void run() {
		String input = getInput();

		String output = "";
		for (int round = 0; round < DAY2_ROUNDS; round++) {
			output = iterateGameOnce(input);
			log.debug("Output length after round[" + round + "]: " + output.length());
			input = output;

			if (round == DAY1_ROUNDS) {
				log.info("Output length after " + round + " rounds: " + output.length());
			}
		}

		log.info("Output length after " + DAY2_ROUNDS + " rounds: " + output.length());
	}

	/**
	 * run one iteration of the game
	 * 
	 * @param input
	 * @return
	 */
	private String iterateGameOnce(String input) {
		StringBuilder output = new StringBuilder();

		// return if input is null or not numeric
		if (StringUtils.isBlank(input) && !StringUtils.isNumeric(input)) {
			log.error("Empty input to iterateGameOnce.");
			return output.toString();
		}

		List<String> inputValues = Arrays.asList(input.toString().split(""));
		String prevVal = "";
		int counter = 0;
		for (String iv : inputValues) {
			if (!prevVal.contentEquals(iv) && counter > 0) {
				output.append(counter + prevVal);
				counter = 0;
			}
			prevVal = iv;
			counter++;
		}
		// output last value
		output.append(counter + prevVal);

		return output.toString();

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
		LookAndSayGame lnsg = new LookAndSayGame();
		lnsg.run();
	}

}
