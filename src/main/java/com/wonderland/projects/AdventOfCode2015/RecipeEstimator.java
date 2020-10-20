package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2015, Day 15">https://adventofcode.com/2015/day/15</a>
 *
 */
public class RecipeEstimator {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day15.txt";

	private static final String INPUT_REGEX = "^(.*): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)$";
	private static final Pattern INPUT_PATTERN = Pattern.compile(INPUT_REGEX);

	private static final int MAX_INGREDIENTS = 100;
	private static final int MAX_CALORIES = 500;
	
	private List<Ingredient> ingredients = new ArrayList<Ingredient>();
	private Map<Integer, int[]> resultsMap = new HashMap<Integer, int[]>();

	/**
	 * main processing unit
	 */
	private void run() {
		List<String> inputs = this.readInput();
		for (String i : inputs) {
			this.processInput(i);
		}
		for (Ingredient ing : ingredients) {
			log.debug(ing);
		}
		
		//Part 1
		generate(ingredients.size(), 0, MAX_INGREDIENTS, new int[ingredients.size()], false);
		int max = Collections.max(resultsMap.keySet());
		log.info("Max score: " + max + " : " + Arrays.toString(resultsMap.get(max)));; 
		
		//Part 2
		resultsMap = new HashMap<Integer, int[]>();
		generate(ingredients.size(), 0, MAX_INGREDIENTS, new int[ingredients.size()], true);
		max = Collections.max(resultsMap.keySet());
		log.info("Max score with [" + MAX_CALORIES + "] calories: " + max + " : " + Arrays.toString(resultsMap.get(max)));; 
	}
	
	/**
	 * generates all possible combinations of 4 numbers that add up to goal/100
	 * @param i
	 * @param sum
	 * @param goal
	 * @param result
	 * @param calorieCheck
	 */
	private void generate(int i, int sum, int goal, int[] result, boolean calorieCheck) {
		if (i == 1) {
			// one number to go, so make it fit
			result[0] = goal - sum;
			if((calorieCheck && this.checkCalories(result)) || !calorieCheck) {
				resultsMap.put(new Integer(this.calculateMaxRecipeScore(result)), result);
			}
		} else {
			// try all possible values for this step
			for (int j = 0; j < goal - sum; j++) {
				// set next number of the result
				result[i - 1] = j;
				// go to next step
				generate(i - 1, sum + j, goal, result, calorieCheck);
			}
		}
	}
	
	/**
	 * checks to see if the combination of ingredients adds up to max cals (500)
	 * @param result
	 * @return
	 */
	private boolean checkCalories(int[] result) {
		int calorieCount = 0;
		for(int i = 0; i < result.length; i++) {
			int index = result[i];
			Ingredient ing = ingredients.get(i);
			calorieCount += ing.getCalories() * index; 
		}
		if(calorieCount == MAX_CALORIES ) {
			return true;
		}
		return false;
	}

	/**
	 * for a given set of values that add up to 100, calculate the recipe score
	 * @param result
	 * @return
	 */
	private int calculateMaxRecipeScore(int[] result) {
		int capacityCount = 0;
		int durabilityCount = 0;
		int flavorCount = 0;
		int textureCount = 0;
		for(int i = 0; i < result.length; i++) {
			int index = result[i];
			Ingredient ing = ingredients.get(i);
			capacityCount += ing.getCapacity() * index; 
			durabilityCount += ing.getDurability() * index;
			flavorCount += ing.getFlavor() * index;
			textureCount += ing.getTexture() * index;
		}
		int ingredientCount = ((capacityCount > 0) ? capacityCount : 0) * ((durabilityCount > 0) ? durabilityCount : 0) * ((flavorCount > 0) ? flavorCount : 0) * ((textureCount > 0) ? textureCount : 0);

		return ingredientCount;

	}

	/**
	 * read thru the ingredients given and puts them into objects
	 * 
	 * @param input
	 */
	private void processInput(String input) {
		Matcher m = INPUT_PATTERN.matcher(input);
		if (m.find() && m.groupCount() == 6) {
			String name = m.group(1);
			int capacity = Integer.parseInt(m.group(2));
			int durability = Integer.parseInt(m.group(3));
			int flavor = Integer.parseInt(m.group(4));
			int texture = Integer.parseInt(m.group(5));
			int calories = Integer.parseInt(m.group(6));
			Ingredient ing = new Ingredient(name, capacity, durability, flavor, texture, calories);
			ingredients.add(ing);
			log.debug("Adding: " + ing);
		}
	}

	/**
	 * reads the ingredients from the input file
	 * 
	 * @return
	 */
	private List<String> readInput() {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
		List<String> ingredients = reader.lines().collect(Collectors.toList());

		IOUtils.closeQuietly(reader);
		return ingredients;

	}

	

	class Ingredient {
		String name;
		int capacity = 0;
		int durability = 0;
		int flavor = 0;
		int texture = 0;
		int calories = 0;

		public Ingredient(String name, int cap, int dur, int flav, int text, int cal) {
			this.name = name;
			this.capacity = cap;
			this.durability = dur;
			this.flavor = flav;
			this.texture = text;
			this.calories = cal;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getCapacity() {
			return capacity;
		}

		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}

		public int getDurability() {
			return durability;
		}

		public void setDurability(int durability) {
			this.durability = durability;
		}

		public int getFlavor() {
			return flavor;
		}

		public void setFlavor(int flavor) {
			this.flavor = flavor;
		}

		public int getTexture() {
			return texture;
		}

		public void setTexture(int texture) {
			this.texture = texture;
		}

		public int getCalories() {
			return calories;
		}

		public void setCalories(int calories) {
			this.calories = calories;
		}

		@Override
		public String toString() {
			return "Ingredient [name=" + name + ", capacity=" + capacity + ", durability=" + durability + ", flavor="
					+ flavor + ", texture=" + texture + ", calories=" + calories + "]";
		}

	}

	public static void main(String[] args) {
		RecipeEstimator re = new RecipeEstimator();
		re.run();
	}

}
