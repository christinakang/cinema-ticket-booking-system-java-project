package admin_Screens;

import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import larn.modernUI.WaveButton;

public class AdminBaseLayout extends ScrollPane{
	private static BorderPane paneBaseLayout = new BorderPane();
	private WaveButton btnLogout;
	
	public AdminBaseLayout(){
		paneBaseLayout.setStyle("-fx-background-image: url(\"Images/BaseLayout/background.jpg\"); -fx-background-repeat: repeat-y; -fx-background-size: 100%;");
		
		//Top left logo
		ImageView imgLogo = new ImageView("Images/BaseLayout/logo.png");
		imgLogo.setFitWidth(150);
		imgLogo.setFitHeight(70);
		WaveButton btnLogo = new WaveButton();
		btnLogo.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1);");
		btnLogo.setGraphic(imgLogo);
		btnLogo.setOnAction(e -> paneBaseLayout.setCenter(new AdminHomeTab()));
		
		//Top right logout button
		btnLogout = new WaveButton("Logout");
		ImageView imgLogin = new ImageView("Images/BaseLayout/login.png");
		imgLogin.setFitWidth(30);
		imgLogin.setFitHeight(30);
		btnLogout.setGraphic(imgLogin);
		btnLogout.setContentDisplay(ContentDisplay.RIGHT);
		btnLogout.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
		btnLogout.setFont(Font.font("Courier", FontWeight.BOLD, 16));
		btnLogout.setOnAction(e -> AdminMainApplication.setScene());
		
		//Title for entire admin user interface
		Label labTitle = new Label("RKC Movie Ticketing System");
		labTitle.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2); -fx-text-fill: white; -fx-font-size: 50px; -fx-font-family: Aharoni;");
		
		//All header elements added to header
		BorderPane paneHeader = new BorderPane();
		paneHeader.setPadding(new Insets(15, 15, 15, 15));
		paneHeader.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(0, 0, 128, 0.65), rgba(25, 25, 112, 0.65));");
		paneHeader.setLeft(btnLogo);
		paneHeader.setCenter(labTitle);
		paneHeader.setRight(btnLogout);
		
		//Footer elements
		Label labContact = new Label("Customer Service Hotline:\n1300 - 888 - 555");
		labContact.setFont(Font.font("Courier", 20));
		labContact.setStyle("-fx-background-color: midnightblue;");
		labContact.setTextFill(Color.ALICEBLUE);
		
		Label labCopyRight = new Label("Copyright Reserved \u00A9\nRKC Cinemas SDN. BHD.\nAll Rights Reserved.");
		labCopyRight.setFont(Font.font("Courier", 15));
		labCopyRight.setStyle("-fx-background-color: midnightblue;");
		labCopyRight.setTextFill(Color.ALICEBLUE);
		
		BorderPane paneFooter = new BorderPane();
		paneFooter.setPadding(new Insets(15, 15, 15, 15));
		paneFooter.setLeft(labContact);
		paneFooter.setRight(labCopyRight);
		paneFooter.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(0, 0, 128, 0.65), rgba(25, 25, 112, 0.75));");
		
		//Set header and footer
		paneBaseLayout.setTop(paneHeader);
		paneBaseLayout.setBottom(paneFooter);
		
		//Placing base layout in scroll pane
		paneBaseLayout.setOnScroll(e -> setVvalue(getVvalue() + -e.getDeltaY()/80));
		setContent(paneBaseLayout);
		setFitToWidth(true);
		setFitToHeight(true);
	}
	
	public static BorderPane getPaneBaseLayout() {
		return paneBaseLayout;
	}
}
