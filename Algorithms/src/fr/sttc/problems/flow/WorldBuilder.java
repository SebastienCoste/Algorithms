package fr.sttc.problems.flow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import fr.sttc.problems.flow.City.Type;

public class WorldBuilder {

	public World map;
	public Map <String, City> cityByName = null;
	
	public WorldBuilder (World map) {
		this.map = map;
	}
	
	public void initLoadingFromStream(String sourceName, String destinationName) {
		cityByName = new HashMap<>();
		map.edgeSplitWithCapacity = new HashMap<>();
		
		City source = new City(Type.SOURCE, sourceName, 0);
		map.source = source;
		cityByName.put(sourceName, source);
		City destination = new City(Type.DESTINATION, destinationName, 0);
		cityByName.put(destinationName, destination);
	}

	public void prepareEdgeSplitWithCapacity(String sourceName, String destinationName, Integer capacity) {
		City source = getCityByName(sourceName);
		City destination = getCityByName(destinationName);
		NamedEdge edgeNoCapacity = new NamedEdge(0, source, destination);
		Integer oldCapacity = map.edgeSplitWithCapacity.get(edgeNoCapacity);
		if (oldCapacity == null) {
			oldCapacity = 0;
		}
		map.edgeSplitWithCapacity.put(edgeNoCapacity, oldCapacity + capacity); 
	}

	public void buildFromStream() {
		if (cityByName != null) {
			map.cities = new HashSet<>(cityByName.values());
		}
		if (map.edgeSplitWithCapacity != null) {
			for (Entry<NamedEdge, Integer> edgeEntry : map.edgeSplitWithCapacity.entrySet()) {
				NamedEdge edge = edgeEntry.getKey();
				edge.capacity = edgeEntry.getValue();
				map.edges.add(edge);
			}
		}
	}
	
	private City getCityByName(String name) {
		City city = cityByName.get(name);
		if (city == null) {
			city = new City(Type.TRANSIT, name, 0);
		}
		return city;
	}
}
