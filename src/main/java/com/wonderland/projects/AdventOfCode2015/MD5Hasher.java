package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2015, Day 4">https://adventofcode.com/2015/day/4</a>
 *
 */
public class MD5Hasher {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day4.txt";

	/**
	 * main processing unit
	 */
	private void run() {
		final String secretKey = getInput();
		// used for testing out sample data
		// final String secretKey = "abcdef";
		// final String secretKey = "pqrstuv";

		MD5Hasher.findHashWith5Zeros(secretKey);

		MD5Hasher.findHashWith6Zeros(secretKey);

	}

	/**
	 * find lowest positive MD5 Hash for a given secretKey that starts with 5 zeros
	 * Part1
	 * @param secretKey
	 * @return
	 */
	private static final long findHashWith5Zeros(String secretKey) {
		long counter = 0L;
		boolean calculating = true;
		while (calculating) {
			String password = secretKey + String.valueOf(counter);
			String md5Hex = DigestUtils.md5Hex(password);
			if (md5Hex.startsWith("00000")) {
				calculating = false;
				log.info("Lowest MD5 Hash starting with 00000: [" + counter + "]: " + md5Hex);
				return counter;
			}
			counter++;
		}
		return counter;
	}

	/**
	 * find lowest positive MD5 Hash for a given secretKey that starts with 6 zeros
	 * Part 2
	 * @param secretKey
	 * @return
	 */
	private static final long findHashWith6Zeros(String secretKey) {
		long counter = 0L;
		boolean calculating = true;
		while (calculating) {
			String password = secretKey + String.valueOf(counter);
			String md5Hex = DigestUtils.md5Hex(password);
			if (md5Hex.startsWith("000000")) {
				calculating = false;
				log.info("Lowest MD5 Hash starting with 000000: [" + counter + "]: " + md5Hex);
				return counter;
			}
			counter++;
		}
		return counter;
	}

	/**
	 * retrieves the password from the input file
	 * 
	 * @return
	 */
	private String getInput() {
		BufferedReader reader = null;
		String line = new String();
		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
			line = reader.readLine();
		} catch (IOException ioe) {
			log.error("IOException while reading iput file " + INPUT, ioe);
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return line;
	}

	public static void main(String[] args) {
		MD5Hasher md5 = new MD5Hasher();
		md5.run();
	}

}
