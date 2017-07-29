import java.io.*;
import java.util.*;

public class Diet {

	BufferedReader br;
	PrintWriter out;
	StringTokenizer st;
	boolean eof;
	int constraint;
	int dishes;
	double[][] A;
	double[] values;
	double[] pleasures;
	double[] answer;

	int solveDietProblem() {
		Arrays.fill(answer, 1);
		// Write your code here
		return 0;
	}

	void solve() throws IOException {
		prepareContext();
		int status = solveDietProblem();
		printResult(dishes, answer, status);
	}

	private void prepareContext() throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
		constraint = nextInt();
		dishes = nextInt();
		A = new double[constraint][dishes];
		for (int i = 0; i < constraint; i++) {
			for (int j = 0; j < dishes; j++) {
				A[i][j] = nextInt();
			}
		}
		values = new double[constraint];
		for (int i = 0; i < constraint; i++) {
			values[i] = nextInt();
		}
		pleasures = new double[dishes];
		for (int i = 0; i < dishes; i++) {
			pleasures[i] = nextInt();
		}
		answer = new double[dishes];
	}

	private void printResult(int dishes, double[] ansx, int status) {
		if (status == -1) {
			out.printf("No solution\n");
			return;
		}
		if (status == 0) {
			out.printf("Bounded solution\n");
			for (int i = 0; i < dishes; i++) {
				out.printf("%.18f%c", ansx[i], i + 1 == dishes ? '\n' : ' ');
			}
			return;
		}
		if (status == 1) {
			out.printf("Infinity\n");
			return;
		}
		

		out.close();
	}

	public static void main(String[] args) throws IOException {
		new Diet().solve();
	}

	String nextToken() {
		while (st == null || !st.hasMoreTokens()) {
			try {
				st = new StringTokenizer(br.readLine());
			} catch (Exception e) {
				eof = true;
				return null;
			}
		}
		return st.nextToken();
	}

	int nextInt() throws IOException {
		return Integer.parseInt(nextToken());
	}
}
