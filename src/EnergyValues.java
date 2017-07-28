import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

class Equation {
	Equation(double a[][], double b[]) {
		this.a = a;
		this.b = b;
	}

	double a[][];
	double b[];
}

class Position {
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

class EnergyValues {
	static boolean useMock = true;

	static Equation ReadEquation() throws IOException {

		if (useMock) {
			int size = 5;

			double a[][] = new double[size][size];
			double b[] = new double[size];
			int value = 2;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					a[i][j] = value*(i+j);
				}
				b[i] = i+1;
			}

			return new Equation(a, b);
		} else {

			Scanner scanner = new Scanner(System.in);
			int size = scanner.nextInt();

			double a[][] = new double[size][size];
			double b[] = new double[size];
			for (int raw = 0; raw < size; ++raw) {
				for (int column = 0; column < size; ++column)
					a[raw][column] = scanner.nextInt();
				b[raw] = scanner.nextInt();
			}
			return new Equation(a, b);
		}
	}

	static Position SelectPivotElement(double a[][], boolean used_raws[], boolean used_columns[], Position previous) {
		// This algorithm selects the first free element.
		// You'll need to improve it to pass the problem.
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
		return pivot;
	}

	static void SwapLines(double a[][], double b[], boolean used_raws[], Position pivot) {
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

	static void ProcessPivotElement(double a[][], double b[], Position pivot) {
		// Write your code here
		reducePivotRaw(a, b, pivot, a[pivot.raw][pivot.column]);
		for (int line = pivot.raw + 1; line < a.length; line++) {
			reducePivotInRawsBelow(a, b, pivot, line);
		}
	}

	private static void reducePivotInRawsBelow(double[][] a, double[] b, Position pivot, int line) {
		double variableCoef = a[line][pivot.column];
		if (variableCoef == 0) {
			return;
		}
		for (int col = 0; col < a.length; col++) {
			a[line][col] = a[line][col] - variableCoef * a[pivot.raw][col];
		}
		b[line] = b[line] - variableCoef * b[pivot.raw];
	}

	private static void reducePivotRaw(double[][] a, double[] b, Position pivot, double variableCoef) {
		if (variableCoef != 1) {
			for (int i = 0; i < a.length; i++) {
				a[pivot.raw][i] = a[pivot.raw][i] / variableCoef;
			}
			b[pivot.raw] = b[pivot.raw] / variableCoef;
		}
	}

	static void MarkPivotElementUsed(Position pivot, boolean used_raws[], boolean used_columns[]) {
		used_raws[pivot.raw] = true;
		used_columns[pivot.column] = true;
	}

	static double[] SolveEquation(Equation equation) {
		double a[][] = equation.a;
		double b[] = equation.b;
		int size = a.length;

		boolean[] used_columns = new boolean[size];
		boolean[] used_raws = new boolean[size];
		Position position = new Position(0, 0);
		for (int step = 0; step < size; ++step) {
			position = SelectPivotElement(a, used_raws, used_columns, position);
			SwapLines(a, b, used_raws, position);
			ProcessPivotElement(a, b, position);
			MarkPivotElementUsed(position, used_raws, used_columns);
		}
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

	static void PrintColumn(double column[]) {
		int size = column.length;
		for (int raw = 0; raw < size; ++raw)
			System.out.printf("%.20f\n", column[raw]);
	}

	public static void main(String[] args) throws IOException {
		long start = 0;
		if (useMock) {
			start = new Date().getTime();
		}
		Equation equation = ReadEquation();
		double[] solution = SolveEquation(equation);
		if (useMock) {
			long stop = new Date().getTime();
			System.err.println("time: " + (stop - start));
			Equation equationControl = ReadEquation();
			double total = 0;
			double[][] a = equationControl.a;
			int size = a.length;
			for (int line = 0; line < size; line ++) {
				double count = 0;
				for (int col = 0; col < size; col++) {
					count += a[line][col] * solution[col];
				}
				total += count - equationControl.b[line];
			}
			System.err.println("total: " + total);
		}
		PrintColumn(solution);
	}
}
