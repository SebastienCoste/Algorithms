package coursera.algoonstring.week1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Trie {
	
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


	List<Map<Character, Integer>> buildTrie(String[] patterns) {
		List<Map<Character, Integer>> trie = new ArrayList<Map<Character, Integer>>();
		trie.add(new HashMap<>()); //root
		for (String pattern : patterns) {
			Integer position = 0;
			for (int i = 0; i < pattern.length(); i++) {
				Character letter = pattern.charAt(i);
				Map<Character, Integer> childrenOfNode = trie.get(position);
				if (childrenOfNode == null) {
					System.err.println("Null node, cannot happen");
				} else {
					Integer nodeLetter = childrenOfNode.get(letter);
					if (nodeLetter == null) {
						position = trie.size();
						childrenOfNode.put(letter, position);
						trie.add(new HashMap<>());
					} else {
						position = nodeLetter;
					}
				}
			}
		}

		return trie;
	}

	static public void main(String[] args) throws IOException {
		new Trie().run();
	}

	public void print(List<Map<Character, Integer>> trie) {
		for (int i = 0; i < trie.size(); ++i) {
			Map<Character, Integer> node = trie.get(i);
			for (Map.Entry<Character, Integer> entry : node.entrySet()) {
				System.out.println(i + "->" + entry.getValue() + ":" + entry.getKey());
			}
		}
	}

	public void run() throws IOException {
		FastScanner scanner = new FastScanner();
		int patternsCount = scanner.nextInt();
		String[] patterns = new String[patternsCount];
		for (int i = 0; i < patternsCount; ++i) {
			patterns[i] = scanner.next();
		}
		List<Map<Character, Integer>> trie = buildTrie(patterns);
		print(trie);
	}
}
