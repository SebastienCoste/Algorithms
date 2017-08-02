package coursera.datastructure.week1;
import java.util.*;
import java.io.*;

public class TreeHeightProcess {
	class FastScanner {
		StringTokenizer tok = new StringTokenizer("");
		BufferedReader in;

		FastScanner() {
			in = new BufferedReader(new InputStreamReader(System.in));
		}

		String next() throws IOException {
			while (!tok.hasMoreElements())
				tok = new StringTokenizer(in.readLine());
			return tok.nextToken();
		}
		int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
	}

	public class TreeHeight {
		int n;
		int parent[];
		int root;

		void read() throws IOException {
			FastScanner in = new FastScanner();
			n = in.nextInt();
			parent = new int[n];
			for (int i = 0; i < n; i++) {
				parent[i] = in.nextInt();
				if (parent[i] == -1) {
					root = i;
				}
			}
		}

		int computeHeight() {
			int maxHeight = 0;
			Map<Integer, Integer> heightByVertice = new HashMap<>();
			for (int vertex = 0; vertex < n; vertex++) {
				
				int height = 0;
				for (int i = vertex; i != -1; i = parent[i]) {
					Integer subHeight = heightByVertice.get(i);
					if (subHeight != null) {
						height += subHeight;
						break;
					}
					height++;
				}
				heightByVertice.put(vertex, height);
				maxHeight = Math.max(maxHeight, height);
			}
			return maxHeight;
		}
	}

	static public void main(String[] args) throws IOException {
		new Thread(null, new Runnable() {
			public void run() {
				try {
					new TreeHeightProcess().run();
				} catch (IOException e) {
				}
			}
		}, "1", 1 << 26).start();
	}
	public void run() throws IOException {
		TreeHeight tree = new TreeHeight();
		tree.read();
		System.out.println(tree.computeHeight());
	}
}
