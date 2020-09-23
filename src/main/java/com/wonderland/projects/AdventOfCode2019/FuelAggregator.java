package com.wonderland.projects.AdventOfCode2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class FuelAggregator {
	private int individualFuel = 0; 

	private int run(String filename) {
		int totalFuel = 0;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				this.calculateFuel(Integer.parseInt(line));
				totalFuel += this.individualFuel;
				individualFuel = 0; 
			}
		} catch (IOException ex) {
			System.err.println("ERROR reading file: " + ex.getMessage());
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return totalFuel;
	}

	/*
	 * calculates the amount of fuel used based off of the mass provided
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
		int total = ag.run(args[0]);
		System.out.println("Total fuel used: " + total);
	}

}
