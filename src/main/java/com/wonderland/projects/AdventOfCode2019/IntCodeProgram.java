package com.wonderland.projects.AdventOfCode2019;

import java.util.HashMap;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class IntCodeProgram<K,V> extends HashMap<K,V> {
	protected V defaultValue;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8332240810019110832L;
	private static final Logger log = LogManager.getLogger();

	public IntCodeProgram(V defaultValue) {
	    this.defaultValue = defaultValue;
	  }
	
	public V get(Object key) {
		if(key instanceof Integer && 
			(int)key < 0) {
			log.error("NegativeKeyException for key: " + key);
			throw new NumberFormatException();
		}
		 return containsKey(key) ? super.get(key) : defaultValue;
	}

}
