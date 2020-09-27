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

public class SpaceImageFormat {

	private void run() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("input/day8.txt")));
			String line;
			//int[][] layerCounts;
			while ((line = reader.readLine()) != null) {
				String linesList   = line.replaceAll("(.{150})", "$1\n");
				List<String> layers = Arrays.asList(linesList.split("\n"));
				//for(int i = 0; i< 150; i++)
				//{
				//	if layer.codePointAt(i) == 0
						
				//}
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
				System.out.println(smallestCounter.getOnesCount() * smallestCounter.getTwosCount());
 				//for(Entry<Integer, LayerCounter>)
			//	for(String layer : layers) {
				//	System.out.println("Layer: " + layer);
				//}
				// mod 6 == 0 layer ++
			}
		} catch (IOException ex) {
			System.err.println("ERROR reading file: " + ex.getMessage());
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpaceImageFormat ag = new SpaceImageFormat();
		ag.run();
		// System.out.println("Total fuel used: " + total);
	}
	
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
