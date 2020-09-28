package com.wonderland.projects.AdventOfCode2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class IntCode {
	
	/** used to store program instruction/code**/
	private int[] code;
	private String[] inputs;
	BufferedReader reader = null;
	
	
	/**
	 * hardcode to day5 input if none provided
	 */
	public IntCode() {
		this("input/day5.txt");

	}
	
	/**
	 * load up the program instructions/code based on input filename parameter
	 * @param filename
	 */
	public IntCode(String filename) {
		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)));
			String line;
			while ((line = reader.readLine()) != null) {
				code = ArrayUtils.addAll(code, Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray());
			}
		} catch (IOException ex) {
			System.err.println("ERROR reading file: " + ex.getMessage());
		} finally {
			IOUtils.closeQuietly(reader);
		}

	}

	/**
	 * main processing unit
	 * @returns the output for a given instruction
	 */
	public int run(String[] args) {
		int index = 0;
		inputs = args;
		boolean calculating = true;
		int retVal = 0;

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

			
			//instructions on how to operate based on opcode
			switch (opCode) {
			case 99:
				calculating = false;
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
				if (paramModeArray[1] == 1) {
					code[index + 1] = getInput();
				}
				index += 2;
				break;
			case 4:
				retVal = determineParam(code[index + 1], paramModeArray[0]);
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
		return retVal;

	}
	
	/**
	 * helper method to determine if args were sent in or if they should be retrieved from user
	 * @param inputs
	 * @return
	 */
	private int getInput() {
		//if empty args, get from user
		if(ArrayUtils.isEmpty(inputs)) {
			return Integer.parseInt(IntCode.getUserInput());
		}
		//if not empty, pop off next value in array
		int input = Integer.parseInt(inputs[0]);
		inputs=(String[]) ArrayUtils.remove(inputs, 0);
		System.out.println("returning: " + input);
		return input;
	}
	
	/**
	 * given a target and mode define how the param should be used 
	 * position mode vs immediate mode 
	 * @param target
	 * @param mode
	 * @return
	 */
	private int determineParam(int target, int mode) {
		if (mode == 0) { 		//position mode
			return code[target];
		} else if (mode == 1) { //immediate mode
			return target;
		} else {
			return -1;
		}
	}

	/**
	 * retrieve stdin from user
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

	public static void main(String[] args) {
		IntCode ic = new IntCode();
		int output = ic.run(args);
		System.out.println("Output: " + output );

	}

}
