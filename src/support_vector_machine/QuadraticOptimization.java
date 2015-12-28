package support_vector_machine;

public class QuadraticOptimization {
    private Simplex simplex = new Simplex();

    public double[] solve(final double x[], final double y[][]) {
        final double[] C = new double[x.length];
        for (int i = 0; i < C.length; i++) {
            C[i] = 1;
        }
        return solve(getD(x, y), y, C, new double[]{0}, y.length, 1);
    }

    //Y is a (m,n) matrix.

    public double[] solve(final double[][] D, final double y[][], final double c[], final double b[], final int n, final int m) {
        final double MATRIX[][] = new double[n + m][(n + m) << 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(D[i], 0, MATRIX[i], 0, D.length);
        }
        for (int i = 0; i < n; i++) {
            System.arraycopy(y[i], 0, MATRIX[i], n, m);
        }
        for (int i = 0; i < n; i++) {
            MATRIX[i][n + 1 + i] = 1;
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                MATRIX[n + i][j] = y[j][i];
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                MATRIX[n + i][MATRIX.length - 1 - j] = 1;
            }
        }
        final double RESULT[] = new double[(n + m) << 1];
        System.arraycopy(c, 0, RESULT, 0, c.length);
        System.arraycopy(b, 0, RESULT, n, b.length);
        final int productConstraints[][] = new int[n][3];
        for (int i = 0; i < n; i++) {
            productConstraints[i][0] = i;
            productConstraints[i][1] = n + m + i;
        }
        return simplex.solve(MATRIX, RESULT, productConstraints, new double[RESULT.length], true);
    }

    private double[][] getD(final double[] x, final double[][] y) {
        final double D[][] = new double[x.length][x.length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < i; j++) {
                D[j][i] = D[i][j] = (x[i] * x[j] * y[0][i] * y[0][j]) / 2;
            }
        }
        for (int i = 0; i < x.length; i++) {
            D[i][i] = x[i] * x[i] * y[0][i] * y[0][i];
        }
        return D;
    }
}

class Simplex {

    public double[] solve(final double[][] matrix, final double[] rightHandSide, final int productConstraints[][], final double coeffs[], boolean quadraticOptimization) {
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
            coefficients[coeffs.length + i] = -1;
        }
        final int basisVariable[] = new int[rhs.length];
        final double theta[] = new double[rhs.length];
        for (int i = 0; i < basisVariable.length; i++) {
            basisVariable[i] = rightHandSide.length + i;
        }
        double result[] = new double[coefficients.length + 1];
        //table initialized
        //now find Cj-Zj
        while (true) {
            for (int i = 0; i < coefficients.length; i++) {
                result[i] = coefficients[i];
                for (int j = 0; j < basis.length; j++) {
                    result[i] = -coefficients[basisVariable[j]] * basis[j][i];
                }
            }
            for (int j = 0; j < basis.length; j++) {
                result[coefficients.length] = coefficients[basisVariable[j]] * basis[j][coefficients.length];
            }
            double maximum = -1;
            int pivotColumn = 0;
            for (int i = 0; i < basisVariable.length; i++) {
                if (result[i] > maximum && quadraticOptimization && doesNotViolateMew(productConstraints, basisVariable, result)) {
                    maximum = result[i];
                    pivotColumn = i;
                }
            }
            if (maximum <= 0) {
                return rhs;
            } else {
                double maximumTheta = -1;
                int pivotRow = 0;
                for (int i = 0; i < theta.length; i++) {
                    theta[i] = rhs[i] / basis[i][pivotColumn];
                    if (theta[i] > maximumTheta) {
                        maximumTheta = theta[i];
                        pivotRow = i;
                    }
                }
                for (int i = 0; i < basis[pivotRow].length; i++) {
                    basis[pivotRow][i] /= basis[pivotRow][pivotColumn];
                }
                basisVariable[pivotRow] = pivotColumn;
                //sort out other columns
                for (int i = 0; i < basis.length; i++) {
                    if (i != pivotRow && basis[i][pivotColumn] != 0) {
                        double factor = basis[pivotRow][pivotColumn] / basis[i][pivotColumn];
                        for (int j = 0; j < basis[i].length; j++) {
                            basis[i][j] = basis[i][j] - factor * basis[pivotRow][j];
                        }
                    }
                }
            }
        }

    }

    private boolean doesNotViolateMew(int[][] productConstraints, int[] basisVariable, double[] result) {
        double variableValues[] = new double[result.length << 1];
        for (int i = 0; i < basisVariable.length; i++) {
            variableValues[basisVariable[i]] = result[i];
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