package gradeCalc.fxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GradeCalcApp extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
        Parent parent = FXMLLoader.load(getClass().getResource("GradeCalcGUI.fxml"));
        primaryStage.setScene(new Scene(parent));
		
		primaryStage.setTitle("Karakterkalkulator");
		
		primaryStage.getIcons().add(new Image(GradeCalcApp.class.getResourceAsStream("logo_transparent.png")));
		primaryStage.setResizable(false);
		
		primaryStage.show();
	}

	public static void main(final String[] args) {
		launch(GradeCalcApp.class, args);
		
	}
}