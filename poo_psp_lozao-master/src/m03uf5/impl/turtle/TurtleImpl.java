package m03uf5.impl.turtle;

import m03uf5.prob.turtle.Canvas;
import m03uf5.prob.turtle.Turtle;
import lib.StdDraw;

public class TurtleImpl implements Turtle {
	private Canvas canvas;
	boolean pen;
	double angle;
	double x = 0;
	double y = 0;

	@Override
	public void up() {

		pen = false;

	}

	@Override
	public void down() {

		pen = true;

	}

	@Override
	public double x() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public double y() {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public double angle() {

		return angle;
	}

	@Override
	public void turnLeft(double delta) {

		angle = angle + delta;

	}

	@Override
	public void turnRight(double delta) {
		angle = angle - delta;
	}

	@Override
	public void goForward(double step) {
		if (pen = true) {
			StdDraw.line(x, y, x = x + step * Math.cos(Math.toRadians(angle)),
					y = y + step * Math.sin(Math.toRadians(angle)));
		}else {
				x = x + step * Math.cos(Math.toRadians(angle));
				y = y + step * Math.sin(Math.toRadians(angle));
		}
	}

	@Override
	public void goBackward(double step) {
		if (pen = true) {
			StdDraw.line(x, y, x = x - step * Math.cos(Math.toRadians(angle)),
					y = y - step * Math.sin(Math.toRadians(angle)));
		}else {
			x = x - step * Math.cos(Math.toRadians(angle));
			y = y - step * Math.sin(Math.toRadians(angle));
	}
	}

	@Override
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;

	}

	@Override
	public Canvas getCanvas() {
		// TODO Auto-generated method stub
		return canvas;
	}

	public static void main(String[] args) {

	}

}
