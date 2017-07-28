package fr.sttc.problems.flow;

import java.util.List;

public class FordFulkerson {

	
	public static Integer getMaxFlow(World map) {
		
		List<City> path = map.getPathSourceToDestination();
		while (path != null) {
			System.out.println("intermediate path: " + path);
			map.updateCapacityAndEdged(path);
			map = map.buildResidualWorld();
			System.out.println("intermediate capacity: " + map.flowCapacity);
			path = map.getPathSourceToDestination();
		}
		
		
		return map.flowCapacity;
	}
}
