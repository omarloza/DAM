package m03uf5.impl.penjatTemps;

import javafx.application.Application;
import javafx.stage.Stage;


public class PenjatTempsApp extends Application {
	private PenjatTempsPresenterImpl p;
	private PenjatTempsViewImpl v;
	private PenjatTempsModelImpl m;

	@Override
	public void start(Stage primaryStage) throws Exception {

		p = new PenjatTempsPresenterImpl();
		m = new PenjatTempsModelImpl();
		v = new PenjatTempsViewImpl(primaryStage);

		p.setView(v);
		p.setModel(m);
		v.setPresenter(p);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
