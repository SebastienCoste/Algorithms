package fr.sttc.problems.flow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResidualWorldBuilder {


	public static World buildResidualWorld(World map) {

		World residualWorld = new World();
		residualWorld.cities = map.cities;
		residualWorld.edges = map.edges;
		
		Map<Edge, Integer> edgeSplitWithCapacity = new HashMap<>(map.edges.size());
		//prepare the edges 
		for (NamedEdge edge: map.edges) {
			Edge edgeNoCapacity = new Edge(edge);
			edgeNoCapacity.capacity = 0;
			edgeSplitWithCapacity.put(edgeNoCapacity, edge.capacity);
		}
		//for each flow build the reverse flow of complementary capacity
		Set<NamedEdge> reversedFlow = new HashSet<>(map.flows.size());
		for (NamedEdge flow : map.flows) {
			if (flow.capacity > 0) {
				Edge edgeNoCapacity = new Edge(flow);
				edgeNoCapacity.capacity = 0;
				Integer edgeCapacity = edgeSplitWithCapacity.get(edgeNoCapacity);
				if (flow.capacity != edgeCapacity) {
					NamedEdge revFlow = new NamedEdge(edgeCapacity - flow.capacity, 
							flow.source, flow.destination);
					revFlow.roadName = flow.roadName;
					reversedFlow.add(revFlow);
				}
			}
		}
		
		residualWorld.flows = reversedFlow;

		return residualWorld;
	}
}
