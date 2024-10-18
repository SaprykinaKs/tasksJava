// на вход подаются 4 числа - размер матриц
// матрицы генерятся рандомайзером (комплексные числа типа double)
// на выходе результаты сложения, вычитания, умножения, транспонирования, деления матриц и детерминант
// (при условии что действие возможно для матриц заданного размера)

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

class Complex {
    private final double real;
    private final double imag;

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public double getReal() {
        return real;
    }

    public double getImag() {
        return imag;
    }

    public Complex add(Complex other) {
        return new Complex(this.real + other.real, this.imag + other.imag);
    }

    public Complex subtract(Complex other) {
        return new Complex(this.real - other.real, this.imag - other.imag);
    }

    public Complex multiply(Complex other) {
        return new Complex(
                this.real * other.real - this.imag * other.imag,
                this.real * other.imag + this.imag * other.real);
    }

    public Complex divide(Complex other) {
        double den = other.real * other.real + other.imag * other.imag;
        if (den == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return new Complex(
                (this.real * other.real + this.imag * other.imag) / den,
                (this.imag * other.real - this.real * other.imag) / den);
    }

    @Override
    public String toString() {
        return String.format("%.2f + %.2fi", real, imag);
    }
}

class Matrix {
    private final Complex[][] matrix;
    private final int rows;
    private final int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new Complex[rows][cols];
    }

    public void setElement(int row, int col, Complex val) {
        matrix[row][col] = val;
    }

    public Complex getElement(int row, int col) {
        return matrix[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Matrix add(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Matrix dimension must match");
        }
        Matrix res = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.setElement(i, j, this.getElement(i, j).add(other.getElement(i, j)));
            }
        }
        return res;
    }

    public Matrix subtract(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Matrix dimension must match");
        }
        Matrix res = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.setElement(i, j, this.getElement(i, j).subtract(other.getElement(i, j)));
            }
        }
        return res;
    }

    public Matrix multiply(Matrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("Invalid matrix dimension for multiplication");
        }
        Matrix res = new Matrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                Complex sum = new Complex(0, 0);
                for (int k = 0; k < this.cols; k++) {
                    sum = sum.add(this.getElement(i, k).multiply(other.getElement(k, j)));
                }
                res.setElement(i, j, sum);
            }
        }
        return res;
    }

    public Matrix transpose() {
        Matrix res = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.setElement(j, i, this.getElement(i, j));
            }
        }
        return res;
    }

    public Complex determinant() {
        if (rows != cols) {
            throw new IllegalArgumentException("Determinant can only be calculated for square matrices");
        }
        return determinantSearch(matrix);
    }

    public Complex determinantSearch(Complex[][] matrix) {
        int n = matrix.length;
        
        if (n == 1) {
            return matrix[0][0];
        }
        if (n == 2) {
            return matrix[0][0].multiply(matrix[1][1])
                    .subtract(matrix[0][1].multiply(matrix[1][0])); 
        }
    
        Complex det = new Complex(0, 0); 
        for (int p = 0; p < n; p++) {
            Complex[][] subMatrix = new Complex[n - 1][n - 1];
    
            for (int i = 1; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (j < p) {
                        subMatrix[i - 1][j] = matrix[i][j];
                    } else if (j > p) {
                        subMatrix[i - 1][j - 1] = matrix[i][j];
                    }
                }
            }
    
            Complex cofactor = matrix[0][p].multiply(new Complex(Math.pow(-1, p), 0)); 
            det = det.add(cofactor.multiply(determinantSearch(subMatrix)));  
        }
    
        return det;  
    }
    
    public Matrix inverse() {
        if (rows != cols) {
            throw new IllegalArgumentException("Inverse can only be calculated for square matrices");
        }
        Complex det = determinant();

        if (det.getReal() == 0 && det.getImag() == 0) {
            throw new ArithmeticException("Matrix is not invertible");
        }
    
        Matrix adjoint = new Matrix(rows, cols);
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Complex[][] subMatrix = new Complex[rows - 1][cols - 1];
                for (int m = 0; m < rows; m++) {
                    for (int n = 0; n < cols; n++) {
                        if (m != i && n != j) {
                            int rowIndex = m < i ? m : m - 1;
                            int colIndex = n < j ? n : n - 1;
                            subMatrix[rowIndex][colIndex] = matrix[m][n];
                        }
                    }
                }
    
                Complex subDeterminant = determinantSearch(subMatrix);
                Complex cofactor = subDeterminant.multiply(new Complex(Math.pow(-1, i + j), 0));
                adjoint.setElement(j, i, cofactor);
            }
        }
        Matrix inverse = new Matrix(rows, cols);
 
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Complex adjElement = adjoint.getElement(i, j);
                inverse.setElement(i, j, adjElement.divide(det)); 
            }
        }
        return inverse;
    }    

    public void randoming() {
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double real = rand.nextDouble() * 10; 
                double imag = rand.nextDouble() * 10;
                matrix[i][j] = new Complex(real, imag);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(matrix[i][j]).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static int[] getRC() {
        int rows1, cols1, rows2, cols2;
    
        try (Scanner scanner = new Scanner(System.in)) {
            rows1 = getCorrect(scanner, "Enter the number of rows for matrix 1: ");
            cols1 = getCorrect(scanner, "Enter the number of cols for matrix 1: ");
            rows2 = getCorrect(scanner, "Enter the number of rows for matrix 2: ");
            cols2 = getCorrect(scanner, "Enter the number of cols for matrix 2: ");
        }
    
        return new int[]{rows1, cols1, rows2, cols2};
    }

    private static int getCorrect(Scanner scanner, String message) {
        int val;
        while (true) {
            System.out.print(message);
            try {
                val = scanner.nextInt();
                if (val > 0) {
                    return val;
                } else {
                    System.out.println("Enter a positive integer greater than 0");
                }
            } catch (InputMismatchException e) {
                System.out.println("Enter an integer");
                scanner.next(); 
            }
        }
    }

    public static void main(String[] args) {

        // Matrix matrix1 = new Matrix(2, 2); 
        // Matrix matrix2 = new Matrix(2, 2);

        int[] rc = getRC();
        Matrix matrix1 = new Matrix(rc[0], rc[1]);
        Matrix matrix2 = new Matrix(rc[2], rc[3]);

        // да, можно было написать ввод матриц с клавиатуры, но мне стало лень, извините
        // рандом всегда поможет
        matrix1.randoming();
        matrix2.randoming();

        System.out.println("Matrix 1:");
        System.out.println(matrix1);

        System.out.println("Matrix 2:");
        System.out.println(matrix2);

        if (matrix1.rows == matrix2.rows && matrix1.cols == matrix2.cols){
            // сумма
            System.out.println("Sum:");
            System.out.println(matrix1.add(matrix2));

            // разница
            System.out.println("Difference:");
            System.out.println(matrix1.subtract(matrix2));
        }
        // произведение
        if (matrix1.cols == matrix2.rows) {
            System.out.println("Product:");
            System.out.println(matrix1.multiply(matrix2));
        }

        // транспонирование
        System.out.println("Transposed matrix 1:");
        System.out.println(matrix1.transpose());
        System.out.println("Transposed matrix 2:");
        System.out.println(matrix2.transpose());

        // детерминант и обратная матрицы
        if (matrix1.rows == matrix1.cols){
            System.out.println("Determinant of matrix 1: " + matrix1.determinant() +'\n');
            System.out.println("Inverse of matrix 1:");
            System.out.println(matrix1.inverse());
        }
        if (matrix2.rows == matrix2.cols){
            System.out.println("Determinant of matrix 2: " + matrix2.determinant()+'\n');
            System.out.println("Inverse of matrix 2:");
            System.out.println(matrix2.inverse());
        }

        // деление (умножение 1 на обратную 2)
        if (matrix1.cols == matrix1.rows && matrix1.rows == matrix1.cols && matrix2.rows == matrix2.cols) {
            System.out.println("Quotient:");
            System.out.println(matrix1.multiply(matrix2.inverse()));
        }    

        // сначала я писала throw для проверок, но ловить эксепшены мне не понравилось, поэтому ифы в выводах 
        // но и на всякий исключения тоже оставила
    }
}
