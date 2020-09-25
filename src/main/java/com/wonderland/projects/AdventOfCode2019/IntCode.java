package com.wonderland.projects.AdventOfCode2019;

import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class IntCode {

	private static final int[] BASE = { 3, 225, 1, 225, 6, 6, 1100, 1, 238, 225, 104, 0, 1002, 114, 19, 224, 1001, 224,
			-646, 224, 4, 224, 102, 8, 223, 223, 1001, 224, 7, 224, 1, 223, 224, 223, 1101, 40, 62, 225, 1101, 60, 38,
			225, 1101, 30, 29, 225, 2, 195, 148, 224, 1001, 224, -40, 224, 4, 224, 1002, 223, 8, 223, 101, 2, 224, 224,
			1, 224, 223, 223, 1001, 143, 40, 224, 101, -125, 224, 224, 4, 224, 1002, 223, 8, 223, 1001, 224, 3, 224, 1,
			224, 223, 223, 101, 29, 139, 224, 1001, 224, -99, 224, 4, 224, 1002, 223, 8, 223, 1001, 224, 2, 224, 1, 224,
			223, 223, 1101, 14, 34, 225, 102, 57, 39, 224, 101, -3420, 224, 224, 4, 224, 102, 8, 223, 223, 1001, 224, 7,
			224, 1, 223, 224, 223, 1101, 70, 40, 225, 1102, 85, 69, 225, 1102, 94, 5, 225, 1, 36, 43, 224, 101, -92,
			224, 224, 4, 224, 1002, 223, 8, 223, 101, 1, 224, 224, 1, 224, 223, 223, 1102, 94, 24, 224, 1001, 224,
			-2256, 224, 4, 224, 102, 8, 223, 223, 1001, 224, 1, 224, 1, 223, 224, 223, 1102, 8, 13, 225, 1101, 36, 65,
			224, 1001, 224, -101, 224, 4, 224, 102, 8, 223, 223, 101, 3, 224, 224, 1, 223, 224, 223, 4, 223, 99, 0, 0,
			0, 677, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1105, 0, 99999, 1105, 227, 247, 1105, 1, 99999, 1005, 227, 99999,
			1005, 0, 256, 1105, 1, 99999, 1106, 227, 99999, 1106, 0, 265, 1105, 1, 99999, 1006, 0, 99999, 1006, 227,
			274, 1105, 1, 99999, 1105, 1, 280, 1105, 1, 99999, 1, 225, 225, 225, 1101, 294, 0, 0, 105, 1, 0, 1105, 1,
			99999, 1106, 0, 300, 1105, 1, 99999, 1, 225, 225, 225, 1101, 314, 0, 0, 106, 0, 0, 1105, 1, 99999, 8, 677,
			226, 224, 1002, 223, 2, 223, 1006, 224, 329, 1001, 223, 1, 223, 1108, 226, 226, 224, 1002, 223, 2, 223,
			1005, 224, 344, 101, 1, 223, 223, 1108, 226, 677, 224, 1002, 223, 2, 223, 1006, 224, 359, 101, 1, 223, 223,
			107, 226, 226, 224, 1002, 223, 2, 223, 1005, 224, 374, 101, 1, 223, 223, 1107, 226, 226, 224, 1002, 223, 2,
			223, 1005, 224, 389, 101, 1, 223, 223, 107, 677, 677, 224, 102, 2, 223, 223, 1006, 224, 404, 101, 1, 223,
			223, 1008, 226, 226, 224, 1002, 223, 2, 223, 1006, 224, 419, 101, 1, 223, 223, 108, 677, 226, 224, 1002,
			223, 2, 223, 1006, 224, 434, 101, 1, 223, 223, 1108, 677, 226, 224, 102, 2, 223, 223, 1005, 224, 449, 101,
			1, 223, 223, 1008, 677, 226, 224, 102, 2, 223, 223, 1006, 224, 464, 1001, 223, 1, 223, 108, 677, 677, 224,
			102, 2, 223, 223, 1005, 224, 479, 101, 1, 223, 223, 7, 677, 677, 224, 102, 2, 223, 223, 1005, 224, 494,
			1001, 223, 1, 223, 8, 226, 677, 224, 102, 2, 223, 223, 1006, 224, 509, 101, 1, 223, 223, 107, 677, 226, 224,
			1002, 223, 2, 223, 1005, 224, 524, 1001, 223, 1, 223, 7, 677, 226, 224, 1002, 223, 2, 223, 1005, 224, 539,
			1001, 223, 1, 223, 1007, 226, 677, 224, 1002, 223, 2, 223, 1005, 224, 554, 1001, 223, 1, 223, 8, 677, 677,
			224, 102, 2, 223, 223, 1006, 224, 569, 101, 1, 223, 223, 7, 226, 677, 224, 102, 2, 223, 223, 1006, 224, 584,
			1001, 223, 1, 223, 1008, 677, 677, 224, 102, 2, 223, 223, 1005, 224, 599, 101, 1, 223, 223, 1007, 677, 677,
			224, 1002, 223, 2, 223, 1006, 224, 614, 101, 1, 223, 223, 1107, 677, 226, 224, 1002, 223, 2, 223, 1006, 224,
			629, 101, 1, 223, 223, 1107, 226, 677, 224, 1002, 223, 2, 223, 1006, 224, 644, 101, 1, 223, 223, 1007, 226,
			226, 224, 102, 2, 223, 223, 1005, 224, 659, 1001, 223, 1, 223, 108, 226, 226, 224, 102, 2, 223, 223, 1006,
			224, 674, 101, 1, 223, 223, 4, 223, 99, 226 };

	private int[] code = BASE.clone();
	
	/**
	 * main processing unit
	 * @param userInput
	 */
	private void run(int userInput) {
		int index = 0;
		boolean calculating = true;

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
					code[code[index + 1]] = userInput;
				}
				if (paramModeArray[1] == 1) {
					code[index + 1] = userInput;
				}
				index += 2;
				break;
			case 4:
				System.out.println("Output: " + determineParam(code[index + 1], paramModeArray[0]));
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
		int userInput = Integer.parseInt(IntCode.getUserInput());
		ic.run(userInput);

	}

}
