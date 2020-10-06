package com.wonderland.projects.AdventOfCode2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2019, Day 1">https://adventofcode.com/2019/day/1</a>
 *
 */
public class FuelAggregator {
	
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2019/day1.txt"; 
	private int individualFuel = 0; 

	/**
	 * main processing unit
	 * @return
	 */
	private int run() {
		int totalFuel = 0;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
			String line;
			while ((line = reader.readLine()) != null) {
				this.calculateFuel(Integer.parseInt(line));
				totalFuel += this.individualFuel;
				individualFuel = 0; 
			}
		} catch (IOException ex) {
			log.error("IOException reading file: " + INPUT, ex);
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return totalFuel;
	}

	/**
	 * calculates the amount of fuel used based off of the mass provided
	 * @param mass
	 * @return
	 */
	private int calculateFuel(int mass) {
		int fuelForMass = ((int)Math.floor(mass / 3)) - 2;
		if( fuelForMass > 0) {
			this.individualFuel +=fuelForMass;
			this.calculateFuel(fuelForMass);
		}

		return fuelForMass;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FuelAggregator ag = new FuelAggregator();
		int total = ag.run();
		log.info("Total fuel used: " + total);
	}

}
