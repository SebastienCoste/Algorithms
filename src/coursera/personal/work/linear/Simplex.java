package coursera.personal.work.linear;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Simplex {

	BufferedReader br;
	PrintWriter out;
	StringTokenizer st;
	boolean eof;
	void solve() throws IOException {
		int n = nextInt();
		int m = nextInt();
		double[][] A = new double[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				A[i][j] = nextInt();
			}
		}
		double[] b = new double[n];
		for (int i = 0; i < n; i++) {
			b[i] = nextInt();
		}
		double[] c = new double[m];
		for (int i = 0; i < m; i++) {
			c[i] = nextInt();
		}
		LinearEquationsSolver ls = new LinearEquationsSolver(A, b, c);
		ls.print();
	}

	Simplex() throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
		solve();
		out.close();
	}

	public static void main(String[] args) throws IOException {
		new Simplex();
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

class LinearEquationsSolver {
	double[][] constraintByDish; //co-efficient matrix
	double[] maxByConstraint; //output matrix for co-efficient
	double[] pleasures; //objective function matrix

	int constraints;//no of input equations
	int dishes; // no of variables
	double[] answer = null;
	double maxValue = Double.NEGATIVE_INFINITY;
	static double INFINITY = Math.pow(10,9);
	private boolean bounded = true;

	LinearEquationsSolver(double[][] A, double[] b, double[] c){
		constraints = A.length;
		dishes = c.length;
		this.constraintByDish = A;
		this.maxByConstraint = b;
		this.pleasures = c;
		//total no of inequalities = n +m + 1(for infinity)
		int numberIneq = constraints + dishes +1;
		compute(numberIneq);
	}

	public void print(){
		if (maxValue == Double.NEGATIVE_INFINITY){
			System.out.println("No solution");
			return;
		}
		if (!bounded){
			System.out.println("Infinity");
			return;
		}
		System.out.println("Bounded solution");
		for (int i=0; i< answer.length; i++){
			System.out.print(String.format("%.15f", answer[i]) + " ");
		}
	}

	private void compute(int numberIneq){
		int[] arr = new int[numberIneq];
		for (int i=0; i < arr.length; i++){
			arr[i] = i;
		}
		genProcessCombinations(arr,numberIneq, dishes);
	}

	/**
	 * Process subset of size
	 * @param subset
	 */
	private void solveAsEqualities(Set<Integer> subset){
		double[][] A = new double[dishes][dishes];
		double[] b = new double[dishes];
		prepareMatrix(subset, A, b);
		GaussianElimination gElim = new GaussianElimination(A, b);
		if (!gElim.hasSolution){
			return;
		}
		double[] temp_result = gElim.b;
		if (!satisfiesAllInEq(temp_result)){
			return;
		}
		double val = computeVal(temp_result);
		if (val > maxValue){
			maxValue = val;
			answer = temp_result;
			if (subset.contains(constraints+dishes)){
				bounded = false;
			} else{
				bounded = true;
			}
		}
	}

	/**
	 * Verify result satisfies all the equations
	 * @param result
	 * @return
	 */
	private boolean satisfiesAllInEq(double[] result){
		boolean satisfied = true;
		//check to see if eq satisfies regular equations
		for (int i=0; i < constraints; i++){
			double inEqSum = 0;
			for(int j= 0; j < dishes; j++){
					inEqSum += result[j] * this.constraintByDish[i][j];
			}
			if (inEqSum > maxByConstraint[i] + Math.pow(10, -3)){
				//not satisfied
				satisfied = false;
				break;
			}
		}
		//check to see if it satisfies constraints
		for (int i=0 ; i < dishes; i++){
			if (result[i] * -1 > Math.pow(10, -3)){
				//not satisfied
				satisfied = false;
				break;
			}

		}
		return  satisfied;

	}

	private double computeVal(double[] result){
		double val = 0;
		for (int i=0; i < result.length; i++){
			val += result[i] * pleasures[i];
		}
		return val;
	}

	/**
	 * Update matrices
	 */
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


	/**
	  arr[]  ---> Input Array
	data[] ---> Temporary array to store current combination
	start & end ---> Staring and Ending indexes in arr[]
	index  ---> Current index in data[]
	r ---> Size of a combination to be printed (variables)
	 **/

//	combinationUtil(arr, numberIneq, variables, 0, bufferData, 0);
	void combinateDishesIneqInCOnstraintsPossibilities(int indexesOfIneq[], int numberIneq, int variables, int index,
								int bufferData[], int endIndex)
	{
		// Current combination is ready to be printed, print it
		if (index == variables)
		{
			Set<Integer> set = new HashSet<>();
			for (int j=0; j<variables; j++)
				set.add(bufferData[j]);
			solveAsEqualities(set);
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

	/**
	 *     The main function that prints all combinations of size r
	 *      in arr[] of size n. This function mainly uses combinationUtil()
	 */
	 void genProcessCombinations(int arr[], int numberIneq, int variables)
	{
		// A temporary array to store all combination one by one
		int bufferData[]=new int[variables];
		//Set<Set<Integer>> result = new HashSet<Set<Integer>>();
		// Print all combination using temprary array 'data[]'
		combinateDishesIneqInCOnstraintsPossibilities(arr, numberIneq, variables, 0, bufferData, 0);
	}
}

class GaussianElimination {
	private double[][] A; //refers to co-efficient matrix
	double[] b; //refers to output matrix
	boolean hasSolution = true;


	GaussianElimination(double[][] A, double[] b){
		if (A == null || b == null){
			hasSolution = false;
			return;
		}
		if (A.length == 0 || b.length == 0){
			hasSolution = false;
			return;
		}
		copyMatrix(A);
		copyMatrix(b);
		rowReduce();
	}

	private void rowReduce(){
		int rowLength = A.length;
		for (int row =0; row < rowLength; row++){
			int rowPivot = getRowPivot(A, row);
			if (rowPivot == -1){
				hasSolution = false;
				return;
			}
			if (rowPivot != row){
				//swap rows
				swapRowsInA(row, rowPivot);
				swapIndexInb(row, rowPivot);
			}
			//pivot element is located in col <row> for current row < row>
			//rescale to make pivot as 1
			if (A[row][row] != 1){
				//rescale entire row
				rescalePivot(row);
			}
			//make col zero
			for (int r = 0; r < rowLength; r++){
				if (row == r){
					continue;
				}
				makeColZero(row, r);

			}
		}
	}

	/**
	 * Print result of b
	 */
	public void printResult(){
		if (hasSolution) {
			for (int row = 0; row < b.length; row++) {
				System.out.print(b[row] + " ");
			}
		}
	}

	/**
	 * Make col entries as zero for the pivot row of A and update b
	 * @param current_row pi
	 * @param row
	 */
	private void makeColZero(int current_row, int row){
		double scale_factor = A[row][current_row];
		for (int col = 0; col < A[0].length; col++){
			A[row][col] = A[row][col] - scale_factor * A[current_row][col];
		}
		b[row] = b[row] - scale_factor * b[current_row];
	}

	private void rescalePivot(int row){
		double scale_factor = A[row][row];
		for (int col =0 ; col < A[0].length; col++){
			A[row][col] = A[row][col]/scale_factor;
		}
		b[row] = b[row] / scale_factor;
	}

	/**
	 * Swap rows i and j of A
	 */
	private void swapRowsInA(int i, int j){
		double[] temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}

	/**
	 * Swap values in ith and jth index of b
	 */
	private void swapIndexInb(int i, int j){
		double temp = b[i];
		b[i] = b[j];
		b[j] = temp;
	}

	private int getRowPivot(double[][] matrix, int row){
		//select first non zero entry in left most column
		for (int r = row; r < matrix.length; r++){
			if (matrix[r][row] != 0){
				return r;
			}
		}
		return -1;
	}

	/**
	 * Copy input A to have a local copy < avoid modifying client</>
	 * @param matrix
	 */
	private void copyMatrix(double[][] matrix){
		A = new double[matrix.length][matrix[0].length];
		for (int i=0; i < matrix.length; i++){
			for (int j=0; j< matrix[0].length; j++){
				this.A[i][j] = matrix[i][j];
			}
		}
	}

	/**
	 * Copy input B to have a local copy < avoid modifying client</>
	 * @param matrix
	 */
	private void copyMatrix(double[] matrix){
		b = new double[matrix.length];
		for (int i=0; i< matrix.length; i++){
				b[i] = matrix[i];
		}

	}

}