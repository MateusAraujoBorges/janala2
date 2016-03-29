package tests.casestudies.robothunt;
/*
 * Java implementation inspired by the tutorial http://www.nolithius.com/game-development/world-generation-breakdown
 * with some variations on both noise and gradient generation
 */
 
import java.util.Random;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomAdaptor;

import com.google.common.base.Preconditions;

public class TerrainGenerator {
	public static final int WATER = -1;
	public static final int TERRAIN = 0;
	public static final int MOUNTAIN = 1;

	private final long seed;
	private final int width;
	private final int height;
	private Random random;
	private final double persistance = .7;

	private static final double waterThreshold = .4;
	private static final double mountainThreshold = .6;

	public TerrainGenerator(long seed, int width, int height) {
		super();
		this.seed = seed;
		this.width = width;
		this.height = height;
		reset();
	}

	public final void reset() {
		this.random = new RandomAdaptor(new MersenneTwister(seed));
	}

	private final double[][] genWhiteNoise() {
		double[][] noise = new double[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				noise[x][y] = random.nextDouble() % 1;
			}
		}

		return noise;
	}

	private final double[][] generateSmoothNoise(double[][] baseNoise,
			int octave) {
		double[][] smoothNoise = new double[width][height];

		int samplePeriod = 1 << octave;
		double sampleFrequency = 1.0f / samplePeriod;

		for (int x = 0; x < width; x++) {
			int sample_x0 = (x / samplePeriod) * samplePeriod;
			int sample_x1 = (sample_x0 + samplePeriod) % width;
			double horizontal_blend = (x - sample_x0) * sampleFrequency;

			for (int y = 0; y < height; y++) {
				int sample_y0 = (y / samplePeriod) * samplePeriod;
				int sample_y1 = (sample_y0 + samplePeriod) % height;
				double vertical_blend = (y - sample_y0) * sampleFrequency;

				double top = interpolate(baseNoise[sample_x0][sample_y0],
						baseNoise[sample_x1][sample_y0], horizontal_blend);

				double bottom = interpolate(baseNoise[sample_x0][sample_y1],
						baseNoise[sample_x1][sample_y1], horizontal_blend);

				smoothNoise[x][y] = interpolate(top, bottom, vertical_blend);
			}
		}

		return smoothNoise;
	}

	private final double interpolate(double x0, double x1, double alpha) {
		return x0 * (1 - alpha) + alpha * x1;
	}

	private final double[][] perlinNoise(int octaveCount) {
		double[][] baseNoise = genWhiteNoise();

		double[][][] smoothNoise = new double[octaveCount][][];

		for (int i = 0; i < octaveCount; i++) {
			smoothNoise[i] = generateSmoothNoise(baseNoise, i);
		}

		double[][] perlinNoise = new double[width][height];
		double amplitude = 1.0;
		double totalAmplitude = 0.0d;

		for (int octave = octaveCount - 1; octave >= 0; octave--) {
			amplitude *= persistance;
			totalAmplitude += amplitude;

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					perlinNoise[x][y] += smoothNoise[octave][x][y] * amplitude;
				}
			}
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				perlinNoise[x][y] /= totalAmplitude;
			}
		}

		return perlinNoise;
	}

	private final double[][] generateCentralGradient(double smothingPower) {
		double gradient[][] = new double[width][height];

		double xCenter = width * 1d / 2;
		double yCenter = height * 1d / 2;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				gradient[x][y] = Math
						.exp(-1
								/ Math.pow(10, smothingPower)
								* (Math.pow(x - xCenter, 2) + Math.pow(y
										- yCenter, 2)));
			}
		}
		return gradient;
	}

	private final int[][] generateMap(int octaveCount, double smothingPower) {
		Preconditions.checkArgument(octaveCount > 0);
		Preconditions.checkArgument(smothingPower >= 0);
		double[][] candidateMap = perlinNoise(octaveCount);
		double[][] gradient = generateCentralGradient(smothingPower);

		return finalizeMap(multiplyMaps(candidateMap, gradient));
	}

	public final int[][] generateMap() {
		return generateMap(6, 1d * (width + height) / 2 / 10);
	}

	private int[][] finalizeMap(double[][] candidateMap) {
		int[][] map = new int[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (candidateMap[x][y] <= waterThreshold) {
					map[x][y] = WATER;
				} else if (candidateMap[x][y] >= mountainThreshold) {
					map[x][y] = MOUNTAIN;
				} else {
					map[x][y] = TERRAIN;
				}
			}
		}
		return map;
	}

	private double[][] multiplyMaps(double[][] candidateMap, double[][] gradient) {
		double[][] product = new double[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				product[x][y] = candidateMap[x][y] * gradient[x][y];
			}
		}
		return product;
	}

}
