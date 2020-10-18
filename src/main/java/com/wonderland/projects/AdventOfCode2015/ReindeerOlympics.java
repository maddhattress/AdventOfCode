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
 *      href="Advent of Code 2015, Day 14">https://adventofcode.com/2015/day/14</a>
 *
 */
public class ReindeerOlympics {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day14.txt";
	private static final int TIME_LIMIT = 2503;

	private static final String INPUT_REGEX = "^(.*) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.$";
	private static final Pattern INPUT_PATTERN = Pattern.compile(INPUT_REGEX);

	private List<Reindeer> reindeerCrew = new ArrayList<Reindeer>();

	/**
	 * main processing unit
	 */
	private void run() {
		List<String> inputs = this.readInput();
		for (String input : inputs) {
			this.processInput(input);
		}
		log.debug("Total Reindeer in crew: " + reindeerCrew.size());
		log.info("Max distance for heat: " + this.runHeat());
		
		Reindeer pointWinner = this.getPointWinner();
		log.info("Winning point Reindeer[" + pointWinner.getName() + "]: " + pointWinner.getPoints());
	}
	
	/**
	 * iterate thru crew list and identify winner 
	 * @return
	 */
	private Reindeer getPointWinner() {
		Reindeer pointWinner = new Reindeer();
		for(Reindeer deer : reindeerCrew) {
			pointWinner = (pointWinner.getPoints() < deer.getPoints()) ? deer : pointWinner;
		}
		return pointWinner;
	}

	/**
	 * run the race/heat
	 * @return
	 */
	private int runHeat() {
		int maxDistance = 0;
		for (int i = 1; i <= TIME_LIMIT; i++) {
			Reindeer winningDeer = new Reindeer();
			maxDistance = 0;
			for (Reindeer deer : reindeerCrew) {
				deer.setDistance(this.calculateDistance(deer, i));
				if(deer.getDistance() > maxDistance) {
					maxDistance = deer.getDistance();
					winningDeer = deer;
				}
			}
			for(Reindeer deer : reindeerCrew) {
				log.debug(deer);
			}
			winningDeer.addPoint();
			log.info("Point Winner for Round[" + i + "]:" + winningDeer);
		}
		return maxDistance;
	}

	/**
	 * calculates the distance a reindeer traveled for a given duration
	 * @param deer
	 * @param maxTime
	 * @return
	 */
	private int calculateDistance(Reindeer deer, int maxTime) {
		log.debug(deer);
		int durations =  maxTime / (deer.getRestTime() + deer.getActiveTime());
		log.debug("Reindeer[" + deer.getName() + "] can go " + durations + " durations.");
		int leftOver = maxTime % (deer.getRestTime() + deer.getActiveTime());
		log.debug("Reindeer[" + deer.getName() + "] leftover time " + leftOver + " seconds.");
		int distance = durations * (deer.getSpeed() * deer.getActiveTime());
		log.debug("Reindeer[" + deer.getName() + "] distance after " + durations + " durations: " + distance + " km.");
		distance += ((leftOver < deer.getActiveTime()) ? leftOver : deer.getActiveTime()) * deer.getSpeed();
		log.debug("Reindeer[" + deer.getName() + "] traveled " + distance + " km.");
		return distance;
	}

	/**
	 * read reindeer speeds from input file
	 * @param input
	 */
	private void processInput(String input) {
		Matcher m = INPUT_PATTERN.matcher(input);
		if (m.find() && m.groupCount() == 4) {
			String name = m.group(1);
			int speed = Integer.parseInt(m.group(2));
			int activeTime = Integer.parseInt(m.group(3));
			int restTime = Integer.parseInt(m.group(4));
			Reindeer deer = new Reindeer(name, speed, activeTime, restTime);
			reindeerCrew.add(deer);
			log.debug("Adding: " + deer);
		}
	}

	/**
	 * reads the instructions from the input file
	 * 
	 * @return
	 */
	private List<String> readInput() {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
		List<String> routes = reader.lines().collect(Collectors.toList());

		IOUtils.closeQuietly(reader);
		return routes;

	}

	class Reindeer {
		String name = "";
		int speed = 0;
		int activeTime = 0;
		int restTime = 0;
		int distance = 0;
		int points = 0;

		public Reindeer(String name, int speed, int activeTime, int restTime) {
			this.name = name;
			this.speed = speed;
			this.activeTime = activeTime;
			this.restTime = restTime;
		}
		 
		public Reindeer() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getSpeed() {
			return speed;
		}

		public void setSpeed(int speed) {
			this.speed = speed;
		}

		public int getActiveTime() {
			return activeTime;
		}

		public void setActiveTime(int activeTime) {
			this.activeTime = activeTime;
		}

		public int getRestTime() {
			return restTime;
		}

		public void setRestTime(int restTime) {
			this.restTime = restTime;
		}

		public int getDistance() {
			return distance;
		}

		public void setDistance(int distance) {
			this.distance = distance;
		}

		public int getPoints() {
			return points;
		}

		public void addPoint() {
			this.points++;
		}

		@Override
		public String toString() {
			return "Reindeer [name=" + name + ", speed=" + speed + ", activeTime=" + activeTime + ", restTime="
					+ restTime + ", distance=" + distance + ", points=" + points + "]";
		}
	}

	public static void main(String[] args) {
		ReindeerOlympics ro = new ReindeerOlympics();
		ro.run();
	}

}
