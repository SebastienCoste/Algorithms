package coursera.algoongraph.week1;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class ConnectedComponents {
	private static int numberOfComponents(ArrayList<Integer>[] adj) {
		int result = 0;
		int[] vertices = new int[adj.length];
		for (int i = 0; i < adj.length; i++) {
			vertices[i] = i;
		}

		int x = getFreeVertice(vertices);
		while (x > -1) {
			result++;

			Set<Integer> visited = new HashSet<>();
			Stack<Integer> queue = new Stack<>();
			queue.add(x);

			while (!queue.isEmpty()) {
				Integer bfs = queue.pop();
				visited.add(bfs);

				for (Integer nbr : adj[bfs]) {
					if (!visited.contains(nbr)) {
						queue.add(nbr);
					}
				}
			}
			
			for (Integer vertice : visited) {
				vertices[vertice] = -1;
			}
			
			x = getFreeVertice(vertices);
		}

		return result;
	}

	private static int getFreeVertice(int[] vertices) {
		for (int vertice : vertices) {
			if (vertice >=0){
				return vertice;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
		for (int i = 0; i < n; i++) {
			adj[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < m; i++) {
			int x, y;
			x = scanner.nextInt();
			y = scanner.nextInt();
			adj[x - 1].add(y - 1);
			adj[y - 1].add(x - 1);
		}
		System.out.println(numberOfComponents(adj));
	}
}

