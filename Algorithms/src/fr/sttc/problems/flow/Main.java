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
		System.out.println("is invalid invalid ? " + !Validator.isMapValid(invalid));
		assert(!Validator.isMapValid(invalid));
		
		valid.hasPathSourceToDestination();
	}


}
