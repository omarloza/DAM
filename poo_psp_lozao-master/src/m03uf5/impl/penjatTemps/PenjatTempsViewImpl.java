package m03uf5.impl.penjatTemps;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import m03uf5.impl.penjat.PenjatContract.PenjatPresenter;
import m03uf5.impl.penjatTemps.PenjatTempsContract.PenjatTempsPresenter;
import m03uf5.impl.penjatTemps.PenjatTempsContract.PenjatTempsView;

public class PenjatTempsViewImpl implements PenjatTempsView{
	private PenjatTempsPresenter presenter;
	private Label label;
	private Label tiempo = new Label("15");
	private Label puntuacion;
	private Label turno;
	private EventHandler<ActionEvent> btnHandler;
	String text;
	Button reiniciar;
	private ArrayList<Button> listaBotones = new ArrayList<Button>();

	int segundos =15 ;
	Timer timer = new Timer();
	TimerTask tarea = new TimerTask() {

		@Override
		public void run() {
			
			
			
			if(segundos==0) {
				segundos = 15;
		
				
				
			}else {
				segundos --;
				
			}
			presenter.pasarTurno();
			
		}
		
	};

	public PenjatTempsViewImpl(Stage primaryStage) {

		setStage(primaryStage);

	}

	private void setStage(Stage primaryStage) {
		VBox vb = new VBox(10);
		vb.setAlignment(Pos.CENTER);

		label = new Label("");

		label.setAlignment(Pos.CENTER);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);

		btnHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Button b = (Button) event.getSource();
				String letra = b.getText();
				presenter.buttonClicked(letra);
				b.setDisable(true);
				String seg =""+segundos;
				tiempo.setText(seg);
				timer.cancel();
				timer.schedule(tarea, 1000, 15000);

			}
		};

		addButton(grid, "Q", 0, 0);
		addButton(grid, "W", 1, 0);
		addButton(grid, "E", 2, 0);
		addButton(grid, "R", 3, 0);
		addButton(grid, "T", 4, 0);
		addButton(grid, "Y", 5, 0);
		addButton(grid, "U", 6, 0);
		addButton(grid, "I", 7, 0);
		addButton(grid, "O", 8, 0);
		addButton(grid, "P", 9, 0);

		addButton(grid, "A", 0, 1);
		addButton(grid, "S", 1, 1);
		addButton(grid, "D", 2, 1);
		addButton(grid, "F", 3, 1);
		addButton(grid, "G", 4, 1);
		addButton(grid, "H", 5, 1);
		addButton(grid, "J", 6, 1);
		addButton(grid, "K", 7, 1);
		addButton(grid, "L", 8, 1);
		addButton(grid, "Ñ", 9, 1);

		addButton(grid, "Z", 1, 2);
		addButton(grid, "X", 2, 2);
		addButton(grid, "C", 3, 2);
		addButton(grid, "V", 4, 2);
		addButton(grid, "B", 5, 2);
		addButton(grid, "N", 6, 2);
		addButton(grid, "M", 7, 2);
		addButton(grid, "Ç", 8, 2);

		reiniciar = new Button("Volver a Jugar");
		turno = new Label("Turno : Jugador 1");
		puntuacion = new Label("Marcador: Jugador 1 [0,0] Jugador2");
		Button comenzar = new Button("Comenzar");

		if (comenzar.isVisible()) {
			for (int i = 0; i < listaBotones.size(); i++) {
				Button a = listaBotones.get(i);
				a.setDisable(true);
			}

		}

		comenzar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String seg =""+segundos;
				tiempo.setText(seg);
				presenter.comenzar();
				comenzar.setVisible(false);
				for (int i = 0; i < listaBotones.size(); i++) {
					Button a = listaBotones.get(i);
					a.setDisable(false);
				}

				timer.schedule(tarea, 1000, 15000);
			
				
			}
		});

		reiniciar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				presenter.reiniciar();
				comenzar.setVisible(true);

				for (int i = 0; i < listaBotones.size(); i++) {
					Button a = listaBotones.get(i);
					a.setDisable(true);
				}

			}
		});

		vb.getChildren().addAll(tiempo,comenzar, label, grid, reiniciar, turno, puntuacion);
		Scene scene = new Scene(vb, 600, 300);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Penjat");
		primaryStage.show();

	}

	private void addButton(GridPane grid, String text, int columna, int fila) {
		int medidaColumna = 1;
		int medidaFila = 1;
		Button button = new Button(text);
		listaBotones.add(button);

		button.setMaxWidth(Double.POSITIVE_INFINITY);
		button.setMaxHeight(Double.POSITIVE_INFINITY);
		button.setOnAction(btnHandler);

		grid.add(button, columna, fila, medidaColumna, medidaFila);
	}

	@Override
	public void setPresenter(PenjatTempsPresenter p) {
		this.presenter = p;

	}

	@Override
	public void mostra(String resultat) {
		label.setText(resultat);

	}

	@Override
	public void mostraComprovaciones(String resultat) {

		label.setText(resultat);

	}

	@Override
	public void tornarAJugar() {
		reiniciar.setDisable(true);
	}

	@Override
	public void mostraTurno(String resultat) {
		turno.setText(resultat);

	}

	@Override
	public void mostraResultado(String resultat) {
		puntuacion.setText(resultat);

	}

}
