package eu.neurovertex.labyrinth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Neurovertex
 *         Date: 17/11/2014, 00:46
 */
public class Main {
	public static void main(String[] args) throws IOException {
		byte l = Grid.LEFT, r = Grid.RIGHT, u = Grid.UP, d = Grid.DOWN,
				dr = (byte) (d | r), ul = (byte) (u | l);
		System.out.printf("%d, %d, %d, %d, %d, %d%n", l, d, r, u, dr, ul);
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
		grid.printGrid();
		System.out.println(grid.get(1, 0));
		Dweller dweller = new Dweller(grid, 0.2);
		dweller.initRandom();
		GUI gui = new GUI(dweller);
		dweller.addObserver(gui);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (reader.readLine() != null)
			dweller.evolve();
	}
}
