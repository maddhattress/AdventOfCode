package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
 *      href="Advent of Code 2015, Day 9">https://adventofcode.com/2015/day/9</a>
 *
 */
public class RouteCalculator {
	private static final Logger log = LogManager.getLogger();
	private static final String INPUT = "input/2015/day9.txt";

	// keywords
	private static final String TO = "to";
	private static final String EQUALS = "=";

	private static final String ROUTE_REGEX = "^(.+)\\s" + TO + "\\s(.+)\\s" + EQUALS + "\\s(\\d+)$";
	private static final Pattern ROUTE_PATTERN = Pattern.compile(ROUTE_REGEX);

	private HashMap<String, Map<String, Integer>> routeMap = new HashMap<String, Map<String, Integer>>();

	/**
	 * main processing unit
	 */
	public void run() {
		List<String> routes = this.readRoutes();
		for (String route : routes) {
			log.debug("Parsing Route[" + route + "].");
			Matcher m = ROUTE_PATTERN.matcher(route);
			// check if instruction is valid.
			if (m.find() && m.groupCount() == 3) {
				String origin = m.group(1);
				String destination = m.group(2);
				int distance = Integer.parseInt(m.group(3));
				// add both to and from to map
				this.addToMap(origin, destination, distance);
				this.addToMap(destination, origin, distance);
			} else {
				log.error("Unknown format for Route[" + route + "].");
			}
		}
		// print map for debugging
		this.printRouteMap();
		// calculate all possibilities
		Map<List<String>, Integer> calculatedRoutes = this.calculateAllRoutes();
		log.info("Shortest Route: " + Collections.min(calculatedRoutes.values()));
		log.info("Longest Route: " + Collections.max(calculatedRoutes.values()));

	}

	/**
	 * helper function to 2 points and the distance between them to the map
	 * 
	 * @param origin
	 * @param destination
	 * @param distance
	 */
	private void addToMap(String origin, String destination, int distance) {
		if (!routeMap.containsKey(origin)) {
			routeMap.put(origin, new HashMap<String, Integer>());
		}
		routeMap.get(origin).put(destination, distance);
	}

	/**
	 * calculates all distance of all possible routes
	 * 
	 * @return
	 */
	private Map<List<String>, Integer> calculateAllRoutes() {
		log.info("Calculating Routes:");
		// hacking shortest route because 'each location must be visited exactly once'
		PermutationIterator<String> iter = new PermutationIterator<String>(routeMap.keySet());
		Map<List<String>, Integer> calculatedRoutes = new HashMap<List<String>, Integer>();
		while (iter.hasNext()) {
			List<String> route = iter.next();
			int distance = 0;
			String origin = null;
			// go thru the route and figure out the distance between each point to
			// identify total distance for entire route
			for (String destination : route) {
				if (origin != null) {
					int distanceBetween = routeMap.get(origin).get(destination);
					log.debug("Distance between [" + origin + "] --> [" + destination + "] = " + distanceBetween);
					distance += distanceBetween;
					log.debug("Total distance so far: " + distance);
				}
				origin = destination;
			}
			// if not in route list. add
			if (!calculatedRoutes.keySet().contains(route)) {
				log.debug("Adding Route[" + route.toString() + "] with distance [" + distance + "] to map.");
				calculatedRoutes.put(route, distance);
			}
		}
		log.debug("Total routes: " + calculatedRoutes.size());
		return calculatedRoutes;
	}

	/**
	 * helper function to print out the map neatly
	 */
	private void printRouteMap() {
		log.info("Printing Route Map:");
		for (Entry<String, Map<String, Integer>> sourceRoutes : routeMap.entrySet()) {
			log.info(sourceRoutes.toString());
		}
	}

	/**
	 * reads the instructions from the input file
	 * 
	 * @return
	 */
	private List<String> readRoutes() {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INPUT)));
		List<String> routes = reader.lines().collect(Collectors.toList());

		IOUtils.closeQuietly(reader);
		return routes;

	}

	public static void main(String[] args) {
		RouteCalculator sr = new RouteCalculator();
		sr.run();
	}

}
