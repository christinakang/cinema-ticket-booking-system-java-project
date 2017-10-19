package admin_Screens;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import larn.modernUI.WaveButton;
import real_objects.Admin;

public class AdminLoginTab extends BorderPane{
	private ImageView imgviewlogo = new ImageView();
	private Admin admin = new Admin();
	private String usernameTmp;
	private BorderPane backPane = new BorderPane();
	private Label lbl_error = new Label();
	private GridPane gridPane = new GridPane();
	
	public AdminLoginTab(){
	     setStyle("-fx-background-image: url(\"Images/BaseLayout/background.jpg\")");
	     VBox vlogin = loginPage();
	     backPane.setCenter(vlogin);
	     setCenter(backPane);
	     setAlignment(backPane,Pos.CENTER);    
	 }
 
	 public VBox loginPage(){		
		     VBox pane = new VBox(10);
			//Add picture to the VBox layout 
		     try{
		    	 imgviewlogo = new ImageView("Images/BaseLayout/logo.png");
		    	 imgviewlogo.setFitHeight(150);
		    	 imgviewlogo.setFitWidth(300); 
		     }
		     catch(Exception ex){
		    	 System.out.println("Something wrong");
		     }		  		     
			 pane.getChildren().add(imgviewlogo);
			 		 
			 //DropShadow effect
		     DropShadow dropShadow = new DropShadow();
		     dropShadow.setOffsetX(5);
		     dropShadow.setOffsetY(5);
		     //Adding text and DropShadow effect to it
		     Text text = new Text("Welcome to RKC cinema");
		     text.setFont(Font.font("Courier New", FontWeight.BOLD, 40));
		     text.setFill(Color.AZURE);
		     text.setEffect(dropShadow);
		     //Adding text to vbox at back pane 
		     pane.getChildren().add(text);
	
		    //Implementing Nodes for GridPane
		     Label lblUserName = new Label("Username");
		     lblUserName.setStyle("-fx-font-family: Courier; -fx-font-size: 30px; -fx-font-weight: bold;");
		     lblUserName.setTextFill(Color.AZURE);
		     TextField txtUserName = new TextField();
		     txtUserName.setStyle("-fx-background-color:beige");
		     Label lblPassword = new Label("Password");
		     lblPassword.setStyle("-fx-font-family: Courier; -fx-font-size: 30px; -fx-font-weight: bold;");
		     lblPassword.setTextFill(Color.AZURE);
		     PasswordField pf = new PasswordField();
		     pf.setStyle("-fx-background-color:beige");	    
		     	     
		     //Adding GridPane for the log in details	    
		     gridPane.setPadding(new Insets(8,8,8,8));
		     gridPane.setHgap(10);
		     gridPane.setVgap(15);
		     gridPane.setStyle("-fx-background-color:rgb(112,128,144,0.5);-fx-background-radius: 25px");
		     
		      //Adding Nodes to GridPane layout
		     WaveButton btnLogin = new WaveButton("Login");
		     btnLogin.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;-fx-border-radius:10px");
		     btnLogin.setFont(Font.font("Courier", FontWeight.BOLD, 28));
		     btnLogin.setOnAction(e->{
		    	 lbl_error.setText("");
		    	 login(txtUserName.getText(),pf.getText());
		     });
		     Label lblForgetPw = new Label("Forget Password?");
		     lblForgetPw.setStyle("-fx-font-family: Courier; -fx-font-size: 30px; -fx-font-weight: bold;");
		     lblForgetPw.setUnderline(true);
		     lblForgetPw.setTextFill(Color.WHITESMOKE);
		     lblForgetPw.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		     lblForgetPw.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		     		     
		     GridPane.setHgrow(lbl_error, Priority.ALWAYS);
		     gridPane.add(lblUserName, 0, 0);
		     gridPane.add(txtUserName, 1, 0);
		     gridPane.add(lblPassword, 0, 1);
		     gridPane.add(pf, 1, 1);
		     GridPane.setHalignment(btnLogin, HPos.RIGHT);
		     gridPane.add(lblForgetPw, 0, 2);
		     GridPane.setHalignment(lblForgetPw, HPos.LEFT);
		     gridPane.add(btnLogin, 1, 2);
		     gridPane.setAlignment(Pos.CENTER);
		     gridPane.setMaxWidth(500);
		     gridPane.setMaxHeight(200);
		     
		     lblForgetPw.setOnMouseClicked(e->{
		    	 gridPane.getChildren().clear();
		    	 VBox tmp = forgetPw();
		    	 gridPane.add(tmp, 0, 0);
		    	 lbl_error.setText("");
		     });
		     pane.getChildren().add(lbl_error);
		     pane.getChildren().add(gridPane);		     	     
		     pane.setAlignment(Pos.CENTER);	
         return pane;
	 }
 public void login(String username, String password){ 
	
	 if(admin.retrieveFileRecord(username)!= null & admin.getPassword().equals(password)){
		 admin.retrieveFileRecord(username);
		 AdminMainApplication.setScene();
	 }
	 else
	 {
		 lbl_error.setText("Wrong username and password");
		 lbl_error.setTextFill(Color.ORANGERED);
		 lbl_error.setStyle("-fx-font-family: Courier; -fx-font-size: 20px; -fx-font-weight: bold;");
		 lbl_error.setAlignment(Pos.CENTER);
	 } 	
 }
 
 public VBox forgetPw(){
		 VBox vbResetPw = new VBox(20);

		 Label lbl_title = new Label("Reset your password");
		 DropShadow dropShadow = new DropShadow();
	     dropShadow.setOffsetX(5);
	     dropShadow.setOffsetY(5);
	     lbl_title.setStyle("-fx-font-family: Courier; -fx-font-size: 35px; -fx-font-weight: bold;");
	     lbl_title.setTextFill(Color.ALICEBLUE);
	     lbl_title.setEffect(dropShadow);
	     vbResetPw.getChildren().add(lbl_title);
	     	
	     GridPane userInfo = new GridPane();
	     userInfo.setHgap(10);
	     userInfo.setVgap(10);
	     
	     Label lbl_username= new Label("Username");
	   	 lbl_username.setTextFill(Color.ALICEBLUE);
		 lbl_username.setStyle("-fx-font-family: Courier; -fx-font-size: 25px; -fx-font-weight: bold;");
		 userInfo.add(lbl_username, 0, 0);
			
		 TextField tf_username = new TextField();
		 userInfo.add(tf_username, 1, 0);
	  	 tf_username.setOnKeyReleased(e->{
			usernameTmp = tf_username.getText();
		 });
		
		Label lbl_pw = new Label("New Password");
		lbl_pw.setTextFill(Color.ALICEBLUE);
		lbl_pw.setStyle("-fx-font-family: Courier; -fx-font-size: 25px; -fx-font-weight: bold;");
		userInfo.add(lbl_pw, 0, 1);
		
		PasswordField pw1 = new PasswordField();
		userInfo.add(pw1, 1,1 );
		
		Label lbl_error = new Label();
		lbl_error.setStyle("-fx-font-family: Courier; -fx-font-size: 25px; -fx-font-weight: bold;");
		Label lbl_pw2 = new Label("Confirm");
		lbl_pw2.setTextFill(Color.ALICEBLUE);
		lbl_pw2.setStyle("-fx-font-family: Courier; -fx-font-size: 25px; -fx-font-weight: bold;");
		userInfo.add(lbl_pw2, 0, 2);
		PasswordField pw2 = new PasswordField();
		userInfo.add(pw2, 1, 2);
		pw2.setOnKeyReleased(e->{
			if(pw1.getText().equals(pw2.getText())){
				lbl_error.setText("Match");
				lbl_error.setTextFill(Color.ALICEBLUE);
			}
			
			else{
				lbl_error.setText("Not Match");
				lbl_error.setTextFill(Color.ALICEBLUE);
			}
		});
		userInfo.add(lbl_error, 2, 2);
		
		
		
		Label lbl_error2 = new Label();
		lbl_error2.setStyle("-fx-font-family: Courier; -fx-font-size: 25px; -fx-font-weight: bold;");
		lbl_error2.setTextFill(Color.ALICEBLUE);
		
		
		//Button for cancel and submit 
		WaveButton btn_back = new WaveButton("Back");
		btn_back.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
		btn_back.setFont(Font.font("Courier", FontWeight.BOLD, 26));
		userInfo.add(btn_back, 0, 3);
		GridPane.setHalignment(btn_back, HPos.RIGHT);
		
		btn_back.setOnAction(e->{
			lbl_error.setText("");
			vbResetPw.getChildren().clear();
			backPane.getChildren().clear();
			getChildren().clear(); 
			VBox vlogin = loginPage();
		    backPane.setCenter(vlogin);
		    setCenter(backPane);
		});
		
		WaveButton btn_submit = new WaveButton("Submit");
		btn_submit.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
		btn_submit.setFont(Font.font("Courier", FontWeight.BOLD, 26));
		userInfo.add(btn_submit, 1, 3);
		btn_submit.setOnAction(e->{
			lbl_error.setText("");
			try{
				if(admin.retrieveFileRecord(usernameTmp)!=null){
						if(pw1.getText().equals(pw2.getText())){
							admin.setPassword(pw1.getText());
								if(admin.updateFileRecord()==true){
									lbl_error2.setText("Successful Update password");
								}						
						}
						else{
							lbl_error2.setText("Password not match");
						}
				}
				else{
					lbl_error2.setText("Not exsiting user, please try again");
				}
			}
			catch(NullPointerException ex){
				lbl_error2.setText("Not exsiting user, please try again");
			}		
		});	
		
		vbResetPw.getChildren().add(userInfo);
		vbResetPw.getChildren().add(lbl_error2);
	
		return vbResetPw;
 	}
}
