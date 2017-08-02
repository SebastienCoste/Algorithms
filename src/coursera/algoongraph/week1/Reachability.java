package coursera.algoongraph.week1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;


public class Reachability {
	
	
    private static int reach(ArrayList<Integer>[] adj, int x, int y) {
    	
    	Set<Integer> visitedX = new HashSet<>();
    	Set<Integer> visitedY = new HashSet<>();

    	Stack<Integer> queueX = new Stack<>();
    	Stack<Integer> queueY = new Stack<>();
    	queueX.add(x);
    	queueY.add(y);
    	
    	while (!queueX.isEmpty() && !queueY.isEmpty()) {
    		Integer bfsX = queueX.pop();
    		visitedX.add(bfsX);
    		if (visitedY.contains(bfsX)) {
    			return 1;
    		}
    		
    		Integer bfsY = queueY.pop();
    		visitedY.add(bfsY);
    		if (visitedX.contains(bfsY)) {
    			return 1;
    		}

    		for (Integer nbr : adj[bfsX]) {
    			if (!visitedX.contains(nbr)) {
    				queueX.add(nbr);
    			}
    		}
    		for (Integer nbr : adj[bfsY]) {
    			if (!visitedY.contains(nbr)) {
    				queueY.add(nbr);
    			}
    		}
    	}
    	
    	return 0;
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
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        System.out.println(reach(adj, x, y));
    }
}

