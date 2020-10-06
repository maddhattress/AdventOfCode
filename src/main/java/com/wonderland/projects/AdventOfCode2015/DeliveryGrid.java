package com.wonderland.projects.AdventOfCode2015;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2015, Day 1">https://adventofcode.com/2015/day/3</a>
 *
 */
public class DeliveryGrid {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day3.txt";

	private static final Point START_POINT = new Point(); // initializes to (0,0)
	private static final String MOVE_UP = "^";
	private static final String MOVE_DOWN = "v";
	private static final String MOVE_RIGHT = ">";
	private static final String MOVE_LEFT = "<";

	/**
	 * main processing unit
	 */
	private void run() {
		int uniqueHouses = this.runSanta();
		log.info("Number of houses that get at least one present from Santa: " + uniqueHouses);
		uniqueHouses = this.runWithRoboSanta();
		log.info("Number of houses that get at least one present from Santa or Robo-Santa: " + uniqueHouses);
	}

	/**
	 * calculate number of unique houses that get at least one present with JUST
	 * Santa
	 * 
	 * @return
	 */
	private int runSanta() {
		List<String> instructions = this.readInstructions();
		Point currentPoint = new Point(START_POINT);
		Bag<Point> deliveryPoints = new HashBag<Point>();
		// initial point is included in count of houses
		deliveryPoints.add(currentPoint);
		for (String instruction : instructions) {
			Point newPoint = this.parseInstruction(instruction, currentPoint);
			log.debug("New Point: " + newPoint);
			deliveryPoints.add(newPoint);
			currentPoint = new Point(newPoint);
		}

		int uniqueHouses = deliveryPoints.uniqueSet().size();
		return uniqueHouses;
	}

	/**
	 * round robin instructions between Santa and Robo-Santa, how many unique houses get presents
	 * @return
	 */
	private int runWithRoboSanta() {
		List<String> instructions = this.readInstructions();
		Point currentSantaPoint = new Point(START_POINT);
		Point currentRoboSantaPoint = new Point(START_POINT);
		Bag<Point> deliveryPoints = new HashBag<Point>();

		// initial point is included in count of houses
		deliveryPoints.add(currentSantaPoint);
		deliveryPoints.add(currentRoboSantaPoint);

		boolean santaTurn = true;
		for (String instruction : instructions) {
			Point newPoint;
			if (santaTurn) {
				newPoint = this.parseInstruction(instruction, currentSantaPoint);
				log.debug("New Santa Point: " + newPoint);
				currentSantaPoint = new Point(newPoint);
				santaTurn = false;
			} else { // robo santa turn
				newPoint = this.parseInstruction(instruction, currentRoboSantaPoint);
				log.debug("New Robo-Santa Point: " + newPoint);
				currentRoboSantaPoint = new Point(newPoint);
				santaTurn = true;
			}
			deliveryPoints.add(newPoint);
		}

		int uniqueHouses = deliveryPoints.uniqueSet().size();
		return uniqueHouses;
	}

	/**
	 * reads the input file and puts them into an instruction String
	 * 
	 * @return
	 */
	private List<String> readInstructions() {
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
		return Arrays.asList(instructions.toString().split(""));
	}

	/**
	 * interpret the instruction and update to the next point
	 * 
	 * @param instruction
	 * @param p
	 * @return
	 */
	private Point parseInstruction(String instruction, Point p) {
		Point newPoint = new Point();
		switch (instruction) {
		case MOVE_UP:
			newPoint = new Point(p.x, p.y + 1);
			break;
		case MOVE_DOWN:
			newPoint = new Point(p.x, p.y - 1);
			break;
		case MOVE_RIGHT:
			newPoint = new Point(p.x + 1, p.y);
			break;
		case MOVE_LEFT:
			newPoint = new Point(p.x - 1, p.y);
			break;
		default:
			log.error("Unknown Instruction Error: " + instruction);
			break;
		}
		return newPoint;
	}

	public static void main(String[] args) {
		DeliveryGrid dg = new DeliveryGrid();
		dg.run();
	}

}
