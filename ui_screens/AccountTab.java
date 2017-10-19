package ui_screens;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import larn.modernUI.WaveButton;
import real_objects.Cinema;
import real_objects.Customer;
import real_objects.Movie;
import real_objects.MovieSession;
import real_objects.Payment;
import real_objects.Ticket;

public class AccountTab extends GridPane{

		 public AccountTab(){
		 HBox top = new HBox();
		 setPadding(new Insets(10,10,10,10));
		 top.setSpacing(10);
		 setHgap(50);
		 setVgap(10);
		    
		 VBox accinfo = new VBox();
		 accinfo.setSpacing(8);
		 accinfo.setPadding(new Insets (20,20,20,20));
		 accinfo.setStyle("-fx-background-color: rgba(0, 0, 0,0.5);");
		 accinfo.setMinWidth(600);
		 add(accinfo,0,2);
		 
		    Text title = new Text("Account Info");
		    title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		    title.setFill(Color.WHITE);
		    getChildren().add(title);
		    
		    String username = BaseLayout.getDisplayUsername();
		    Customer current = new Customer();
		    current.retrieveFileRecord(username);
		    
		    Text txt[] = new Text[] {
		    		new Text("Account Details"),
		            new Text("Username	: " + username),
		            new Text("Password	: ********"),
		            new Text("First Name	: " + current.getFirstName()),
		            new Text("Last Name	: " + current.getLastName()),
		            new Text("Email		: " + current.getEmailAddress()),
		            new Text("DOB			: " + current.getDateOfBirth()),
		            new Text("Tel No		: " + current.getPhoneNumber()),
		            new Text ("Movie Token  : " + current.getMovieTokens()),
		    };
		    
		    txt[0].setFont(Font.font("Arial",FontWeight.BOLD,28));
		    txt[0].setFill(Color.WHITE);
		    
		    for (int i=1; i<9; i++) {
		    txt[i].setFont(Font.font("Arial",FontWeight.BOLD,18));
		    txt[i].setFill(Color.WHITE);
		    }
		   
		        for (int i=0; i<9; i++) {
		            setMargin(txt[i], new Insets(10, 8, 0, 8));
		            accinfo.getChildren().add(txt[i]);       
		        }
		        
	        HBox inaccinfo = new HBox(); 
		    inaccinfo.setSpacing(10);
		        
		    WaveButton modifybtn = new WaveButton ("Modify");
			WaveButton submitbtn = new WaveButton ("Submit");
			modifybtn.setStyle("-fx-background-color: teal; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
			submitbtn.setStyle("-fx-background-color: teal; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
			Text successtxt = new Text ("Account detail modification successful!");
			successtxt.setFont(Font.font("Arial",FontWeight.BOLD,18));
			successtxt.setFill(Color.YELLOWGREEN);
			submitbtn.setVisible(false);
			successtxt.setVisible(false);
				
			inaccinfo.getChildren().addAll(modifybtn,submitbtn,successtxt);
			accinfo.getChildren().add(inaccinfo);
			
			GridPane modifyinfo = new GridPane();
			modifyinfo.setPadding(new Insets (20,20,20,20));
			modifyinfo.setStyle("-fx-background-color: rgba(0, 0, 0,0.5);");
			modifyinfo.setMinWidth(600);
			modifyinfo.setVisible(false);
			add(modifyinfo,0,3);
			
			Text modifytxt[] = new Text[] {
			    	new Text(" New Password	:"),
			        new Text(" New First Name	: ") ,
			        new Text(" New Last Name	: " ),
			        new Text(" New Email		: " ),
			        new Text(" New Tel No		: " ),
			        new Text(" New DOB		: " )
			 };
			
			 for (int i=0; i<6; i++) {
				 modifytxt[i].setFont(Font.font("Arial",FontWeight.BOLD,18));
				 modifytxt[i].setFill(Color.WHITE);
			 }
				   
			 for (int i=0; i<6; i++) {
				 setMargin(modifytxt[i], new Insets(10, 8, 0, 8));
				 modifyinfo.add(modifytxt[i],0,i);       
			 }
			 
			 TextField modifytxtfield[] = new TextField[] {
					 	new PasswordField(),
			    		new TextField(),
			    		new TextField(),
			    		new TextField(),
			    		new TextField()
			    };
			
			 for (int i=0; i<5; i++) {
				    setMargin(modifytxtfield[i], new Insets(10, 8, 0, 8));
				    modifyinfo.add(modifytxtfield[i],1,i);       
			 }
			
			 DatePicker datepicker = new DatePicker();
			 modifyinfo.add(datepicker, 1, 5);
			 setMargin(datepicker, new Insets(10, 8, 0, 8));
			 		
			 		//creates a panel to display customer ticket history on the right 
					VBox acchistory = new VBox();
					acchistory.setSpacing(8);
					acchistory.setPadding(new Insets (20,20,20,20));
					acchistory.setStyle("-fx-background-color: rgba(0, 0, 0,0.5);");
					acchistory.setMinWidth(600);
					acchistory.setMinHeight(450);
					add(acchistory,1,2);
					
					//adds title to the customer ticket history
					Text titlehistory = new Text("Account Ticket History");
					titlehistory.setFont(Font.font("Arial",FontWeight.BOLD,28));
					titlehistory.setFill(Color.WHITE);
					acchistory.getChildren().add(titlehistory);
					
					//cretes a scrollpane to display list of tickets
					ScrollPane scrollticket = new ScrollPane();
					acchistory.getChildren().add(scrollticket);
					
					//retrive all ticket data in the database
					ArrayList<Ticket> ticketlist =Ticket.retrieveAllFileRecords();
					
					
					GridPane TicketShow = new GridPane();
					TicketShow.setHgap(10);
					TicketShow.setVgap(10);
					scrollticket.setContent(TicketShow);
					scrollticket.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
					
					
					// for tickets with the same username, set them to the gridpane inside scrollticket
					int i = 0;
					for ( Ticket ticketdata : ticketlist){
						if (ticketdata.getUsername().contains(username)){
							TicketShow.add(new Text("Booking ID 		:"), 0, i);
							TicketShow.add(new Text(ticketdata.getBookingID()), 1, i);
							TicketShow.add(new Text("Username 		:"), 0, i+1);
							TicketShow.add(new Text(ticketdata.getUsername()), 1, i+1);
							
							//gets the strings of seats, split them at ; and combine again where each is a new line in String finalseat
							String seats =ticketdata.getSeats();
							String[] seatlinearray = seats.split(Pattern.quote(";"));
							String finalseat="";
							
							for (String seatline :seatlinearray){
							finalseat = finalseat.concat(seatline +"\n");
							}
							
							TicketShow.add(new Text("Seats 			:"), 0, i+4);
							TicketShow.add(new Text(finalseat), 1,i+4);
							
							//gets the strings of addons, split them at ; and combine again where each is a new line in String finaladdons
							String addons =ticketdata.getAddOns();
							String[] addonslinearray = addons.split(Pattern.quote(";"));
							String finaladdons="";
							for (String addonsline :addonslinearray){
							finaladdons = finaladdons.concat(addonsline + " \n");
							}
							TicketShow.add(new Text("AddOns 			:"), 0, i+5);
							TicketShow.add(new Text(finaladdons), 1, i+5);
							
							TicketShow.add(new Text("QR Code 			:"), 0,i+6);
							TicketShow.add(new ImageView(ticketdata.getImgQRCodeAddress()), 1,i+6);
							
							//creates and stores the related classes data to the ticket to call 
							String movieSessionID = ticketdata.getMovieSessionID();
							MovieSession session = new MovieSession();
							session.retrieveFileRecord(movieSessionID);
							Movie movie = new Movie();
							movie.retrieveFileRecord(session.getMovieID());
							Cinema cinema = new Cinema();
							cinema.retrieveFileRecord(session.getCinemaID());
							Payment payment = new Payment();
							payment.retrieveFileRecord(ticketdata.getPaymentID());
							
							TicketShow.add(new Text("Movie Name :"), 0, i+2);
							TicketShow.add(new Text(movie.getTitle()), 1, i+2);
							
							TicketShow.add(new Text("Cinema :"), 0, i+3);
							TicketShow.add(new Text(cinema.getName()), 1, i+3);
							
							String totalamount = String.format("%.2f", payment.getTotalAmountPaid());
							TicketShow.add(new Text("Total Amount Paid :"), 0, i+7);
							TicketShow.add(new Text("RM " + totalamount), 1, i+7);
							
							TicketShow.add(new Text("Movie Tokens Used :"), 0, i+8); 
							TicketShow.add(new Text(payment.getMovieTokensUsed() + ""), 1, i+8);
							
							//adds space between tickets
							TicketShow.add(new Text(""), 0, i+9); 
							TicketShow.add(new Text(""), 0, i+10);
							
							i = i +12; // ensures the iteration does not create text over existing Text nodes
						}
					}				
							
						modifybtn.setOnMouseClicked(e->{
						//shows the gridpane that allows the user to modify their account details(except username)
						submitbtn.setVisible(true);
				    	modifybtn.setVisible(false);
				    	modifyinfo.setVisible(true);
				        successtxt.setVisible(false);
				    
						});
					
						submitbtn.setOnMouseClicked(e->{
						//on click, hides the gridpane and write the new info into customer object which is then used to
						//update the file Customer.dat with the new information
						
						submitbtn.setVisible(false);
				    	modifybtn.setVisible(true);
				    	modifyinfo.setVisible(false);
				    	
				    	current.setPassword(modifytxtfield[0].getText());
				    	current.setFirstName(modifytxtfield[1].getText());
				    	current.setLastName(modifytxtfield[2].getText());
				    	current.setEmailAddress(modifytxtfield[3].getText());
				    	current.setPhoneNumber(modifytxtfield[4].getText());
				    	current.setDateOfBirth(java.sql.Date.valueOf(datepicker.getValue()));
				    	
				    	//update new information to Customer.dat and set success message to true
				    	current.updateFileRecord();
				    	successtxt.setVisible(true);
				    	
				    	txt[3].setText("First Name	: " + current.getFirstName());
				    	txt[4].setText("Last Name	: " + current.getLastName());
				    	txt[5].setText("Email		: " + current.getEmailAddress());
				    	txt[6].setText("DOB			: " + current.getDateOfBirth());
				    	txt[7].setText("Tel No		: " + current.getPhoneNumber());
						});			
	}
}
