package com.wonderland.projects.AdventOfCode2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntCode extends Thread {

	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/day5.txt"; 
	
	/** used to store program instruction/code **/
	private int[] code;
	private String[] inputs;
	BufferedReader reader = null;
	PipedInputStream in = null;
	PipedOutputStream out = null;
	String args[];

	private int outputSignal = 0;

	/**
	 * hardcode to day5 input if none provided
	 */
	public IntCode() {
		this(INPUT);

	}

	/**
	 * load up the program instructions/code based on input filename parameter
	 * 
	 * @param filename
	 */
	public IntCode(String filename) {
		try {
			reader = new BufferedReader(
					new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)));
			String line;
			while ((line = reader.readLine()) != null) {
				code = ArrayUtils.addAll(code, Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray());
			}

		} catch (IOException ioe) {
			log.error("IOException while reading program instruction file.", ioe);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	/**
	 * setting up pipedinput and output streams to daisy chain versus getting user
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
		int index = 0;
		boolean calculating = true;
		int retVal = 0;
		inputs = args;
		while (calculating) {
			int opCode = code[index];
			// change opcode to String and reverse it
			String opCodeStr = StringUtils.reverse(Integer.toString(opCode));
			// break down each character as separate value 1001 = {1, 0, 0, 1}
			int[] oplex = Arrays.stream(opCodeStr.split("")).mapToInt(Integer::parseInt).toArray();
			int[] paramModeArray = { 0, 0, 0 };

			// figure out if its more than 2 digits long, if so use parameter mode
			if (oplex.length > 2) {
				opCode = (oplex[1] * 10) + oplex[0];
				int pIndex = 0;
				for (int i = 2; i < oplex.length; i++) {
					paramModeArray[pIndex] = oplex[i];
					pIndex += 1;
				}
			}

			// instructions on how to operate based on opcode
			switch (opCode) {
			case 99:
				calculating = false;
				this.shutdown();
				break;
			case 1:
				int sum = determineParam(code[index + 1], paramModeArray[0])
						+ determineParam(code[index + 2], paramModeArray[1]);
				code[code[index + 3]] = sum;
				index += 4;
				break;
			case 2:
				int product = determineParam(code[index + 1], paramModeArray[0])
						* determineParam(code[index + 2], paramModeArray[1]);
				code[code[index + 3]] = product;
				index += 4;
				break;
			case 3:
				if (paramModeArray[0] == 0) {
					code[code[index + 1]] = getInput();
				}
				if (paramModeArray[0] == 1) {
					code[index + 1] = getInput();
				}
				index += 2;
				break;
			case 4:
				retVal = determineParam(code[index + 1], paramModeArray[0]);
				this.write(Integer.toString(retVal));
				this.setOutputSignal(retVal);
				index += 2;
				break;
			case 5:
				if (determineParam(code[index + 1], paramModeArray[0]) != 0) {
					index = determineParam(code[index + 2], paramModeArray[1]);
				} else {
					index += 3;
				}
				break;
			case 6:
				if (determineParam(code[index + 1], paramModeArray[0]) == 0) {
					index = determineParam(code[index + 2], paramModeArray[1]);
				} else {
					index += 3;
				}
				break;
			case 7:
				if (determineParam(code[index + 1], paramModeArray[0]) < determineParam(code[index + 2],
						paramModeArray[1])) {
					code[code[index + 3]] = 1;
				} else {
					code[code[index + 3]] = 0;
				}
				index += 4;
				break;
			case 8:
				if (determineParam(code[index + 1], paramModeArray[0]) == determineParam(code[index + 2],
						paramModeArray[1])) {
					code[code[index + 3]] = 1;
				} else {
					code[code[index + 3]] = 0;
				}
				index += 4;
				break;
			}

		}
	}

	/**
	 * helper method to determine if args were sent in or if they should be
	 * retrieved from user
	 * 
	 * @param inputs
	 * @return
	 */
	private synchronized int getInput() {
		if (ArrayUtils.isNotEmpty(inputs)) {
			// if not empty, pop off next value in array
			int input = Integer.parseInt(inputs[0]);
			inputs = (String[]) ArrayUtils.remove(inputs, 0);
			return input;
		} else if (in == null) {
			// if empty args and in is not set, get from user
			return Integer.parseInt(IntCode.getUserInput());
		}
		// if none of the above, read from piped input
		return getPipedInput();

	}

	/**
	 * given a target and mode define how the param should be used position mode vs
	 * immediate mode
	 * 
	 * @param target
	 * @param mode
	 * @return
	 */
	private synchronized int determineParam(int target, int mode) {
		if (mode == 0) { // position mode
			return code[target];
		} else if (mode == 1) { // immediate mode
			return target;
		} else {
			return -1;
		}
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
			//forcing to check availability because its faster
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

	public int getOutputSignal() {
		return outputSignal;
	}

	public void setOutputSignal(int outputSignal) {
		this.outputSignal = outputSignal;
	}
	
	public void setArgs(String args[]) {
		this.args = args;
	}

	public static void main(String[] args) {
		IntCode ic = new IntCode();
		ic.run();
		System.out.println("Output: " + ic.getOutputSignal());

	}

}
