package com.wonderland.projects.AdventOfCode2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2019, Day 9">https://adventofcode.com/2019/day/9</a>
 *
 */
public class SensorBoostIntCode extends Thread {

	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/day9.txt";
	//private static final String INPUT = "input/day9.sample.txt";

	/** used to store program instruction/code **/
	private IntCodeProgram<Integer, Long> code;

	private int index = 0;
	private String[] inputs;
	BufferedReader reader = null;
	PipedInputStream in = null;
	PipedOutputStream out = null;
	String args[];

	private long relativeBase = 0L;

	private long outputSignal = 0L;

	/**
	 * hardcode to day5 input if none provided
	 */
	public SensorBoostIntCode() {
		this(INPUT);

	}

	/**
	 * load up the program instructions/code based on input filename parameter
	 * 
	 * @param filename
	 */
	public SensorBoostIntCode(String filename) {
		try {
			reader = new BufferedReader(
					new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)));
			String line;
			while ((line = reader.readLine()) != null) {
				//read string input, assumption is that its all on one line
				final long[] c = ArrayUtils.addAll(new long[0],	Arrays.stream(line.split(","))
						.mapToLong(Long::parseLong).toArray());
				//put input into custom HashMap with "position/index" as the key
				code = IntStream.range(0, c.length)
						.boxed()
						.collect(Collectors.toMap(Function.identity(), k->c[k], (a,b)->a,() -> new IntCodeProgram<Integer,Long>(0L)));
			}
		} catch (IOException ioe) {
			log.error("IOException while reading program instruction file.", ioe);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	
	/**
	 * setting up piped input and output streams to daisy chain versus getting user
	 * input
	 * 
	 * @param is
	 * @param os
	 * @param phaseSetting
	 * @param startVal     only set for amp A, otherwise null
	 */
	public void initializeStreams(PipedInputStream is, PipedOutputStream os, String phaseSetting, String startVal) {
		// if input is null keep null, if not set to dataInputStream
		in = is;
		out = os;
		this.write(phaseSetting);
		this.write(startVal);
	}

	/**
	 * main processing unit
	 * 
	 * @returns the output for a given instruction
	 */
	public synchronized void run() {
		log.debug("Running program[" + this.hashCode() + "]:" + Thread.currentThread().getName());
		log.debug("Program instruction size: " + code.size());
		boolean calculating = true;
		long retVal = 0L;
		int opCodeCounter = 0;
		inputs = args;
		while (calculating) {
			long opCode = code.get(index);
			// change opcode to String and reverse it
			String opCodeStr = StringUtils.reverse(Long.toString(opCode));
			// break down each character as separate value 1001 = {1, 0, 0, 1}
			long[] oplex = Arrays.stream(opCodeStr.split("")).mapToLong(Long::parseLong).toArray();
			int[] paramModeArray = { 0, 0, 0 };

			// figure out if its more than 2 digits long, if so use parameter mode
			if (oplex.length > 2) {
				//log.debug("Parameter mode initiated for " + opCode);
				opCode = (oplex[1] * 10) + oplex[0];
				int pIndex = 0;
				for (int i = 2; i < oplex.length; i++) {
					paramModeArray[pIndex] = (int) oplex[i];
					pIndex += 1;
				}
				//log.debug("Parameters: " + Arrays.toString(paramModeArray));
			}
			// instructions on how to operate based on opcode
			log.debug("Processing Opcode: " + opCode);
			opCodeCounter++;
			switch ((int) opCode) {
			case 99:
				calculating = false;
				log.info("Output of position 0: " + code.get(0));
				log.info("Output: " + this.getOutputSignal());
				log.debug("Number of opCode instructions processed: " + opCodeCounter);
				this.shutdown();
				break;
			case 1:
				// Opcode 1 adds together numbers read from two positions and stores the result
				// in a third position. The three integers immediately after the opcode tell you
				// these three positions - the first two indicate the positions from which you
				// should read the input values, and the third indicates the position at which
				// the output should be stored.
				long sum = determineParam(code.get(index + 1), paramModeArray[0])
						+ determineParam(code.get(index + 2), paramModeArray[1]);
				code.put(code.get(index + 3).intValue(), sum);
				index += 4;
				break;
			case 2:
				// Opcode 2 works exactly like opcode 1, except it multiplies the two inputs
				// instead of adding them. Again, the three integers after the opcode indicate
				// where the inputs and outputs are, not their values.
				long product = determineParam(code.get(index + 1), paramModeArray[0])
						* determineParam(code.get(index + 2), paramModeArray[1]);
				code.put(code.get(index + 3).intValue(), product);
				index += 4;
				break;
			case 3:
				// Opcode 3 takes a single integer as input and saves it to the position given
				// by its only parameter. For example, the instruction 3,50 would take an input
				// value and store it at address 50.
				log.debug("Getting user input...");
				long input = getInput();
				if (paramModeArray[0] ==0 ) { //position mode
					code.put(code.get(index + 1).intValue(), input);
				}
				if (paramModeArray[0] == 1) { //immediate mode
					// will never be in immediate mode according to instructions. throw error.
					log.error("Immediate mode specified for OpCode[3], Parameter[" + code.get(index + 1) + "] and Value["
							+ input + "]");
				}
				if (paramModeArray[0] == 2) { //relative mode
					code.put(new Long(code.get(index + 1) + relativeBase).intValue(), input);
				}
				index += 2;
				break;
			case 4:
				// Opcode 4 outputs the value of its only parameter. For example, the
				// instruction 4,50 would output the value at address 50.
				retVal = determineParam(code.get(index + 1), paramModeArray[0]);
				log.info("Outputting in opCode 4: " + retVal);
				this.write(Long.toString(retVal));
				this.setOutputSignal(retVal);
				index += 2;
				break;
			case 5:
				// Opcode 5 is jump-if-true: if the first parameter is non-zero, it sets the
				// instruction pointer to the value from the second parameter.
				if (determineParam(code.get(index + 1), paramModeArray[0]) != 0) {
					index = (int) determineParam(code.get(index + 2), paramModeArray[1]);
					log.debug("Updating index: " + index);

				} else {
					log.debug("No Op for OpCode 5");
					index += 3;
				}
				break;
			case 6:
				// Opcode 6 is jump-if-false: if the first parameter is zero, it sets the
				// instruction pointer to the value from the second parameter. Otherwise, it
				// does nothing.
				if (determineParam(code.get(index + 1), paramModeArray[0]) == 0) {
					index = (int) determineParam(code.get(index + 2), paramModeArray[1]);
					log.debug("Updating index: " + index);

				} else {
					log.debug("No Op for OpCode 6");
					index += 3;
				}
				break;
			case 7:
				// Opcode 7 is less than: if the first parameter is less than the second
				// parameter, it stores 1 in the position given by the third parameter.
				// Otherwise, it stores 0.
				if (determineParam(code.get(index + 1), paramModeArray[0]) < determineParam(code.get(index + 2),
						paramModeArray[1])) {
					code.put(code.get(index + 3).intValue(), new Long(1));
				} else {
					code.put(code.get(index + 3).intValue(), new Long(0));
				}
				index += 4;
				break;
			case 8:
				// Opcode 8 is equals: if the first parameter is equal to the second parameter,
				// it stores 1 in the position given by the third parameter. Otherwise, it
				// stores 0.
				if (determineParam(code.get(index + 1), paramModeArray[0]) == determineParam(code.get(index + 2),
						paramModeArray[1])) {
					code.put(code.get(index + 3).intValue(), new Long(1));
				} else {
					code.put(code.get(index + 3).intValue(), new Long(0));
				}
				index += 4;
				break;
			case 9:
				// Opcode 9 adjusts the relative base by the value of its only parameter. The
				// relative base increases (or decreases, if the value is negative) by the value
				// of the parameter.
				relativeBase += determineParam(code.get(index + 1), paramModeArray[0]);
				log.debug("Relative base: " + relativeBase);
				index += 2;
				break;
			}

		}
	}

	/**
	 * given a target and mode define how the param should be used position mode vs
	 * immediate mode
	 * 
	 * @param target
	 * @param mode
	 * @return
	 */
	private long determineParam(long t, long m) {
		int target = new Long(t).intValue();
		int mode = new Long(m).intValue();
		log.debug("Parameter -- Target: " + target + " Mode: " + mode);
		if (mode == 0) { // position mode
			return code.get(target);
		} else if (mode == 1) { // immediate mode
			return t; //t is the long value for target
		} else if (mode == 2) { // relative mode
			return code.get((int)relativeBase + target);
		} else {
			return -1;
		}
	}

	/**
	 * helper method to determine if args were sent in or if they should be
	 * retrieved from user
	 * 
	 * @param inputs
	 * @return
	 */
	private synchronized long getInput() {
		log.debug("Looking for input.");
		if (ArrayUtils.isNotEmpty(inputs)) {
			// if not empty, pop off next value in array
			long input = Long.parseLong(inputs[0]);
			inputs = (String[]) ArrayUtils.remove(inputs, 0);
			return input;
		} else if (in == null) {
			// if empty args and in is not set, get from user
			return Long.parseLong(SensorBoostIntCode.getUserInput());
		}
		// if none of the above, read from piped input
		return getPipedInput();

	}


	/**
	 * retrieve stdin from user
	 * 
	 * @return
	 */
	public static final String getUserInput() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter a number: ");
		// Read user input
		String inputString = scanner.nextLine();
		IOUtils.closeQuietly(scanner);
		return inputString;
	}

	/**
	 * reads from pipe and handles the IOException
	 * 
	 * @return
	 */
	private synchronized int getPipedInput() {
		try {
			int input = Integer.parseInt(readLine(in));
			log.debug("[" + in.hashCode() + "]: Read input=" + input);
			return input;
		} catch (IOException ioe) {
			log.error("IOException while reading piped input.", ioe);
		}
		return 0;
	}

	/**
	 * helper method to read line by line from PipedInputStream
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public synchronized String readLine(PipedInputStream in) throws IOException {
		String input = "";
		do {
			// forcing to check availability because its faster
			while (in.available() == 0) {
				try {
					log.debug("Sleeping for 10ms");
					Thread.sleep(10);
				} catch (InterruptedException e) {
					log.error("InterruptedException while reading from pipedInputStream.", e);
				}
			}
			char c = (char) in.read();
			input = input + c;
		} while (!input.endsWith(System.lineSeparator()));
		return input.replace(System.lineSeparator(), "");
	}

	/**
	 * helper method to write to pipedOutputstream
	 * 
	 * @param val
	 */
	private synchronized void write(String val) {
		try {
			if (val != null && out != null) {
				log.debug("[" + out.hashCode() + "]: Writing: " + val);
				val = val + System.lineSeparator();
				out.write(val.getBytes());
			}
		} catch (IOException ioe) {
			// ignore if pipe is closed
			if (!ioe.getMessage().contains("Pipe closed")) {
				log.error("IOException when writing out.", ioe);
			}
		}
	}

	/**
	 * helper method to close input and output streams
	 */
	public void shutdown() {
		IOUtils.closeQuietly(in);
		IOUtils.closeQuietly(out);
	}

	public long getOutputSignal() {
		return outputSignal;
	}

	public void setOutputSignal(long outputSignal) {
		this.outputSignal = outputSignal;
	}

	public void setArgs(String args[]) {
		this.args = args;
	}

	public static void main(String[] args) {
		SensorBoostIntCode ic = new SensorBoostIntCode();
		ic.run();
		System.out.println("Output: " + ic.getOutputSignal());

	}

}
