package com.wonderland.projects.AdventOfCode2019;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.iterators.PermutationIterator;

public class AmplificationCircuit {
	/** List of all the possible phase settings**/
	private static final List<String> PHASE_SETTINGS = Arrays.asList(new String[] {"0", "1", "2", "3", "4"});
	
	
	/**
	 * main processing unit
	 * @return the highest possible signal that can be sent to the thrusters
	 */
	private int run() {
		//iterate thru all the permutations of the phase settings
		PermutationIterator<String> iter = new PermutationIterator<String>(PHASE_SETTINGS);
		int highestSignal = 0;
		//List<String> bestPhaseSettingCombo = new ArrayList<String>();
		while(iter.hasNext()) {
			List<String> phaseSettingCombo = iter.next();
			int signal = calculateSignal(phaseSettingCombo);
			if(signal > highestSignal) {
				highestSignal = signal;
				//bestPhaseSettingCombo = phaseSettingCombo;
				//System.out.println("Highest Signal: " + highestSignal);
				//System.out.println("Best Combo: " + phaseSettingCombo);
			}
		}
		return highestSignal;
	}
	
	/**
	 * calculate the signal for a given phase setting list
	 * @param phaseSettings
	 * @return
	 */
	private int calculateSignal(List<String> phaseSettings) {
		//initialize first amplifiers output to 0 
		int output = 0;
		for(String ps : phaseSettings) {
			IntCode amplifier = new IntCode("input/day7.txt");
			//IntCode amplifier = new IntCode("input/day7.sample.txt");
			//set the output to the current run
			output = amplifier.runProgram(new String[] {ps,Integer.toString(output)});
		}
		return output;
	}

	@SuppressWarnings("unused")
	private void scratch() {
		IntCode thing = new IntCode("input/day7.sample.txt");
		System.out.println("Output: " + thing.runProgram(new String[] {"1","432"}));
		//System.setIn(new ByteArrayInputStream("4".getBytes()));
		//IntCode.main(new String[] {"0","4"});
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AmplificationCircuit ac = new AmplificationCircuit();
		int total = ac.run();
		System.out.println("Highest Phase Setting: " + total);
	}

}
