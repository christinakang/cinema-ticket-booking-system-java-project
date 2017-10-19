package admin_Screens;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import larn.modernUI.WaveButton;

public class AdminHomeTab extends BorderPane{
	
	public AdminHomeTab(){
		
		WaveButton[] btnHomeButtons = new WaveButton[5];
		String[] homeButtonsName = {"Sessions", "Movies", "Cinemas", "Seats", "Promotions"};
		Image imgLoadGear = new Image("Images/BaseLayout/Gear.png");
		ObservableList<Node> buttonsList = FXCollections.observableArrayList();
		
		for(int i=0; i<5; i++){
			
			ImageView imgGear = new ImageView(imgLoadGear);
			imgGear.setFitWidth(200);
			imgGear.setFitHeight(200);
			Timeline rotation = new Timeline(new KeyFrame(Duration.millis(100), e -> imgGear.setRotate(imgGear.getRotate() + 1)));
			rotation.setCycleCount(Timeline.INDEFINITE);
			rotation.play();
			
			ImageView imgHomeButton = new ImageView("Images/BaseLayout/" + homeButtonsName[i] + ".png");
			imgHomeButton.setFitWidth(80);
			imgHomeButton.setFitHeight(80);
			
			StackPane paneOptions = new StackPane();
			paneOptions.getChildren().addAll(imgGear, imgHomeButton);
			paneOptions.setOnMouseEntered(e -> rotation.setRate(rotation.getRate() * 25));
			paneOptions.setOnMouseExited(e -> rotation.setRate(rotation.getRate() / 25));
			
			btnHomeButtons[i] = new WaveButton("Configure " + homeButtonsName[i]);
			btnHomeButtons[i].setStyle("-fx-background-color: rgb(0, 0, 0, 0); -fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: Tahoma; -fx-font-weight: bold;");
			openTab(btnHomeButtons[i], i);
			btnHomeButtons[i].setGraphic(paneOptions);
			btnHomeButtons[i].setContentDisplay(ContentDisplay.TOP);
			
			Pane paneHolder = new Pane(btnHomeButtons[i]);
			buttonsList.add(paneHolder);
		}
		
		FlowPane flowMenuButtons = new FlowPane();
		for(Node btnMenuButton: buttonsList)
			flowMenuButtons.getChildren().add(btnMenuButton);
		
		setPadding(new Insets(15, 15, 15, 15));
		setCenter(flowMenuButtons);
	}
	
	public void openTab(Button homeButton, int index){
		if(index == 0)
			homeButton.setOnAction(e -> AdminBaseLayout.getPaneBaseLayout().setCenter(new ConfigureSessionTab()));
		else if(index == 1)
			homeButton.setOnAction(e -> AdminBaseLayout.getPaneBaseLayout().setCenter(new ConfigureMovieTab()));
		else if(index == 2)
			homeButton.setOnAction(e -> AdminBaseLayout.getPaneBaseLayout().setCenter(new ConfigureCinemaTab()));
		else if(index == 3)
			homeButton.setOnAction(e -> AdminBaseLayout.getPaneBaseLayout().setCenter(new ConfigureSeatTab()));
		else if(index == 4)
			homeButton.setOnAction(e -> AdminBaseLayout.getPaneBaseLayout().setCenter(new ConfigurePromotionTab()));
	}
}
