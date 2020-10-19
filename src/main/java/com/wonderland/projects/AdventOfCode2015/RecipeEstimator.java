package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
	
	private List<Ingredient> ingredients = new ArrayList<Ingredient>();
	/**
	 * main processing unit
	 */
	private void run() {
		List<String> inputs = this.readInput();
		for(String i : inputs) {
			this.processInput(i);
		}
		for(Ingredient ing : ingredients) {
			log.debug(ing);
		}
	}
	
	/**
	 * read thru the ingredients given and puts them into objects
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
	
	class Ingredient{
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
		RecipeEstimator	re = new RecipeEstimator();
		re.run();
	}

}
