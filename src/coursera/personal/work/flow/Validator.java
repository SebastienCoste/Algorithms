package coursera.personal.work.flow;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import coursera.personal.work.flow.City.Type;

public class Validator {


	public static boolean isMapValid(World map) {

		Map<City, Integer> flowOutOfCity = new HashMap<>(map.cities.size());
		Map<City, Integer> flowInCity = new HashMap<>(map.cities.size());
		Map<Edge, Integer> flowByEdge = new HashMap<>(map.edges.size());

		fillTheFlows(map, flowOutOfCity, flowInCity, flowByEdge);

		boolean validationCities = validateCitiesFlows(map, flowOutOfCity, flowInCity);
		
		if (!validationCities) {
			System.err.println("Cities invalids");
			return false;
		}
		
		//We have the flow used for all edges, now we substract the capacity of each edge
		//We do it after the cities, because if cities are invalid this is useless
		updateEdgesWithCapacity(map, flowByEdge);
		

		return validateEdgesFlow(flowByEdge);
	}

	private static boolean validateEdgesFlow(Map<Edge, Integer> flowByEdge) {
		// capacity > 0 means we used more capacity than allowed
		for (Entry<Edge, Integer> flowEntry : flowByEdge.entrySet()) {
			if (flowEntry.getValue() > 0) {
				Edge edge = flowEntry.getKey();
				System.err.println(edge.source + " to " + edge.destination + " (" + 
				edge.capacity + "), overflow of " + flowEntry.getValue());
				return false;
			}
		}
		return true;
	}

	private static void updateEdgesWithCapacity(World map, Map<Edge, Integer> flowByEdge) {
		for (NamedEdge edge : map.edges) {
			Edge unnamedEdge = new Edge(edge);
			Integer capacityOnEdge = flowByEdge.get(unnamedEdge);
			if (capacityOnEdge == null) {
				capacityOnEdge = 0;
			}
			flowByEdge.put(unnamedEdge, capacityOnEdge - edge.capacity);
		}
	}

	private static boolean validateCitiesFlows(World map, Map<City, Integer> flowOutOfCity,
			Map<City, Integer> flowInCity) {
		//Validate each city
		for (City city : map.cities) {
			if (city.type == Type.TRANSIT) {
				Integer capacityOut = flowOutOfCity.get(city);
				Integer capacityIn = flowInCity.get(city);
				if (capacityIn != capacityOut || capacityIn > city.capacity) {
					System.err.println("city " + city.name + "/" + city.type
							+ " (" + city.capacity + "): flow in: " + capacityIn
							+ ", flow out: " + capacityOut);
					return false;
				}
			}
		}
		return true;
	}

	private static void fillTheFlows(World map, Map<City, Integer> flowOutOfCity, Map<City, Integer> flowInCity,
			Map<Edge, Integer> flowByEdge) {
		//the big point here is to have several edges between the same cities
		for (NamedEdge edge : map.flows) {

			//How much out of the city source until now ?
			updateFlowOutOfCity(flowOutOfCity, edge);

			//How much going to the city destination until now ?
			updateFlowInCity(flowInCity, edge);

			//How much through this edge until now ?
			updateFlowThroughEdge(flowByEdge, edge);
		}
	}

	private static void updateFlowThroughEdge(Map<Edge, Integer> flowByEdge, NamedEdge edge) {
		Edge unnamedEdge = edge instanceof NamedEdge ? new Edge(edge) : edge;
		Integer capacityOnEdge = flowByEdge.get(unnamedEdge);
		if (capacityOnEdge == null) {
			capacityOnEdge = 0;
		}
		flowByEdge.put(unnamedEdge, capacityOnEdge + edge.capacity);
	}

	private static void updateFlowInCity(Map<City, Integer> flowInCity, NamedEdge edge) {
		Integer capacityInCity = flowInCity.get(edge.destination);
		if (capacityInCity == null) {
			capacityInCity = 0;
		}
		flowInCity.put(edge.destination, capacityInCity + edge.capacity);
	}

	private static void updateFlowOutOfCity(Map<City, Integer> flowOutOfCity, NamedEdge edge) {
		Integer capacityOutCity = flowOutOfCity.get(edge.source);
		if (capacityOutCity == null) {
			capacityOutCity = 0;
		}
		flowOutOfCity.put(edge.source, capacityOutCity + edge.capacity);
	}

}
