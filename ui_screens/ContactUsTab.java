package ui_screens;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import larn.modernUI.WaveButton;

public class ContactUsTab extends GridPane{

	public ContactUsTab(){
		//creates and adds title to the gridpane
		setPadding(new Insets(10));
		setStyle("-fx-background-color: rgba(0, 0, 0,0.5);");
		setMaxWidth(700);
	 
	    Text title = new Text("Contact Details");
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
	    title.setUnderline(true);
	    title.setFill(Color.WHITE);
	    
	    ImageView imgMap = new ImageView("Images/Icons/map.jpg");
		imgMap.setFitWidth(300);
		imgMap.setFitHeight(200);
	    add(title,0,0);
	    add(imgMap,1,1);
	    
	    Text txt[] = new Text[] {
	            new Text("Tel		:	019 -9125711"),
	            new Text("Fax		:	03-4127 3827"),
	            new Text("Email	:	admin@kapiCinema.com"),
	            new Text("Address	:	No 20,Jalan Nanas,93250,Petaling Jaya, Selangor."),
	            new Text("Have an enquiry? Fill in the form below:"),
	            new Text("Name	:"),
	            new Text("Tel		:"),
	            new Text("Email	:"),
	            new Text("Enquiry	:"),
	    };
	    
	    for (int i=0; i<9; i++) {
	    txt[i].setFont(Font.font("Arial",FontWeight.BOLD,18));
	    txt[i].setFill(Color.WHITE);
	    }
	    
	    TextField txtfield[] = new TextField[] {
	            new TextField(),
	            new TextField(),
	            new TextField(), 
	    };

	    VBox info = new VBox();
	    info.setSpacing(40);
	        for (int i=0; i<4; i++) {
	            setMargin(txt[i], new Insets(10, 20, 20, 8));
	            info.getChildren().add(txt[i]);
	        }
	        add(info,0,1);
	        // create a space between the contact us and form info
	        setMargin(txt[3], new Insets(10, 8, 50, 8));
	        setMargin(imgMap, new Insets (10,8, 50, 8));
	        //adds text before the contact form
            add(txt[4],0,2);
            
            //creates a flowpane for the contact form at the bottom of the contact us page
            FlowPane form = new FlowPane();
            add(form,0,3);
            form.setStyle("-fx-background-color: rgba(0, 0, 0,0.5);");
            setMargin(form, new Insets (15,15, 15, 15));
            form.setPadding(new Insets(20,20,20,20));
            form.setVgap(8);
            form.setHgap(8);
            form.setMaxWidth(200);
            
            for (int i=5,j=0; i<8; i++) {
	            setMargin(txt[i], new Insets(20, 8, 8, 8));
	            setMargin(txtfield[j], new Insets(10, 8, 8, 8));
	            txtfield[j].setMinWidth(300);
	            form.getChildren().add(txt[i]);
	            form.getChildren().add(txtfield[j]);
	            j++;
	        }
            
            
            TextArea txtarea= new TextArea(); 
            form.getChildren().add(txt[8]);
            form.getChildren().add(txtarea);   
            txtarea.setMinSize(300, 200);
	        setMargin(txtarea,new Insets(10,8,50,8));
	    WaveButton submitbtn = new WaveButton("Submit");
	    submitbtn.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white; -fx-font-size: 20;");
	    
	    setMargin(submitbtn, new Insets(10, 20, 20, 8));
	    form.getChildren().add(submitbtn); 
	    
	    Text txtconfirm = new Text(" Message sent successfully!");
	    txtconfirm.setFont(Font.font("Arial",18));
	    txtconfirm.setFill(Color.YELLOWGREEN);
	    txtconfirm.setVisible(false);
	    form.getChildren().add(txtconfirm);
	     
	    submitbtn.setOnMouseClicked(e->{
	    	if (txtfield[2].getText().contains("@") && txtfield[2].getText().contains(".com")){
	    		 // javamail code from javapoint (Mailer class) which allows user to send gmail using 
			    // a gmail account (self to self email) with all the inputted details 	
			    Mailer.send("rhotestjava@gmail.com","bakadesu","rhotestjava@gmail.com","New Customer Enquiry!",
			    		"Name : " + txtfield[0].getText() + "\nTel: " + txtfield[1].getText() + "\nEmail: "
			    				+ txtfield[2].getText() + "\nEnquiry : " + txtarea.getText());
			    txtconfirm.setText("Message sent succesfully!");
	    		txtconfirm.setFill(Color.YELLOWGREEN);
			    txtconfirm.setVisible(true);
			    
			    Timeline timeOpenHomeTab = new Timeline(new KeyFrame(Duration.millis(2000), event -> BaseLayout.getPaneBaseLayout().setCenter(new HomeTab())));
			    timeOpenHomeTab.setCycleCount(1);
			    timeOpenHomeTab.play();
	    	}
	    	else {
	    		// change txtconfirm's text and show that the email is in an invalid format 
	    		txtconfirm.setText("Invalid email, please change it");
	    		txtconfirm.setFill(Color.RED);
			    txtconfirm.setVisible(true);
	    	}
		  
	    });
	}
}

