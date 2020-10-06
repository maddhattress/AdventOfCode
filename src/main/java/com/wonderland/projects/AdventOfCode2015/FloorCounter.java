package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2015, Day 1">https://adventofcode.com/2015/day/1</a>
 *
 */
public class FloorCounter {

	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day1.txt";

	private static final int START_FLOOR = 0;
	private static final int BASEMENT = -1;
	private static final String MOVE_UP = "(";
	private static final String MOVE_DOWN = ")";
	private static final int UP_AMT = 1;
	private static final int DOWN_AMT = -1;

	/**
	 * main processing unit
	 */
	private void run() {
		String instructions = this.readInstructions();
		int currentFloor = this.calculateFloor(instructions);
		log.info("Current Floor: " + currentFloor);
	}

	/**
	 * reads the input file and puts them into an instruction String
	 * 
	 * @return
	 */
	private String readInstructions() {
		BufferedReader reader = null;
		StringBuilder instructions = new StringBuilder();

		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
			String line;
			while ((line = reader.readLine()) != null) {
				// assumes all lines are apart of the instructions
				instructions.append(line);
			}
		} catch (IOException ex) {
			log.error("IOException reading input file " + INPUT, ex);
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return instructions.toString();
	}

	/**
	 * calculates the currentFloor based on inputed instructions
	 * and identify output what instruction number when the basement is reached
	 * 
	 * @param i
	 * @return
	 */
	private int calculateFloor(String i) {
		int currentFloor = START_FLOOR;
		List<String> instructions = Arrays.asList(i.split(""));
		log.debug("Instructions: " + instructions.toString());

		boolean basementIdentified = false;

		for (int x = 0; x < instructions.size(); x++) {
			String instruction = instructions.get(x);

			switch (instruction) {
			case MOVE_UP:
				currentFloor += UP_AMT;
				break;
			case MOVE_DOWN:
				currentFloor += DOWN_AMT;
				break;
			}

			if ((!basementIdentified) && (currentFloor == BASEMENT)) {
				log.debug("Found Basement! " + currentFloor);
				basementIdentified = true;
				int basementFloorInstruction = x + 1;
				log.info("Basement Floor Instruction: " + basementFloorInstruction);
			}
		}

		return currentFloor;
	}

	public static void main(String[] args) {
		FloorCounter fc = new FloorCounter();
		fc.run();
	}

}
