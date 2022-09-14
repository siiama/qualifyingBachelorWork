package com.company;

public class Data {
    public int index;
    public double[][] matrix;

    public int getIndex() {
        return index;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }
    public Data(int index, double[][] matrix) {
        this.index=index;
        this.matrix=matrix;
    }
}
