package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WrappingCalculator {

	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day2.txt";

	private static final String PRISM_PATTERN = "^(\\d+)x(\\d+)x(\\d+)$";
	private static final Pattern PATTERN = Pattern.compile(PRISM_PATTERN);

	/**
	 * main processing unit
	 */
	private void run() {
		long totalWP = 0L;
		long totalRibbon = 0L;
		List<String> instructions = this.readInstructions();
		for (String instruction : instructions) {
			Measurement measurement = new Measurement(instruction);
			totalWP += WrappingCalculator.calculateSurfaceArea(measurement);
			totalRibbon += WrappingCalculator.calculateVolume(measurement);
			totalRibbon += WrappingCalculator.calculatPerimeter(measurement);
		}
		log.info("Total Wrapping Paper needed: " + totalWP);
		log.info("Total Ribbon needed: " + totalRibbon);
	}

	/**
	 * read from the input file and put all the instructions into an arraylist
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

	/**
	 * calculate the surface area for the given instruction
	 * 
	 * @param instruction
	 * @return
	 */
	private static final long calculateSurfaceArea(Measurement meas) {
		log.debug("Calculating Surface Area for measurements: " + meas);
		long area = 0L;
		int length = meas.getLength();
		int width = meas.getWidth();
		int height = meas.getHeight();

		// identify sides to calculate slack
		List<Integer> sides = new ArrayList<Integer>();
		sides.add(length * width);
		sides.add(width * height);
		sides.add(height * length);

		// TODO: Figure out how to make this a constant and do replaces and still have
		// it math properly
		area = (2 * sides.get(0)) + (2 * sides.get(1)) + (2 * sides.get(2));
		log.debug("2*LENGTH[" + length + "]*WIDTH[" + width + "] " + "+ 2*WIDTH[" + width + "]*HEIGHT[" + height + "] "
				+ "+ 2*HEIGHT[" + height + "]*LENGTH[" + length + "]" + "=" + area);

		// slack is the smallest side
		int slack = Collections.min(sides);
		log.debug("Slack: " + slack);
		area += slack;

		return area;
	}
	
	/**
	 * calculate volume
	 * @param meas
	 * @return
	 */
	private static final long calculateVolume(Measurement meas) {
		log.debug("Calculating Volume for measurements: " + meas);
		long volume = 0L;
		int length = meas.getLength();
		int width = meas.getWidth();
		int height = meas.getHeight();

		volume = length * width * height;
		log.debug("LENGTH[" + length + "] * WIDTH[" + width + "] * HEIGHT[" + height + "] = " + volume);
		return volume;
	}

	/**
	 * calculate perimeter
	 * @param meas
	 * @return
	 */
	private static final long calculatPerimeter(Measurement meas) {
		log.debug("Calculating Perimeter for measurements: " + meas);
		long perimeter = 0L;

		List<Integer> sides = new ArrayList<Integer>();
		sides.add(meas.getLength());
		sides.add(meas.getWidth());
		sides.add(meas.getHeight());

		Collections.sort(sides);

		int side1 = sides.get(0);
		int side2 = sides.get(1);

		perimeter = side1 + side1 + side2 + side2;
		log.debug("SIDE1[" + side1 + "] + SIDE1[" + side1 + "] + SIDE2[" + side2 + "]  + SIDE2 [" + side2 + "]= "
				+ perimeter);
		return perimeter;
	}

	public static void main(String[] args) {
		WrappingCalculator wpc = new WrappingCalculator();
		wpc.run();
	}

	class Measurement {
		int length = 0;
		int width = 0;
		int height = 0;
		String instruction;

		public Measurement(String instruction) {
			this.instruction = instruction;
			Matcher m = PATTERN.matcher(instruction);
			// check if instruction is valid.
			if (m.find() && m.groupCount() == 3) {
				// group(0) = entire group
				this.length = Integer.parseInt(m.group(1));
				this.width = Integer.parseInt(m.group(2));
				this.height = Integer.parseInt(m.group(3));
			} else {
				log.error("Format Error with instruction: " + instruction);
			}
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		@Override
		public String toString() {
			return "Measurement [length=" + length + ", width=" + width + ", height=" + height + "]";
		}

	}

}
