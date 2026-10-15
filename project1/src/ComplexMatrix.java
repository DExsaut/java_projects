public final class ComplexMatrix {
    private final Complex[][] data;
    private final int rows;
    private final int cols;

    public ComplexMatrix(Complex[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    public ComplexMatrix add(ComplexMatrix o) {
        if (rows != o.rows || cols != o.cols) {
            throw new IllegalArgumentException("Для сложения размеры матриц должны совпадать");
        }
        Complex[][] r = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                r[i][j] = data[i][j].add(o.data[i][j]);
            }
        }
        return new ComplexMatrix(r);
    }

    public ComplexMatrix multiply(ComplexMatrix o) {
        if (cols != o.rows) {
            throw new IllegalArgumentException("Для умножения число столбцов первой матрицы должно равняться числу строк второй");
        }
        Complex[][] r = new Complex[rows][o.cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < o.cols; j++) {
                Complex s = new Complex(0, 0);
                for (int k = 0; k < cols; k++) {
                    s = s.add(data[i][k].multiply(o.data[k][j]));
                }
                r[i][j] = s;
            }
        }
        return new ComplexMatrix(r);
    }

    public ComplexMatrix divide(ComplexMatrix o) {
        if (cols != o.rows) {
            throw new IllegalArgumentException("Для деления число столбцов первой матрицы должно равняться числу строк второй");
        }
        return multiply(o.inverse());
    }

    public ComplexMatrix transpose() {
        Complex[][] r = new Complex[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                r[j][i] = data[i][j];
            }
        }
        return new ComplexMatrix(r);
    }

    public Complex determinant() {
        requireSquare("Определитель существует только у квадратной матрицы");
        Complex[][] a = copy();
        Complex det = new Complex(1, 0);
        for (int c = 0; c < rows; c++) {
            int p = pivot(a, c);
            if (a[p][c].isZero()) {
                return new Complex(0, 0);
            }
            if (p != c) {
                Complex[] t = a[p];
                a[p] = a[c];
                a[c] = t;
                det = det.multiply(new Complex(-1, 0));
            }
            det = det.multiply(a[c][c]);
            for (int i = c + 1; i < rows; i++) {
                Complex f = a[i][c].divide(a[c][c]);
                for (int j = c; j < rows; j++) {
                    a[i][j] = a[i][j].subtract(f.multiply(a[c][j]));
                }
            }
        }
        return det;
    }

    private ComplexMatrix inverse() {
        requireSquare("Делитель должен быть квадратной матрицей");
        int n = rows;
        Complex[][] a = new Complex[n][2 * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = data[i][j];
                a[i][n + j] = i == j ? new Complex(1, 0) : new Complex(0, 0);
            }
        }
        for (int c = 0; c < n; c++) {
            int p = pivot(a, c);
            if (a[p][c].isZero()) {
                throw new IllegalArgumentException("Делитель является вырожденной матрицей");
            }
            Complex[] t = a[p];
            a[p] = a[c];
            a[c] = t;
            Complex d = a[c][c];
            for (int j = 0; j < 2 * n; j++) {
                a[c][j] = a[c][j].divide(d);
            }
            for (int i = 0; i < n; i++) {
                if (i != c) {
                    Complex f = a[i][c];
                    for (int j = 0; j < 2 * n; j++) {
                        a[i][j] = a[i][j].subtract(f.multiply(a[c][j]));
                    }
                }
            }
        }
        Complex[][] r = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                r[i][j] = a[i][n + j];
            }
        }
        return new ComplexMatrix(r);
    }

    private int pivot(Complex[][] a, int c) {
        int p = c;
        for (int i = c + 1; i < a.length; i++) {
            if (a[i][c].abs() > a[p][c].abs()) {
                p = i;
            }
        }
        return p;
    }

    private void requireSquare(String message) {
        if (rows != cols) {
            throw new IllegalArgumentException(message);
        }
    }

    private Complex[][] copy() {
        Complex[][] r = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                r[i][j] = data[i][j];
            }
        }
        return r;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (j > 0) {
                    sb.append("  ");
                }
                sb.append(data[i][j]);
            }
            if (i < rows - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}