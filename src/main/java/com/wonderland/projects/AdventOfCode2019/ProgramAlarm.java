package com.wonderland.projects.AdventOfCode2019;

public class ProgramAlarm {
	private static final int[] intCode = { 1, 0, 0, 3, 1, 1, 2, 3, 1, 3, 4, 3, 1, 5, 0, 3, 2, 13, 1, 19, 1, 10, 19, 23,
			1, 23, 9, 27, 1, 5, 27, 31, 2, 31, 13, 35, 1, 35, 5, 39, 1, 39, 5, 43, 2, 13, 43, 47, 2, 47, 10, 51, 1, 51,
			6, 55, 2, 55, 9, 59, 1, 59, 5, 63, 1, 63, 13, 67, 2, 67, 6, 71, 1, 71, 5, 75, 1, 75, 5, 79, 1, 79, 9, 83, 1,
			10, 83, 87, 1, 87, 10, 91, 1, 91, 9, 95, 1, 10, 95, 99, 1, 10, 99, 103, 2, 103, 10, 107, 1, 107, 9, 111, 2,
			6, 111, 115, 1, 5, 115, 119, 2, 119, 13, 123, 1, 6, 123, 127, 2, 9, 127, 131, 1, 131, 5, 135, 1, 135, 13,
			139, 1, 139, 10, 143, 1, 2, 143, 147, 1, 147, 10, 0, 99, 2, 0, 14, 0 };
	// private int[] intCodeTester;
	private static final int MCCOYS_FINALE = 19690720;

	private void run() {

		for (int noun = 0; noun < 100; noun++) {
			 for (int verb = 0; verb < 100; verb++) {
				int[] intCodeTester = intCode.clone();
				intCodeTester[1] = noun;
				intCodeTester[2] = verb;
				System.out.println("noun: " + intCodeTester[1] + " , verb: " + intCodeTester[2]);

				int operator = -1;
				int firstVal = -1;
				int secondVal = -1;
				int indexUpdate = -1;
				CALC: for (int i = 0; i < intCodeTester.length; i++) {
					if (i % 4 == 0) {
						operator = intCodeTester[i];
					} else if (i % 4 == 1) {
						firstVal = intCodeTester[intCodeTester[i]];
					} else if (i % 4 == 2) {
						secondVal = intCodeTester[intCodeTester[i]];
					} else if (i % 4 == 3) {
						indexUpdate = intCodeTester[i];
						if (operator == 1) {
							intCodeTester[indexUpdate] = firstVal + secondVal;
						} else if (operator == 2) {
							intCodeTester[indexUpdate] = firstVal * secondVal;
						} else if (operator == 99) {
							break CALC;
						}
					}
				}
				System.out.println(
						"noun: " + intCodeTester[1] + " , verb: " + intCodeTester[2] + ", total: " + intCodeTester[0]);
				if (intCodeTester[0] == MCCOYS_FINALE) {
					System.out.println("noun: " + intCodeTester[1] + ", verb: " + intCodeTester[2]);
					System.out.println(100 * intCodeTester[1] + intCodeTester[2]);
					return;
				}

			}

		}
	}

	public static void main(String[] args) {
		ProgramAlarm pa = new ProgramAlarm();
		pa.run();
		// System.out.println("noun: " + pa.intCodeTester[1] + ", verb: " +
		// pa.intCodeTester[2]);
		// System.out.println(100 * pa.intCodeTester[1] + pa.intCodeTester[2]);

	}

}
