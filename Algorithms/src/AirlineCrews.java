import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
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

		//        3 4
//		boolean [][] bipartiteGraph = {
//				{true, true, false, true},
//				{false, true, false, false},
//				{false, false, false, false}};

		int[] matching = findMatching(bipartiteGraph);
		writeResponse(matching);
		out.close();
	}

	private int[] findMatching(boolean[][] bipartiteGraph) {

		int flightLength = bipartiteGraph.length;
		int[] assignment = new int[flightLength];
		
		World map = buildMapFromGraph(bipartiteGraph);
		List<List<City>> paths = FordFulkerson.getPathsMaxFlow(map);
		for (List<City> list : paths) {
			assignment[Integer.valueOf(list.get(1).name) -1] = Integer.valueOf(list.get(2).name) - flightLength; 
		}
		
		return assignment;
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
		
		World map = mapBuilder.map;
		return map;
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


		public static Integer getMaxFlow(World map) {

			List<City> path = map.getPathSourceToDestination();
			while (path != null) {
				//				System.out.println("intermediate path: " + path);
				map.updateCapacityAndEdged(path);
				map = map.buildResidualWorld();
				//				System.out.println("intermediate capacity: " + map.flowCapacity);
				path = map.getPathSourceToDestination();
			}


			return map.flowCapacity;
		}
		
		public static List<List<City>> getPathsMaxFlow(World map) {

			List<List<City>> validPaths = new ArrayList<>();
			List<City> path = map.getPathSourceToDestination();
			while (path != null) {
				//				System.out.println("intermediate path: " + path);
				validPaths.add(path);
				map.updateCapacityAndEdged(path);
				map = map.buildResidualWorld();
				//				System.out.println("intermediate capacity: " + map.flowCapacity);
				path = map.getPathSourceToDestination();
			}


			return validPaths;
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

				edgeSplitWithCapacity.remove(edgeNoCapacity);
			}
			if (edgeSplitWithCapacity.size()>0) {
				for (Entry<NamedEdge, Integer> edgeEntry : edgeSplitWithCapacity.entrySet()) {
					NamedEdge key = edgeEntry.getKey();
					residualEdges.add(new NamedEdge(edgeEntry.getValue(), key.source, key.destination));
				}
			}
			edgeSplitWithCapacity = null;

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
			splitEdgeAndCapacity();
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
			//			Stack<City> toVisitDFS = new Stack<>();
			Queue<City> toVisitBFS = new LinkedList<>();
			Map<City, City> mapCityAndPrevious = new HashMap<>();
			toVisitBFS.add(source);
			while(!toVisitBFS.isEmpty()) {
				City city = toVisitBFS.poll();
				if (!visited.contains(city)) {

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
								toVisitBFS.add(neightboor);
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
