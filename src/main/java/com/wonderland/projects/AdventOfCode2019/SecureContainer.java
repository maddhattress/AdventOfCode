package com.wonderland.projects.AdventOfCode2019;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecureContainer {
	 private static final int MIN = 136760; //136777
	//private static final int MIN = 155555;//153517; // 155555

	 private static final int MAX = 595730; //589999
	//private static final int MAX = 599999; //630395;

	private static final int PASSWORD_LENGTH = 6;
	// input: 136760-595730

	// 6 digit number
	// within range
	// two adjacent digits
	// only increase left to right

	// valid input: 136,777 - 589,999

	private int calculatePossiblePasswords() {
		int val = MIN;
		int counter = 0;
		List<Integer> possibilities = new ArrayList<Integer>();

		while (val <= MAX) {
			String strVal = Integer.toString(val);
			char[] charVal = strVal.toCharArray();

			for (int c = 1; c < PASSWORD_LENGTH; c++) {
				if (charVal[c] < charVal[c - 1]) {
					charVal[c] = charVal[c - 1];
				}
			}
			val = Integer.parseInt(new String(charVal));
			if (this.checkDuplicates(charVal) && val <= MAX && this.checkStrictDuplicates(charVal)) {
				System.out.println(val);
				possibilities.add(val);
				counter++;
			}
			val++;
		}
		// return possibilities.size();
		return counter;
	}

	public boolean checkDuplicates(char[] charVal) {
		for (int c = 1; c < PASSWORD_LENGTH; c++) {
			if (charVal[c] == charVal[c - 1]) {
				return true;
			}
		}
		return false;
	}

	public boolean checkStrictDuplicates(char[] charVal) {
		Map<Integer, Integer> frequency = new HashMap<Integer, Integer>();
		for (int c = 1; c < PASSWORD_LENGTH; c++) {
			if (charVal[c] == charVal[c - 1]) {
				int key = Character.getNumericValue(charVal[c]);
				if (frequency.containsKey(key)) {
					frequency.replace(key, (frequency.get(key)) + 1);
				} else {
					frequency.put(key, 2);
				}
			}
		}
		if (frequency.containsValue(2)) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SecureContainer sc = new SecureContainer();
		System.out.println("Total Passwords Possible: " + sc.calculatePossiblePasswords());

	}

}
