package com.wonderland.projects.AdventOfCode2019;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2019, Day 7, Part 1">https://adventofcode.com/2019/day/7</a>
 *
 */
public class AmplificationCircuit {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "2019/input/day7.txt";
	//private static final String INPUT = "input/day7.sample.txt";
	
	/** List of all the possible phase settings **/
	private static final List<String> PHASE_SETTINGS = Arrays.asList(new String[] { "0", "1", "2", "3", "4" });

	/**
	 * main processing unit
	 * 
	 * @return the highest possible signal that can be sent to the thrusters
	 */
	private int run() {
		// iterate thru all the permutations of the phase settings
		PermutationIterator<String> iter = new PermutationIterator<String>(PHASE_SETTINGS);
		int highestSignal = 0;
		List<String> bestPhaseSettingCombo = new ArrayList<String>();
		while (iter.hasNext()) {
			List<String> phaseSettingCombo = iter.next();
			int signal = calculateSignal(phaseSettingCombo);
			if (signal > highestSignal) {
				highestSignal = signal;
				bestPhaseSettingCombo = phaseSettingCombo;
			}
		}
		log.info("Highest Signal: " + highestSignal);
		log.info("Best Combo: " + bestPhaseSettingCombo);
		return highestSignal;
	}

	/**
	 * calculate the signal for a given phase setting list
	 * 
	 * @param phaseSettings
	 * @return
	 */
	private int calculateSignal(List<String> phaseSettings) {
		// initialize first amplifiers output to 0
		int output = 0;
		for (String ps : phaseSettings) {
			IntCode amplifier = new IntCode(INPUT);

			// set the output to the current run
			amplifier.setArgs(new String[] { ps, Integer.toString(output) });
			amplifier.run();
			output = amplifier.getOutputSignal();
		}
		return output;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AmplificationCircuit ac = new AmplificationCircuit();
		int total = ac.run();
		log.info("Highest Phase Setting: " + total);
	}

}
