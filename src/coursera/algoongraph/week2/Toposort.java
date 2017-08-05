package coursera.algoongraph.week2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Toposort {
	private static ArrayList<Integer> toposort(ArrayList<Integer>[] adj,ArrayList<Integer>[] revAdj) {
		int used[] = new int[adj.length];
		int totalUsed = 0;
		ArrayList<Integer> order = new ArrayList<Integer>();
		while(totalUsed < adj.length){
			int source = getSink(revAdj, used);
			List<Integer> dfsOrder = dfs(adj, used, source, new ArrayList<>());
			totalUsed += dfsOrder.size();	
			order.addAll(dfsOrder);
		}

		Collections.reverse(order);
		return order;
	}

	private static List<Integer> dfs(ArrayList<Integer>[] adj, int[] used, int root, List<Integer> order) {
		used[root] = 1;
		ArrayList<Integer> children = adj[root];
		for (Integer child : children) {
			if (used[child] == 0){
				dfs(adj, used, child, order);
			}
		}
		order.add(root);
		return order;
	}

	private static int getSink(ArrayList<Integer>[] adj, int[] used){
		int root = -1;
		int i = 0;
		while(root == -1){
			if (used[i] == 0){
				root = i;
			} else {
				i++;
			}
		}
		return getSink(adj, root, used);
	}

	private static int getSink(ArrayList<Integer>[] adj, int root, int[] used) {
		ArrayList<Integer> children = adj[root];
		boolean isSink = children.size() == 0;
		if (!isSink){
			for (Integer child : children) {
				if (used[child] == 0){
					return getSink(adj, child, used);
				}
			}
		}
		return root;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
		ArrayList<Integer>[] revAdj = (ArrayList<Integer>[])new ArrayList[n];
		for (int i = 0; i < n; i++) {
			adj[i] = new ArrayList<Integer>();
			revAdj[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < m; i++) {
			int x, y;
			x = scanner.nextInt();
			y = scanner.nextInt();
			adj[x - 1].add(y - 1);
			revAdj[y - 1].add(x - 1);
		}
		ArrayList<Integer> order = toposort(adj, revAdj);
		for (int x : order) {
			System.out.print((x + 1) + " ");
		}
	}
}

