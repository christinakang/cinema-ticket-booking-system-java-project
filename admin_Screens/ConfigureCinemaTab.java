package admin_Screens;

import java.io.File;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import larn.modernUI.WaveButton;
import real_objects.Cinema;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ConfigureCinemaTab extends BorderPane {
	
	private static ArrayList<Cinema> arrcinema = new ArrayList<>();
	private VBox vbBackPane = new VBox(10);	

	public ConfigureCinemaTab(){
		//retrieve all the object from the database 
		arrcinema = Cinema.retrieveAllFileRecords();	
		vbBackPane.getChildren().add(cinemalist());
		setCenter(vbBackPane);
}
	
	public VBox cinemalist() {
		arrcinema = Cinema.retrieveAllFileRecords();
		VBox vbox_cinema = new VBox(10);
		vbox_cinema.setPadding(new Insets(10,10,10,10));
		
		FlowPane fpcinemalist = new FlowPane();
		fpcinemalist.setHgap(30);
		fpcinemalist.setVgap(20);	
		
		ArrayList<Label> lbl_name = new ArrayList<>();
	    ArrayList<Text> txt_add = new ArrayList<>();
		ArrayList<WaveButton> btn_moreInfo = new ArrayList<>();
		ArrayList<ImageView> cinemaImg = new ArrayList<>();
		
		Label lbl_cinema = new Label("Cinemas");
		lbl_cinema.setTextFill(Color.ALICEBLUE);
		lbl_cinema.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
		
		vbox_cinema.getChildren().add(lbl_cinema);
		lbl_cinema.setAlignment(Pos.TOP_LEFT);	
	
		for(int i = 0; i <arrcinema.size(); i++ ){	
		    //set image for the cinema 
			try{
		    	File file = new File("bin/" + arrcinema.get(i).getImgAddress());		    	
		    	//System.out.println(cinema.get(i).getImgAddress()+"\n"+num_of_cinema);
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
			
			//set button for modify
			WaveButton btn_tmp = new WaveButton("Modify");			
			btn_moreInfo.add(btn_tmp);
			btn_moreInfo.get(i).setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
			btn_moreInfo.get(i).setFont(Font.font("Courier", FontWeight.BOLD, 16));
		
			//add elements to the gridpane 
			GridPane gdPane = new GridPane();
			gdPane.setHgap(10);
			gdPane.setVgap(10);
			gdPane.setPadding(new Insets(10,10,10,10));
			gdPane.add(cinemaImg.get(i),i,0);
			gdPane.add(lbl_name.get(i), i, 1);
			gdPane.add(txt_add.get(i), i, 2);
			gdPane.add(btn_moreInfo.get(i), i, 3);
			
			fpcinemalist.getChildren().add(gdPane);
		}
		
		  for(int i = 0; i< arrcinema.size(); i++){
			  final Cinema cinemaTmp = arrcinema.get(i);
			//set action for the button 
			btn_moreInfo.get(i).setOnAction(e->{
				vbox_cinema.getChildren().clear();
				modifyCinema(cinemaTmp);
			});
		  }
		vbox_cinema.getChildren().add(fpcinemalist);
		vbox_cinema.setPadding(new Insets(20,20,20,20));
	    
		ImageView plus = new ImageView("Images/Icons/plus.png");
		plus.setFitHeight(220);
		plus.setFitWidth(150);
		plus.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		plus.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		vbox_cinema.getChildren().add(plus);
	    
		plus.setOnMouseClicked(e->{
			vbox_cinema.getChildren().clear();
	        addNewCinema();
		});
		
		return vbox_cinema;
	}
	
	
	public void addNewCinema(){
		Cinema newCinema = new Cinema();
		
		GridPane addCinema = new GridPane();
		addCinema.setPadding(new Insets(20,20,20,20));
		addCinema.setStyle("-fx-background-color: rgba(26, 43, 60);");
		addCinema.setVgap(20);
		addCinema.setHgap(25);
		addCinema.setMaxWidth(400);
		
		Label lbl_addCinema = new Label("Add Cinema");
		DropShadow dropShadow = new DropShadow();
	     dropShadow.setOffsetX(5);
	     dropShadow.setOffsetY(5);
	     lbl_addCinema.setStyle("-fx-font-family: Courier; -fx-font-size: 20px; -fx-font-weight: bold;");
	     lbl_addCinema.setTextFill(Color.ALICEBLUE);
	     lbl_addCinema.setEffect(dropShadow);
	     addCinema.add(lbl_addCinema, 0, 0);
		
	     
	   //add lable for first column 
			ObservableList<String> tableColumns = null;
			tableColumns = newCinema.getTableColumns();
			for (int i = 0 ;i< 6 ;i++){
				Label labColumnName = new Label(tableColumns.get(i)+" : ");
				labColumnName.setStyle("-fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold;");
				labColumnName.setTextFill(Color.ALICEBLUE);
				addCinema.add(labColumnName, 0, (i+1));
			}
	   
	   //add lable for the second column 
			//set cinema id 
			newCinema.setCinemaID(Cinema.generateID());
			TextField tf_cinemaID = new TextField(newCinema.getCinemaID());
			tf_cinemaID.setEditable(false);
			addCinema.add(tf_cinemaID, 1, 1);
			
			//set cinema title 
			TextField tf_cinemaTitle = new TextField();
			addCinema.add(tf_cinemaTitle, 1, 2);
			tf_cinemaTitle.setOnKeyReleased(e->{
				newCinema.setName(tf_cinemaTitle.getText());
			});
		
			//set cinema address
			TextField tf_cinemaAdd = new TextField();
			addCinema.add(tf_cinemaAdd, 1, 3);
			tf_cinemaAdd.setOnKeyReleased(e->{
				newCinema.setAddress(tf_cinemaAdd.getText());
			});
			
			//set number of halls 
			TextField tf_numHall = new TextField();
			addCinema.add(tf_numHall, 1, 4);
				Label lbl_error = new Label();
				lbl_error.setTextFill(Color.ALICEBLUE);
				lbl_error.setFont(Font.font(10));	
			tf_numHall.setOnKeyReleased(e->{					
				if(tf_numHall.getText().matches("-?([1-9][0-9]*)?")){
					lbl_error.setText("Correct");					
				    try{
				    	newCinema.setNumberOfHalls(Integer.parseInt(tf_numHall.getText()));
				    }
				    catch(NumberFormatException ex){
				    	System.out.println("Format wrong");
				    }
				}				  
				else{			
					lbl_error.setText("Invalid");
				}
			});
				addCinema.add(lbl_error, 2, 4);
			//set sitting capacity  
			TextField tf_seatCap = new TextField();
			addCinema.add(tf_seatCap, 1, 5);
			Label lbl_error2 = new Label();
			lbl_error2.setTextFill(Color.ALICEBLUE);
			lbl_error2.setFont(Font.font(10));
			tf_seatCap.setOnKeyReleased(e->{					
				if(tf_seatCap.getText().matches("[0-9]*")){
					lbl_error2.setText("Correct");
					try{
						newCinema.setSeatingCapacity(Integer.parseInt(tf_seatCap.getText()));
					}catch(NumberFormatException ex){
				    	System.out.println("Format wrong");
				    }
				}				  
				else{			
					lbl_error2.setText("Invalid");
				}					
			});
			addCinema.add(lbl_error2, 2, 5);
			
			//set image of the cinema 
			TextField txtCinemaPoster = new TextField();
			txtCinemaPoster.setMaxWidth(350);
			FileChooser posterFileChooser = new FileChooser();
			posterFileChooser.setTitle("Choose Image File for Cinema Poster");
			posterFileChooser.setInitialDirectory(new File("bin/Images/Cinema"));
			txtCinemaPoster.setOnMouseClicked(e -> {
				File imageFile = posterFileChooser.showOpenDialog(null);
				if (imageFile != null){
					String imageAddress = "Images/Cinema/" + imageFile.getName();
					txtCinemaPoster.setText(imageAddress);
					newCinema.setImgAddress(imageAddress);
				}
			});
			addCinema.add(txtCinemaPoster, 1, 6);
			
			//Button for back and submit 
			WaveButton btn_back = new WaveButton("Back");
			btn_back.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
			btn_back.setFont(Font.font("Courier", FontWeight.BOLD, 16));
			addCinema.add(btn_back, 0, 7);
			GridPane.setHalignment(btn_back, HPos.RIGHT);
			btn_back.setOnAction(e->{
				addCinema.getChildren().clear();
				vbBackPane.getChildren().clear();
				vbBackPane.getChildren().add(cinemalist());
				setCenter(vbBackPane);
			});
							
			WaveButton btn_submit = new WaveButton("Submit");
			btn_submit.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
			btn_submit.setFont(Font.font("Courier", FontWeight.BOLD, 16));
			addCinema.add(btn_submit, 1, 7);
			btn_submit.setOnAction(e->{
				if(newCinema.addFileRecord()==true){
					addCinema.getChildren().clear();
					vbBackPane.getChildren().clear();
					vbBackPane.getChildren().add(cinemalist());
					setCenter(vbBackPane);
				}
				else {
					Label lbl_error5 = new Label("Can't ADD file");
					setTop(lbl_error5);
				}
			});					
			vbBackPane.getChildren().add(addCinema); 
	}
	
	public void modifyCinema(Cinema cinema){
		GridPane modifyCinema = new GridPane();
		modifyCinema.setPadding(new Insets(20,20,20,20));
		modifyCinema.setStyle("-fx-background-color: rgba(26, 43, 60);-fx-background-radius:10px;");
		modifyCinema.setVgap(20);
		modifyCinema.setHgap(25);
		modifyCinema.setMaxWidth(400);
		
		Label lbl_addCinema = new Label("Modify Cinema");
		DropShadow dropShadow = new DropShadow();
	     dropShadow.setOffsetX(5);
	     dropShadow.setOffsetY(5);
	     lbl_addCinema.setStyle("-fx-font-family: Courier; -fx-font-size: 20px; -fx-font-weight: bold;");
	     lbl_addCinema.setTextFill(Color.ALICEBLUE);
	     lbl_addCinema.setEffect(dropShadow);
	     modifyCinema.add(lbl_addCinema, 1, 0);
	     
	     WaveButton btn_back = new WaveButton("Back");
			btn_back.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
			btn_back.setFont(Font.font("Courier", FontWeight.BOLD, 16));
			ImageView imgBack = new ImageView("Images/Icons/back.png");
			imgBack.setFitHeight(50);
			imgBack.setFitWidth(50);
			btn_back.setGraphic(imgBack);
			modifyCinema.add(btn_back, 0, 0);
			btn_back.setOnAction(e->{
				getChildren().clear();
			    VBox gtmp = cinemalist();
			    vbBackPane.getChildren().clear();
			    vbBackPane.getChildren().add(gtmp);			
				setCenter(vbBackPane);		
			});
		
	     
	   //add lable for first column 
			ObservableList<String> tableColumns = null;
			tableColumns = cinema.getTableColumns();
			for (int i = 0 ;i< 6 ;i++){
				Label labColumnName = new Label(tableColumns.get(i)+" : ");
				labColumnName.setStyle("-fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold;");
				labColumnName.setTextFill(Color.ALICEBLUE);
				modifyCinema.add(labColumnName, 0, (i+1));
			}
	   
	   //add lable for the second column 
			//set cinema id 
			TextField tf_cinemaID = new TextField(cinema.getCinemaID());
			tf_cinemaID.setEditable(false);
			modifyCinema.add(tf_cinemaID, 1, 1);
			
			//set cinema title 
			TextField tf_cinemaTitle = new TextField(cinema.getName());
			modifyCinema.add(tf_cinemaTitle, 1, 2);
			tf_cinemaTitle.setOnKeyReleased(e->{
				cinema.setName(tf_cinemaTitle.getText());
			});
		
			//set cinema address
			TextField tf_cinemaAdd = new TextField(cinema.getAddress());
			modifyCinema.add(tf_cinemaAdd, 1, 3);
			tf_cinemaAdd.setOnKeyReleased(e->{
				cinema.setAddress(tf_cinemaAdd.getText());
			});
			
			//set number of halls 
			TextField tf_numHall = new TextField(Integer.toString(cinema.getNumberOfHalls()));
			modifyCinema.add(tf_numHall, 1, 4);
				Label lbl_error = new Label();
				lbl_error.setTextFill(Color.ALICEBLUE);
				lbl_error.setFont(Font.font(10));	
			tf_numHall.setOnKeyReleased(e->{					
				if(tf_numHall.getText().matches("-?([1-9][0-9]*)?")){
					lbl_error.setText("Correct");					
				    try{
				    	cinema.setNumberOfHalls(Integer.parseInt(tf_numHall.getText()));
				    }
				    catch(NumberFormatException ex){
				    	System.out.println("Format wrong");
				    }
				}				  
				else{			
					lbl_error.setText("Invalid");
				}
			});
			modifyCinema.add(lbl_error, 2, 4);
			//set sitting capacity  
			TextField tf_seatCap = new TextField(Integer.toString(cinema.getSeatingCapacity()));
			modifyCinema.add(tf_seatCap, 1, 5);
			Label lbl_error2 = new Label();
			lbl_error2.setTextFill(Color.ALICEBLUE);
			lbl_error2.setFont(Font.font(10));
			tf_seatCap.setOnKeyReleased(e->{					
				if(tf_seatCap.getText().matches("[0-9]*")){
					lbl_error2.setText("Correct");
					try{
						cinema.setSeatingCapacity(Integer.parseInt(tf_seatCap.getText()));
					}catch(NumberFormatException ex){
				    	System.out.println("Format wrong");
				    }
				}				  
				else{			
					lbl_error2.setText("Invalid");
				}					
			});
			modifyCinema.add(lbl_error2, 2, 5);
			
			
			//set image of the cinema 
			TextField txtCinemaPoster = new TextField(cinema.getImgAddress());
			txtCinemaPoster.setMaxWidth(350);
			FileChooser posterFileChooser = new FileChooser();
			posterFileChooser.setTitle("Choose Image File for Cinema Poster");
			posterFileChooser.setInitialDirectory(new File("bin/Images/Cinema"));
			txtCinemaPoster.setOnMouseClicked(e -> {
				File imageFile = posterFileChooser.showOpenDialog(null);
				if (imageFile != null){
					String imageAddress = "Images/Cinema/" + imageFile.getName();
					txtCinemaPoster.setText(imageAddress);
					cinema.setImgAddress(imageAddress);
				}
			});
			modifyCinema.add(txtCinemaPoster, 1, 6);
			
			//Button for delete and submit 
			WaveButton btn_delete = new WaveButton("Delete");
			btn_delete.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
			btn_delete.setFont(Font.font("Courier", FontWeight.BOLD, 16));
			modifyCinema.add(btn_delete, 0, 7);
			GridPane.setHalignment(btn_delete, HPos.RIGHT);
			btn_delete.setOnAction(e->{
				if(cinema.deleteFileRecord()==true){
					getChildren().clear();
				    VBox gtmp = cinemalist();
				    vbBackPane.getChildren().clear();
				    vbBackPane.getChildren().add(gtmp);			
					setCenter(vbBackPane);
				}
				else {
					Label lbl_error5 = new Label("Can't delete file");
					lbl_error5.setTextFill(Color.ALICEBLUE);
					lbl_error5.setStyle("-fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold;");
					setTop(lbl_error5);
				}
			});
							
			WaveButton btn_submit = new WaveButton("Submit");
			btn_submit.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
			btn_submit.setFont(Font.font("Courier", FontWeight.BOLD, 16));
			modifyCinema.add(btn_submit, 1, 7);
			btn_submit.setOnAction(e->{
				if(cinema.updateFileRecord()==true){
					getChildren().clear();
				    VBox gtmp = cinemalist();
				    vbBackPane.getChildren().clear();
				    vbBackPane.getChildren().add(gtmp);			
					setCenter(vbBackPane);
				}
				else {
					Label lbl_error5 = new Label("Can't delete file");
					setTop(lbl_error5);
				}
			});					
			vbBackPane.getChildren().add(modifyCinema); 
	}	
}
	
