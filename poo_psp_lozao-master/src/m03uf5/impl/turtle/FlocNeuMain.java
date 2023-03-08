package m03uf5.impl.turtle;

import lib.Problems;
import m03uf5.prob.turtle.Canvas;
import m03uf5.prob.turtle.Figure;
import m03uf5.prob.turtle.Turtle;

public class FlocNeuMain{

public static void main(String[] args) {
		
		String packageName = Problems.getImplPackage(Canvas.class);		
		Canvas canvas = new CanvasImpl();
		Turtle turtle = new TurtleImpl();
		
		canvas.setScale(100);
		turtle.setCanvas(canvas);
		
		turtle.goForward(50);
		turtle.turnLeft(90);
		
		draw(turtle, 25, 5);
	}
	
	static void draw(Turtle t, double branca, double petal) {
		
		t.goForward(branca);
		
		t.turnLeft(45);
		t.goForward(petal);
		t.turnRight(90);
		t.goForward(petal);
		t.turnRight(90);
	
		t.goForward(petal);
		t.turnRight(90);
		t.goForward(petal);
		t.turnLeft(45);
		
		t.goForward(branca);			
		t.turnLeft(180);
	}



}
