import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


class InsufficientElementsException extends Exception {
    public InsufficientElementsException() {
        super("Insufficient number of elements");
    }
}

class InvalidRowNumberException  extends Exception {
    public InvalidRowNumberException () {
        super("Invalid row number");
    }
}

class InvalidColumnNumberException  extends Exception {
    public InvalidColumnNumberException  () {
        super("Invalid column number");
    }
}




class DoubleMatrix {
    double[][] matrix;
    int rows;
    int cols;

    public DoubleMatrix(double[] a, int m, int n) throws InsufficientElementsException {
        if (a.length < m * n) {
            throw new InsufficientElementsException();
        }
        this.matrix = new double[m][n];
        this.rows = m;
        this.cols = n;

        int startIndex = a.length - (m * n);

        for(int i = 0; i<m; i++){
            for(int j = 0; j<n; j++){
                matrix[i][j] = a[startIndex++];
            }
        }


    }

    public int rows() {
        return rows;
    }

    public int columns(){
        return cols;
    }

    public double maxElementAtRow(int row) throws InvalidRowNumberException {
        if (row < 1 || row > rows) {
            throw new InvalidRowNumberException();
        }
        double max = matrix[row - 1][0];
        for (int i = 0; i < cols; i++) {
            if (matrix[row - 1][i] > max) {
                max = matrix[row - 1][i];
            }
        }
        return max;
    }

    public double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        if (column < 1 || column > cols) {
            throw new InvalidColumnNumberException();
        }
        double max = matrix[0][column - 1];
        for (int i = 0; i < rows; i++) {
            if (matrix[i][column - 1] > max) {
                max = matrix[i][column - 1];
            }
        }
        return max;
    }

    public double sum(){
        double sum = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    public double [] toSortedArray(){
        double [] sorted = new double[rows * cols];
        int z = 0;
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                sorted[z] = matrix[i][j];
                z++;
            }
        }

        double temp;

        for(int i = 0; i < sorted.length; i++){
            for(int j = 0; j< sorted.length; j++){
                if(sorted[i] < sorted[j]){
                    temp = sorted[i];
                    sorted[i] = sorted[j];
                    sorted[j] = temp;
                }
            }
        }
        return sorted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%.2f", matrix[i][j]));
                if (j < cols - 1) sb.append("\t");
            }
            if (i < rows - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleMatrix that = (DoubleMatrix) o;
        return rows == that.rows && cols == that.cols && Arrays.deepEquals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, cols);
        result = 31 * result + Arrays.deepHashCode(matrix);
        return result;
    }

    public String getDimensions(){
        return String.format("[%d x %d]", rows, cols);
    }
}

class MatrixReader {

    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String firstLine = reader.readLine();

            String[] dimensions = firstLine.trim().split("\\s+");
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);

            double[] elements = new double[rows * cols];
            int index = 0;

            for (int i = 0; i < rows; i++) {
                String line = reader.readLine();
                String[] rowElements = line.trim().split("\\s+");

                if (rowElements.length != cols) {
                    throw new InsufficientElementsException();
                }

                for (String elem : rowElements) {
                    elements[index++] = Double.parseDouble(elem);
                }
            }

            return new DoubleMatrix(elements, rows, cols);

        } catch (Exception e) {
            throw new RuntimeException("Error reading matrix: " + e.getMessage(), e);
        }
    }
}


public class DoubleMatrixTester {


    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }

}
