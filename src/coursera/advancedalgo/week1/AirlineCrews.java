package coursera.advancedalgo.week1;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;


public class AirlineCrews {
	private FastScanner in;
	private PrintWriter out;

	public static void main(String[] args) throws IOException {
		new AirlineCrews().solve();
	}

	public void solve() throws IOException {
		in = new FastScanner();
		out = new PrintWriter(new BufferedOutputStream(System.out));
						boolean[][] bipartiteGraph = readData();

//		long start = new Date().getTime();
//		int size = 100;
//		boolean [][] bipartiteGraph = new boolean [size][size];
//		for (int i = 0; i  < size; i++) {
//			for (int j = 0; j  < size; j++) {
//				bipartiteGraph [i][j] = i<=j;
//			}	
//		}
		//						boolean [][] bipartiteGraph = {
		//								{true, true, false, true},
		//								{false, true, false, false},
		//								{false, false, false, false}};
		//				Set<Integer> valid = new HashSet<>(100);

		int[] matching = findMatching(bipartiteGraph);
		writeResponse(matching);
		out.close();

//		long stop = new Date().getTime();
//		System.err.println(stop - start);
	}

	private int[] findMatching(boolean[][] bipartiteGraph) {

		int flightLength = bipartiteGraph.length;
		int[] assignment = new int[flightLength];

		World map = buildMapFromGraph(bipartiteGraph);

		map = FordFulkerson.getMaxFlow(map);

		fillAssignement(flightLength, assignment, map);

		return assignment;
	}

	private void fillAssignement(int flightLength, int[] assignment, World map) {
		for (NamedEdge edge : map.edges) {
			if (edge.source.type == Type.TRANSIT && edge.destination.type == Type.TRANSIT) {
				Integer src = Integer.valueOf(edge.source.name);
				Integer dst = Integer.valueOf(edge.destination.name);
				if (src>dst) {
					//The remaining flow in reverse order is the good assignment
					assignment[dst-1] = src - flightLength;
				}
			}
		}
	}

	private World buildMapFromGraph(boolean[][] bipartiteGraph) {
		WorldBuilder mapBuilder = new WorldBuilder(); 
		int crewLength = bipartiteGraph[0].length;
		int flightLength = bipartiteGraph.length;
		int maxEdgeCount = crewLength * flightLength + crewLength + flightLength;

		String sink = String.valueOf(flightLength + crewLength +1);
		mapBuilder.initLoadingFromStream("0", 
				sink, flightLength + crewLength +2, maxEdgeCount);

		for (int flight = 0; flight < flightLength; ++flight) {
			for (int crew = 0; crew < bipartiteGraph[flight].length; ++crew) {
				if (bipartiteGraph[flight][crew]) {
					mapBuilder.prepareEdgeSplitWithCapacity(
							String.valueOf(flight+1), 
							String.valueOf(flightLength + crew +1), 
							1);
				}
			}
			mapBuilder.prepareEdgeSplitWithCapacity("0", String.valueOf(flight+1), 1);
		}

		for (int crew = 0; crew < crewLength; ++crew) {
			mapBuilder.prepareEdgeSplitWithCapacity(String.valueOf(flightLength + crew +1), sink, 1);	
		}

		mapBuilder.buildFromStream();

		return mapBuilder.map;
	}

	boolean[][] readData() throws IOException {
		int numLeft = in.nextInt();
		int numRight = in.nextInt();
		boolean[][] adjMatrix = new boolean[numLeft][numRight];
		for (int i = 0; i < numLeft; ++i)
			for (int j = 0; j < numRight; ++j)
				adjMatrix[i][j] = (in.nextInt() == 1);
		return adjMatrix;
	}


	private void writeResponse(int[] matching) {
		for (int i = 0; i < matching.length; ++i) {
			if (i > 0) {
				out.print(" ");
			}
			if (matching[i] <= 0) {
				out.print("-1");
			} else {
				out.print(matching[i]);
			}
		}
		out.println();
	}

	static class FastScanner {
		private BufferedReader reader;
		private StringTokenizer tokenizer;

		public FastScanner() {
			reader = new BufferedReader(new InputStreamReader(System.in));
			tokenizer = null;
		}

		public String next() throws IOException {
			while (tokenizer == null || !tokenizer.hasMoreTokens()) {
				tokenizer = new StringTokenizer(reader.readLine());
			}
			return tokenizer.nextToken();
		}

		public int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
	}

	public static class City {

		@Override
		public String toString() {
			return "City["+ type + " / " + name + "]";
		}
		public Type type;
		public String name;
		public Integer capacity;

		public City(Type type, String name, Integer capacity) {
			this.type = type;
			this.capacity = capacity;
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((capacity == null) ? 0 : capacity.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			City other = (City) obj;
			if (capacity == null) {
				if (other.capacity != null)
					return false;
			} else if (!capacity.equals(other.capacity))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (type != other.type)
				return false;
			return true;
		}
	}

	public static class Edge {

		public Integer capacity;
		public City source;
		public City destination;

		public Edge (Integer capacity, City source, City destination) {
			this.capacity = capacity;
			this.source = source;
			this.destination = destination;
		}
		public Edge (Edge copy) {
			this.capacity = copy.capacity;
			this.source = copy.source;
			this.destination = copy.destination;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((capacity == null) ? 0 : capacity.hashCode());
			result = prime * result + ((destination == null) ? 0 : destination.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Edge other = (Edge) obj;
			if (capacity == null) {
				if (other.capacity != null)
					return false;
			} else if (!capacity.equals(other.capacity))
				return false;
			if (destination == null) {
				if (other.destination != null)
					return false;
			} else if (!destination.equals(other.destination))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}

	}
	public static class FordFulkerson {


		public static World getMaxFlow(World map) {

			List<City> path = map.getPathSourceToDestination();
			while (path != null) {
				map.updateCapacityAndEdged(path);
				map = map.buildResidualWorld();
				path = map.getPathSourceToDestination();
			}
			return map;
		}
	}



	public static class NamedEdge extends Edge {

		@Override
		public String toString() {
			return "Edge[c=" + capacity + ", s=" + source.name + ", d=" + destination.name + "]";
		}

		public String roadName;

		public NamedEdge (Integer capacity, City source, City destination) {
			super(capacity, source, destination);
		}

		public NamedEdge (Edge copy) {
			super(copy);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((roadName == null) ? 0 : roadName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			NamedEdge other = (NamedEdge) obj;
			if (roadName == null) {
				if (other.roadName != null)
					return false;
			} else if (!roadName.equals(other.roadName))
				return false;
			return true;
		}


	}
	public enum Type {
		SOURCE,
		TRANSIT,
		DESTINATION;
	}

	public static class World {

		public City source;

		public Integer flowCapacity = 0;

		public Map<City, Collection<City>> mapCityToNeightboor = null;
		public Map<NamedEdge, Integer> edgeSplitWithCapacity = null;

		public Set<City> cities = new HashSet<>();
		public Set<NamedEdge> edges = new HashSet<>();
		public Set<NamedEdge> flows = new HashSet<>();



		public World buildResidualWorld() {

			//for each flow build the reverse flow of complementary capacity
			Set<NamedEdge> residualEdges = new HashSet<>(this.edges.size());
			for (NamedEdge flow : this.flows) {
				NamedEdge oppositeFlow = new NamedEdge(1, flow.destination, flow.source);
				residualEdges.add(oppositeFlow);
				addNeightboor(flow.destination, flow.source);
				removeNeightboor(flow.source, flow.destination);
				this.edges.remove(flow);
			}
			this.edges.addAll(residualEdges);
			this.flows = new HashSet<>();

			return this;
		}

		private void removeNeightboor(City origin, City neightboor) {
			Collection<City> neightboors = this.mapCityToNeightboor.get(origin);
			if (neightboors != null) {
				neightboors.remove(neightboor);
			}
		}

		private void addNeightboor(City origin, City neightboor) {
			Collection<City> neightboors = this.mapCityToNeightboor.get(origin);
			if (neightboors == null) {
				neightboors = new HashSet<>();
				mapCityToNeightboor.put(origin, neightboors);
			}
			neightboors.add(neightboor);
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

			for (int i = 0; i < path.size() -1; i++) {
				City source = path.get(i+1);
				City destination = path.get(i);
				NamedEdge edge = new NamedEdge(1, source, destination);
				this.flows.add(edge);
			}

			this.flowCapacity += 1;

		}

		public List<City> getPathSourceToDestination() {

			if (mapCityToNeightboor == null) {
				buildNeightboor();
			}
			//We need one, not the best, DFS is easier to compute
			Set<City> visited = new HashSet<>();
			Stack<City> toVisitDFS = new Stack<>();
			Map<City, City> mapCityAndPrevious = new HashMap<>();
			toVisitDFS.add(source);
			while(!toVisitDFS.isEmpty()) {
				City city = toVisitDFS.pop();
				if (!visited.contains(city)) {

					if (city.type == Type.DESTINATION) {
						List<City> path = new ArrayList<>();
						path.add(city);
						City previous = mapCityAndPrevious.get(city);
						while(previous != null) {
							path.add(previous);
							previous = mapCityAndPrevious.get(previous);
						}
						return path;
					} 
					visited.add(city);
					Collection<City> neightboorhood = mapCityToNeightboor.get(city);
					if (neightboorhood != null && neightboorhood.size() > 0) {

						for (City neightboor : neightboorhood ) {
							if (!visited.contains(neightboor)) {
								toVisitDFS.add(neightboor);
								mapCityAndPrevious.put(neightboor, city);
							}
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

	public static class WorldBuilder {

		public World map;
		public Map <String, City> cityByName = null;
		public Integer cityCapacity = 0;

		public WorldBuilder () {
			this.map = new World();
		}

		public void initLoadingFromStream(String sourceName, String destinationName, Integer citySize, Integer edgeSize) {

			cityByName = new HashMap<>(citySize);
			map.edgeSplitWithCapacity = new HashMap<>(edgeSize);

			City source = new City(Type.SOURCE, sourceName, cityCapacity);
			map.source = source;
			cityByName.put(sourceName, source);
			City destination = new City(Type.DESTINATION, destinationName, cityCapacity);
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

		public World buildFromStream() {
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

			return map;
		}

		private City getCityByName(String name) {
			City city = cityByName.get(name);
			if (city == null) {
				city = new City(Type.TRANSIT, name, cityCapacity);
			}
			return city;
		}
	}

}
