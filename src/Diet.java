import java.io.*;
import java.util.*;

public class Diet {

	BufferedReader br;
	PrintWriter out;
	StringTokenizer st;
	boolean eof;
	int constraints;
	int dishes;
	double[][] constraintByDish;
	double[] maxByConstraint;
	double[] pleasures;
	double[] answer;

	double maxValue = Double.NEGATIVE_INFINITY;
	static double INFINITY = Math.pow(10,9);
	private boolean bounded = true;

	int solveDietProblem(int numberIneq) {

		//constraintByDish * answer <= maxByConstraint
		//Maximize pleasures * answer
		int[] indexesOfIneq = new int[numberIneq];
		for (int i=0; i < indexesOfIneq.length; i++){
			indexesOfIneq[i] = i;
		}
		//		int bufferData[]=new int[dishes];
		combinateDishesIneqInCOnstraintsPossibilities(indexesOfIneq, numberIneq, dishes, 0, new int[dishes], 0);

		// Write your code here
		return 0;
	}

	void combinateDishesIneqInCOnstraintsPossibilities(int indexesOfIneq[], int numberIneq, int variables, int index,
			int bufferData[], int endIndex)
	{
		// Current combination is ready to be printed, print it
		if (index == variables)
		{
			Set<Integer> chosenIndexes = new HashSet<>();
			for (int j=0; j<variables; j++)
				chosenIndexes.add(bufferData[j]);
			solveAsEqualities(chosenIndexes);
			return;
		}

		// When no more elements are there to put in data[]
		if (endIndex >= numberIneq)
			return;

		// current is included, put next at next location
		bufferData[index] = indexesOfIneq[endIndex];
		combinateDishesIneqInCOnstraintsPossibilities(indexesOfIneq, numberIneq, variables, index+1, bufferData, endIndex+1);

		// current is excluded, replace it with next (Note that
		// i+1 is passed, but index is not changed)
		combinateDishesIneqInCOnstraintsPossibilities(indexesOfIneq, numberIneq, variables, index, bufferData, endIndex+1);
	}

	private double computeVal(double[] result){
		double val = 0;
		for (int i=0; i < result.length; i++){
			val += result[i] * pleasures[i];
		}
		return val;
	}
	
	private boolean satisfiesAllInEq(double[] result){
		//check to see if eq satisfies regular equations
		for (int i=0; i < constraints; i++){
			double inEqSum = 0;
			for(int j= 0; j < dishes; j++){
					inEqSum += result[j] * this.constraintByDish[i][j];
			}
			if (inEqSum > maxByConstraint[i] + Math.pow(10, -3)){
				return false;
			}
		}
		//check to see if it satisfies constraints
		for (int i=0 ; i < dishes; i++){
			if (result[i] * -1 > Math.pow(10, -3)){
				return false;
			}
		}
		return true;
	}
	
	private void solveAsEqualities(Set<Integer> subset){
		double[][] A = new double[dishes][dishes];
		double[] b = new double[dishes];
		prepareMatrix(subset, A, b);
		LinearEqualitySolver les = new LinearEqualitySolver(A, b);
		double[] solution = les.solveEquation();
		
		if (solution == null){
			return;
		}
		if (!satisfiesAllInEq(solution)){
			return;
		}
		double val = computeVal(solution);
		if (val > maxValue){
			maxValue = val;
			answer = solution;
			if (subset.contains(constraints+dishes)){
				bounded = false;
			} else{
				bounded = true;
			}
		}
	}
	
	private void prepareMatrix(Set<Integer> set, double[][] matrix, double[] values){
		int index = 0;
		for (Integer val: set) {
			if (val < constraints) {
				matrix[index] = this.constraintByDish[val];
				values[index] = this.maxByConstraint[val];
			}
			else if (val < constraints+dishes){
				int diff = val - constraints;
				matrix[index] = new double[dishes];
				matrix[index][diff] = -1;
				values[index] = 0;
			}else{
				matrix[index] = new double[dishes];
				Arrays.fill(matrix[index], 1);
				values[index] = INFINITY;
			}
			index += 1;
		}
	}
	void solve() throws IOException {
		prepareContext();
		int numberIneq = constraints + dishes +1;
		int status = solveDietProblem(numberIneq);
		printResult(dishes, answer, status);
	}

	private void prepareContext() throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
		constraints = nextInt();
		dishes = nextInt();

		constraintByDish = new double[constraints][dishes];
		for (int i = 0; i < constraints; i++) {
			for (int j = 0; j < dishes; j++) {
				constraintByDish[i][j] = nextInt();
			}
		}
		maxByConstraint = new double[constraints]; 
		for (int i = 0; i < constraints; i++) {
			maxByConstraint[i] = nextInt();
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
		new Simplex().solve();
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

	public void print(){
		for(int i = 0; i < constraints; i++){
			for(int j = 0; j < dishes; j++){
				String value = String.format("%.2f", constraintByDish[i][j]);
				System.out.print(value + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}


	static class Equation {
		Equation(double a[][], double b[]) {
			this.a = a;
			this.b = b;
		}

		double a[][];
		double b[];
	}

	static class Position {
		@Override
		public String toString() {
			return "P[c=" + column + ", r=" + raw + "]";
		}
		Position(int column, int raw) {
			this.column = column;
			this.raw = raw;
		}

		int column;
		int raw;
	}

	static class LinearEqualitySolver {

		Equation equation = null;
		public LinearEqualitySolver(double[][] a, double[] b) {

			double[][] ea = new double[a.length][a[0].length];
			for (int i=0; i < a.length; i++){
				for (int j=0; j< a[0].length; j++){
					ea[i][j] = a[i][j];
				}
			}

			double[] eb = new double[b.length];
			for (int i=0; i< b.length; i++){
				eb[i] = b[i];
			}

			equation = new Equation(ea, eb);

			//			   double[] solution = solveEquation(equation);
		}


		Position selectPivotElement(double a[][], boolean used_raws[], boolean used_columns[], Position previous) {
			// This algorithm selects the first free element.
			Position pivot = new Position(0, 0);
			while (used_raws[pivot.raw])
				++pivot.raw;
			while (used_columns[pivot.column])
				++pivot.column;
			boolean columnUsable = false;
			while (!columnUsable && pivot.column < a.length) {
				int freeRaw = pivot.raw;
				while (freeRaw < a.length && a[freeRaw][pivot.column] == 0) {
					freeRaw++;
				}
				columnUsable = freeRaw < a.length;
				if (!columnUsable) {
					pivot.column++;
				} else {
					pivot.raw = freeRaw;
				}
			}
			return columnUsable ? pivot : null;
		}

		void swapLines(double a[][], double b[], boolean used_raws[], Position pivot) {
			int size = a.length;
			if (pivot.column == pivot.raw) {
				return;
			}
			for (int column = 0; column < size; ++column) {
				double tmpa = a[pivot.column][column];
				a[pivot.column][column] = a[pivot.raw][column];
				a[pivot.raw][column] = tmpa;
			}

			double tmpb = b[pivot.column];
			b[pivot.column] = b[pivot.raw];
			b[pivot.raw] = tmpb;

			boolean tmpu = used_raws[pivot.column];
			used_raws[pivot.column] = used_raws[pivot.raw];
			used_raws[pivot.raw] = tmpu;

			pivot.raw = pivot.column;
		}

		void processPivotElement(double a[][], double b[], Position pivot) {
			reducePivotRaw(a, b, pivot, a[pivot.raw][pivot.column]);
			for (int line = pivot.raw + 1; line < a.length; line++) {
				reducePivotInRawsBelow(a, b, pivot, line);
			}
		}

		private void reducePivotInRawsBelow(double[][] a, double[] b, Position pivot, int line) {
			double variableCoef = a[line][pivot.column];
			if (variableCoef == 0) {
				return;
			}
			for (int col = 0; col < a.length; col++) {
				a[line][col] = a[line][col] - variableCoef * a[pivot.raw][col];
			}
			b[line] = b[line] - variableCoef * b[pivot.raw];
		}

		private void reducePivotRaw(double[][] a, double[] b, Position pivot, double variableCoef) {
			if (variableCoef != 1) {
				for (int i = 0; i < a.length; i++) {
					a[pivot.raw][i] = a[pivot.raw][i] / variableCoef;
				}
				b[pivot.raw] = b[pivot.raw] / variableCoef;
			}
		}

		void markPivotElementUsed(Position pivot, boolean used_raws[], boolean used_columns[]) {
			used_raws[pivot.raw] = true;
			used_columns[pivot.column] = true;
		}

		double[] solveEquation() {
			double a[][] = equation.a;
			double b[] = equation.b;
			int size = a.length;

			boolean[] used_columns = new boolean[size];
			boolean[] used_raws = new boolean[size];
			Position position = new Position(0, 0);
			for (int step = 0; step < size; ++step) {
				position = selectPivotElement(a, used_raws, used_columns, position);
				if (position == null) {
					return null;
				}
				swapLines(a, b, used_raws, position);
				processPivotElement(a, b, position);
				markPivotElementUsed(position, used_raws, used_columns);
			}
			return buildResult(a, b);
		}


		private double[] buildResult(double[][] a, double[] b) {
			for (int var = b.length -1; var >=0; var--) {
				int notNull = 0;
				for (int col = 0; col < a.length; col++) {
					if (a[var][col] != 0) {
						notNull++;
					}
				}
				if (notNull != 1) {
					return null;
				}
				for (int line = 0; line < var; line ++) {
					b[line] = b[line] - a[line][var] * b[var] / a[var][var];
					a[line][var] = 0;
				}
			}
			return b;
		}
	}

}
