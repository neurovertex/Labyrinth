package eu.neurovertex.labyrinth;

import java.awt.*;

/**
 * @author Neurovertex
 *         Date: 17/11/2014, 00:46
 */
public class Grid {
	public static final byte RIGHT = 0x1, DOWN = 0x2, LEFT = 0x4, UP = 0x8;
	private final int width, height;
	private final byte[][] grid;

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new byte[width][height];
	}

	public Grid(byte[][] grid) {
		this.width = grid.length;
		this.height = grid[0].length;
		this.grid = new byte[width][height];
		for (int i = 0; i < width; i++)
			System.arraycopy(grid[i], 0, this.grid[i], 0, height);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public byte get(int x, int y) {
		return grid[x][y];
	}

	public boolean getRight(int x, int y) {
		return (get(x, y) & RIGHT) != 0;
	}

	public boolean getDown(int x, int y) {
		return (get(x, y) & DOWN) != 0;
	}

	public boolean getLeft(int x, int y) {
		return (get(x, y) & LEFT) != 0;
	}

	public boolean getUp(int x, int y) {
		return (get(x, y) & UP) != 0;
	}

	public boolean isEmpty(int x, int y) {
		return get(x, y) == 0;
	}

	public void set(int x, int y, byte val) {
		grid[x][y] = val;
	}

	public void set(int x, int y, boolean right, boolean down, boolean left, boolean up) {
		byte r = (right ? RIGHT : (byte) 0), d = (down ? DOWN : (byte) 0), l = (left ? LEFT : (byte) 0), u = (up ? UP : (byte) 0);
		set(x, y, (byte) (r | d | l | u));
	}

	public void printGrid() {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				System.out.printf("%2d ", grid[i][j]);
			}
			System.out.println();
		}
	}

	public Point shift(int x, int y, byte direction) {
		switch (direction) {
			case RIGHT:
				x++;
				break;
			case DOWN:
				y++;
				break;
			case LEFT:
				x--;
				break;
			case UP:
				y--;
				break;
			default:
				throw new IllegalArgumentException("Illegal direction " + direction);
		}
		return new Point(x, y);
	}
}
