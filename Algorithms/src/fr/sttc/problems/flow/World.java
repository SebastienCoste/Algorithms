package fr.sttc.problems.flow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import fr.sttc.problems.flow.City.Type;

public class World {

	public City source;
	public City destination;
	
	public Map<City, Collection<City>> mapCityToNeightboor = null;
	
	public Set<City> cities = new HashSet<>();
	public Set<NamedEdge> edges = new HashSet<>();
	public Set<NamedEdge> flows = new HashSet<>();
	
	
	public World() {
		
	}
	
	public World buildResidualWorld() {

		Map<Edge, Integer> edgeSplitWithCapacity = new HashMap<>(this.edges.size());
		//prepare the edges 
		for (NamedEdge edge: this.edges) {
			Edge edgeNoCapacity = new Edge(edge);
			edgeNoCapacity.capacity = 0;
			edgeSplitWithCapacity.put(edgeNoCapacity, edge.capacity);
		}
		//for each flow build the reverse flow of complementary capacity
		Set<NamedEdge> residualEdges = new HashSet<>(2 * this.flows.size());
		for (NamedEdge flow : this.flows) {
			Edge edgeNoCapacity = new Edge(flow);
			edgeNoCapacity.capacity = 0;
			Integer edgeCapacity = edgeSplitWithCapacity.get(edgeNoCapacity);

			if (flow.capacity > 0) {
				NamedEdge oppositeFlow = new NamedEdge(flow.capacity, 
						flow.destination, flow.source);
				oppositeFlow.roadName = flow.roadName;
				residualEdges.add(oppositeFlow);
			}
			if (flow.capacity < edgeCapacity) {
				NamedEdge residualFlow = new NamedEdge(edgeCapacity - flow.capacity, 
						flow.source, flow.destination);
				residualFlow.roadName = flow.roadName;
				residualEdges.add(residualFlow);
			}
		}

		World residualWorld = new World();
		residualWorld.cities = this.cities;
		residualWorld.flows = this.flows;
		residualWorld.edges = residualEdges;

		return residualWorld;
	}
	

	public Integer getCapacityOfCut(Set<City> cut) {
		
		Integer capacity = 0;
		for (NamedEdge edge : this.edges) {
			if (cut.contains(edge.source) && !cut.contains(edge.destination)) {
				capacity+=edge.capacity;
			}
		}
		return capacity;
	}
	
	public List<City> getPathSourceToDestination() {
		
		if (mapCityToNeightboor == null) {
			buildNeightboor();
		}
		//We need one, not the best, DFS is easier to compute
		Set<City> visited = new HashSet<>();
		Stack<City> toVisit = new Stack<>();
		Map<City, City> mapCityAndPrevious = new HashMap<>();
		toVisit.add(source);
		while(!toVisit.isEmpty()) {
			City city = toVisit.pop();
			if (city.type == Type.DESTINATION) {
				List<City> path = new ArrayList<>();
				path.add(city);
				City previous = mapCityAndPrevious.get(city);
				while(previous != null) {
					path.add(previous);
					previous = mapCityAndPrevious.get(previous);
				}
				Collections.reverse(path);
				return path;
			} 
			visited.add(city);
			for (City neightboor : mapCityToNeightboor.get(city)) {
				if (!visited.contains(neightboor)) {
					toVisit.add(neightboor);
					mapCityAndPrevious.put(neightboor, city);
				}
			}
			
			
		}
		
		return null;
	}
	
	public void buildNeightboor() {
		
		mapCityToNeightboor = new HashMap<>();
		
		for (NamedEdge edge : edges) {
			Collection<City> neightboor = mapCityToNeightboor.get(edge.source);
			if (neightboor == null) {
				neightboor = new HashSet<>();
				mapCityToNeightboor.put(edge.source, neightboor);
			}
			neightboor.add(edge.destination);
			
		}
		
	}
}
