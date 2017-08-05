package coursera.algoongraph.week2;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Acyclicity {
	private static int acyclic(ArrayList<Integer>[] adj) {
		Set<Integer> acycle = new HashSet<>();
		for (int i = 0; i < adj.length; i++) {
			if (!acycle.contains(i)){
				Set<Integer> visible = getVisibleFromNode(i, adj, acycle);
				if (visible == null){
					return 1;
				}
				acycle.addAll(visible);
			}
		}

		return 0;
	}

	private static Set<Integer> getVisibleFromNode(int root, ArrayList<Integer>[] mapToNbr, Set<Integer> excluded) {

		HashSet<Integer> ancestors = new HashSet<>();
		ancestors.add(root);
		return getVisited(root, mapToNbr, ancestors, excluded);
	}

	private static Set<Integer> getVisited(int root, ArrayList<Integer>[] mapToNbr, Set<Integer> ancestors, Set<Integer> excluded){

		Set<Integer> visited = new HashSet<>();
		visited.add(root);
		ArrayList<Integer> children = mapToNbr[root];
		for (Integer child : children) {
			if (!excluded.contains(child)){
				if (ancestors.contains(child)){
					return null;
				}
				Set<Integer> ancestorspp = new HashSet<>(ancestors);
				ancestorspp.add(child);
				Set<Integer> visitedChild = getVisited(child, mapToNbr, ancestorspp, excluded);
				if (visitedChild == null){
					return null;
				}
				visited.addAll(visitedChild);
			}
		}

		return visited;
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
		}
		System.out.println(acyclic(adj));
	}
}

