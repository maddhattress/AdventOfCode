package com.wonderland.projects.AdventOfCode2019;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2019, Day 3">https://adventofcode.com/2019/day/3</a>
 *
 */
public class CrossedWires {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "2019/input/day3.txt";

	private static final int[] CENTRAL_POINT = { 0, 0 }; // [x,y]

	/**
	 * main processing unit
	 */
	private void run() {
		BufferedReader reader = null;
		// read input file and if it contains 2 lines, put those into the wire1 and
		// wire2paths
		reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
		List<String> linesInFile = reader.lines().collect(Collectors.toList());
		log.debug("Number of lines in file: " + linesInFile.size());
		if (linesInFile.size() != 2) {
			log.error("Input file " + INPUT + " does not have 2 lines as expected.");
			return;
		}

		List<int[]> wire1Path = findPath(Arrays.asList(linesInFile.get(0).split(",")));
		List<int[]> wire2Path = findPath(Arrays.asList(linesInFile.get(1).split(",")));
		for (int wire1Move = 1; wire1Move < wire1Path.size(); wire1Move++) {

			for (int wire2Move = 1; wire2Move < wire2Path.size(); wire2Move++) {
				int[] wire1From = wire1Path.get(wire1Move - 1);
				int[] wire1To = wire1Path.get(wire1Move);

				int[] wire2From = wire2Path.get(wire2Move - 1);
				int[] wire2To = wire2Path.get(wire2Move);
				// if distance between x between move 1 and move 2 of wire1 crosses the x of
				// wire2
				// if distance between y between move 1 and move 2 of wire1 crosses the y of
				// wire2

				if (wire1From[0] != wire1To[0]) { // horizontal movement of wire1
					int xaxis = wire1From[0];
					// calculate
					if (wire2From[0] < xaxis && xaxis > wire2To[0]) {
						int yaxis = wire2From[1];

						if (wire1From[1] < yaxis && yaxis > wire1To[1]) {

							log.debug("WIRE1[" + wire1Move + "]: " + Arrays.toString(wire1From) + " to "
									+ Arrays.toString(wire1To));
							log.debug("WIRE2[" + wire2Move + "]:" + Arrays.toString(wire2From) + " to "
									+ Arrays.toString(wire2To));
							log.debug("CROSS?[" + wire2From[0] + "," + wire1From[1] + "]");
						}
					}
				} else if (wire1From[1] != wire1To[1]) { // vertical movement of wire1
					int yaxis = wire1From[1];

					if (yaxis < yaxis && yaxis > wire2To[1]) {
						int xaxis = wire2From[0];

						if (wire1From[0] < xaxis && xaxis > wire1To[0]) {

							log.debug("WIRE1[" + wire1Move + "]: " + Arrays.toString(wire1From) + " to "
									+ Arrays.toString(wire1To));
							log.debug("WIRE2[" + wire2Move + "]:" + Arrays.toString(wire2From) + " to "
									+ Arrays.toString(wire2To));
							log.debug("CROSS?[" + wire1From[0] + "," + wire2From[1] + "]");
						}
					}
				}

			}
		}

		IOUtils.closeQuietly(reader);

	}

	private List<int[]> findPath(List<String> wire) {
		List<int[]> path = new ArrayList<int[]>();
		int[] position = CENTRAL_POINT.clone();
		path.add(position);
		for (String turn : wire) {
			int val = Integer.parseInt(turn.substring(1));
			log.debug("1: " + Arrays.toString(position));
			int[] newPosition = position.clone();
			if (turn.startsWith("D")) {
				newPosition[1] = newPosition[1] - val;
			} else if (turn.startsWith("L")) {
				newPosition[0] = newPosition[0] - val;
			} else if (turn.startsWith("R")) {
				newPosition[0] = newPosition[0] + val;
			} else if (turn.startsWith("U")) {
				newPosition[1] = newPosition[1] + val;
			} else {
				log.error("OHHHSS NOOSSSSSS BAD DATA!");
			}
			log.debug("2: " + Arrays.toString(position));
			path.add(newPosition);
			position[0] = newPosition[0];
			position[1] = newPosition[1];
		}

		return path;
	}

	public static void main(String[] args) {
		CrossedWires cw = new CrossedWires();
		cw.run();
	}

}
