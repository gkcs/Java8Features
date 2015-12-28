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
        return simplex.solve(MATRIX, RESULT, productConstraints);
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

    public double[] solve(final double[][] matrix, final double[] result, final int productConstraints[][]) {
        return null;
    }
}