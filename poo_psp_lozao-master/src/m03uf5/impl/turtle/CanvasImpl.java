package m03uf5.impl.turtle;

import lib.StdDraw;
import lib.Problems;
import lib.Reflections;
import m03uf5.prob.turtle.Canvas;
import m03uf5.prob.turtle.Color;

public class CanvasImpl implements Canvas {

	@Override
	public void setScale(double scale) {
		StdDraw.setScale(0, scale);
	}

	@Override
	public double getWidth() {

		return 0;
	}

	@Override
	public double getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setColor(Color color) {

		System.out.println(color);

		switch (color) {
		case BLACK: StdDraw.setPenColor(StdDraw.BLACK);
			;
			break;
		case RED: StdDraw.setPenColor(StdDraw.RED);
			;
			break;
		case YELLOW: StdDraw.setPenColor(StdDraw.YELLOW);
			;
			break;
		case BLUE: StdDraw.setPenColor(StdDraw.BLUE);
			;
			break;
		case GREEN: StdDraw.setPenColor(StdDraw.GREEN);
			;
			break;
		default:
			
		}

	}

	@Override
	public void setPenSize(double size) {

		size = size * 0.002;
		System.out.println(size);
		StdDraw.setPenRadius(size);

	}

	@Override
	public void drawPoint(double x, double y) {
		StdDraw.point(x, y);

	}

	@Override
	public void drawLine(double x0, double y0, double x1, double y1) {
		StdDraw.line(x0, y0, x1, y1);
	}

	@Override
	public void save(String filename) {

		StdDraw.save(filename);
	}

}
