package com.nred.nuclearcraft.multiblock.quantum;

import com.nred.nuclearcraft.util.IntPair;

import java.util.Arrays;
import java.util.Comparator;

public class QuantumGate {
	public final int[] targets;
	public final double[] matrix;
	public final int dim;

	public QuantumGate(int[] targets, double[] matrix) {
		this.targets = targets;
		Arrays.sort(this.targets);
		this.matrix = matrix;
		dim = 1 << targets.length;
	}

	public void partialMap(double[] source, double[] target, int start, int[] window) {
		for (int i = 0; i < dim; ++i) {
			int x = start + window[i], r = i * dim;
			target[x] = target[x + 1] = 0D;
			for (int j = 0; j < dim; ++j) {
				int k = (r + j) << 1, y = start + window[j];
				double a = source[y], b = source[y + 1], c = matrix[k], d = matrix[k + 1];
				target[x] += a * c - b * d;
				target[x + 1] += a * d + b * c;
			}
		}
	}

	public QuantumGate copyWith(int[] targets) {
		return new QuantumGate(targets, matrix);
	}

	public static QuantumGate controlled(int[] controls, int[] targets, double[] matrix) {
		int targetCount = targets.length;
		if (targetCount == 0) {
			return UNIT;
		}

		int controlCount = controls.length;
		if (controlCount == 0) {
			return new QuantumGate(targets, matrix);
		}

		int combinedCount = controlCount + targetCount;
		int[] combined = new int[combinedCount];

		System.arraycopy(controls, 0, combined, 0, controlCount);
		System.arraycopy(targets, 0, combined, controlCount, targetCount);

		IntPair[] basisPairs = new IntPair[combinedCount];
		for (int i = 0; i < combinedCount; ++i) {
			basisPairs[i] = new IntPair(i, combined[i]);
		}
		Arrays.sort(basisPairs, Comparator.comparingInt(p -> p.y));

		int[] basis = new int[combinedCount];
		for (int i = 0; i < combinedCount; ++i) {
			basis[i] = basisPairs[i].x;
		}

		int targetDim = 1 << targetCount, controlDim = 1 << controlCount;
		double[] p = QuantumMatrix.identity(controlDim);
		int projectionSize = p.length;
		p[projectionSize - 2] = 0;

		double[] combinedMatrix = QuantumMatrix.tensorProduct(targetDim, QuantumMatrix.identity(targetDim), controlDim, p);

		double[] q = new double[projectionSize];
		q[projectionSize - 2] = 1;

		QuantumMatrix.addTo(combinedMatrix, QuantumMatrix.tensorProduct(targetDim, matrix, controlDim, q));

		Arrays.sort(combined);
		QuantumMatrix.changeIndexBasis(1 << combinedCount, combinedMatrix, basis);

		return new QuantumGate(combined, combinedMatrix);
	}

	public static final QuantumGate UNIT = new QuantumGate(new int[] {}, new double[] {1, 0});
}
