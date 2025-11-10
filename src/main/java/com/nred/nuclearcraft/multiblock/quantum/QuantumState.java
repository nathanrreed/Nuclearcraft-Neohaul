package com.nred.nuclearcraft.multiblock.quantum;

import com.nred.nuclearcraft.util.Complex;

import java.util.Random;

public class QuantumState {

    public final int size, dim;
    public double[] vector, backup;

    protected final Random rand = new Random();

    public QuantumState(int size) {
        this.size = size;
        this.dim = 1 << size;
        this.vector = new double[dim << 1];
        vector[0] = 1D;
        this.backup = new double[dim << 1];
    }

    public double absSq() {
        double absSq = 0D;

        for (int i = 0, len = vector.length; i < len; i += 2) {
            double re = vector[i], im = vector[i + 1];
            absSq += re * re + im * im;
        }

        return absSq;
    }

    private void scale(double scalar) {
        for (int i = 0, len = vector.length; i < len; ++i) {
            vector[i] *= scalar;
        }
    }

    public void normalize() {
        scale(1D / Math.sqrt(absSq()));
    }

    public double[] probs() {
        double[] probs = new double[dim];

        for (int i = 0; i < dim; ++i) {
            int x = i << 1;
            double re = vector[x], im = vector[x + 1];
            probs[i] = re * re + im * im;
        }

        return probs;
    }

    public double[] probs(int[] targets) {
        int count = targets.length;
        if (count == 0) {
            return new double[]{1D};
        } else if (count == size) {
            return probs();
        }

        return probs(targets, QuantumHelpers.steps(targets, size), QuantumHelpers.window(targets, size));
    }

    private double[] probs(int[] targets, int[] steps, int[] window) {
        double[] probs = new double[window.length];

        for (int i = 0, span = 1 << steps.length; i < span; ++i) {
            int start = QuantumHelpers.at(i, steps);
            for (int j = 0, len = window.length; j < len; ++j) {
                int x = start + window[j];
                double re = vector[x], im = vector[x + 1];
                probs[j] += re * re + im * im;
            }
        }

        return probs;
    }

    public void collapse(int[] targets, boolean[] results) {
        int count = targets.length;
        if (count == 0) {
            return;
        }

        int index = 0;

        for (int i = 0; i < count; ++i) {
            if (results[i]) {
                index |= 1 << i;
            }
        }

        collapse(targets, index, QuantumHelpers.steps(targets, size), QuantumHelpers.window(targets, size));
    }

    private void collapse(int[] targets, int results, int[] steps, int[] window) {
        double absSq = 0D;

        for (int i = 0, span = 1 << steps.length; i < span; ++i) {
            int start = QuantumHelpers.at(i, steps);
            for (int j = 0, len = window.length; j < len; ++j) {
                int x = start + window[j];
                if (j != results) {
                    vector[x] = vector[x + 1] = 0D;
                } else {
                    double re = vector[x], im = vector[x + 1];
                    absSq += re * re + im * im;
                }
            }
        }

        scale(1D / Math.sqrt(absSq));
    }

    public boolean[] measure(int[] targets, boolean collapse) {
        int count = targets.length;
        if (count == 0) {
            return new boolean[]{};
        }

        int[] steps = QuantumHelpers.steps(targets, size), window = QuantumHelpers.window(targets, size);

        double[] probs = probs(targets, steps, window);

        double r = rand.nextDouble();
        int index = 0;
        for (int end = probs.length - 1; index < end; ++index) {
            r -= probs[index];
            if (r <= 0D) {
                break;
            }
        }

        if (collapse) {
            collapse(targets, index, steps, window);
        }

        boolean[] results = new boolean[count];
        for (int i = 0; i < count; ++i) {
            results[i] = ((index >> i) & 1) == 1;
        }

        return results;
    }

    public double expectation(int[] targets) {
        return expectation(targets, null);
    }

    public double expectation(int[] targets, boolean[] results) {
        double expectation = 0D;

        int[] steps = QuantumHelpers.steps(targets, size);
        int offset = QuantumHelpers.offset(targets, results);

        for (int i = 0, span = 1 << steps.length; i < span; ++i) {
            int x = offset + QuantumHelpers.at(i, steps);
            double re = vector[x], im = vector[x + 1];
            expectation += re * re + im * im;
        }

        return expectation;
    }

    public Complex inner(QuantumState other) {
        double[] u = other.vector;
        double re = 0D, im = 0D;

        for (int i = 0, len = vector.length; i < len; i += 2) {
            double a = vector[i], b = vector[i + 1], c = u[i], d = -u[i + 1];
            re += a * c - b * d;
            im += a * d + b * c;
        }

        return new Complex(re, im);
    }

    public double fidelity(QuantumState other) {
        return inner(other).absSq();
    }

    public void update(QuantumGate gate) {
        if (gate.equals(QuantumGate.UNIT)) {
            return;
        }

        int[] steps = QuantumHelpers.steps(gate.targets, size), window = QuantumHelpers.window(gate.targets, size);

        for (int i = 0, span = 1 << steps.length; i < span; ++i) {
            gate.partialMap(vector, backup, QuantumHelpers.at(i, steps), window);
        }

        double[] t = vector;
        vector = backup;
        backup = t;
    }

    @SuppressWarnings("all")
    public QuantumState clone() {
        QuantumState copy = new QuantumState(size);
        System.arraycopy(vector, 0, copy.vector, 0, vector.length);
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        boolean start = true;
        for (int i = 0; i < dim; ++i) {
            if (!start) {
                sb.append(", ");
            }
            start = false;

            int x = i << 1;
            double re = vector[x], im = vector[x + 1];
            sb.append(re);
            if (im >= 0D) {
                sb.append('+');
            }
            sb.append(im).append('i');
        }

        return sb.append(']').toString();
    }
}