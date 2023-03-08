package m03uf5.impl.penjat;

import javafx.application.Application;
import javafx.stage.Stage;


public class PenjatApp extends Application {
	private PenjatPresenterImpl p;
	private PenjatViewImpl v;
	private PenjatModelImpl m;

	@Override
	public void start(Stage primaryStage) throws Exception {

		p = new PenjatPresenterImpl();
		m = new PenjatModelImpl();
		v = new PenjatViewImpl(primaryStage);

		p.setView(v);
		p.setModel(m);
		v.setPresenter(p);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
