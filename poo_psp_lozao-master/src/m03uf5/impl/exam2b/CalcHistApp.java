package m03uf5.impl.exam2b;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import m03uf5.impl.exam2b.CalcHistContract.CalcHistPresenter;
import m03uf5.impl.exam2b.CalcHistContract.CalcHistView;


public class CalcHistApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		CalcHistView v = new CalcBoxViewImpl(primaryStage);
		CalcHistPresenter p = new CalcBoxPresenterImpl();

		p.setView(v);
		v.setPresenter(p);
	}

	public static void main(String[] args) {
		launch(args);
	}

	// CLASSES

	static class CalcBoxViewImpl implements CalcHistView {

		private CalcHistPresenter p;
		private Label label;
		private int posicion;
		

		TextField text;
		public CalcBoxViewImpl(Stage stage) {
			initStage(stage);
		}

		@Override
		public void setPresenter(CalcHistPresenter p) {

			this.p = p;

		}

		private void initStage(Stage stage) {

			System.out.println("posicion : " + posicion);
			VBox vb = new VBox(10);

			Font font = new Font(20);

			text = new TextField();
			text.setFont(font);

			this.label = new Label();
			this.label.setFont(font);

			HBox hb = new HBox();
			Font font2 = new Font(15);

			hb.setAlignment(Pos.CENTER);
			Button button = new Button("Calcula");
			button.setFont(font);

			Button anterior = new Button("Anterior");
			anterior.setFont(font2);
			anterior.setDisable(true);

			Button siguiente = new Button("Siguiente");
			siguiente.setFont(font2);
			siguiente.setDisable(true);


			button.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {

					p.calcula(text.getText());

					posicion = p.getHistorial().size();
					System.out.println("posicion : " + posicion);

					if (p.getHistorial().size() >= 2) {
						anterior.setDisable(false);
					}

				}
			});

			anterior.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					System.out.println("posicion actual: " + posicion);

					posicion--;
					if (posicion == 1) {
						anterior.setDisable(true);
					}
					siguiente.setDisable(false);
					String txtAnterior = p.getHistorial().get(posicion - 1);
					p.anteriorYsiguiente(txtAnterior);

				}
			});

			siguiente.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					posicion++;
					if (posicion >= p.getHistorial().size()) {
						siguiente.setDisable(true);
					}

					if (posicion > 0) {
						anterior.setDisable(false);
					}

					System.out.println("posicion actual: " + posicion);

					String txtAnterior = p.getHistorial().get(posicion - 1);
					text.setText(txtAnterior);
					p.anteriorYsiguiente(txtAnterior);

				}
			});

			hb.getChildren().addAll(anterior, button, siguiente);
			vb.getChildren().addAll(text, this.label, hb);

			Scene scene = new Scene(vb, 400, 200);
			stage.setScene(scene);
			stage.setTitle("Calculadora senzilla");
			stage.show();
		}

		@Override
		public void mostra(String display) {
			this.label.setText(display);
		}

		@Override
		public void mostraHistorial(String resultat) {
				text.setText(resultat);
			
		}

	

		
	}

	static class CalcBoxPresenterImpl implements CalcHistPresenter {

		private ArrayList<String> historial;

		private ExprAvaluator2 ea;
		private CalcHistView v;

		public CalcBoxPresenterImpl() {
			ea = new ExprAvaluator2();
			historial = new ArrayList<String>();
		}

		@Override
		public void setView(CalcHistView v) {
			this.v = v;
		}

		@Override
		public void calcula(String expr) {
			double valor = 0;
			try {

				
				valor= ea.avalua(expr);
				
				if(Double.valueOf(valor) != null) {
					historial.add(expr);
					
				}
				
				
				v.mostra(Double.toString(valor));

			} catch (Exception e) {


				
				
				v.mostra(e.getClass().getSimpleName());
			}

		}

		@Override
		public void anteriorYsiguiente(String expr) {

			try {
				
				if(historial.contains(expr)) {
				v.mostraHistorial(expr);	
				double valor = ea.avalua(expr);
				v.mostra(Double.toString(valor));
				}
				
				

			} catch (Exception e) {
				v.mostra(e.getClass().getSimpleName());
			}
		}

		

		@Override
		public ArrayList<String> getHistorial() {
			return historial;
		}

	}
}
