package admin_Screens;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class AdminMainApplication extends Application{
	private static Scene scene;
	
	public void start(Stage stage){
		
		AdminLoginTab loginTab = new AdminLoginTab();
		//setScene();
		scene = new Scene(loginTab);
		stage.setScene(scene);
		stage.setTitle("RKC Cinema Administration");
		stage.setMaximized(true);
		stage.show();
	}
	
	public static void main(String[] args){
		launch();
	}
	
	public static void setScene(){
		if(scene.getRoot() instanceof AdminLoginTab){
			AdminBaseLayout tab = new AdminBaseLayout();
			AdminBaseLayout.getPaneBaseLayout().setCenter(new AdminHomeTab());
			scene.setRoot(tab);
		}
		else if(scene.getRoot() instanceof AdminBaseLayout){
			AdminLoginTab loginTab = new AdminLoginTab();
			scene.setRoot(loginTab);
		}
		
	}
}
