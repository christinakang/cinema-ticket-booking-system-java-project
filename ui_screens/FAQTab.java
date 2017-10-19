package ui_screens;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class FAQTab extends BorderPane{
		
		//creates a object called FAQTab that can be called to other interfaces
		public FAQTab(){
		setStyle("-fx-background-color: rgba(0, 0, 0,0.5);"); //sets background to black semi-transparent color	
		setPadding(new Insets(20,20,20,20));
		GridPane GridPane1 = new GridPane();
		setCenter(GridPane1);
		GridPane1.getColumnConstraints().add(new ColumnConstraints(30)); //overwrite predefined columnconstraints in BorderPane class
		GridPane1.setHgap(10);
		GridPane1.setVgap(10);
		GridPane1.setPadding(new Insets(20,20,20,20));
		
		GridPane GridPane2 = new GridPane();
		setRight(GridPane2);
		GridPane2.getColumnConstraints().add(new ColumnConstraints(30)); //overwrite predefined columnconstraints in BorderPane class
		GridPane2.setHgap(10);
		GridPane2.setVgap(10);
		GridPane2.setPadding(new Insets(20,20,20,20));
		
		//creates the title and text to be put into the interface
		Text title = new Text("Frequently Asked Questions (FAQ)");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		title.setFill(Color.WHITE);
		setTop(title);
		
		//declares and creates the text of questions (Q) to display in FAQTab
		Text Q[] = new Text[] {
			new Text("Q: How do I register for an account?"),
		    new Text("Q: Do I need to pay to the application?"),
		    new Text("Q: How long can i keep my membership?"),
		    new Text("Q: Am I allowed to use other people's account to purchase tickets?"),
		    new Text("Q: Are there any extra charges for purchasing tickets using this application?"),
		    new Text("Q: Are there any benefits to creating an account for this Cinema?"),
	        new Text("Q: Is there a way to transfer points from another account to my account?"),
		    new Text("Q: How do i check my account details or my current points?"),
		    new Text("Q: How can i check for my previous ticket purchase information? "),
		    new Text("Q: What if I have more questions than the listed FAQ?")
		};
		
		// sets font type and color for each text of Q
		for (int i=0; i<10; i++) {
			Q[i].setFont(Font.font("Arial",FontWeight.BOLD,18));
			Q[i].setFill(Color.WHITE);
		}
		
		//declares and creates buttons for each text to display answer    
		Button btnAns[] = new Button[] {
		    new Button(),
		    new Button(),
		    new Button(),
		    new Button(),
		    new Button(),
		   	new Button(),
		   	new Button(),
		   	new Button(),
		   	new Button(),
		   	new Button(),
		};
		
		for(Button button: btnAns){
			button.setOnMouseEntered(e -> setCursor(Cursor.HAND));
			button.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		}
		
		//declares an ImageView for each respective buttons using the same image show.png 
		ImageView show0 = new ImageView("Images/Icons/show.png");
		ImageView show1 = new ImageView("Images/Icons/show.png");
		ImageView show2 = new ImageView("Images/Icons/show.png");
		ImageView show3 = new ImageView("Images/Icons/show.png");
		ImageView show4 = new ImageView("Images/Icons/show.png");
		ImageView show5 = new ImageView("Images/Icons/show.png");
		ImageView show6 = new ImageView("Images/Icons/show.png");
		ImageView show7 = new ImageView("Images/Icons/show.png");
		ImageView show8 = new ImageView("Images/Icons/show.png");
		ImageView show9 = new ImageView("Images/Icons/show.png");
		   
		//re-adjust all button image size
		show0.setFitHeight(10);
		show0.setFitWidth(10);
		show1.setFitHeight(10);
		show1.setFitWidth(10);
		show2.setFitHeight(10);
		show2.setFitWidth(10);
		show3.setFitHeight(10);
		show3.setFitWidth(10);
		show4.setFitHeight(10);
		show4.setFitWidth(10);
		show5.setFitHeight(10);
		show5.setFitWidth(10);
		show6.setFitHeight(10);
		show6.setFitWidth(10);
		show7.setFitHeight(10);
		show7.setFitWidth(10);
		show8.setFitHeight(10);
		show8.setFitWidth(10);
		show9.setFitHeight(10);
		show9.setFitWidth(10);
		    
		//set imageview to their respective buttons 
		btnAns[0].setGraphic(show0);
		btnAns[1].setGraphic(show1);
		btnAns[2].setGraphic(show2);
		btnAns[3].setGraphic(show3);
		btnAns[4].setGraphic(show4);
		btnAns[5].setGraphic(show5);
		btnAns[6].setGraphic(show6);
		btnAns[7].setGraphic(show7);
		btnAns[8].setGraphic(show8);
		btnAns[9].setGraphic(show9);
		
		//declares and creates the text of answers (A) to display in FAQTab	
		Text A[] = new Text[] {
		    new Text("A: You can register by clicking on the sign up button."),
		    new Text("A: No, you don't have to pay for using the application.\n    You only pay for tickets and membership upgrades."),
		    new Text("A: The membership is a permenant and free for life. However, you're required to \n     login monthly to maintain your account in our database."),
		    new Text("A: Yes, you may use other people's accounts to buy tickets,\n     provided you have the permission to do so."),
		    new Text("A: No, extra charges will not be applied when using this application."),
		    new Text("A: Yes, by creating an account you're able to receive points that can be redeemed\n     for free tickets."),
		    new Text("A: We do not support the transfer of points from another account in order to avoid\n     fraud and scams."),
		    new Text("A: You can check these related information under the Account page."),
		    new Text("A: All ticket purchasing history can be found in the Account page\n     under the ticket history section."),
		    new Text("A: You can always drop off a question for us at the ContactUs page.")
		};
		
		//sets the font and color of texts of A and make it invisible
	    for (int i=0; i<10; i++) {
	    	A[i].setFont(Font.font("Arial",18));
			A[i].setFill(Color.WHITE);
			A[i].setVisible(false);
		}
	    
	    //adds button , question and answer from 0 - 4 into GridPane1 in the center of BorderPane
		for (int i=0,j=0; i<5; i++) {
		    
		    GridPane1.add(btnAns[i],0,j);        
		    GridPane1.add(Q[i],1,j);       
		    GridPane1.add(A[i],0,j+1);
		    j+=2;   
		}
		
		//adds button , question and answer from 5 - 9 into GridPane2 in the right of BorderPane 
		for (int i=5,j=0; i<10; i++) {
		    
		    GridPane2.add(btnAns[i],0,j);        
		    GridPane2.add(Q[i],1,j);       
		    GridPane2.add(A[i],0,j+1);
		    j+=2;   
		}

		//Set all button functionality to redisplay and undisplay text when the button with image show.png is clicked
		btnAns[0].setOnMouseClicked(e->{
			if (A[0].isVisible() == false){
	    		A[0].setVisible(true);	
	        }
	        else {
	        	A[0].setVisible(false);
	        }
	    });
	    	
		btnAns[1].setOnMouseClicked(e->{
	    	if (A[1].isVisible() == false){
	    		A[1].setVisible(true);	
	        }
	        else {
	        	A[1].setVisible(false);
	        }
		});
	    	
	    btnAns[2].setOnMouseClicked(e->{
	    	if (A[2].isVisible() == false){
	    		A[2].setVisible(true);	
	        }
	        else {
	        	A[2].setVisible(false);
	        }
		});
	    	
	    btnAns[3].setOnMouseClicked(e->{
	    	if (A[3].isVisible() == false){
	    		A[3].setVisible(true);	
	        }
	        else {
	        	A[3].setVisible(false);
	        }
		});
	    	
	    btnAns[4].setOnMouseClicked(e->{
	    	if (A[4].isVisible() == false){
	    		A[4].setVisible(true);	
	        }
	        else {
	        	A[4].setVisible(false);
	        }
		});
	    	
	    btnAns[5].setOnMouseClicked(e->{
	    	if (A[5].isVisible() == false){
	    		A[5].setVisible(true);	
	        }
	        else {
	        	A[5].setVisible(false);
	        }
		});
	    	
	    btnAns[6].setOnMouseClicked(e->{
	    	if (A[6].isVisible() == false){
	    		A[6].setVisible(true);	
	        }
	        else {
	        	A[6].setVisible(false);
	        }
		});
	    
	    btnAns[7].setOnMouseClicked(e->{
	    	if (A[7].isVisible() == false){
	    		A[7].setVisible(true);	
	        }
	        else {
	        	A[7].setVisible(false);
	        }
		});
	    	
	    btnAns[8].setOnMouseClicked(e->{
	    	if (A[8].isVisible() == false){
	    		A[8].setVisible(true);	
	        }
	        else {
	        	A[8].setVisible(false);
	        }
		});
	    	
	    btnAns[9].setOnMouseClicked(e->{
	    	if (A[9].isVisible() == false){
	    		A[9].setVisible(true);	
	        }
	        else {
	        	A[9].setVisible(false);
	        }
		});	
	} //end public FAQTab object
}//end class
