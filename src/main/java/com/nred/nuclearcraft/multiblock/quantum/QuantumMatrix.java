package com.nred.nuclearcraft.multiblock.quantum;

import com.nred.nuclearcraft.util.Complex;
import com.nred.nuclearcraft.util.IntPair;

import java.util.HashSet;
import java.util.Set;

public class QuantumMatrix {
    public static void addTo(double[] a, double[] b) {
        for (int i = 0, len = a.length; i < len; ++i) {
            a[i] += b[i];
        }
    }

    public static void multiplyBy(double[] matrix, double re, double im) {
        for (int i = 0, len = matrix.length; i < len; i += 2) {
            int x = i << 1;
            Complex mult = Complex.multiply(matrix[x], matrix[x + 1], re, im);
            matrix[x] = mult.re;
            matrix[x + 1] = mult.im;
        }
    }

    public static double[] identity(int dim) {
        double[] matrix = new double[(dim * dim) << 1];
        for (int i = 0; i < dim; ++i) {
            matrix[((dim + 1) * i) << 1] = 1;
        }
        return matrix;
    }

    public static Complex det(double[] matrix) {
        switch (matrix.length) {
            case 0:
                return new Complex(1, 0);
            case 2:
                return new Complex(matrix[0], matrix[1]);
            case 8:
                Complex ad = Complex.multiply(matrix[0], matrix[1], matrix[6], matrix[7]);
                Complex bc = Complex.multiply(matrix[2], matrix[3], matrix[4], matrix[5]);
                return new Complex(ad.re - bc.re, ad.im - bc.im);
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static double[] tensorProduct(int dimA, double[] matrixA, int dimB, double[] matrixB) {
        int dimC = dimA * dimB;
        double[] matrixC = new double[(dimC * dimC) << 1];
        for (int i = 0; i < dimA; ++i) {
            int rA = i * dimA, u = i * dimB;
            for (int j = 0; j < dimA; ++j) {
                int xA = (rA + j) << 1, v = j * dimB;
                for (int k = 0; k < dimB; ++k) {
                    int rB = k * dimB, rC = (u + k) * dimC + v;
                    for (int l = 0; l < dimB; ++l) {
                        int xB = (rB + l) << 1, xC = (rC + l) << 1;
                        Complex c = Complex.multiply(matrixA[xA], matrixA[xA + 1], matrixB[xB], matrixB[xB + 1]);
                        matrixC[xC] = c.re;
                        matrixC[xC + 1] = c.im;
                    }
                }
            }
        }
        return matrixC;
    }

    public static void changeIndexBasis(int dim, double[] matrix, int[] basis) {
        Set<IntPair> completed = new HashSet<>();
        for (int i = 0; i < dim; ++i) {
            int u = changeIntBasis(i, basis);
            int r = i * dim, s = u * dim;
            for (int j = 0; j < dim; ++j) {
                int v = changeIntBasis(j, basis);
                if ((i == u && j == v) || completed.contains(new IntPair(i, j))) {
                    continue;
                }
                int x = (r + j) << 1, y = (s + v) << 1;
                double t = matrix[x];
                matrix[x++] = matrix[y];
                matrix[y++] = t;
                t = matrix[x];
                matrix[x] = matrix[y];
                matrix[y] = t;
                completed.add(new IntPair(u, v));
            }
        }
    }

    public static int changeIntBasis(int x, int[] basis) {
        int y = 0;
        for (int i = 0, len = basis.length; i < len; ++i) {
            y |= ((x >> basis[i]) & 1) << i;
        }
        return y;
    }

    public static final double[] I = {
            1, 0, 0, 0,
            0, 0, 1, 0,
    };

    public static final double[] X = {
            0, 0, 1, 0,
            1, 0, 0, 0,
    };

    public static final double[] Y = {
            0, 0, 0, -1,
            0, 1, 0, 0,
    };

    public static final double[] Z = {
            1, 0, 0, 0,
            0, 0, -1, 0,
    };

    public static final double[] H = {
            Math.sqrt(0.5D), 0, Math.sqrt(0.5D), 0,
            Math.sqrt(0.5D), 0, -Math.sqrt(0.5D), 0,
    };

    public static final double[] S = {
            1, 0, 0, 0,
            0, 0, 0, 1,
    };

    public static final double[] Sdg = {
            1, 0, 0, 0,
            0, 0, 0, -1,
    };

    public static final double[] T = {
            1, 0, 0, 0,
            0, 0, Math.sqrt(0.5D), Math.sqrt(0.5D),
    };

    public static final double[] Tdg = {
            1, 0, 0, 0,
            0, 0, Math.sqrt(0.5D), -Math.sqrt(0.5D),
    };

    public static double[] phase(double angle) {
        return new double[]{
                1, 0, 0, 0,
                0, 0, Math.cos(angle), Math.sin(angle),
        };
    }

    public static double[] rotateX(double angle) {
        double c = Math.cos(angle), s = Math.sin(angle);
        return new double[]{
                c, 0, 0, -s,
                0, -s, c, 0,
        };
    }

    public static double[] rotateY(double angle) {
        double c = Math.cos(angle), s = Math.sin(angle);
        return new double[]{
                c, 0, -s, 0,
                s, 0, c, 0,
        };
    }

    public static double[] rotateZ(double angle) {
        double c = Math.cos(angle), s = Math.sin(angle);
        return new double[]{
                c, -s, 0, 0,
                0, 0, c, s,
        };
    }

    public static final double[] SWAP = {
            1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 0, 0, 0,
            0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 0,
    };
}