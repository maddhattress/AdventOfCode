package com.wonderland.projects.AdventOfCode2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShortestRoute {
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
		for(String route : routes) {
			log.debug("Parsing Route[" + route + "].");
			Matcher m = ROUTE_PATTERN.matcher(route);
			// check if instruction is valid.
			if (m.find() && m.groupCount() == 3) {
				String origin = m.group(1);
				String destination = m.group(2);
				int distance = Integer.parseInt(m.group(3));
				//add both to and from to map
				this.addToMap(origin, destination, distance);
				this.addToMap(destination, origin, distance);
			}else {
				log.error("Unknown format for Route[" + route + "].");
			}
		}
		this.printRouteMap();
	}

	private void addToMap(String origin, String destination, int distance) {
		if(!routeMap.containsKey(origin)) {
			routeMap.put(origin, new HashMap<String, Integer>());    
		}
		routeMap.get(origin).put(destination, distance);
	}
	
	private void printRouteMap() {
		log.info("Printing Route Map:");
		for(Entry<String, Map<String, Integer>> sourceRoutes : routeMap.entrySet()) {
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
		ShortestRoute sr = new ShortestRoute();
		sr.run();
	}

}
