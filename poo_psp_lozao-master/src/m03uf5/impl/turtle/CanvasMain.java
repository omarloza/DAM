package m03uf5.impl.turtle;
import lib.Problems;
import lib.Reflections;
import m03uf5.prob.turtle.Canvas;
import m03uf5.prob.turtle.Color;

public class CanvasMain {

	public static void main(String[] args) {
		
		// magia: crea una instancia d'una implementacio de Canvas anomenada m03uf5.impl.turtle.CanvasImpl
		// ho fa utilitzant el constructor sense parametres
		// alternativament, es podria fer Canvas canvas = new CanvasImpl();
			
//		String packageName = Problems.getImplPackage(Canvas.class);		
//		Canvas canvas = Reflections.newInstanceOfType(Canvas.class, packageName + ".CanvasImpl");		
		
		Canvas canvas = new CanvasImpl();
		
		canvas.setScale(100); // quadrat amb costat de 0 a 100	
		canvas.setPenSize(25);
		canvas.setColor(Color.YELLOW);
		canvas.drawPoint(20, 80);
		canvas.drawPoint(80, 80);
		canvas.setColor(Color.BLUE);
		canvas.drawPoint(50, 50);
		canvas.setColor(Color.RED);
		canvas.drawLine(20, 20, 80, 20);
		canvas.save("test2.png");
		
	}
}
