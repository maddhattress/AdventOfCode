package com.wonderland.projects.AdventOfCode2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2019, Day 8">https://adventofcode.com/2019/day/8#part2</a>
 *
 */
public class SpaceImageFormat2 {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2019/day8.txt";
	
	/**
	 * main processing unit
	 */
	private void run() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
			String line;
			while ((line = reader.readLine()) != null) {
				String linesList = line.replaceAll("(.{150})", "$1\n");
				List<String> layers = Arrays.asList(linesList.split("\n"));
				String finalImage = layers.get(8);
				for (String layer : layers) {
					log.debug("F:" + finalImage);
					log.debug("N:" + layer);
					String tempImage = "";
					for (int i = 0; i < 150; i++) {
						if(finalImage.charAt(i) == '2' && layer.charAt(i) != '2') {
							tempImage+=layer.charAt(i);
						}else {
							tempImage+=finalImage.charAt(i);
						}
					}

					finalImage = tempImage;
					log.debug("X:" + finalImage);
					log.debug("______________________________________________");


				}
				this.printImage(finalImage);
			}
		} catch (IOException ex) {
			log.error("IOException reading input file " + INPUT, ex);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
	
	/**
	 * prints out the image
	 * @param image
	 */
	private void printImage(String image) {
		String linesList = image.replaceAll("(.{25})", "$1\n");
		List<String> rows = Arrays.asList(linesList.split("\n"));
		for(String row : rows) {
			//row = row.replaceAll("1", "X");
			row = row.replaceAll("0", " ");
			row = row.replaceAll("2", " ");
			log.info(row);
		}


	}

	public static void main(String[] args) {
		SpaceImageFormat2 ag = new SpaceImageFormat2();
		ag.run();
	}

}
