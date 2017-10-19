package ui_screens;

import java.io.File;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import larn.modernUI.WaveButton;
import real_objects.Cinema;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CinemaTab extends BorderPane {
	
	private static ArrayList<Cinema> arrcinema = new ArrayList<>();
	private GridPane gpCD = new GridPane();//gpCD : grid pane cinema details
	private VBox vbBackPane = new VBox(10);
	
	public CinemaTab(){
		//retrive all the object from the database 
		arrcinema = Cinema.retrieveAllFileRecords();
		VBox vbox_cinema = cinemalist();
		vbBackPane.getChildren().add(vbox_cinema);		
		setCenter(vbBackPane);
	}
    	
	public VBox cinemalist() {	
		arrcinema = Cinema.retrieveAllFileRecords();
		VBox vbox_cinema = new VBox(10);
		GridPane gdPane = new GridPane();
		gdPane.setHgap(30);
		gdPane.setVgap(20);
		
		vbox_cinema.setPadding(new Insets(10,10,10,10));
		
		Label lbl_cinema = new Label("Cinemas");
		lbl_cinema.setTextFill(Color.ALICEBLUE);
		lbl_cinema.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
		DropShadow dropShadow = new DropShadow();
		dropShadow.setOffsetX(5);
	    dropShadow.setOffsetY(5);
	    lbl_cinema.setEffect(dropShadow);
	    
		vbox_cinema.getChildren().add(lbl_cinema);
		lbl_cinema.setAlignment(Pos.TOP_LEFT);	
		
		ArrayList<Label> lbl_name = new ArrayList<>();
		ArrayList<Text> txt_add = new ArrayList<>();
		ArrayList<ImageView> cinemaImg = new ArrayList<>();
		for(int i = 0; i <arrcinema.size(); i++ ){	
		    //set image for the cinema 
			try{
		    	File file = new File("bin/" + arrcinema.get(i).getImgAddress());		    	
		    	ImageView imgTmp = new ImageView(file.toURI().toString());
		    	cinemaImg.add(imgTmp);
		    	cinemaImg.get(i).setFitHeight(150);
		    	cinemaImg.get(i).setFitWidth(200);
		    }
		    catch (Exception ex){
		    	System.out.println("Something wrong");
		    }	
			//set label for the cinema 
		    Label lbl_name_tmp = new Label(arrcinema.get(i).getName());
			lbl_name.add(lbl_name_tmp);
			lbl_name.get(i).setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
			lbl_name.get(i).setTextFill(Color.ALICEBLUE);
			
			//set address for the cinema 
			Text txt_tmp = new Text(arrcinema.get(i).getAddress());
			txt_add.add(txt_tmp);
			txt_add.get(i).setWrappingWidth(200);
			txt_add.get(i).setFont(Font.font(15));
			txt_add.get(i).setFill(Color.ALICEBLUE);

			//add elements to the gridpane 
			gdPane.add(cinemaImg.get(i),i,0);
			gdPane.add(lbl_name.get(i), i, 1);
			gdPane.add(txt_add.get(i), i, 2);
			
		}
		
		vbox_cinema.getChildren().add(gdPane);
		vbox_cinema.setPadding(new Insets(20,20,20,20));
		
		ArrayList<WaveButton> btn_moreInfo = new ArrayList<>();
		//set the button to the pane  
		for(int i = 0; i< arrcinema.size(); i++){		    
				WaveButton btn_tmp = new WaveButton("More Info");
				btn_moreInfo.add(btn_tmp);
				btn_moreInfo.get(i).setStyle("-fx-background-color:lightseagreen;-fx-font-size: 20px;-fx-background-radius: 10px;");
				btn_moreInfo.get(i).setTextFill(Color.WHITE);	
				gdPane.add(btn_moreInfo.get(i), i, 3);
		  }	
		 //set actions for the button  
		  for(int i = 0; i< arrcinema.size(); i++){
				Cinema cinemashow = arrcinema.get(i);
			  btn_moreInfo.get(i).setOnAction(e->{
				getChildren().clear();
				gpCD = cinemaDetails(cinemashow);
				vbBackPane.getChildren().clear();
				vbBackPane.getChildren().addAll(gpCD,vbox_cinema);
				setCenter(vbBackPane);
			});
		  }
		return vbox_cinema;
	}
	
	public GridPane cinemaDetails(Cinema cinemaTmp){
		gpCD.getChildren().clear();
		gpCD.setPadding(new Insets(10,10,10,10));
		gpCD.setHgap(10);
		gpCD.setVgap(10);
		
		Label lbl_name = new Label(cinemaTmp.getName());
		lbl_name.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
		lbl_name.setTextFill(Color.ALICEBLUE);
		gpCD.add(lbl_name, 0, 0);
		
		File file = new File("bin/" + cinemaTmp.getImgAddress());		    	
    	//System.out.println(cinema.get(i).getImgAddress()+"\n"+num_of_cinema);
    	ImageView imgTmp = new ImageView(file.toURI().toString());
    	imgTmp.setFitHeight(250);
    	imgTmp.setFitWidth(400);
    	gpCD.add(imgTmp, 0, 1);
				
		BorderPane address = new BorderPane();
		Text txt_tmp = new Text(cinemaTmp.getAddress());
		txt_tmp.setWrappingWidth(300);
		txt_tmp.setFont(Font.font(25));
		txt_tmp.setFill(Color.BLACK);
		address.setCenter(txt_tmp);
		address.setStyle("-fx-background-color:rgb(211,211,211,0.8);-fx-background-radius: 25px");
		gpCD.add(address, 1, 1);
		
		//vbox for number of halls 
		VBox vb1 = new VBox(5);
		Label lbl_numHall = new Label("No. Halls");
		lbl_numHall.setFont(Font.font("Helvetica", 30));
		lbl_numHall.setTextFill(Color.BLACK);
		lbl_numHall.setAlignment(Pos.CENTER);
		
		Label lbl_hall = new Label(Integer.toString(cinemaTmp.getNumberOfHalls()));
		lbl_hall.setFont(Font.font("Helvetica",FontWeight.BOLD,30));
		lbl_hall.setTextFill(Color.BLACK);
		lbl_hall.setAlignment(Pos.CENTER);
		
		vb1.getChildren().addAll(lbl_numHall,lbl_hall);
		vb1.setStyle("-fx-background-color:rgb(211,211,211,0.8);-fx-background-radius: 25px");
		vb1.setAlignment(Pos.CENTER);
		gpCD.add(vb1, 0, 2);
		
		//vbox for number of seates 
		VBox vb2 = new VBox(5);
		Label lbl_numSeats = new Label("No. Seats");
		lbl_numSeats.setFont(Font.font("Helvetica", 30));
		lbl_numSeats.setTextFill(Color.BLACK);
		lbl_numSeats.setAlignment(Pos.CENTER);
			
		Label lbl_seat = new Label(Integer.toString(cinemaTmp.getSeatingCapacity()));
		lbl_seat.setFont(Font.font("Helvetica",FontWeight.BOLD,30));
		lbl_seat.setTextFill(Color.BLACK);
		lbl_seat.setAlignment(Pos.CENTER);
			
		vb2.getChildren().addAll(lbl_numSeats,lbl_seat);
		vb2.setStyle("-fx-background-color:rgb(211,211,211,0.8);-fx-background-radius: 25px");
		vb2.setAlignment(Pos.CENTER);
		gpCD.add(vb2, 1, 2);
		
		return gpCD;
	}
}
	
