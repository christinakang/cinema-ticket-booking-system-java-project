package ui_screens;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class CustomerMainApplication extends Application {
	public void start(Stage stage){
		
		BaseLayout tab = new BaseLayout();
		BaseLayout.getPaneBaseLayout().setCenter(new HomeTab());
		
		Scene scene = new Scene(tab);
		stage.setScene(scene);
		stage.setTitle("RKC Movie Ticketing System");
		stage.setMaximized(true);
		stage.show();
	}
	
	public static void main(String[] args){
		launch();
	}
}
