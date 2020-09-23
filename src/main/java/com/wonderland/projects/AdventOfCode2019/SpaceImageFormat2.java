package com.wonderland.projects.AdventOfCode2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class SpaceImageFormat2 {

	private void run(String filename) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				String linesList = line.replaceAll("(.{150})", "$1\n");
				List<String> layers = Arrays.asList(linesList.split("\n"));
				String finalImage = layers.get(8);
				for (String layer : layers) {
					//System.out.println("F:" + finalImage);
					//System.out.println("N:" + layer);
					String tempImage = "";
					for (int i = 0; i < 150; i++) {
						if(finalImage.charAt(i) == '2' && layer.charAt(i) != '2') {
							tempImage+=layer.charAt(i);
						}else {
							tempImage+=finalImage.charAt(i);
						}
					}
					//this.printImage(finalImage);

					finalImage = tempImage;
					//System.out.println("X:" + finalImage);
					//System.out.println("______________________________________________");


				}
				this.printImage(finalImage);
			}
		} catch (IOException ex) {
			System.err.println("ERROR reading file: " + ex.getMessage());
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
	
	private void printImage(String image) {
		String linesList = image.replaceAll("(.{25})", "$1\n");
		List<String> rows = Arrays.asList(linesList.split("\n"));
		for(String row : rows) {
			//row = row.replaceAll("1", "X");
			row = row.replaceAll("0", " ");
			row = row.replaceAll("2", " ");
			System.out.println(row);
		}


	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpaceImageFormat2 ag = new SpaceImageFormat2();
		ag.run(args[0]);
		// System.out.println("Total fuel used: " + total);
	}

}
