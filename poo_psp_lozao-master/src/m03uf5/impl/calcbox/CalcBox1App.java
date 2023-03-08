package m03uf5.impl.calcbox;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import m03uf5.prob.calcbox.ExprAvaluator;


public class CalcBox1App extends Application{
	Label label;
	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox vb = new VBox(10);
		
		Font font = new Font(20);
		
		TextField text = new TextField();
		text.setFont(font);
		
		label = new Label();
		label.setFont(font);
		
		Button button = new Button("Calcula");
		button.setFont(font);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				ExprAvaluator calcular = new ExprAvaluator();
				
				try {
					
					calcular.avalua(text.getText());
					
					double result = calcular.avalua(text.getText());			
					if (result == (long) result) {
						resultat(Long.toString((long) result));
					}
					else {
						resultat(Double.toString(result));
					}
					
					
				} catch (IOException e) {
		
					e.printStackTrace();
				}
		
				
			}			
		});
		
		vb.getChildren().addAll(text, label, button);
		
		Scene scene = new Scene(vb, 400, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Calculadora App1");
		primaryStage.show();
	}
	public void resultat(String value) {
		label.setText(value);	
	}

	public static void main(String[] args) {
		launch(args);
	}
}
