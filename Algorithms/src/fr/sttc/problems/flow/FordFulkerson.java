package fr.sttc.problems.flow;

import java.util.List;

public class FordFulkerson {

	
	public static Integer getMaxFlow(World map) {
		
		List<City> path = map.getPathSourceToDestination();
		while (path != null) {
			map.updateCapacityAndEdged(path);
			map = map.buildResidualWorld();
			path = map.getPathSourceToDestination();
		}
		
		
		return map.flowCapacity;
	}
}
