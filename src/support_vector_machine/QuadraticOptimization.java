package support_vector_machine;

import java.util.Arrays;

public class QuadraticOptimization {
    private Simplex simplex = new Simplex();

    public double[][] solve(final double x[][], final double y[][]) {
        final double[] C = new double[x.length];
        for (int i = 0; i < C.length; i++) {
            C[i] = -1;
        }
        return solve(getD(x, y), y, C, new double[]{0}, y.length, 1);
    }

    public int[] classify(final double u[][], final double x[][], final double y[], final double classifier[]) {
        final int result[] = new int[u.length];
        double[] w = new double[x[0].length];
        for (int i = 0; i < w.length; i++) {
            for (double[] row : x) {
                w[i] += row[i];
            }
            w[i] *= classifier[i] * y[i];
        }
        double b = 1;
        for (int i = 0; i < u.length; i++) {
            if (dotProduct(u[i], w) + b > 0) {
                result[i] = 1;
            } else {
                result[i] = -1;
            }
        }
        return result;
    }

    //Y is a (m,n) matrix.

    public double[][] solve(final double[][] D, final double y[][], final double c[], final double b[], final int n, final int m) {
        final double MATRIX[][] = new double[n + m][(n + m) << 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //todo:find if this is correct
                MATRIX[i][j] = D[i][j];
            }
        }
        for (int i = 0; i < n; i++) {
            System.arraycopy(y[i], 0, MATRIX[i], n, m);
        }
        for (int i = 0; i < n; i++) {
            MATRIX[i][n + 1 + i] = -1;
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                MATRIX[n + i][j] = y[j][i];
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                MATRIX[n + i][MATRIX[0].length - 1 - j] = 1;
            }
        }
        final double RESULT[] = new double[n + m];
        System.arraycopy(c, 0, RESULT, 0, c.length);
        System.arraycopy(b, 0, RESULT, n, b.length);
        final int productConstraints[][] = new int[n][3];
        for (int i = 0; i < n; i++) {
            productConstraints[i][0] = i;
            productConstraints[i][1] = n + m + i;
        }
        System.out.println("D:");
        System.out.println(Arrays.deepToString(D));
        System.out.println("MATRIX:");
        System.out.println(Arrays.deepToString(MATRIX));
        return simplex.solve(MATRIX, RESULT, productConstraints, new double[(n + m) << 1], true);
    }

    private double[][] getD(final double[][] x, final double[][] y) {
        final double D[][] = new double[x.length][x.length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < i; j++) {
                D[j][i] = D[i][j] = (dotProduct(x[i], x[j]) * y[i][0] * y[j][0]) / 2;
            }
            D[i][i] = dotProduct(x[i], x[i]) * y[i][0] * y[i][0];
        }
        return D;
    }

    private double dotProduct(double[] x, double[] y) {
        double result = 0;
        for (int i = 0; i < x.length; i++) {
            result += x[i] * y[i];
        }
        return result;
    }
}

class Simplex {

    public double[][] solve(final double[][] matrix, final double[] rightHandSide, final int productConstraints[][], final double coeffs[], boolean quadraticOptimization) {
        makeRightHandSidePositive(matrix, rightHandSide);
        final double rhs[] = new double[rightHandSide.length];
        final double basis[][] = new double[matrix.length][matrix[0].length + rightHandSide.length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, basis[i], 0, matrix[0].length);
            basis[i][matrix[0].length + i] = 1;
        }
        System.arraycopy(rightHandSide, 0, rhs, 0, rightHandSide.length);
        final double coefficients[] = new double[coeffs.length + rhs.length];
        System.arraycopy(coeffs, 0, coefficients, 0, coeffs.length);
        for (int i = 0; i < rhs.length; i++) {
            coefficients[coeffs.length + i] = Integer.MIN_VALUE;
        }
        final int basisVariable[] = new int[rhs.length];
        final double theta[] = new double[rhs.length];
        for (int i = 0; i < basisVariable.length; i++) {
            basisVariable[i] = coeffs.length + i;
        }
        double result[] = new double[coefficients.length + 1];
        //table initialized
        //now find Cj-Zj
        System.out.println("COEFFICIENTS:");
        System.out.println(Arrays.toString(coefficients));
        printArrays(rhs, basis, basisVariable, result, theta);
        while (true) {
            for (int i = 0; i < coefficients.length; i++) {
                result[i] = coefficients[i];
                for (int j = 0; j < basis.length; j++) {
                    result[i] = result[i] - coefficients[basisVariable[j]] * basis[j][i];
                }
            }
            result[coefficients.length] = 0;
            for (int j = 0; j < basis.length; j++) {
                result[coefficients.length] = result[coefficients.length] + coefficients[basisVariable[j]] * rhs[j];
            }
            double maximum = -1;
            int pivotColumn = 0;
            for (int i = 0; i < coefficients.length; i++) {
                if (result[i] > maximum && (!quadraticOptimization || doesNotViolateMew(productConstraints, basisVariable, rhs, coefficients.length))) {
                    maximum = result[i];
                    pivotColumn = i;
                }
            }
            if (maximum <= 0) {
                double answer[][] = new double[2][basisVariable.length];
                for (int i = 0; i < basisVariable.length; i++) {
                    answer[0][i] = basisVariable[i];
                }
                System.out.println("MAX: " + maximum + " COL: " + pivotColumn);
                printArrays(rhs, basis, basisVariable, result, theta);
                System.arraycopy(rhs, 0, answer[1], 0, basisVariable.length);
                return answer;
            } else {
                double minimumTheta = Integer.MAX_VALUE;
                int pivotRow = 0;
                for (int i = 0; i < theta.length; i++) {
                    if (basis[i][pivotColumn] <= 0) {
                        theta[i] = Integer.MAX_VALUE;
                    } else {
                        theta[i] = rhs[i] / basis[i][pivotColumn];
                    }
                    if (theta[i] < minimumTheta) {
                        minimumTheta = theta[i];
                        pivotRow = i;
                    }
                }
//                System.out.println("ROW: " + pivotRow + " COL: " + pivotColumn);
                double pivotElement = basis[pivotRow][pivotColumn];
                for (int i = 0; i < basis[pivotRow].length; i++) {
                    basis[pivotRow][i] /= pivotElement;
                }
                rhs[pivotRow] /= pivotElement;
                basisVariable[pivotRow] = pivotColumn;
                //sort out other columns
                for (int i = 0; i < basis.length; i++) {
                    if (i != pivotRow) {
                        double factor = basis[i][pivotColumn];
                        for (int j = 0; j < basis[i].length; j++) {
                            basis[i][j] = basis[i][j] - factor * basis[pivotRow][j];
                        }
                        rhs[i] = rhs[i] - factor * rhs[pivotRow];
                    }
                }
                makeRightHandSidePositive(basis, rhs);
            }
            printArrays(rhs, basis, basisVariable, result, theta);
        }
    }

    private void printArrays(double[] rhs, double[][] basis, int[] basisVariable, double[] result, double[] theta) {
        System.out.println("BASIS:");
        for (double[] basi : basis) {
            System.out.println(Arrays.toString(basi));
        }
        System.out.println("Cj - Zj");
        System.out.println(Arrays.toString(result));
        System.out.println("THETA:");
        System.out.println(Arrays.toString(theta));
        System.out.println("RHS:");
        System.out.println(Arrays.toString(rhs));
        System.out.println("BASIC VARIABLES:");
        System.out.println(Arrays.toString(basisVariable));
    }

    private boolean doesNotViolateMew(int[][] productConstraints, int[] basisVariable, double[] rhs, int length) {
        double variableValues[] = new double[length];
        for (int i = 0; i < rhs.length; i++) {
            variableValues[basisVariable[i]] = rhs[i];
        }
        for (int[] productConstraint : productConstraints) {
            if (variableValues[productConstraint[0]] * variableValues[productConstraint[1]] != productConstraint[2]) {
                return false;
            }
        }
        return true;
    }

    private void makeRightHandSidePositive(final double[][] matrix, final double[] rightHandSide) {
        for (int i = 0; i < rightHandSide.length; i++) {
            if (rightHandSide[i] < 0) {
                rightHandSide[i] = -rightHandSide[i];
                for (int j = 0; j < matrix[i].length; j++) {
                    matrix[i][j] = -matrix[i][j];
                }
            }
        }
    }
}