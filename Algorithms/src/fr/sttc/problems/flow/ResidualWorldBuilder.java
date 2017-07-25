package fr.sttc.problems.flow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResidualWorldBuilder {


	public static World buildResidualWorld(World map) {

		Map<Edge, Integer> edgeSplitWithCapacity = new HashMap<>(map.edges.size());
		//prepare the edges 
		for (NamedEdge edge: map.edges) {
			Edge edgeNoCapacity = new Edge(edge);
			edgeNoCapacity.capacity = 0;
			edgeSplitWithCapacity.put(edgeNoCapacity, edge.capacity);
		}
		//for each flow build the reverse flow of complementary capacity
		Set<NamedEdge> residualEdges = new HashSet<>(2 * map.flows.size());
		for (NamedEdge flow : map.flows) {
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
		residualWorld.cities = map.cities;
		residualWorld.flows = map.flows;
		residualWorld.edges = residualEdges;

		return residualWorld;
	}
}
