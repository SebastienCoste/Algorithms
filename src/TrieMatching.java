import java.io.*;
import java.util.*;

class Node
{
	public static final int Letters =  4;
	public static final int NA      = -1;
	//	public int next [];
	public Node nextNode [];
	public boolean isEnd = false;
	public int size = 0;

	Node ()
	{
		//next = new int [Letters];
		nextNode = new Node [Letters];
		//value = Node.NA;
		//Arrays.fill (next, NA);
		Arrays.fill (nextNode, null);
	}
}

public class TrieMatching implements Runnable {
	private static final boolean useMock = false;

	int letterToIndex (char letter){
		switch (letter){
		case 'A': return 0;
		case 'C': return 1;
		case 'G': return 2;
		case 'T': return 3;
		default: assert (false); return Node.NA;
		}
	}

	List <Integer> solve (String text, int n, List <String> patterns) {
		List <Integer> result = new ArrayList <Integer> ();

		Node root = new Node();
		for(String pattern : patterns){
			Node node = root;
			for (int i = 0; i < pattern.length(); i++) {
				Character letter = pattern.charAt(i);
				Integer index = letterToIndex(letter);
				Node nextNode = node.nextNode[index];
				if (nextNode == null) {
					nextNode = new Node();
					node.nextNode[index] = nextNode;
				}
				node = nextNode;
			}
			node.isEnd = true;
			node.size = pattern.length();
		}

		List<Node> nodeExplored = new ArrayList<>();

		for (int i = 0; i < text.length(); i++) {
			char letter = text.charAt(i);
			Integer index = letterToIndex(letter);
			Node firstLetter = root.nextNode[index];
			List<Node> nextSteps = new ArrayList<>();
			if (firstLetter != null){
				if (firstLetter.isEnd) {
					result.add(i - firstLetter.size +1);
				}
				nextSteps.add(firstLetter);
			}
			for (Node node : nodeExplored) {
				Node next = node.nextNode[index];
				if (next != null) {
					if (next.isEnd) {
						result.add(i - next.size +1);
					}
					nextSteps.add(next);
				}
			}
			nodeExplored = nextSteps;
		}

		Collections.sort(result);
		return result;
	}

	public void run () {
		try {
			String text = null;
			int n = 0;
			List <String> patterns = new ArrayList <String> ();
			if (useMock) {
				text = "TCTGGGCCTAACCAACGGAGCCCGAGAACCAGTGGCGATTAACACTAAGATCTTCGAAACTTCCAAGTGAGCTATTTACTTGTCAAGCCTCCTGCTGGTTCGGTCCCGAGCTACTAAAAAATTGGGAGGCACACCGCGTAAGTGGATGTTTAGAGTCGACGGAGCCCCCTAAAACATATCTAATCACGTAGACCGTACACTCTATCCAGGTCCCCATACTGCAACCACAAGTAGTTCGCGGCTCGTGCTTGTTATCCGGAGCAACAATAACTTGTCATATGAATCTTCCATCCTGGTCCTCTGGAGCGTGATTGTCCAGCGCTACGGAGAACGGCGGCCACGAATAGAATTATCTCCCTGTTCCCCTTCCTAATTATTCACCCTTGTTCAAAAAAAAAGTGCATCCTACGTTACGGCTGCATATTACTTTCACCGCTAGCTTTTTAGGGCCGACAGAGTCTTAATTCTCTATGATATGGTCGTGTTGGGCGTTGGTAGGGTGAGATTTACTAGTCGAAGCTGGATCTTTTGATTATAAAGGAGCCCCTAAGGTGGCATAAGGTTTTGCAGGCATCGGTAATGATACAAGTTGACAAAATTCATCGTACCTCACGACGCTAGGTATCGCTTGCCTTAAAAATGAAAGCTCTCGATGTTACCTCCCTGAGCGGAATTTATGTTCAGAAATTGGCATGTACCATACGCAATGCCCCCATGAGGTGAGAGCGAGGTTGCCCGACTAGTATGTCACGGGGAGCTCCGATTGTAAATATTATAAGGCGTATGGGCAAGGCGCCACGATCTATCCTGACTGATCTTAATCCTACAGTATATATCTTGGATGCATAATAAATAGACCTCTGGAGGTCTCTGTCCACTGTGTGGCTCTATCCAACGCTACTTCATAGCTGACATCGTCCGCCGACGACGCAGTTTAAACGCACTGGTCCGAAGCGCACCGAGTGAATTTGGTTATCCGCTGGTTTGGAATAAATTAATAGAGTCGTTCGTGCAGTTATGCTCGGACCTCCAAGAGTAAAATCCCACACAGCGATACCATTGTCAGTTTCTCACCGGAACAGGTCTGAAGTTCCGGGGCCGCCGTCGACTTACCGGTCATTTGGAATTCCGTTGATCAAGGACGCGAAGTATTTCGCCAGACGCCCTAATGTATGTTTTCGATCCTATTATCCCAGTCGGACGTAACGTAGGACCCGTTAAGTACCCGAAACTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
				patterns.add("ATCG");
				patterns.add("GGGT");
				patterns.add("ATCGA");
				patterns.add("GGGTA");
				n = 4;
			} else {
				BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
				text = in.readLine ();
				n = Integer.parseInt (in.readLine ());
				for (int i = 0; i < n; i++) {
					patterns.add (in.readLine ());
				}
			}
			List <Integer> ans = solve (text, n, patterns);

			for (int j = 0; j < ans.size (); j++) {
				System.out.print ("" + ans.get (j));
				System.out.print (j + 1 < ans.size () ? " " : "\n");
			}
		}
		catch (Throwable e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}

	public static void main (String [] args) {
		new Thread (new TrieMatching ()).start ();
	}
}
