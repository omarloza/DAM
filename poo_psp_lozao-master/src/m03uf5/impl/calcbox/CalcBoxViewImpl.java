package m03uf5.impl.calcbox;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import m03uf5.impl.calcbox.CalcBoxContract.CalcBoxPresenter;
import m03uf5.impl.calcbox.CalcBoxContract.CalcBoxView;
import m03uf5.prob.calcbox.ExprAvaluator;

public class CalcBoxViewImpl implements CalcBoxView {
	private CalcBoxPresenter presenter;
	private Label label;
	
	public CalcBoxViewImpl(Stage primaryStage) {
		setStage(primaryStage);
	}

	
	private void setStage(Stage primaryStage) {
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
				String cadena = text.getText();
				presenter.calcula(cadena);
		
				
			}			
		});
		
		vb.getChildren().addAll(text, label, button);
		
		Scene scene = new Scene(vb, 800, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Calculadora App1");
		primaryStage.show();
	
		
	}

	@Override
	public void mostra(String resultat) {
		label.setText(resultat);	
		
	}

	@Override
	public void setPresenter(CalcBoxPresenter p) {
		this.presenter=p;
	}

}
