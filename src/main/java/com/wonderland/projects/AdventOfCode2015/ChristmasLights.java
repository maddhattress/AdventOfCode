package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
 *      href="Advent of Code 2015, Day 6">https://adventofcode.com/2015/day/6</a>
 *
 */
public class ChristmasLights {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day6.txt";

	
	// instructions keywords
	private static final String TURN_ON = "turn on";
	private static final String TURN_OFF = "turn off";
	private static final String TOGGLE = "toggle";
	private static final String THROUGH = "through";
	private static final String INSTRUCTION_REGEX = 
			"^(" + TURN_ON + "|" + TURN_OFF + "|" + TOGGLE + ")\\s(\\d+),(\\d+)\\s"	+ THROUGH +  "\\s(\\d+),(\\d+)$";
			//e.g. turn on 887,9 through 959,629
	private static final Pattern INST_PATTERN = Pattern.compile(INSTRUCTION_REGEX);
	
	private static final int ON = 1;
	private static final int OFF = 0;
	
	private static final int UP = 1;
	private static final int DOWN = -1;

	private static final int GRID_SIZE = 1000;
	//by default, initializes to all zeros (off)
	private int[][] grid = new int[GRID_SIZE][GRID_SIZE];
	
	
	/**
	 * main processing unit for Part 1
	 */
	public void run() {
		grid = new int[GRID_SIZE][GRID_SIZE];
		List<String> instructions = this.readInstructions();
		for (String instruction : instructions) {
			Matcher m = INST_PATTERN.matcher(instruction);
			// check if instruction is valid.
			if (m.find() && m.groupCount() == 5) {
				String op = m.group(1);
				int startX = Integer.parseInt(m.group(2));
				int startY = Integer.parseInt(m.group(3));
				int endX = Integer.parseInt(m.group(4));
				int endY = Integer.parseInt(m.group(5));
				this.processInstruction(startX, startY, endX, endY, op);
			} else {
				log.error("Unknown instruction format for instruction[" + instruction + "].");
			}
		}
		log.info("Total number of lights on: " + this.countOnOrBrightness());

	}

	/**
	 * main processing unit for Part 2
	 */
	public void run2() {
		// make sure grid starts at all off
		grid = new int[GRID_SIZE][GRID_SIZE];
		List<String> instructions = this.readInstructions();
		for (String instruction : instructions) {
			Matcher m = INST_PATTERN.matcher(instruction);
			// check if instruction is valid.
			if (m.find() && m.groupCount() == 5) {
				String op = m.group(1);
				int startX = Integer.parseInt(m.group(2));
				int startY = Integer.parseInt(m.group(3));
				int endX = Integer.parseInt(m.group(4));
				int endY = Integer.parseInt(m.group(5));
				this.processInstructionPart2(startX, startY, endX, endY, op);
			} else {
				log.error("Unknown instruction format for instruction[" + instruction + "].");
			}
		}
		log.info("Total brightness: " + this.countOnOrBrightness());

	}

	/**
	 * performs a given operation on the range of lights in the grid specified
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param op
	 */
	private void processInstruction(int startX, int startY, int endX, int endY, String op) {
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				switch (op) {
				case TURN_ON:
					grid[x][y] = ON;
					break;
				case TURN_OFF:
					grid[x][y] = OFF;
					break;
				case TOGGLE:
					grid[x][y] = (grid[x][y] == OFF) ? ON : OFF;
					break;
				default:
					log.error("Unknown operation[" + op + "].");
					break;
				}
			}
		}
	}

	/**
	 * performs a given operation on the range of lights in the grid specified for
	 * Part 2
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param op
	 */
	private void processInstructionPart2(int startX, int startY, int endX, int endY, String op) {
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				switch (op) {
				case TURN_ON:
					grid[x][y] += UP;
					break;
				case TURN_OFF:
					grid[x][y] += (grid[x][y] == OFF) ? OFF : DOWN;
					break;
				case TOGGLE:
					grid[x][y] += (UP + UP);
					break;
				default:
					log.error("Unknown operation[" + op + "].");
					break;
				}
			}
		}
	}

	/**
	 * counts the number of lights that are on
	 * 
	 * @return
	 */
	private int countOnOrBrightness() {
		int count = 0;
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				count += grid[x][y];
			}
		}
		return count;
	}

	/**
	 * logs in debug the grid used for debugging
	 */
	@SuppressWarnings("unused")
	private void printGrid() {
		for (int x = 0; x < grid.length; x++) {
			StringBuilder row = new StringBuilder();
			for (int y = 0; y < grid[0].length; y++) {
				row.append(grid[x][y] + " ");
			}
			log.debug(row);
		}
	}

	/**
	 * reads the instructions from the input file
	 * 
	 * @return
	 */
	private List<String> readInstructions() {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
		List<String> instructions = reader.lines().collect(Collectors.toList());

		IOUtils.closeQuietly(reader);
		return instructions;

	}

	public static void main(String[] args) {
		ChristmasLights cl = new ChristmasLights();
		cl.run();
		cl.run2();

	}

}
