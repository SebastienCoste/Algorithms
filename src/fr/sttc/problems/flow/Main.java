package fr.sttc.problems.flow;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fr.sttc.problems.flow.City.Type;

public class Main {

	public static void main(String[] args) {

		
		World valid = new World();
		World invalid = new World();
		
		City a = new City(Type.SOURCE, "a", 10);
		valid.cities.add(a);
		invalid.cities.add(a);
		valid.source = a;
		invalid.source = a;
		
		City b = new City(Type.TRANSIT, "b", 10);
		valid.cities.add(b);
		invalid.cities.add(b);
		City c = new City(Type.TRANSIT, "c", 10);
		valid.cities.add(c);
		invalid.cities.add(c);
		City d = new City(Type.TRANSIT, "d", 10);
		valid.cities.add(d);
		invalid.cities.add(d);
		City e = new City(Type.DESTINATION, "e", 10);
		valid.cities.add(e);
		invalid.cities.add(e);

		NamedEdge ab = new NamedEdge(5, a, b);
		NamedEdge ac = new NamedEdge(10, a, c);
		NamedEdge bd = new NamedEdge(5, b, d);
		NamedEdge de = new NamedEdge(10, d, e);
		NamedEdge cd = new NamedEdge(5, c, d);
		NamedEdge ce = new NamedEdge(5, c, e);
		Set<NamedEdge> allEdges= new HashSet<>(Arrays.asList(ab, ac, bd, de, cd, ce));
		valid.edges = allEdges;
		invalid.edges = allEdges;
		
		NamedEdge flowab = new NamedEdge(5, a, b);
		NamedEdge flowac = new NamedEdge(10, a, c);
		NamedEdge flowbd = new NamedEdge(5, b, d);
		NamedEdge flowde = new NamedEdge(10, d, e);
		NamedEdge flowcd = new NamedEdge(5, c, d);
		NamedEdge flowce = new NamedEdge(5, c, e);
		Set<NamedEdge> flowEdges = new HashSet<>(
				Arrays.asList(flowab, flowac, flowbd, flowde, flowcd, flowce));
		valid.flows = flowEdges;
		
		NamedEdge badflowab = new NamedEdge(5, a, b);
		NamedEdge badflowac = new NamedEdge(5, a, c);
		NamedEdge badflowbd = new NamedEdge(5, b, d);
		NamedEdge badflowde = new NamedEdge(5, d, e);
		NamedEdge badflowcd = new NamedEdge(10, c, d);
		NamedEdge badflowce = new NamedEdge(5, c, e);
		Set<NamedEdge> badFlowEdges= new HashSet<>(
				Arrays.asList(badflowab, badflowac, badflowbd, badflowde, badflowcd, badflowce));
		invalid.flows = badFlowEdges;
		
		System.out.println("is valid valid ? " + Validator.isMapValid(valid));
		assert(Validator.isMapValid(valid));
		System.out.println("is invalid valid ? " + Validator.isMapValid(invalid));
		assert(!Validator.isMapValid(invalid));
		
		System.out.println(valid.getPathSourceToDestination());
		
		System.out.println("MaxFlow capacity: " + FordFulkerson.getMaxFlow(valid));
		
		
		World ffWorld = new World();
		City s = new City(Type.SOURCE, "s", 0);
		City t = new City(Type.DESTINATION, "t", 0);
		City aa = new City(Type.TRANSIT, "aa", 0);
		City bb = new City(Type.TRANSIT, "bb", 0);
		City cc = new City(Type.TRANSIT, "cc", 0);
		City dd = new City(Type.TRANSIT, "dd", 0);
		Set<City> cities = new HashSet<>(Arrays.asList(s, t, aa, bb, cc, dd));
		ffWorld.cities = cities;
		ffWorld.source = s;
		
		NamedEdge e3 = new NamedEdge(3, aa, s);
		NamedEdge e8 = new NamedEdge(8, bb, aa);
		NamedEdge e7 = new NamedEdge(7, aa, t);
		NamedEdge e4 = new NamedEdge(4, s, cc);
		NamedEdge e5 = new NamedEdge(5, cc, bb);
		NamedEdge e2 = new NamedEdge(2, cc, dd);
		NamedEdge e1 = new NamedEdge(1, bb, dd);
		NamedEdge e6 = new NamedEdge(6, t, bb);
		Set<NamedEdge> edges= new HashSet<>(Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8));
		ffWorld.edges = edges;
		
		System.out.println("MaxFlow: " + FordFulkerson.getMaxFlow(ffWorld));
	}
}
