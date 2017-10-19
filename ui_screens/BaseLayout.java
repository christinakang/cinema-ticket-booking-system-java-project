package ui_screens;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.geometry.Pos;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import larn.modernUI.WaveButton;
import real_objects.Customer;
import real_objects.Movie;

public class BaseLayout extends ScrollPane {
	
	private static BorderPane paneBaseLayout = new BorderPane();
	private boolean isLoggedIn = false;
	private WaveButton btnLogin;
	private WaveButton btnLogout;
	private WaveButton btnSignUp;
	private WaveButton btnUser;
	private WaveButton[] btnMenus = new WaveButton[7];
	private WaveButton btnSearch;
	private VBox vBoxRightHeader;
	private Label labError;
	private static String displayUsername = "";
	
	public BaseLayout(){
		paneBaseLayout.setStyle("-fx-background-image: url(\"Images/BaseLayout/background.jpg\"); -fx-background-repeat: repeat-y; -fx-background-size: 100%;");
		
		//Top left logo
		ImageView imgLogo = new ImageView("Images/BaseLayout/logo.png");
		imgLogo.setFitWidth(150);
		imgLogo.setFitHeight(70);
		WaveButton btnLogo = new WaveButton();
		btnLogo.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1);");
		btnLogo.setGraphic(imgLogo);
		btnLogo.setOnAction(e -> paneBaseLayout.setCenter(new HomeTab()));
		
		//Right header elements
		vBoxRightHeader = new VBox(5);
		vBoxRightHeader.setAlignment(Pos.TOP_LEFT);
		
		//Loading graphical elements for logged-in and logged-out situations
		if (isLoggedIn)
			loginAction();
		else
			logoutAction();
		
		//Bottom header containing menu buttons and search field
		String[] menuButtonsName = {"Home", "Movies", "Cinemas", "Promotions", "Movie Club", "Contact Us", "FAQ"};
		
		HBox hBoxMenuHeader = new HBox(10);
		for (int i=0; i<btnMenus.length; i++){
			btnMenus[i] = new WaveButton(menuButtonsName[i]);
			ImageView imgMenu = new ImageView("Images/BaseLayout/" + menuButtonsName[i] + ".png");
			imgMenu.setFitWidth(30);
			imgMenu.setFitHeight(30);
			btnMenus[i].setGraphic(imgMenu);
			btnMenus[i].setContentDisplay(ContentDisplay.LEFT);
			btnMenus[i].setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
			btnMenus[i].setFont(Font.font("Courier", FontWeight.BOLD, 16));
			setTabOnButtonClick(btnMenus[i], i);
			hBoxMenuHeader.getChildren().add(btnMenus[i]);
		}
		
		//MovieClub Tab access restriction which allows access only when logged in
		btnMenus[4].setOnMouseClicked(e -> {
			if(isLoggedIn)
				paneBaseLayout.setCenter(new MovieClubTab());
			else{
				if (vBoxRightHeader.getChildren().size() == 1)
					loginPrompt();
				else
					labError.setText("Login to access Movie Club!");
			}
		});
		
		TextField txtSearch = new TextField();
		txtSearch.setPromptText("Search Movies");
		txtSearch.setMinWidth(250);
		txtSearch.setMinHeight(40);
		txtSearch.setFont(Font.font("Courier", 16));
		txtSearch.setOnMouseClicked(e -> {
			txtSearch.setText("");
			txtSearch.setStyle("-fx-text-fill: black;");
		});
		txtSearch.setOnAction(e -> {
			if(!searchMovies(txtSearch.getText())){
				txtSearch.setText("Movie not found!");
				txtSearch.setStyle("-fx-text-fill: red;");
			}
		});
		
		txtSearch.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER){
				if(!searchMovies(txtSearch.getText())){
					txtSearch.setText("Movie not found!");
					txtSearch.setStyle("-fx-text-fill: red;");
				}
			}
		});
		
		btnSearch = new WaveButton();
		btnSearch.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		ImageView imgSearch = new ImageView("Images/BaseLayout/search.png");
		imgSearch.setFitWidth(30);
		imgSearch.setFitHeight(30);
		btnSearch.setGraphic(imgSearch);
		btnSearch.setOnAction(e -> {
			if(!searchMovies(txtSearch.getText())){
				txtSearch.setText("Movie not found!");
				txtSearch.setStyle("-fx-text-fill: red;");
			}
		});
		
		HBox hBoxSearch = new HBox();
		hBoxSearch.getChildren().addAll(txtSearch, btnSearch);
		hBoxMenuHeader.getChildren().add(hBoxSearch);
		
		BorderPane paneBottomHeader = new BorderPane();
		paneBottomHeader.setPadding(new Insets(10, 0, 0, 0));
		paneBottomHeader.setLeft(hBoxMenuHeader);
		paneBottomHeader.setRight(hBoxSearch);
		
		//All header elements added to header
		BorderPane paneHeader = new BorderPane();
		paneHeader.setPadding(new Insets(15, 15, 15, 15));
		paneHeader.setStyle("-fx-background-color: linear-gradient(to bottom right, rgba(0, 0, 128, 0.65), rgba(25, 25, 112, 0.65));");
		paneHeader.setLeft(btnLogo);
		paneHeader.setRight(vBoxRightHeader);
		paneHeader.setBottom(paneBottomHeader);
		
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
	
	//Login Pane
	public void loginPrompt(){
		//Labels for login
		Label labUsername = new Label("Username: ");
		labUsername.setFont(Font.font("Courier", 16));
		labUsername.setTextFill(Color.ALICEBLUE);
		Label labPassword = new Label("Password: ");
		labPassword.setFont(Font.font("Courier", 16));
		labPassword.setTextFill(Color.ALICEBLUE);
		
		//Input fields for login
		TextField txtUsername = new TextField();
		txtUsername.setFont(Font.font("Courier", 12));
		txtUsername.setPrefWidth(vBoxRightHeader.getWidth());
		txtUsername.setOnMouseClicked(e -> labError.setText(""));
		PasswordField password = new PasswordField();
		password.setFont(Font.font(12));
		password.setOnMouseClicked(e -> labError.setText(""));
		password.setOnAction(e -> login(txtUsername.getText(), password.getText()));
		
		//Button for login
		WaveButton btnLogin = new WaveButton("Login");
		btnLogin.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
		btnLogin.setFont(Font.font("Courier", FontWeight.BOLD, 16));
		btnLogin.setOnAction(e -> login(txtUsername.getText(), password.getText()));
		
		//Label for login error
		labError = new Label();
		labError.setWrapText(true);
		labError.setTextFill(Color.MISTYROSE);
		labError.setFont(Font.font("Courier", 16));
		
		
		//VBox for holding login button and login error label
		HBox hboxLoginHolder = new HBox();
		hboxLoginHolder.setSpacing(10);
		hboxLoginHolder.getChildren().addAll(labError, btnLogin);
		hboxLoginHolder.setAlignment(Pos.TOP_RIGHT);
		
		//Add all the controls to grid pane
		GridPane paneLogin = new GridPane();
		paneLogin.setPadding(new Insets(5, 5, 5, 5));
		paneLogin.setVgap(5);
		paneLogin.addColumn(0, labUsername, txtUsername, labPassword, password, hboxLoginHolder);
		paneLogin.setPickOnBounds(false);
		paneLogin.toFront();
		paneLogin.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35)");
		
		vBoxRightHeader.getChildren().add(paneLogin);
	}
	
	//Linking other tabs with menu buttons
	public void setTabOnButtonClick(WaveButton btnMenu, int index){
		if (index == 0)
			btnMenu.setOnAction(e -> paneBaseLayout.setCenter(new HomeTab()));
		else if (index == 1)
			btnMenu.setOnAction(e -> paneBaseLayout.setCenter(new MovieTab()));
		else if (index == 2)
			btnMenu.setOnAction(e -> paneBaseLayout.setCenter(new CinemaTab()));
		else if (index == 3)
			btnMenu.setOnAction(e -> paneBaseLayout.setCenter(new PromotionTab()));
		else if (index == 5)
			btnMenu.setOnAction(e -> paneBaseLayout.setCenter(new ContactUsTab()));
		else if (index == 6)
			btnMenu.setOnAction(e -> paneBaseLayout.setCenter(new FAQTab()));
	}
	
	//Logging into customer account
	private void login(String username, String password){
		Customer customer = new Customer();
		if(customer.retrieveFileRecord(username) != null){
			if(customer.getPassword().equals(password)){
				isLoggedIn = true;
				displayUsername = customer.getUsername();
				loginAction();
				paneBaseLayout.setCenter(new HomeTab());
			}
			else
				labError.setText("Incorrect password entered!"); 
		}
		else
			labError.setText("Username does not exist!");
	}
	
	//Loading up graphical elements when logged out
	public void logoutAction(){
		vBoxRightHeader.getChildren().clear();
		isLoggedIn = false;
		HBox hBoxRightHeader = new HBox(10);
		
		btnLogin = new WaveButton("Login");
		ImageView imgLogin = new ImageView("Images/BaseLayout/login.png");
		imgLogin.setFitWidth(30);
		imgLogin.setFitHeight(30);
		btnLogin.setGraphic(imgLogin);
		btnLogin.setContentDisplay(ContentDisplay.RIGHT);
		btnLogin.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
		btnLogin.setFont(Font.font("Courier", FontWeight.BOLD, 16));
		btnLogin.setOnAction(e -> {
			if (vBoxRightHeader.getChildren().size() == 2)
				vBoxRightHeader.getChildren().remove(1);
			else
				loginPrompt();
		});
		
		btnSignUp = new WaveButton("Sign Up");
		ImageView imgSignup = new ImageView("Images/BaseLayout/signup.png");
		imgSignup.setFitWidth(30);
		imgSignup.setFitHeight(30);
		btnSignUp.setGraphic(imgSignup);
		btnSignUp.setContentDisplay(ContentDisplay.RIGHT);
		btnSignUp.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
		btnSignUp.setFont(Font.font("Courier", FontWeight.BOLD, 16));
		btnSignUp.setOnAction(e -> paneBaseLayout.setCenter(new RegistrationTab()));
		
		hBoxRightHeader.getChildren().addAll(btnLogin, btnSignUp);
		vBoxRightHeader.getChildren().add(hBoxRightHeader);
	}
	
	//Loading up graphical elements when logged in
	public void loginAction(){
		vBoxRightHeader.getChildren().clear();
		HBox hBoxRightHeader = new HBox(10);
		
		btnLogout = new WaveButton("Logout");
		ImageView imgLogin = new ImageView("Images/BaseLayout/login.png");
		imgLogin.setFitWidth(30);
		imgLogin.setFitHeight(30);
		btnLogout.setGraphic(imgLogin);
		btnLogout.setContentDisplay(ContentDisplay.RIGHT);
		btnLogout.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
		btnLogout.setFont(Font.font("Courier", FontWeight.BOLD, 16));
		btnLogout.setOnAction(e -> {
			logoutAction();
			paneBaseLayout.setCenter(new HomeTab());
		});
		
		btnUser = new WaveButton(displayUsername + " Profile");
		ImageView imgUser = new ImageView("Images/BaseLayout/user.png");
		imgUser.setFitWidth(30);
		imgUser.setFitHeight(30);
		btnUser.setGraphic(imgUser);
		btnUser.setContentDisplay(ContentDisplay.LEFT);
		btnUser.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
		btnUser.setFont(Font.font("Courier", FontWeight.BOLD, 16));
		btnUser.setOnAction(e -> paneBaseLayout.setCenter(new AccountTab()));
		
		hBoxRightHeader.getChildren().addAll(btnUser, btnLogout);
		vBoxRightHeader.getChildren().add(hBoxRightHeader);
	}
	
	public boolean searchMovies(String keyword){
		boolean movieFound = false;
		ArrayList<Movie> movieList = Movie.retrieveAllFileRecords();
		for(Movie movie: movieList){
			if(keyword.toUpperCase().equals(movie.getTitle().toUpperCase())){
				paneBaseLayout.setCenter(new MovieTab(movie));
				movieFound = true;
				break;
			}
			String[] keywordList = keyword.split(" ");
			for(String word: keywordList){
				if(movie.getTitle().toUpperCase().contains(word.toUpperCase())){
					paneBaseLayout.setCenter(new MovieTab(movie));
					movieFound = true;
					break;
				}
			}
		}
		return movieFound;
	}
	
	public static BorderPane getPaneBaseLayout() {
		return paneBaseLayout;
	}

	public WaveButton getBtnLogin() {
		return btnLogin;
	}

	public WaveButton getBtnSignUp() {
		return btnSignUp;
	}

	public WaveButton getBtnUser() {
		return btnUser;
	}

	public WaveButton[] getBtnMenus() {
		return btnMenus;
	}

	public WaveButton getBtnSearch() {
		return btnSearch;
	}
	
	public static String getDisplayUsername(){
		return displayUsername;
	}
}
