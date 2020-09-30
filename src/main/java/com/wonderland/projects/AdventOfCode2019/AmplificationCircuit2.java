package com.wonderland.projects.AdventOfCode2019;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2019, Day 7, Part 2">https://adventofcode.com/2019/day/7#part2</a>
 *
 */
public class AmplificationCircuit2 {
	private final static Logger log = LogManager.getLogger();
	/** List of all the possible phase settings **/
	private static final List<String> PHASE_SETTINGS = Arrays.asList(new String[] { "5", "6", "7", "8", "9" });

	// private static final String INPUT_PROGRAM = "input/day7Pt2.sample.txt";
	private static final String INPUT_PROGRAM = "input/day7.txt";

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
//
//		Amplifier ampA = new Amplifier(new IntCode(INPUT_PROGRAM), "ampA");
//		Amplifier ampB = new Amplifier(new IntCode(INPUT_PROGRAM), "ampB");
//		Amplifier ampC = new Amplifier(new IntCode(INPUT_PROGRAM), "ampC");
//		Amplifier ampD = new Amplifier(new IntCode(INPUT_PROGRAM), "ampD");
//		Amplifier ampE = new Amplifier(new IntCode(INPUT_PROGRAM), "ampE");
//
//		log.debug("connecting B[" + ampB.hashCode() + "] input to output of A[" + ampA.hashCode() + "]");
//		ampB.connect(ampA.getPout());
//		log.debug("connecting C[" + ampC.hashCode() + "] input to output of B[" + ampB.hashCode() + "]");
//		ampC.connect(ampB.getPout());
//		log.debug("connecting D[" + ampD.hashCode() + "] input to output of C[" + ampC.hashCode() + "]");
//		ampD.connect(ampC.getPout());
//		log.debug("connecting E[" + ampE.hashCode() + "] input to output of D[" + ampD.hashCode() + "]");
//		ampE.connect(ampD.getPout());
//		log.debug("connecting A[" + ampA.hashCode() + "] input to output of E[" + ampE.hashCode() + "]");
//		ampA.connect(ampE.getPout());
//
//		// add amplifiers to list
//		List<Amplifier> amplifiers = Arrays.asList(new Amplifier[] { ampA, ampB, ampC, ampD, ampE });

		// Below command does what above 20 lines had
		List<Amplifier> amplifiers = Stream.generate(new AmplifierGenerator()).limit(5).collect(Collectors.toList());
		amplifiers.get(0).connect(amplifiers.get(amplifiers.size() - 1));

		log.debug("Phase Settings: " + phaseSettings);

		for (int i = 0; i < phaseSettings.size(); i++) {
			int ampIndex = i - 1;
			Amplifier amp = (ampIndex == -1) ? amplifiers.get(amplifiers.size() - 1) : amplifiers.get(ampIndex);
			IntCode ic = amp.getIntCode();
			String startVal = (ampIndex == -1) ? "0" : null;
			log.debug("Writing phase: " + phaseSettings.get(i) + " to amp: " + amp.getName() + " outputStream.");
			ic.initializeStreams(amp.getPin(), amp.getPout(), phaseSettings.get(i), startVal);
			ampIndex += 1;
		}

		// start each thread
		amplifiers.forEach(amp -> amp.startProgram());
		// join each thread
		amplifiers.forEach(amp -> amp.joinProgram());
		// return value from last amplifier
		return amplifiers.get(amplifiers.size() - 1).getOutput();
	}

	private class AmplifierGenerator implements Supplier<Amplifier> {
		private Amplifier connectedAmp = null;
		private char counter = 'A';

		@Override
		public Amplifier get() {
			Amplifier amp = new Amplifier(new IntCode(INPUT_PROGRAM), "amp" + counter);
			if (connectedAmp != null) {
				amp.connect(connectedAmp);
			}
			connectedAmp = amp;
			counter++;
			return amp;
		}
	}

	private class Amplifier {
		private IntCode intCode;
		private PipedInputStream pin;
		private PipedOutputStream pout;
		private String name;
		private int output = 0;

		public Amplifier(IntCode intCode, String name) {
			this.intCode = intCode;
			this.name = name;
			pin = new PipedInputStream();
			pout = new PipedOutputStream();
			log.debug("Creating " + this.toString() + " with PipedInputStream[" + this.pin.hashCode()
					+ "], PipedOutputStream[" + this.pout.hashCode() + "]");
		}

		private void connect(Amplifier connectedAmp) {
			log.debug("Connecting " + this.toString() + " input to output of " + connectedAmp.toString());

			try {
				log.debug("Connecting PipedOuputStream[" +connectedAmp.getPout().hashCode() + "]");
				pin.connect(connectedAmp.getPout());
			} catch (IOException ioe) {
				log.error("IOException while connecting outputstream to amp: " + this.toString() + ".", ioe);
				// ioe.printStackTrace();
			}
		}

		public void startProgram() {
			this.intCode.start();
		}

		public void joinProgram() {
			try {
				intCode.join();
			} catch (InterruptedException ex) {
				log.error("InterruptedException while joining thread.", ex);
			}
			output = intCode.getOutputSignal();
		}

		public IntCode getIntCode() {
			return intCode;
		}

		public PipedInputStream getPin() {
			return pin;
		}

		public PipedOutputStream getPout() {
			return pout;
		}

		public String getName() {
			return name;
		}

		public int getOutput() {
			return output;
		}

		@Override
		public String toString() {
			return "Amplifier " + name + "[" + intCode.hashCode() + "]";
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AmplificationCircuit2 ac = new AmplificationCircuit2();
		int total = ac.run();
		log.info("Highest Phase Setting: " + total);
	}

}
