package com.wonderland.projects.AdventOfCode2019;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections4.iterators.PermutationIterator;

/**
 * @author Jenn Dell
 * @see <a href="Advent of Code 2019, Day 7, Part 2">https://adventofcode.com/2019/day/7#part2</a>
 *
 */
public class AmplificationCircuit2 {
	/** List of all the possible phase settings**/
	private static final List<String> PHASE_SETTINGS = Arrays.asList(new String[] {"5", "6", "7", "8", "9"});
	private static final String INPUT_PROGRAM="input/day7Pt2.sample.txt";
	//private static final String INPUT_PROGRAM="input/day7.txt";
	
	
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
		IntCode a = new IntCode(INPUT_PROGRAM);
		IntCode b = new IntCode(INPUT_PROGRAM);
		IntCode c = new IntCode(INPUT_PROGRAM);
		IntCode d = new IntCode(INPUT_PROGRAM);
		IntCode e = new IntCode(INPUT_PROGRAM);
		
		Amplifier ampA = new Amplifier(a);
		Amplifier ampB = new Amplifier(b);
		Amplifier ampC = new Amplifier(c);
		Amplifier ampD = new Amplifier(d);
		Amplifier ampE = new Amplifier(e);
		
		List<Amplifier> amplifers = new ArrayList<Amplifier>();
		amplifers.add(ampA);
		amplifers.add(ampB);
		amplifers.add(ampC);
		amplifers.add(ampD);
		amplifers.add(ampE);

		
		for(int i = 0; i< phaseSettings.size(); i++) {
			Amplifier amp = amplifers.get(i);
			//	output = amplifier.run(new String[] {ps,Integer.toString(output)});
		}
		return output;
	}

	@SuppressWarnings("unused")
	private void scratch() {
		IntCode thing = new IntCode("input/day7.sample.txt");
		//System.out.println("Output: " + thing.run(new String[] {"1","432"}));
		//System.setIn(new ByteArrayInputStream("4".getBytes()));
		//IntCode.main(new String[] {"0","4"});
	}

	private class Amplifier{
		private IntCode amplifier; 
		private PipedInputStream pin;
		private PipedOutputStream pout;
		public Amplifier(IntCode amplifier) {
			this.amplifier = amplifier;
			pin = new PipedInputStream();
			pout = new PipedOutputStream();
		}
		
		public void connect(PipedOutputStream pos) throws IOException{
			this.pout = pos;
			pin.connect(this.pout);
		}

		public IntCode getAmplifier() {
			return amplifier;
		}

		public void setAmplifier(IntCode amplifier) {
			this.amplifier = amplifier;
		}

		public PipedInputStream getPin() {
			return pin;
		}

		public PipedOutputStream getPout() {
			return pout;
		}
		


	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AmplificationCircuit2 ac = new AmplificationCircuit2();
		int total = ac.run();
		System.out.println("Highest Phase Setting: " + total);
	}
	

}
