package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jenn Dell
 * @see <a
 *      href="Advent of Code 2015, Day 13">https://adventofcode.com/2015/day/13</a>
 *
 */
public class SeatingArrangement {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day13.txt";

	private static final String ADD = "gain";
	private static final String SUBTRACT = "lose";
	private static final String HAPPINESS = "happiness units by sitting next to";

	private static final String INPUT_REGEX = "^(.*) would (" + ADD + "|" + SUBTRACT + ") (\\d+) " + HAPPINESS + " (.*)\\.$";
	private static final Pattern INPUT_PATTERN = Pattern.compile(INPUT_REGEX);

	private Map<String, Attendee> attendees = new HashMap<String, Attendee>();

	private static final String SELF = "Jenn";
	private static final int SELF_HAPPINESS = 0;

	/**
	 * main processing unit
	 */
	private void run() {
		List<String> inputInstructions = this.readInput();
		for (String input : inputInstructions) {
			this.processinput(input);
		}

		int maxHappiness = this.calculateMaxHappinessSeating();
		log.info("Max happiness count: " + maxHappiness);

		this.addSelfToAttendees();
		for (Map.Entry<String, Attendee> attendee : attendees.entrySet()) {
			log.debug(attendee.getValue());
		}
		maxHappiness = this.calculateMaxHappinessSeating();
		log.info("Max happiness count including self: " + maxHappiness);

	}

	/**
	 * calculates the optimal happiness score seating arrangement
	 * 
	 * @return
	 */
	private int calculateMaxHappinessSeating() {
		List<String> maxHappinessSeating = new ArrayList<String>();
		int maxHappiness = 0;
		PermutationIterator<String> iter = new PermutationIterator<String>(attendees.keySet());
		while (iter.hasNext()) {
			List<String> arrangement = iter.next();
			int happinessScore = 0;
			Attendee lastSeat = attendees.get(arrangement.get(arrangement.size() - 1));
			for (String name : arrangement) {
				Attendee curSeat = attendees.get(name);
				happinessScore += curSeat.getFeelings().get(lastSeat.getName()) + lastSeat.getFeelings().get(name);
				lastSeat = curSeat;
			}
			if (happinessScore > maxHappiness) {
				maxHappiness = happinessScore;
				maxHappinessSeating = arrangement;
			}
		}
		log.info("Max happiness arrangement: " + maxHappinessSeating);
		return maxHappiness;
	}

	/**
	 * adds self to attendees list
	 */
	private void addSelfToAttendees() {
		Attendee selfAttendee = new Attendee(SELF);
		for (String name : attendees.keySet()) {
			Attendee partnerAttendee = attendees.get(name);
			selfAttendee.addFeeling(name, SELF_HAPPINESS);
			partnerAttendee.addFeeling(SELF, SELF_HAPPINESS);
		}
		attendees.put(SELF, selfAttendee);
	}

	/**
	 * based on each instruction, break down the instruction and populate attendees
	 * list with happiness values
	 * 
	 * @param input
	 */
	private void processinput(String input) {
		Matcher m = INPUT_PATTERN.matcher(input);
		if (m.find() && m.groupCount() == 4) {
			String attendeeName = m.group(1);
			String operation = m.group(2);
			Attendee attendee = attendees.containsKey(attendeeName) ? attendees.get(attendeeName)
					: new Attendee(attendeeName);

			// if gain then calculate add, else subtract
			int feeling = Integer.parseInt((operation.equals(SUBTRACT) ? "-" : "") + m.group(3));
			String otherName = m.group(4);

			attendee.addFeeling(otherName, feeling);
			attendees.put(attendeeName, attendee);
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

	class Attendee {
		private String attendeeName = "";
		private Map<String, Integer> feelings = new HashMap<String, Integer>();

		public Attendee(String name) {
			this.attendeeName = name;
		}

		public void addFeeling(String name, int feeling) {
			log.debug("[" + attendeeName + "] adding feeling {" + name + ", " + feeling + "}.");
			if (feelings.containsKey(name)) {
				feelings.put(name, feelings.get(name) + feeling);
			} else {
				feelings.put(name, feeling);
			}
		}

		public String getName() {
			return attendeeName;
		}

		public void setName(String name) {
			this.attendeeName = name;
		}

		public Map<String, Integer> getFeelings() {
			return feelings;
		}

		public void setFeelings(Map<String, Integer> feelings) {
			this.feelings = feelings;
		}

		@Override
		public String toString() {
			return "Attendee [name=" + attendeeName + ", feelings=" + feelings + "]";
		}

	}

	public static void main(String[] args) {
		SeatingArrangement sa = new SeatingArrangement();
		sa.run();
	}

}
