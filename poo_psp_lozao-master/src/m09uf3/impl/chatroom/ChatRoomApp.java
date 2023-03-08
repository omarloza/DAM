package m09uf3.impl.chatroom;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf3.prob.chatroom.ChatRoomClient;

public class ChatRoomApp extends Application {
	
	static final Logger LOGGER = Logger.getLogger(ChatRoomApp.class.getName());
	
	static final String HOST = "10.1.5.10";
	static final int PORT = 5511;

	private ChatPresenter p;
	private ChatView v;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		LoggingConfigurator.configure(Level.INFO);
		
		p = new ChatPresenter();		
		v = new ChatView(primaryStage);
		
		p.setView(v);
		v.setPresenter(p);
	}
	
	@Override
	public void stop() {
		try {
			p.disconnect();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "disconnecting", e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private static ChatRoomClient getClientInstance(String host) {
		
		String packageName = Problems.getImplPackage(ChatRoomClient.class);		
		return Reflections.newInstanceOfType(
				ChatRoomClient.class, packageName + ".ChatRoomClientImpl",
				new Class[] {String.class, int.class}, 
				new Object[] {host, PORT});
	}
	
	static class ChatPresenter {

		private ChatView view;
		private ChatRoomClient client;
		private boolean started;
		
		public void setView(ChatView v) {
			this.view = v;
		}

		public void message(String text) throws IOException {	
			
			client.message(text);
		}		

		public void connect(String username) throws IOException {
			
			this.started = true;
			this.client = getClientInstance(HOST);
			
			client.connect(username, (from, message) -> {					
				Platform.runLater(() -> {
					view.addItem(from + ": " + message);	
				});
			});
		}
		
		public void disconnect() throws IOException {	
			
			if (!this.started) {
				return;
			}
			
			client.disconnect();
		}
	}
	
	static class ChatView {

		private Stage primaryStage;
		private ChatPresenter presenter;
		private ListView<String> chatListView;

		public ChatView(Stage primaryStage) {	
			
			this.primaryStage = primaryStage;
			this.primaryStage.setScene(makeUserUI());
			this.primaryStage.setTitle("ChatRoom");
			this.primaryStage.show();
		}
		
		public void setPresenter(ChatPresenter p) {
			
			this.presenter = p;
		}
		
		public void addItem(String text) {
			
			ObservableList<String> items = chatListView.getItems();
			int index = items.size();
			items.add(text);
			chatListView.scrollTo(index);
		}
				
		private Scene makeUserUI() {
			
			VBox rootPane = new VBox();
			rootPane.setAlignment(Pos.CENTER);
			rootPane.setPadding(new Insets(10));
			rootPane.setSpacing(10);
			
			TextField textField = new TextField();
			Label label = new Label("username");
			
			rootPane.getChildren().addAll(label, textField);
			textField.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					String username = textField.getText();
					try {
						presenter.connect(username);
					} catch (IOException e) {
						LOGGER.log(Level.SEVERE, "connecting", e);
					}
					primaryStage.setScene(makeChatUI());
					primaryStage.setTitle("ChatRoom: " + username);					
				}
			});
			
			return new Scene(rootPane, 250, 150);
		}
		
		private Scene makeChatUI() {
			
			GridPane rootPane = new GridPane();
			rootPane.setPadding(new Insets(10));
			rootPane.setAlignment(Pos.CENTER);
			rootPane.setHgap(10);
			rootPane.setVgap(10);

			chatListView = new ListView<String>();
			chatListView.setPrefWidth(700);
			chatListView.setFocusTraversable(false);
			
			TextField chatTextField = new TextField();			
			chatTextField.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						presenter.message(chatTextField.getText());
					} catch (IOException e) {
						LOGGER.log(Level.SEVERE, "messaging", e);
					}
					chatTextField.clear();
				}
			});

			rootPane.add(chatListView, 0, 0);
			rootPane.add(chatTextField, 0, 1);
			
			return new Scene(rootPane, 800, 400);
		}

	}
}
