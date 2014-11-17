package eu.neurovertex.labyrinth;

import java.awt.*;
import java.util.Observable;

/**
 * @author Neurovertex
 *         Date: 17/11/2014, 01:30
 */
public class Dweller extends Observable {
	private final Grid grid;
	private final double stayProba;
	private int curx, cury;
	private double[][] probability;

	public Dweller(Grid grid, double stayProba) {
		this.grid = grid;
		this.stayProba = stayProba;
		probability = new double[grid.getWidth()][grid.getHeight()];
	}

	private void clearProbas() {
		for (int i = 0; i < grid.getWidth(); i++)
			for (int j = 0; j < grid.getHeight(); j++)
				probability[i][j] = 0.0;
	}

	public void init(int x, int y) {
		clearProbas();
		probability[x][y] = 1.0;
		curx = x;
		cury = y;
	}

	public Point initRandom() {
		int x, y;
		do {
			x = (int) (Math.random() * grid.getWidth());
			y = (int) (Math.random() * grid.getHeight());
		} while (grid.isEmpty(x, y));
		init(x, y);
		return new Point(x, y);
	}

	public void evolve() {
		final double[][] newprobs = new double[grid.getWidth()][grid.getHeight()];
		final byte[] dirs = new byte[]{Grid.RIGHT, Grid.DOWN, Grid.LEFT, Grid.UP};
		for (int i = 0; i < grid.getWidth(); i++)
			for (int j = 0; j < grid.getHeight(); j++)
				if (!grid.isEmpty(i, j) && probability[i][j] > 0.0) {
					byte val = grid.get(i, j);
					int directions = (val & 0x1) + (val >> 1 & 0x1) + (val >> 2 & 0x1) + (val >> 3 & 0x1);
					for (byte d : dirs)
						if ((val & d) > 0) {
							Point p = grid.shift(i, j, d);
							newprobs[p.x][p.y] += probability[i][j] * (1 - stayProba) / directions;
						}
					newprobs[i][j] += probability[i][j] * stayProba;
				}

		if (Math.random() > stayProba) {
			byte val = grid.get(curx, cury);
			int directions = (val & 0x1) + (val >> 1 & 0x1) + (val >> 2 & 0x1) + (val >> 3 & 0x1),
					rand = (int) (Math.random() * directions);
			byte dir = dirs[0];
			for (byte d : dirs) {
				dir = d;
				if ((val & d) > 0)
					if (rand-- == 0)
						break;
			}
			Point newPos = grid.shift(curx, cury, dir);
			curx = newPos.x;
			cury = newPos.y;
		}

		probability = newprobs;

		setChanged();
		notifyObservers();
	}

	public int getX() {
		return curx;
	}

	public int getY() {
		return cury;
	}

	public double getProba(int x, int y) {
		return probability[x][y];
	}

	public Grid getGrid() {
		return grid;
	}
}
