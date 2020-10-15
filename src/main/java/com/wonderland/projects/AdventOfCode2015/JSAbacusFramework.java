package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2015, Day 12">https://adventofcode.com/2015/day/12</a>
 *
 */
public class JSAbacusFramework {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day12.json";

	private static final String RED = "red";

	private int sum = 0;

	/**
	 * main processing unit
	 */
	private void run() {
		String input = getInput();
		JSONObject obj = new JSONObject(input);
		//Part 1 calculation
		this.addNumbersInJSON(obj);
		log.info("Sum of numbers in input: " + sum);
		
		//Part 2 calculation
		sum = 0;
		this.addNumbersInJSON(obj, true);
		log.info("Sum of numbers in input ignoring red: " + sum);
	}

	private void addNumbersInJSON(Object obj) {
		this.addNumbersInJSON(obj, false);
	}

	private void addNumbersInJSON(Object obj, boolean ignoreRed) {
		if (obj.getClass() == JSONObject.class) {
			if (!(ignoreRed && ((JSONObject)obj).toMap().containsValue(RED))) {
				for (String key : ((JSONObject) obj).keySet()) {
					log.debug("JSONObject Value: " + JSONObject.valueToString(obj));
					addNumbersInJSON(((JSONObject) obj).get(key), ignoreRed);
				}
			}
		} else if (obj.getClass() == JSONArray.class) {
			for (Object arrayObj : (JSONArray) obj) {
				log.debug("JSONArray Value: " + JSONObject.valueToString(arrayObj));
				addNumbersInJSON(arrayObj, ignoreRed);
			}
		} else if (obj.getClass() == Integer.class) {
			log.debug("Integer Value: " + String.valueOf(obj));
			// add values together
			sum += (int) obj;
		} else if (obj.getClass() == String.class) {
			// do nothing with strings
			log.debug("String Value: " + obj);
		} else {
			// do nothing with other values
			log.warn("Other Class Type: " + obj.getClass());
		}
	}

	/**
	 * retrieves the JSON from the input file
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
		JSAbacusFramework jsaf = new JSAbacusFramework();
		jsaf.run();
	}

}
