package fr.sttc.problems.flow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import fr.sttc.problems.flow.City.Type;

public class World {

	public City source;

	public Integer flowCapacity = 0;

	public Map<City, Collection<City>> mapCityToNeightboor = null;
	public Map<NamedEdge, Integer> edgeSplitWithCapacity = null;

	public Set<City> cities = new HashSet<>();
	public Set<NamedEdge> edges = new HashSet<>();
	public Set<NamedEdge> flows = new HashSet<>();


	public World() {

	}

	public World buildResidualWorld() {

		if (edgeSplitWithCapacity == null) {
			splitEdgeAndCapacity();
		}

		//for each flow build the reverse flow of complementary capacity
		Set<NamedEdge> residualEdges = new HashSet<>(2 * this.flows.size());
		for (NamedEdge flow : this.flows) {
			NamedEdge edgeNoCapacity = new NamedEdge(flow);
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
		residualWorld.flows = new HashSet<>();
		residualWorld.edges = residualEdges;
		residualWorld.source = this.source;
		residualWorld.flowCapacity = this.flowCapacity;

		return residualWorld;
	}

	private void splitEdgeAndCapacity() {
		edgeSplitWithCapacity = new HashMap<>(this.edges.size());
		//prepare the edges 
		for (NamedEdge edge: this.edges) {
			NamedEdge edgeNoCapacity = new NamedEdge(edge);
			edgeNoCapacity.capacity = 0;
			edgeSplitWithCapacity.put(edgeNoCapacity, edge.capacity);
		}
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

	public void updateCapacityAndEdged(List<City> path) {

		//1st get the minimum size
		if (edgeSplitWithCapacity == null) {
			splitEdgeAndCapacity();
		}
		Integer minCapacity = Integer.MAX_VALUE;
		List<NamedEdge> edgesWithoutCapacity = new ArrayList<>();

		for (int i = 0; i < path.size() -1; i++) {
			City source = path.get(i);
			City destination = path.get(i+1);
			NamedEdge edge = new NamedEdge(0, source, destination);
			minCapacity = Math.min(minCapacity, edgeSplitWithCapacity.get(edge));
			edgesWithoutCapacity.add(edge);
		}

		this.flowCapacity += minCapacity;

		for (NamedEdge edge : edgesWithoutCapacity) {
			edge.capacity = minCapacity;
			this.flows.add(edge);
		}

	}

	//TODO optimisation needed here
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
			Collection<City> neightboorhood = mapCityToNeightboor.get(city);
			if (neightboorhood != null && neightboorhood.size() > 0) {

				for (City neightboor : neightboorhood ) {
					if (!visited.contains(neightboor)) {
						toVisit.add(neightboor);
						mapCityAndPrevious.put(neightboor, city);
					}
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
