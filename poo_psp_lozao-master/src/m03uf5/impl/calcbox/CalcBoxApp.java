package m03uf5.impl.calcbox;

import javafx.application.Application;
import javafx.stage.Stage;
import m03uf5.impl.calcbox.CalcBoxContract.CalcBoxPresenter;
import m03uf5.impl.calcbox.CalcBoxContract.CalcBoxView;


public class CalcBoxApp extends Application {
	private CalcBoxPresenter p;
	private CalcBoxView v;

	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
	
		p = new CalcBoxPresenterImpl();
		
		v = new CalcBoxViewImpl(primaryStage);
		
		p.setView(v);
		v.setPresenter(p);	
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
