package com.toxin.render;

public class Matrix {
    private double[] values;

    public Matrix(double[] values) {
        this.values = values;
    }

    public Matrix multiply(Matrix m) {
        double[] res = new double[9];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    res[i * 3 + j] += this.values[i * 3 + k] * m.values[k * 3 + j];
                }
            }
        }

        return new Matrix(res);
    }

    public Vertex transform(Vertex v) {
        return new Vertex(
            v.getX() * values[0] + v.getY() * values[3] + v.getZ() * values[6],
            v.getX() * values[1] + v.getY() * values[4] + v.getZ() * values[7],
            v.getX() * values[2] + v.getY() * values[5] + v.getZ() * values[8]
        );
    }
}
