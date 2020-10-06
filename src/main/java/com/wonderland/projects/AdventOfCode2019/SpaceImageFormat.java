package com.wonderland.projects.AdventOfCode2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2019, Day 8">https://adventofcode.com/2019/day/8</a>
 *
 */
public class SpaceImageFormat {
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
				String linesList   = line.replaceAll("(.{150})", "$1\n");
				List<String> layers = Arrays.asList(linesList.split("\n"));

				Map<Integer, LayerCounter> counters = new HashMap<Integer, LayerCounter>();
				int smallestZeroLayer = 999;
				int smallestLayer = 0;
				for(int i = 0; i < layers.size(); i++) {
					String layer = layers.get(i);
					int zeroCount = StringUtils.countMatches(layer, "0");
					if(zeroCount < smallestZeroLayer) {
						smallestZeroLayer = zeroCount;
						smallestLayer = i;
					}
					counters.put(i, new LayerCounter(
							zeroCount, 
							StringUtils.countMatches(layer, "1"),
							StringUtils.countMatches(layer, "2")
							));
				}
				
				LayerCounter smallestCounter = counters.get(smallestLayer);
				log.debug(smallestCounter.getOnesCount() * smallestCounter.getTwosCount());

			}
		} catch (IOException ex) {
			log.error("IOException reading input file " + INPUT, ex);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	public static void main(String[] args) {
		SpaceImageFormat ag = new SpaceImageFormat();
		ag.run();
	}
	
	/**
	 * sub helper class for layer counting 
	 *
	 */
	class LayerCounter{
		private int zeroCount=0;
		private int onesCount=0;
		private int twosCount=0;
		public LayerCounter(int zero, int ones, int twos) {
			zeroCount = zero;
			onesCount = ones;
			twosCount = twos;
		}
		public int getZeroCount() {
			return zeroCount;
		}
		public void setZeroCount(int zeroCount) {
			this.zeroCount = zeroCount;
		}
		public int getOnesCount() {
			return onesCount;
		}
		public void setOnesCount(int onesCount) {
			this.onesCount = onesCount;
		}
		public int getTwosCount() {
			return twosCount;
		}
		public void setTwosCount(int twosCount) {
			this.twosCount = twosCount;
		}
		@Override
		public String toString() {
			return "LayerCounter [zeroCount=" + zeroCount + ", onesCount=" + onesCount + ", twosCount=" + twosCount
					+ "]";
		}
		
		
	}

}
