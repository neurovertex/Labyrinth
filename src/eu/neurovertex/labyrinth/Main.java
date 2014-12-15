package eu.neurovertex.labyrinth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Neurovertex
 *         Date: 17/11/2014, 00:46
 */
public class Main {
	private static final double RELIABILITY = 0.9;

	public static void main(String[] args) throws IOException {
		byte l = Grid.LEFT, r = Grid.RIGHT, u = Grid.UP, d = Grid.DOWN,
				dr = (byte) (d | r), ul = (byte) (u | l);
		byte[][] gridArray = {
				{r, d, 0, r, r, r, d},
				{u, r, dr, u, 0, 0, d},
				{u, 0, d, 0, d, l, l},
				{u, 0, dr, r, d, 0, 0},
				{u, l, l, 0, dr, r, d},
				{u, 0, u, 0, d, 0, d},
				{u, l, ul, l, l, l, l},
		};
		Grid grid = new Grid(gridArray.length, gridArray[0].length);
		for (int i = 0; i < grid.getWidth(); i++)
			for (int j = 0; j < grid.getHeight(); j++)
				grid.set(i, j, gridArray[j][i]); // transpose
		Dweller dweller = new Dweller(grid, 0.2);
		Set<int[]> sensorPos = new HashSet<>();
		while (sensorPos.size() < 2) {
			int[] p = new int[]{(int) (Math.random() * grid.getWidth()), (int) (Math.random() * grid.getHeight())};
			if (!grid.isEmpty(p[0], p[1]))
				sensorPos.add(p);
		}

		//for (int[] coord : sensorPos) dweller.addSensor(new PositionSensor(coord[0], coord[1], dweller, RELIABILITY));

		dweller.addSensor(new DistanceSensor(grid.getWidth() / 2, grid.getHeight() / 2, dweller, RELIABILITY, DistanceSensor::invertedTaxicabDistance));

		dweller.initRandom();
		GUI gui = new GUI(dweller, 1, 1);
		dweller.addObserver(gui);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (reader.readLine() != null)
			dweller.evolve();
	}
}
