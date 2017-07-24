package fr.sttc.problems.flow;

import java.util.HashSet;
import java.util.Set;

public class World {


	public Set<City> cities = new HashSet<>();
	public Set<NamedEdge> edges = new HashSet<>();
	
	public Set<NamedEdge> flows = new HashSet<>();
	
	public World() {
		
	}
	
}
