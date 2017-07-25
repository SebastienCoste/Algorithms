package fr.sttc.problems.flow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class World {


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
}
