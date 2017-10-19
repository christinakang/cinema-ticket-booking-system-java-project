package admin_Screens;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import larn.modernUI.WaveButton;
import real_objects.Promotion;
import javafx.scene.Cursor;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ConfigurePromotionTab extends BorderPane{
	private ArrayList<Promotion> arrpromotion = new ArrayList<>();
	
	Promotion promotionNew = new Promotion();
	
	
	public ConfigurePromotionTab(){	
		//set details of the promotion from the database 
		setCenter(posterlist());	
}
	
	public FlowPane posterlist() {	
		FlowPane fpPoster = new FlowPane();
		arrpromotion = Promotion.retrieveAllFileRecords();
	       
		fpPoster.setHgap(10);
		fpPoster.setVgap(10);
		fpPoster.setPadding(new Insets(10,10,10,10));
	    
		//set for the title 
		Label lbl_promo = new Label("Promotions");
		lbl_promo.setTextFill(Color.ALICEBLUE);
		lbl_promo.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));		
		DropShadow dropShadow = new DropShadow();
		dropShadow.setOffsetX(5);
	    dropShadow.setOffsetY(5);
	    lbl_promo.setEffect(dropShadow);
	    Pane lblPane = new Pane();
		lblPane.getChildren().add(lbl_promo);
		lblPane.setPrefWidth(800);
		fpPoster.getChildren().add(lblPane);
		
		ArrayList<ImageView> image = new ArrayList<>();
		for(int i = 0; i <arrpromotion.size(); i++ ){
			 final Promotion promo = arrpromotion.get(i);
			VBox VBtmp = new VBox(10);	
			VBtmp.setPadding(new Insets(10,10,10,10));
			//set image file 
			try{
				File file = new File("bin/" + arrpromotion.get(i).getImgAddress());
		    	//System.out.println(promotion.get(i).getImgAddress());
		    	ImageView imgTmp = new ImageView(file.toURI().toString());
		    	image.add(imgTmp);
				image.get(i).setFitHeight(200);
				image.get(i).setFitWidth(250);
			}
			catch(Exception ex){
				System.out.println("Something wrong");
			}
			
			Label label = new Label(promo.getTopic());
			label.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
			label.setTextFill(Color.ALICEBLUE);
			
			Text text = new Text(promo.getDetails());
			text.setWrappingWidth(800);
			text.setFont(Font.font(20));
			text.setFill(Color.ALICEBLUE);
			
			Text textStartDate = new Text("Start Date: " + promo.getStartDate().toString());
			textStartDate.setFont(Font.font(20));
			textStartDate.setFill(Color.ALICEBLUE);
			
			Text textEndDate = new Text("End Date: " + promo.getEndDate().toString());
			textEndDate.setFont(Font.font(20));
			textEndDate.setFill(Color.ALICEBLUE);
			
			WaveButton btn_modify = new WaveButton("Modify");
			btn_modify.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
			btn_modify.setFont(Font.font("Courier", FontWeight.BOLD,20));
			
			btn_modify.setOnAction(e->{
	    		getChildren().clear();
		        setCenter(modifyPromo(promo));
	    	});
			
			WaveButton btn_delete = new WaveButton("Delete");
	    	btn_delete.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
	    	btn_delete.setFont(Font.font("Courier", FontWeight.BOLD,20));;
	    	btn_delete.setOnAction(e->{	
	    		Label lbl_message = new Label("File delete");
	    		lbl_message.setTextFill(Color.ALICEBLUE);
	    		lbl_message.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
	    		if(promo.deleteFileRecord()==true){
	    			setTop(lbl_message);
	    			setCenter(posterlist());
	    		}		
	    	});
			
			VBtmp.getChildren().addAll(label,text,textStartDate,textEndDate);
			VBtmp.setStyle("-fx-background-color: rgba(112,128,144,0.55);-fx-background-radius:20px;");
			HBox hbtmp = new HBox(10);
			hbtmp.getChildren().addAll(image.get(i),VBtmp,btn_modify,btn_delete);
			hbtmp.setAlignment(Pos.CENTER);
			fpPoster.getChildren().add(hbtmp);
		}   	
		
			ImageView plus = new ImageView("Images/Icons/plus.png");
		   	plus.setFitHeight(220);
		   	plus.setFitWidth(150);
		   	plus.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		   	plus.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		   	fpPoster.getChildren().add(plus);
		   	    
		   		plus.setOnMouseClicked(e->{
		   			getChildren().clear();
		   	        setCenter(addNewPromo());
		   		});

		return fpPoster;
	}
	
	
	public GridPane addNewPromo(){
		GridPane promoNew = new GridPane();
		promoNew.setPadding(new Insets(20,20,20,20));
		promoNew.setStyle("-fx-background-color: rgba(26, 43, 60);");
		promoNew.setVgap(20);
		promoNew.setHgap(25);
		
		
		Label lbl_addPromo = new Label("Add new promotion");
		DropShadow dropShadow = new DropShadow();
	     dropShadow.setOffsetX(5);
	     dropShadow.setOffsetY(5);
	     lbl_addPromo.setStyle("-fx-font-family: Courier; -fx-font-size: 20px; -fx-font-weight: bold;");
	     lbl_addPromo.setTextFill(Color.ALICEBLUE);
	     lbl_addPromo.setEffect(dropShadow);
	     promoNew.add(lbl_addPromo, 0, 0);
	     
		ObservableList<String> tableColumns = null;
		tableColumns = promotionNew.getTableColumns();
		for (int i = 0 ;i< 5 ;i++){
			Label labColumnName = new Label(tableColumns.get(i)+" : ");
			labColumnName.setStyle("-fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold;");
			labColumnName.setTextFill(Color.ALICEBLUE);
			promoNew.add(labColumnName, 0, (i+1));
		}
		
		//set textfield for topic 
		TextField tf_topic = new TextField();
		promoNew.add(tf_topic, 1, 1);
		tf_topic.setOnKeyReleased(e->{
			promotionNew.setTopic(tf_topic.getText());
		});
		
		//set textField for details 
		TextField tf_detail = new TextField();
		promoNew.add(tf_detail, 1, 2);
		tf_detail.setOnKeyReleased(e->{
			promotionNew.setDetails(tf_detail.getText());
		});
		
		//set start date 
		 DatePicker dateStart = new DatePicker();
		 promoNew.add(dateStart, 1, 3);
		 dateStart.setOnAction(e->{
	    	 int month = dateStart.getValue().getMonthValue();
	    	 int day = dateStart.getValue().getDayOfMonth();
	    	 int year = dateStart.getValue().getYear();
	    	 @SuppressWarnings("deprecation")
			Date date1 =  new Date(year,month,day);
	    	 promotionNew.setStartDate(date1);
	     });
	     
	   //set start date 
		 DatePicker dateEnd = new DatePicker();
		 promoNew.add(dateEnd, 1, 4);
		 dateEnd.setOnAction(e->{
	    	 int month = dateEnd.getValue().getMonthValue();
	    	 int day = dateEnd.getValue().getDayOfMonth();
	    	 int year = dateEnd.getValue().getYear();
	    	 @SuppressWarnings("deprecation")
			Date date1 =  new Date(year,month,day);
	    	 promotionNew.setEndDate(date1);
	     });
		
		 //set image 
		 TextField promoPoster = new TextField();
		 promoPoster.setPromptText("Promotion Image");
			FileChooser posterFileChooser = new FileChooser();
			posterFileChooser.setTitle("Choose Image File for Promotion Poster");
			posterFileChooser.setInitialDirectory(new File("bin/Images/PromotionPoster"));
			promoPoster.setOnMouseClicked(e -> {
				File imageFile = posterFileChooser.showOpenDialog(null);
				if (imageFile != null){
					String imageAddress = "Images/PromotionPoster/" + imageFile.getName();
					promoPoster.setText(imageAddress);
					promotionNew.setImgAddress(imageAddress);
				}
			});
			promoNew.add(promoPoster,1, 5);
			
			//Button for submit 							
			WaveButton btn_submit = new WaveButton("Submit");
			btn_submit.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
			btn_submit.setFont(Font.font("Courier", FontWeight.BOLD, 16));
			promoNew.add(btn_submit, 2, 6);
			Label lbl_message= new Label();
			lbl_message.setTextFill(Color.ALICEBLUE);
			lbl_message.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
			btn_submit.setOnAction(e->{
				if(promotionNew.addFileRecord()==true){
					lbl_message.setText("Successfully update!");
					promoNew.add(lbl_message, 0, 6);
					getChildren().clear();
					setCenter(posterlist());
				}
			});	
			
			//button for back 
			WaveButton btn_back = new WaveButton("Back");
			btn_back.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
			btn_back.setFont(Font.font("Courier", FontWeight.BOLD, 16));
			promoNew.add(btn_back, 1, 6);
			GridPane.setHalignment(btn_back, HPos.RIGHT);
			btn_back.setOnAction(e->{
				getChildren().clear();
				setCenter(posterlist());
			});
		return promoNew;
	}
	
	public GridPane modifyPromo(Promotion tmpPromo){
		GridPane promoModify = new GridPane();
		promoModify.setPadding(new Insets(20,20,20,20));
		promoModify.setStyle("-fx-background-color: rgba(26, 43, 60);");
		promoModify.setVgap(20);
		promoModify.setHgap(25);
		
		
		Label lbl_addPromo = new Label("Modify promotion");
		DropShadow dropShadow = new DropShadow();
	     dropShadow.setOffsetX(5);
	     dropShadow.setOffsetY(5);
	     lbl_addPromo.setStyle("-fx-font-family: Courier; -fx-font-size: 20px; -fx-font-weight: bold;");
	     lbl_addPromo.setTextFill(Color.ALICEBLUE);
	     lbl_addPromo.setEffect(dropShadow);
	     promoModify.add(lbl_addPromo, 1, 0);
	     
	     
	     
	     WaveButton btn_back = new WaveButton("Back");
			btn_back.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
			btn_back.setFont(Font.font("Courier", FontWeight.BOLD, 16));
			ImageView imgBack = new ImageView("Images/Icons/back.png");
			imgBack.setFitHeight(50);
			imgBack.setFitWidth(50);
			btn_back.setGraphic(imgBack);
			promoModify.add(btn_back, 0, 0);
			btn_back.setOnAction(e->{
				getChildren().clear();
				setCenter(posterlist());	
			});
		
		ObservableList<String> tableColumns = null;
		tableColumns = tmpPromo.getTableColumns();
		for (int i = 0 ;i< 5 ;i++){
			Label labColumnName = new Label(tableColumns.get(i)+" : ");
			labColumnName.setStyle("-fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold;");
			labColumnName.setTextFill(Color.ALICEBLUE);
			promoModify.add(labColumnName, 0, (i+1));
		}
		
		//set textfield for topic 
		TextField tf_topic = new TextField(tmpPromo.getTopic());
		promoModify.add(tf_topic, 1, 1);
		tf_topic.setOnKeyReleased(e->{
			tmpPromo.setTopic(tf_topic.getText());
		});
		
		//set textField for details 
		TextField tf_detail = new TextField(tmpPromo.getDetails());
		promoModify.add(tf_detail, 1, 2);
		tf_detail.setOnKeyReleased(e->{
			tmpPromo.setDetails(tf_detail.getText());
		});
		
		//set start date 
		 DatePicker dateStart = new DatePicker();
		 dateStart.setValue(tmpPromo.getStartDate().toLocalDate());
		 promoModify.add(dateStart, 1, 3);
		 dateStart.setOnAction(e->{
	    	 int month = dateStart.getValue().getMonthValue();
	    	 int day = dateStart.getValue().getDayOfMonth();
	    	 int year = dateStart.getValue().getYear();
	    	 @SuppressWarnings("deprecation")
			Date date1 =  new Date(year,month,day);
	    	 tmpPromo.setStartDate(date1);
	     });
	     
	   //set start date 
		 DatePicker dateEnd = new DatePicker();
		 dateEnd.setValue(tmpPromo.getEndDate().toLocalDate());
		 promoModify.add(dateEnd, 1, 4);
		 dateEnd.setOnAction(e->{
	    	 int month = dateEnd.getValue().getMonthValue();
	    	 int day = dateEnd.getValue().getDayOfMonth();
	    	 int year = dateEnd.getValue().getYear();
	    	 @SuppressWarnings("deprecation")
			Date date1 =  new Date(year,month,day);
	    	 tmpPromo.setEndDate(date1);
	     });
			
			 //set image 
			 TextField promoPoster = new TextField(tmpPromo.getImgAddress());
				FileChooser posterFileChooser = new FileChooser();
				posterFileChooser.setTitle("Choose Image File for Promotion Poster");
				posterFileChooser.setInitialDirectory(new File("bin/Images/PromotionPoster"));
				promoPoster.setOnMouseClicked(e -> {
					File imageFile = posterFileChooser.showOpenDialog(null);
					if (imageFile != null){
						String imageAddress = "Images/PromotionPoster/" + imageFile.getName();
						promoPoster.setText(imageAddress);
						tmpPromo.setImgAddress(imageAddress);
					}
				});
				promoModify.add(promoPoster,1, 5);
				
			
			//Button for submit 							
			WaveButton btn_submit = new WaveButton("Submit");
			btn_submit.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
			btn_submit.setFont(Font.font("Courier", FontWeight.BOLD, 16));
			promoModify.add(btn_submit, 1, 6);
			Label lbl_message= new Label();
			lbl_message.setTextFill(Color.ALICEBLUE);
			lbl_message.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
			btn_submit.setOnAction(e->{
				if(tmpPromo.updateFileRecord()==true){
					lbl_message.setText("Successfully update!");
					promoModify.add(lbl_message, 0, 6);
					
				}
			});					
		return promoModify;
	}
}
