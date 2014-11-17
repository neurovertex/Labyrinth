package eu.neurovertex.labyrinth;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Neurovertex
 *         Date: 17/11/2014, 02:11
 */
public class GUI extends JFrame implements Observer {
	private GridPanel panel;
	private Dweller dweller;

	public GUI(final Dweller dweller) {
		this.dweller = dweller;
		panel = new GridPanel();

		add(panel);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE)
					dweller.evolve();
			}
		});

		pack();
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

	private class GridPanel extends JPanel {
		private static final int SIZE = 700;
		private final int[][] diffs = new int[][]{new int[]{1, 0}, new int[]{0, 1}, new int[]{-1, 0}, new int[]{0, -1}};

		public GridPanel() {
			setPreferredSize(new DimensionUIResource(SIZE * 2 + 10, SIZE));
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
			Graphics gimg = img.createGraphics();
			Grid grid = dweller.getGrid();
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, SIZE * 2 + 10, SIZE);

			int side = SIZE / Math.max(grid.getWidth(), grid.getHeight());

			for (int i = 0; i < grid.getWidth(); i++)
				for (int j = 0; j < grid.getHeight(); j++) {
					if (grid.isEmpty(i, j)) {
						gimg.setColor(Color.BLACK);
						gimg.fillRect(i * side, j * side, side, side);
					} else {
						gimg.setColor(Color.DARK_GRAY);
						gimg.drawRect(i * side, j * side, side, side);
						int srcx = (i * 2 + 1) * side / 2, srcy = (j * 2 + 1) * side / 2, len = 3 * side / 8;
						byte val = grid.get(i, j);

						for (byte d = 0; d < 4; d++)
							if ((val & (0x1 << d)) > 0)
								gimg.drawLine(srcx, srcy, srcx + diffs[d][0] * len, srcy + diffs[d][1] * len);
					}

					g.setColor(new Color(192, 192, 0, (int) (Math.pow(dweller.getProba(i, j), 0.5) * 255)));
					g.fillRect(i * side + SIZE + 10, j * side, side, side);
					g.setColor(Color.BLACK);
					g.drawString(String.format("%.3f%%", dweller.getProba(i, j)), (i * 2 + 1) * side / 2 - 5 + SIZE, (j * 2 + 1) * side / 2 - 5);
				}

			g.drawImage(img, 0, 0, null);
			g.setColor(Color.BLACK);
			g.fillRect(SIZE, 0, 10, SIZE);
			g.drawImage(img, SIZE + 10, 0, null);

			g.setColor(Color.BLUE);
			int srcx = (dweller.getX() * 2 + 1) * side / 2, srcy = (dweller.getY() * 2 + 1) * side / 2, rad = side / 3;
			g.fillOval(srcx - rad / 2, srcy - rad / 2, rad, rad);
		}
	}
}
