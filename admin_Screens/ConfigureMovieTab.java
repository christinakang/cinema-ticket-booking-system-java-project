package admin_Screens;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import org.controlsfx.control.CheckComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import larn.modernUI.WaveButton;
import real_objects.Movie;

public class ConfigureMovieTab extends BorderPane {
		ArrayList<Movie> movielist = new ArrayList<>();
		private VBox vbBackPane = new VBox(10);
		private String s_genre = " ";

		
		public ConfigureMovieTab(){			
			FlowPane movielist = movieList();
			vbBackPane.getChildren().add(movielist);			
			setCenter(vbBackPane);
}					
		
		public FlowPane movieList(){	
			//set movie details from the file 
			movielist = Movie.retrieveAllFileRecords();			
			//Flow pane as a back pane for the movie list 
			FlowPane fpmovielist = new FlowPane();
			fpmovielist.setPadding(new Insets(20,20,30,20));
			fpmovielist.setVgap(30);
			fpmovielist.setHgap(20);
			
			//an array list to save for the post image 
			ArrayList<ImageView> arrPoster = new ArrayList<ImageView>();
			
			//add movie poster to the flow pane 
			for(int i =0 ;i<movielist.size(); i++){
				File file = new File("bin/" + movielist.get(i).getMoviePosterAddress());
				ImageView img = new ImageView(file.toURI().toString());
				img.setFitHeight(220);
				img.setFitWidth(150);
				img.setOnMouseEntered(e -> setCursor(Cursor.HAND));
				img.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
				arrPoster.add(img);
				fpmovielist.getChildren().add(arrPoster.get(i));
			}			
			
			//set action for all the poster 
			for(int i = 0; i<movielist.size();i++){
					Movie tmpMovie = movielist.get(i);
				    arrPoster.get(i).setOnMouseClicked(e->{		
				    	getChildren().clear();
				    	GridPane gtmp = showMovieDetail(tmpMovie);
					    vbBackPane.getChildren().clear();
					    vbBackPane.getChildren().add(gtmp);			
						setCenter(vbBackPane);						
				    });		
		    }

			//set plus image 		
			ImageView imgPlus = new ImageView("Images/Icons/plus.png");
			imgPlus.setFitHeight(220);
			imgPlus.setFitWidth(150);
			imgPlus.setOnMouseEntered(e -> setCursor(Cursor.HAND));
			imgPlus.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
            arrPoster.add(imgPlus);
            fpmovielist.getChildren().add(arrPoster.get(arrPoster.size()-1));
            
            //set action for the plus image 
			arrPoster.get(arrPoster.size()-1).setOnMouseClicked(e->{
				fpmovielist.getChildren().clear();
				GridPane gpTmp = addMovie();
		        fpmovielist.getChildren().add(gpTmp);
			});									
			return fpmovielist;
		}
		
	public GridPane addMovie(){
				//create a new movie object when this method active 
				Movie movieNew = new Movie();	
				
				//create a gridpane for this method add movie which will show a table of insert movie details 
				GridPane gpAddMovie = new GridPane();
				gpAddMovie.setPadding(new Insets(20,20,20,20));
				gpAddMovie.setStyle("-fx-background-color: rgba(26, 43, 60,0.6);-fx-border-radius:15px;");
				gpAddMovie.setVgap(20);
				gpAddMovie.setHgap(25);
				
				
				Label lbl_addMovie = new Label("Add Movie");
				DropShadow dropShadow = new DropShadow();
			     dropShadow.setOffsetX(5);
			     dropShadow.setOffsetY(5);
				lbl_addMovie.setStyle("-fx-font-family: Courier; -fx-font-size: 20px; -fx-font-weight: bold;");
				lbl_addMovie.setTextFill(Color.ALICEBLUE);
				lbl_addMovie.setEffect(dropShadow);
				gpAddMovie.add(lbl_addMovie, 0, 0);
				
				//add lable for first column 
				ObservableList<String> tableColumns = null;
				tableColumns = movieNew.getTableColumns();
				for (int i = 0 ;i< 8 ;i++){
					Label labColumnName = new Label(tableColumns.get(i)+" : ");
					labColumnName.setStyle("-fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold;");
					labColumnName.setTextFill(Color.ALICEBLUE);
					gpAddMovie.add(labColumnName, 0, (i+1));
				}

			 //add content for the second colmn 
				//set the movie ID field not eiditable 
				movieNew.setMovieID(Movie.generateID());
				TextField tf_movieID = new TextField(movieNew.getMovieID());
				tf_movieID.setEditable(false);
				gpAddMovie.add(tf_movieID, 1, 1);
				
				//set the movie title field 
				TextField tf_movieTitle = new TextField();
				gpAddMovie.add(tf_movieTitle, 1, 2);
				tf_movieTitle.setOnKeyReleased(e->{
					movieNew.setTitle(tf_movieTitle.getText());
				});
				
				//set the genre 
				final ObservableList<String> genres = FXCollections.observableArrayList();				
				for(int i = 0; i<Movie.getGenreTypes().length;i++){
					genres.add(Movie.getGenreTypes()[i]);
				}
				CheckComboBox<String> checkComboBox = new CheckComboBox<String>(genres);
				gpAddMovie.add(checkComboBox, 1, 3);
				 checkComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
				     public void onChanged(ListChangeListener.Change<? extends String> c) {
				         s_genre = checkComboBox.getCheckModel().getCheckedItems().toString();
				         movieNew.setGenres(s_genre);
				     }
				 });
				
				
				//set the rating 
				HBox hb_rating = new HBox(10);
				ImageView[] imgRating = new ImageView[3];
			     imgRating[0] = new ImageView("Images/MovieRating/18.png");
			     imgRating[1] = new ImageView("Images/MovieRating/p13.png");
			     imgRating[2] = new ImageView("Images/MovieRating/u.png");
			     for(int i =0; i<3; i++){
			    	 imgRating[i].setFitHeight(40);
			    	 imgRating[i].setFitWidth(40);
			     }
			         ToggleGroup group_rating = new ToggleGroup();
			    	 RadioButton btn_rating1 = new RadioButton();
			    	 btn_rating1.setToggleGroup(group_rating);			    	 
			    	 hb_rating.getChildren().addAll(btn_rating1,imgRating[0]);			     
			    	 btn_rating1.setOnMouseClicked(e->{
			    		 movieNew.setRate("18");
			    	 });
			    	 
			    	 RadioButton btn_rating2 = new RadioButton();
			    	 btn_rating2.setToggleGroup(group_rating);			    	 
			    	 hb_rating.getChildren().addAll(btn_rating2,imgRating[1]);
			    	 btn_rating2.setOnMouseClicked(e->{
			    		 movieNew.setRate("p13");
			    	 });
			     
			    	 RadioButton btn_rating3 = new RadioButton();
			    	 btn_rating3.setToggleGroup(group_rating);			    	 
			    	 hb_rating.getChildren().addAll(btn_rating3,imgRating[2]);
			    	 btn_rating3.setOnMouseClicked(e->{
			    		 movieNew.setRate("u");
			    	 });
			    	 
			    	 gpAddMovie.add(hb_rating, 1, 4);
			     
			     //set subtitles 
			     ComboBox<String> cbox_subtitle= new ComboBox<String>();
			     cbox_subtitle.setStyle("-fx-background-color: white, white;"
			    			+ "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 8, 0.0 , 0 , 0 );"
			    			+ "-fx-text-fill: -fx-selection-bar-text;" );
			     cbox_subtitle.setStyle("-fx-font: 15px \"Serif\";");
			     for(int i = 0; i<Movie.getLanguages().length; i++){
			    	 cbox_subtitle.getItems().add(Movie.getLanguages()[i]);
					}
			       cbox_subtitle.setOnAction(e->{
			    	   movieNew.setSubtitles((String) cbox_subtitle.getValue());
			     });
			     gpAddMovie.add(cbox_subtitle, 1, 5);
			     
				 //set languages 
			     ComboBox<String> cbox_language= new ComboBox<String>();
			     cbox_language.setStyle("-fx-background-color: white, white;"
			    			+ "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 8, 0.0 , 0 , 0 );"
			    			+ "-fx-text-fill: -fx-selection-bar-text;" );
			     cbox_language.setStyle("-fx-font: 15px \"Serif\";");
			     for(int i = 0; i<Movie.getLanguages().length; i++){
			    	 cbox_language.getItems().add(Movie.getLanguages()[i]);
					}
			     cbox_language.setOnAction(e->{
			    	   movieNew.setLanguage((String) cbox_language.getValue());
			     });
			     gpAddMovie.add(cbox_language, 1, 6);
				
			     //set date
			     DatePicker date = new DatePicker();
			     gpAddMovie.add(date, 1, 7);
			     date.setOnAction(e->{
			    	 int month = date.getValue().getMonthValue();
			    	 int day = date.getValue().getDayOfMonth();
			    	 int year = date.getValue().getYear();
			    	 @SuppressWarnings("deprecation")
					Date date1 =  new Date(year,month,day);
			    	 movieNew.setReleaseDate( date1);
			     });
			     
			     //set Running time 
			     HBox hb_time = new HBox(10);
			     TextField tf_time = new TextField();
			     tf_time.setMaxWidth(100);
			     Label lbl_time = new Label("mins");
			     lbl_time.setTextFill(Color.ALICEBLUE);
			     lbl_time.setFont(Font.font(15));
			     hb_time.getChildren().addAll(tf_time,lbl_time);
			     gpAddMovie.add(hb_time, 1, 8);
			     tf_time.setOnKeyReleased(e->{
						movieNew.setRunningTime(Integer.parseInt(tf_time.getText()));
					});
			     
		//add lable for thrid column
			     for (int i = 8 ;i< 15 ;i++){
						Label labColumnName = new Label(tableColumns.get(i)+" : ");
						labColumnName.setStyle("-fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold;");
						labColumnName.setTextFill(Color.ALICEBLUE);
						gpAddMovie.add(labColumnName, 2, (i-7));
					}	
	     
		//add lable for forth column 		
				//text field for directed by 
				TextField tf_direct = new TextField();
				tf_direct.setMaxWidth(250);
				gpAddMovie.add(tf_direct, 3, 1);
				tf_direct.setOnKeyReleased(e->{
						movieNew.setDirector(tf_direct.getText());
					});
				
				//text field for cast 
				TextField tf_cast = new TextField();
				tf_cast.setMaxWidth(250);
				gpAddMovie.add(tf_cast, 3, 2);
				tf_cast.setOnKeyReleased(e->{
					movieNew.setCast(tf_cast.getText());
				});
				
				//text field for distributor 
				TextField tf_distributor = new TextField();
				tf_distributor.setMaxWidth(250);
				gpAddMovie.add(tf_distributor, 3, 3);
				tf_distributor.setOnKeyReleased(e->{
					movieNew.setDistributor(tf_distributor.getText());
				});
				
				//text area for synopsis 
				TextArea tf_Synopsis = new TextArea();
				tf_Synopsis.setMaxWidth(250);
				tf_Synopsis.setMaxHeight(100);
				gpAddMovie.add(tf_Synopsis, 3, 4);
				tf_Synopsis.setOnKeyReleased(e->{
					movieNew.setSynopsis(tf_Synopsis.getText());
				});
				
				//text field for movie poster 
				TextField txtMoviePoster = new TextField();
				txtMoviePoster.setMaxWidth(350);
				txtMoviePoster.setPromptText("Movie Poster");
				FileChooser posterFileChooser = new FileChooser();
				posterFileChooser.setTitle("Choose Image File for Movie Poster");
				posterFileChooser.setInitialDirectory(new File("bin/Images/MoviePoster"));
				txtMoviePoster.setOnMouseClicked(e -> {
					File imageFile = posterFileChooser.showOpenDialog(null);
					if (imageFile != null){
						String imageAddress = "Images/MoviePoster/" + imageFile.getName();
						txtMoviePoster.setText(imageAddress);
						movieNew.setMoviePosterAddress(imageAddress);
					}
				});
				gpAddMovie.add(txtMoviePoster, 3, 5);
				
				//textfield for triler
				TextField txtTrailer = new TextField();
				txtTrailer.setMaxWidth(350);
				txtTrailer.setPromptText("Movie Trailer");
				FileChooser trailerFileChooser = new FileChooser();
				trailerFileChooser.setTitle("Choose Video File for Movie Trailer");
				trailerFileChooser.setInitialDirectory(new File("bin/Videos"));
				txtTrailer.setOnMouseClicked(e -> {
					File videoFile = trailerFileChooser.showOpenDialog(null);
					if (videoFile != null){
						String videoAddress = "Videos/" + videoFile.getName();
						txtTrailer.setText(videoAddress);
						movieNew.setTrailerAddress(videoAddress);
					}
				});
				gpAddMovie.add(txtTrailer, 3, 6);
				
			   //textfield for rate 
				HBox hb_rate = new HBox(10);
				Label lbl_rm = new Label("RM");
				lbl_rm.setTextFill(Color.ALICEBLUE);
				lbl_rm.setFont(Font.font(15));
				TextField tf_rate = new TextField();
				tf_rate.setMaxWidth(100);
				hb_rate.getChildren().addAll(lbl_rm,tf_rate);
				gpAddMovie.add(hb_rate, 3, 7);
				tf_rate.setOnKeyReleased(e->{
					movieNew.setMoviePriceRate(Double.parseDouble(tf_rate.getText()));
				});
				
				//Button for back and submit 
				WaveButton btn_back = new WaveButton("Back");
				btn_back.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
				btn_back.setFont(Font.font("Courier", FontWeight.BOLD, 16));
				gpAddMovie.add(btn_back, 1, 9);
				GridPane.setHalignment(btn_back, HPos.RIGHT);
				btn_back.setOnAction(e->{
					getChildren().clear();
				    FlowPane gtmp = movieList();
				    vbBackPane.getChildren().clear();
				    vbBackPane.getChildren().add(gtmp);			
					setCenter(vbBackPane);		
				});
								
				WaveButton btn_submit = new WaveButton("Submit");
				btn_submit.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
				btn_submit.setFont(Font.font("Courier", FontWeight.BOLD, 16));
				gpAddMovie.add(btn_submit, 2, 9);
				btn_submit.setOnAction(e->{
					movieNew.addFileRecord();
					getChildren().clear();
				    FlowPane gtmp = movieList();
				    vbBackPane.getChildren().clear();
				    vbBackPane.getChildren().add(gtmp);			
					setCenter(vbBackPane);	
				});		
	     return gpAddMovie;
	}
	
	public GridPane showMovieDetail(Movie movieTmp){
				GridPane gpshowMovie = new GridPane();
				gpshowMovie.setPadding(new Insets(10,10,10,10));
				gpshowMovie.setStyle("-fx-background-color: rgba(26, 43, 60);");
				gpshowMovie.setVgap(20);
				gpshowMovie.setHgap(25);
        
		        //label for the page title 
				Label lbl_addMovie = new Label("Modify Movie Details");
				DropShadow dropShadow = new DropShadow();
			     dropShadow.setOffsetX(5);
			     dropShadow.setOffsetY(5);
				lbl_addMovie.setStyle("-fx-font-family: Courier; -fx-font-size: 20px; -fx-font-weight: bold;");
				lbl_addMovie.setTextFill(Color.ALICEBLUE);
				lbl_addMovie.setEffect(dropShadow);
				gpshowMovie.add(lbl_addMovie, 1, 0);
				
				//add lable for first column 
				ObservableList<String> tableColumns = null;
				tableColumns = movieTmp.getTableColumns();
				for (int i = 0 ;i< 8 ;i++){
					Label labColumnName = new Label(tableColumns.get(i)+" : ");
					labColumnName.setStyle("-fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold;");
					labColumnName.setTextFill(Color.ALICEBLUE);
					gpshowMovie.add(labColumnName, 0, (i+1));
				}

		//add content for the second colmn 
				//set the movie ID field 
				movieTmp.setMovieID(movieTmp.getMovieID());
				TextField tf_movieID = new TextField(movieTmp.getMovieID());
				tf_movieID.setEditable(false);
				gpshowMovie.add(tf_movieID, 1, 1);
				
				//set the movie title field 
				TextField tf_movieTitle = new TextField(movieTmp.getTitle());
				gpshowMovie.add(tf_movieTitle, 1, 2);
				tf_movieTitle.setOnKeyReleased(e->{
					movieTmp.setTitle(tf_movieTitle.getText());
				});
				
				//set the genre 
				final ObservableList<String> genres = FXCollections.observableArrayList();				
				for(int i = 0; i<Movie.getGenreTypes().length;i++){
					genres.add(Movie.getGenreTypes()[i]);
				}
				CheckComboBox<String> checkComboBox = new CheckComboBox<String>(genres);
				String[] tmpGenre = movieTmp.getGenres().substring(1, movieTmp.getGenres().length() - 2).split(", ");
				for(int i = 0; i<tmpGenre.length;i++){
					for(int j = 0; j<genres.size();j++){
						if(genres.get(j).equals(tmpGenre[i])){
							checkComboBox.getCheckModel().check(genres.get(j));
							break;
						}
					}
				}
				gpshowMovie.add(checkComboBox, 1, 3);
				 checkComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
				     public void onChanged(ListChangeListener.Change<? extends String> c) {
				         s_genre = checkComboBox.getCheckModel().getCheckedItems().toString()+" ";
				         movieTmp.setGenres(s_genre);
				     }
				 });
				
				
				//set the rating 
				HBox hb_rating = new HBox(10);
				ImageView[] imgRating = new ImageView[3];
			     imgRating[0] = new ImageView("Images/MovieRating/18.png");
			     imgRating[1] = new ImageView("Images/MovieRating/p13.png");
			     imgRating[2] = new ImageView("Images/MovieRating/u.png");
			     for(int i =0; i<3; i++){
			    	 imgRating[i].setFitHeight(40);
			    	 imgRating[i].setFitWidth(40);
			     }
			         ToggleGroup group_rating = new ToggleGroup();
			    	 RadioButton btn_rating1 = new RadioButton();
			    	 btn_rating1.setToggleGroup(group_rating);			    	 
			    	 hb_rating.getChildren().addAll(btn_rating1,imgRating[0]);			     
			    	 btn_rating1.setOnMouseClicked(e->{
			    		 movieTmp.setRate("18");
			    	 });
			    	 
			    	 RadioButton btn_rating2 = new RadioButton();
			    	 btn_rating2.setToggleGroup(group_rating);			    	 
			    	 hb_rating.getChildren().addAll(btn_rating2,imgRating[1]);
			    	 btn_rating2.setOnMouseClicked(e->{
			    		 movieTmp.setRate("P13");
			    	 });
			     
			    	 RadioButton btn_rating3 = new RadioButton();
			    	 btn_rating3.setToggleGroup(group_rating);			    	 
			    	 hb_rating.getChildren().addAll(btn_rating3,imgRating[2]);
			    	 btn_rating3.setOnMouseClicked(e->{
			    		 movieTmp.setRate("u");
			    	 });
			    	 
			    	 if(movieTmp.getRate().equals("18"))
			    		 btn_rating1.setSelected(true);
			    	 else if(movieTmp.getRate().equals("P13"))
			    		 btn_rating2.setSelected(true);
			    	 else 
			    		 btn_rating3.setSelected(true);
			    	 gpshowMovie.add(hb_rating, 1, 4);
			     
			     //set subtitles 
			     ComboBox<String> cbox_subtitle= new ComboBox<String>();
			     cbox_subtitle.setStyle("-fx-background-color: white, white;"
			    			+ "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 8, 0.0 , 0 , 0 );"
			    			+ "-fx-text-fill: -fx-selection-bar-text;" );
			     cbox_subtitle.setStyle("-fx-font: 15px \"Serif\";");
			     for(int i = 0; i<Movie.getLanguages().length; i++){
			    	 cbox_subtitle.getItems().add(Movie.getLanguages()[i]);
					}
			       cbox_subtitle.setOnAction(e->{
			    	   movieTmp.setSubtitles((String) cbox_subtitle.getValue());
			     });
			      cbox_subtitle.getSelectionModel().select(movieTmp.getSubtitles());
			       
			      gpshowMovie.add(cbox_subtitle, 1, 5);
			     
				 //set languages 
			     ComboBox<String> cbox_language= new ComboBox<String>();
			     cbox_language.setStyle("-fx-background-color: white, white;"
			    			+ "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 8, 0.0 , 0 , 0 );"
			    			+ "-fx-text-fill: -fx-selection-bar-text;" );
			     cbox_language.setStyle("-fx-font: 15px \"Serif\";");
			     for(int i = 0; i<Movie.getLanguages().length; i++){
			    	 cbox_language.getItems().add(Movie.getLanguages()[i]);
					}
			     cbox_language.setOnAction(e->{
			    	 movieTmp.setLanguage((String) cbox_language.getValue());
			     });
			     cbox_language.getSelectionModel().select(movieTmp.getLanguage());
			     gpshowMovie.add(cbox_language, 1, 6);
				
			     //set date
			     DatePicker date = new DatePicker();			     
			     date.setValue(movieTmp.getReleaseDate().toLocalDate());
			     gpshowMovie.add(date, 1, 7);
			     date.setOnAction(e->{
			    	 int month = date.getValue().getMonthValue();
			    	 int day = date.getValue().getDayOfMonth();
			    	 int year = date.getValue().getYear();
			    	 @SuppressWarnings("deprecation")
					Date date1 =  new Date(year,month,day);
			    	 movieTmp.setReleaseDate(date1);
			     });
			     
			     //set Running time 
			     HBox hb_time = new HBox(10);
			     TextField tf_time = new TextField();
			     tf_time.setMaxWidth(100);
			     tf_time.setText(Integer.toString(movieTmp.getRunningTime()));
			     Label lbl_time = new Label("mins");
			     lbl_time.setTextFill(Color.ALICEBLUE);
			     lbl_time.setFont(Font.font(15));
			     hb_time.getChildren().addAll(tf_time,lbl_time);
			     gpshowMovie.add(hb_time, 1, 8);
			     tf_time.setOnKeyReleased(e->{
			    	 movieTmp.setRunningTime(Integer.parseInt(tf_time.getText()));
					});
			     
		//add lable for thrid column 
				for (int i = 8 ;i< 15 ;i++){
					Label labColumnName = new Label(tableColumns.get(i)+" : ");
					labColumnName.setStyle("-fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold;");
					labColumnName.setTextFill(Color.ALICEBLUE);
					gpshowMovie.add(labColumnName, 2, (i-7));
				}
	     
		//add lable for forth column 		
				//text field for directed by 
				TextField tf_direct = new TextField(movieTmp.getDirector());
				tf_direct.setMaxWidth(250);
				gpshowMovie.add(tf_direct, 3, 1);
				tf_direct.setOnKeyReleased(e->{
						movieTmp.setDirector(tf_direct.getText());
					});
				
				//text field for cast 
				TextField tf_cast = new TextField(movieTmp.getCast());
				tf_cast.setMaxWidth(250);
				gpshowMovie.add(tf_cast, 3, 2);
				tf_cast.setOnKeyReleased(e->{
					movieTmp.setCast(tf_cast.getText());
				});
				
				//text field for distributor 
				TextField tf_distributor = new TextField(movieTmp.getDirector());
				tf_distributor.setMaxWidth(250);
				gpshowMovie.add(tf_distributor, 3, 3);
				tf_distributor.setOnKeyReleased(e->{
					movieTmp.setDistributor(tf_distributor.getText());
				});
				
				//text area for synopsis 
				TextArea tf_Synopsis = new TextArea(movieTmp.getSynopsis());
				tf_Synopsis.setMaxWidth(250);
				gpshowMovie.add(tf_Synopsis, 3, 4);
				tf_Synopsis.setOnKeyReleased(e->{
					movieTmp.setSynopsis(tf_Synopsis.getText());
				});
				
				//text field for movie poster 
				TextField txtMoviePoster = new TextField(movieTmp.getMoviePosterAddress());
				txtMoviePoster.setMaxWidth(350);
				txtMoviePoster.setPromptText("Movie Poster");
				FileChooser posterFileChooser = new FileChooser();
				posterFileChooser.setTitle("Choose Image File for Movie Poster");
				posterFileChooser.setInitialDirectory(new File("bin/Images/MoviePoster"));
				txtMoviePoster.setOnMouseClicked(e -> {
					File imageFile = posterFileChooser.showOpenDialog(null);
					if (imageFile != null){
						String imageAddress = "Images/MoviePoster/" + imageFile.getName();
						txtMoviePoster.setText(imageAddress);
						movieTmp.setMoviePosterAddress(imageAddress);
					}
				});
				gpshowMovie.add(txtMoviePoster, 3, 5);
				
				//textfield for triler
				TextField txtTrailer = new TextField(movieTmp.getTrailerAddress());
				txtTrailer.setMaxWidth(350);
				txtTrailer.setPromptText("Movie Trailer");
				FileChooser trailerFileChooser = new FileChooser();
				trailerFileChooser.setTitle("Choose Video File for Movie Trailer");
				trailerFileChooser.setInitialDirectory(new File("bin/Videos"));
				txtTrailer.setOnMouseClicked(e -> {
					File videoFile = trailerFileChooser.showOpenDialog(null);
					if (videoFile != null){
						String videoAddress = "Videos/" + videoFile.getName();
						txtTrailer.setText(videoAddress);
						movieTmp.setTrailerAddress(videoAddress);
					}
				});
				gpshowMovie.add(txtTrailer, 3, 6);
				
				
			   //textfield for rate 
				HBox hb_rate = new HBox(10);
				Label lbl_rm = new Label("RM");
				lbl_rm.setTextFill(Color.ALICEBLUE);
				lbl_rm.setFont(Font.font(15));
				TextField tf_rate = new TextField(Double.toString(movieTmp.getMoviePriceRate()));
				tf_rate.setMaxWidth(100);
				hb_rate.getChildren().addAll(lbl_rm,tf_rate);
				gpshowMovie.add(hb_rate, 3, 7);
				tf_rate.setOnKeyReleased(e->{
					movieTmp.setMoviePriceRate(Double.parseDouble(tf_rate.getText()));
				});
				
				
				
				
				//Button for delete and submit 
				WaveButton btn_delete = new WaveButton("Delete");
				btn_delete.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
				btn_delete.setFont(Font.font("Courier", FontWeight.BOLD, 16));
				gpshowMovie.add(btn_delete, 1, 9);
				GridPane.setHalignment(btn_delete, HPos.RIGHT);
				btn_delete.setOnAction(e->{			
					
					if(movieTmp.deleteFileRecord()==true){
						getChildren().clear();
					    FlowPane gtmp = movieList();
					    vbBackPane.getChildren().clear();
					    vbBackPane.getChildren().add(gtmp);			
						setCenter(vbBackPane);
					}
					else {
						Label lbl_error = new Label("Can't delete file");
						lbl_error.setStyle("-fx-background-color: white; -fx-text-fill: white;-fx-font-size:15px;");
						setTop(lbl_error);
					}
				});
				
				WaveButton btn_submit = new WaveButton("Submit");
				btn_submit.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
				btn_submit.setFont(Font.font("Courier", FontWeight.BOLD, 16));
				gpshowMovie.add(btn_submit, 2, 9);
				btn_submit.setOnAction(e->{
					if(movieTmp.updateFileRecord()==true){
						getChildren().clear();
					    FlowPane gtmp = movieList();
					    vbBackPane.getChildren().clear();
					    vbBackPane.getChildren().add(gtmp);			
						setCenter(vbBackPane);
					}
					else {
						Label lbl_error = new Label("Can't modify file");
						lbl_error.setStyle("-fx-background-color: white; -fx-text-fill: white;-fx-font-size:15px;");
						setTop(lbl_error);
					}
				});	
				
				WaveButton btn_back = new WaveButton("Back");
				btn_back.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
				btn_back.setFont(Font.font("Courier", FontWeight.BOLD, 16));
				ImageView imgBack = new ImageView("Images/Icons/back.png");
				imgBack.setFitHeight(50);
				imgBack.setFitWidth(50);
				btn_back.setGraphic(imgBack);
				gpshowMovie.add(btn_back, 0, 0);
				btn_back.setOnAction(e->{
					getChildren().clear();
				    FlowPane gtmp = movieList();
				    vbBackPane.getChildren().clear();
				    vbBackPane.getChildren().add(gtmp);			
					setCenter(vbBackPane);		
				});
							
	     return gpshowMovie;
	}
}

