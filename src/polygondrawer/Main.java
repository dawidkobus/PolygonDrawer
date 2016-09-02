package polygondrawer;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("gui/View.fxml"));
		stage.setTitle(Controller.TITLE);
		stage.setResizable(false);
		stage.setScene(new Scene(root));
		stage.getIcons().add(new Image(Controller.ICONPATH));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
