package admin_Screens;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import larn.modernUI.WaveButton;
import real_objects.Cinema;
import real_objects.Movie;
import real_objects.MovieSession;
import real_objects.Ticket;

public class ConfigureSessionTab extends BorderPane{
	
	private ComboBox<Cinema> comboCinemas = new ComboBox<>();
	private ComboBox<Date> comboDates = new ComboBox<>();
	private ComboBox<Movie> comboMovies = new ComboBox<>();
	private ComboBox<String> comboShowType = new ComboBox<>();
	private ComboBox<Integer> comboHalls = new ComboBox<>();
	private ComboBox<String> comboHours = new ComboBox<>();
	private ComboBox<String> comboMins = new ComboBox<>();
	private WaveButton btnCreateSession = new WaveButton("Create Session");
	private Label labError = new Label();
	private GridPane gridHallsSchedule = new GridPane();
	private BorderPane paneSchedule = new BorderPane();
	private VBox vBoxSessionConfig = new VBox(20);
	private ArrayList<MovieSession> currentSessionList = new ArrayList<>();
	private static final int MAX_DAYS = 5;
	
	public ConfigureSessionTab(){
		//ComboBox for choosing cinema
		comboCinemas.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboCinemas.setPromptText("Select Cinema");
		comboCinemas.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboCinemas.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		for(Cinema cinema: Cinema.retrieveAllFileRecords())
			comboCinemas.getItems().add(cinema);
		comboCinemas.setOnAction(e -> comboCinemaChange());
		
		//ComboBox for choosing date
		comboDates.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboDates.setPromptText("Select Date");
		comboDates.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboDates.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		Date today = new Date(System.currentTimeMillis());
		today = Date.valueOf(today.toString());
		for(int i=0; i<MAX_DAYS; i++){
			Date date = new Date(today.getTime() + (3600000 * 24 * (i + 1)));
			comboDates.getItems().add(date);
		}
		comboDates.setOnAction(e -> comboDateChange());
		
		//HBox for laying out primary controls
		HBox hBoxControls = new HBox(15);
		hBoxControls.setPadding(new Insets(5, 5, 5, 5));
		hBoxControls.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35);");
		hBoxControls.getChildren().addAll(comboCinemas, comboDates);
		
		//ComboBox for choosing movie
		comboMovies.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboMovies.setPromptText("Select Movie");
		comboMovies.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboMovies.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		comboMovies.setMaxWidth(215);
		for(Movie movie: Movie.retrieveAllFileRecords())
			comboMovies.getItems().add(movie);
		
		//ComboBox for choosing show type
		comboShowType.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboShowType.setPromptText("Select Show Type");
		comboShowType.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboShowType.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		comboShowType.setMaxWidth(215);
		for(String showType: MovieSession.getShowTypes())
			comboShowType.getItems().add(showType);
		
		//ComboBox for choosing hall within the selected cinema
		comboHalls.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboHalls.setPromptText("Select Hall");
		comboHalls.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboHalls.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		comboHalls.setMaxWidth(215);
		
		//ComboBox for choosing hours in time
		comboHours.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboHours.setPromptText("Hrs");
		comboHours.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboHours.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		comboHours.setMaxWidth(100);
		for(int i=9; i<24; i++){
			comboHours.getItems().add(String.format("%02d", i));
		}
		for(int i=0; i<4; i++){
			comboHours.getItems().add(String.format("%02d", i));
		}
		
		//ComboBox for choosing minutes in time
		comboMins.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboMins.setPromptText("Mins");
		comboMins.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboMins.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		comboMins.setMaxWidth(110);
		for(int i=0; i<60; i+=5){
			comboMins.getItems().add(String.format("%02d", i));
		}
		
		//HBox for laying out hours and minutes selection ComboBox
		HBox hBoxTimeSelection = new HBox(20);
		hBoxTimeSelection.getChildren().addAll(comboHours, comboMins);
		
		//Button to create new Session
		btnCreateSession.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		btnCreateSession.setOnAction(e -> createNewSession());
		btnCreateSession.setDisable(true);
		
		//Label for error message display
		labError.setTextFill(Color.MISTYROSE);
		labError.setWrapText(true);
		labError.setFont(Font.font("Courier", FontWeight.BOLD, 15));
		
		//VBox for creating and deleting sessions
		vBoxSessionConfig.setPadding(new Insets(10, 10, 10, 10));
		vBoxSessionConfig.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85);");
		vBoxSessionConfig.getChildren().addAll(comboMovies, comboShowType, comboHalls, hBoxTimeSelection, btnCreateSession, labError);
		
		//BorderPane for placing hall schedule chart
		paneSchedule.setPadding(new Insets(10, 10, 10, 10));
		paneSchedule.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
		paneSchedule.setCenter(gridHallsSchedule);
		
		//Setting the sides of the BorderPane
		setTop(hBoxControls);
		setLeft(vBoxSessionConfig);
		setCenter(paneSchedule);
		
		//Loading initial hall schedule chart
		loadEmptyScheduleChart();
	}
	
	//Method to load initial interface for schedule chart
	public void loadEmptyScheduleChart(){
		GridPane gridTimeLabel = new GridPane();
		gridTimeLabel.setPadding(new Insets(20, 5, 0, 5));
		gridTimeLabel.getColumnConstraints().add(new ColumnConstraints(80));
		for(int i=9; i<24; i++){
			Text txtTime = new Text(String.format("%02d : 00", i));
			txtTime.setStyle("-fx-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
			gridTimeLabel.addColumn(0, new Pane(txtTime));
			gridTimeLabel.getRowConstraints().add(new RowConstraints(60));
		}
		for(int i=0; i<4; i++){
			Text txtTime = new Text(String.format("%02d : 00", i));
			txtTime.setStyle("-fx-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
			gridTimeLabel.addColumn(0, new Pane(txtTime));
			gridTimeLabel.getRowConstraints().add(new RowConstraints(60));
		}
		paneSchedule.setLeft(gridTimeLabel);
	}
	
	//Method to process comboCinema change
	public void comboCinemaChange(){
		if(comboDates.getValue() != null){
			comboHalls.getItems().clear();
			for(int i=0; i<comboCinemas.getValue().getNumberOfHalls(); i++){
				comboHalls.getItems().add(i + 1);
			}
			loadHallSchedule();
		}
	}
	
	//Method to process comboDate change
	public void comboDateChange(){
		if(comboCinemas.getValue() != null){
			comboHalls.getItems().clear();
			for(int i=0; i<comboCinemas.getValue().getNumberOfHalls(); i++){
				comboHalls.getItems().add(i + 1);
			}
			loadHallSchedule();
		}
	}
	
	//Method to retrieve existing schedule from file
	public void loadHallSchedule(){
		btnCreateSession.setDisable(false);
		labError.setText("");
		
		GridPane gridHallLabels = new GridPane();
		gridHallLabels.setPadding(new Insets(0, 0, 0, 90));
		for(int i=0; i<comboCinemas.getValue().getNumberOfHalls(); i++)
			gridHallLabels.getColumnConstraints().add(new ColumnConstraints(200));
		currentSessionList.clear();
		
		for(int i=0; i<comboCinemas.getValue().getNumberOfHalls(); i++){
			Text txtHall = new Text("HALL " + (i + 1));
			txtHall.setStyle("-fx-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
			gridHallLabels.addRow(0, txtHall);
		}
		paneSchedule.setTop(gridHallLabels);
		
		//Updating the schedule chart layout
		gridHallsSchedule.getChildren().clear();
		gridHallsSchedule.setPadding(new Insets(15, 0, 0, 0));
		for(int i=0; i<216; i++){
			for(int j=0; j<comboCinemas.getValue().getNumberOfHalls(); j++){
				Pane pane = new Pane();
				pane.setStyle("-fx-border-color: teal");
				pane.setPrefWidth(200);
				pane.setPrefHeight(5);
				gridHallsSchedule.add(pane, j, i);
			}
		}
		
		ArrayList<MovieSession> sessionFromFile = MovieSession.retrieveAllFileRecords();
		String cinemaID = comboCinemas.getValue().getCinemaID();
		Date showTime = comboDates.getValue();
		for(MovieSession session: sessionFromFile){
			if(session.getCinemaID().equals(cinemaID) && session.getShowTime().toString().equals(showTime.toString())){
				currentSessionList.add(session);
				getSessionPane(session);
			}
		}
	}
	
	//Method to create new session based on selected values only if they were selected
	public void createNewSession(){
		if(comboCinemas.getValue() != null && comboDates.getValue() != null && 
		   comboMovies.getValue() != null && comboShowType.getValue() != null && 
		   comboHalls.getValue() != null && comboHours.getValue() != null && comboMins.getValue() != null)
		{
			//Creating new movie session with relevant data
			MovieSession session = new MovieSession();
			session.setMovieSessionID(MovieSession.generateID());
			session.setCinemaID(comboCinemas.getValue().getCinemaID());
			session.setMovieID(comboMovies.getValue().getMovieID());
			session.setShowType(comboShowType.getValue());
			session.setHallNumber(comboHalls.getValue());
			
			//Setting show time in the movie session
			Long dateTime = comboDates.getValue().getTime();
			if(Integer.parseInt(comboHours.getValue()) >= 0 && Integer.parseInt(comboHours.getValue()) <= 4)
				dateTime += (Long.parseLong(comboHours.getValue()) + 24) * 3600000;
			else
				dateTime += Long.parseLong(comboHours.getValue()) * 3600000;
			dateTime += Long.parseLong(comboMins.getValue()) * 60000;
			session.setShowTime(new Date(dateTime));
			
			//Determining show end time for the movie session
			Movie movie = new Movie();
			movie.retrieveFileRecord(session.getMovieID());
			Date showEndTime = new Date(session.getShowTime().getTime() + (60000 * movie.getRunningTime()));
			
			boolean isValidSession = true;
			//Checking whether new session time is valid within opening and closing time
			Date closeTime = new Date(comboDates.getValue().getTime() + (3600000 * 27));
			if(showEndTime.compareTo(closeTime) > 0)
				isValidSession = false;
			
			//Checking whether new session time does not overlap with existing sessions
			if(isValidSession){
				//Getting existing movie session in the same hall
				ArrayList<MovieSession> hallSessionList = new ArrayList<>();
				for(MovieSession currentSession: currentSessionList){
					if(currentSession.getHallNumber() == session.getHallNumber())
						hallSessionList.add(currentSession);
				}
				
				for(MovieSession hallSession: hallSessionList){
					Movie movie2 = new Movie();
					movie2.retrieveFileRecord(hallSession.getMovieID());
					Date existingSessionEndTime = new Date(hallSession.getShowTime().getTime() + (60000 * movie2.getRunningTime()));
					if(session.getShowTime().compareTo(hallSession.getShowTime()) >= 0 && session.getShowTime().compareTo(existingSessionEndTime) <= 0){
						isValidSession = false;
						break;
					}
					if(showEndTime.compareTo(hallSession.getShowTime()) >= 0 && showEndTime.compareTo(existingSessionEndTime) <= 0){
						isValidSession = false;
						break;
					}
				}
			}
			if(isValidSession){
				labError.setText("");
				getSessionPane(session);
				currentSessionList.add(session);
				session.addFileRecord();
			}
			else
				labError.setText("Invalid or overlapping\nsession time requested!");
		}
		else
			labError.setText("Some fields does not\nhave values selected!");
	}
	
	//Method to get scroll pane with session details and adding it to gridHallsSchedule
	public void getSessionPane(MovieSession session){
		Movie movie = new Movie();
		movie.retrieveFileRecord(session.getMovieID());
		
		ImageView imgDelete = new ImageView("Images/Icons/Delete.png");
		imgDelete.setFitHeight(15);
		imgDelete.setFitWidth(15);
		imgDelete.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		imgDelete.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		
		Label labMovieTitle = new Label(movie.getTitle());
		labMovieTitle.setStyle("-fx-text-fill: black; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		HBox hBoxDeleteMovie = new HBox(5);
		hBoxDeleteMovie.getChildren().addAll(imgDelete, labMovieTitle);
		
		Label labHall = new Label("Hall: " + session.getHallNumber());
		labHall.setStyle("-fx-text-fill: black; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		Label labShowType = new Label("Type: " + session.getShowType());
		labShowType.setStyle("-fx-text-fill: black; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		Calendar startTime = new GregorianCalendar();
		startTime.setTime(new Date(session.getShowTime().getTime()));
		String startHours = String.format("%02d", startTime.get(Calendar.HOUR_OF_DAY));
		String startMins = String.format("%02d", startTime.get(Calendar.MINUTE));
		
		Date showEndTime = new Date(session.getShowTime().getTime() + (60000 * movie.getRunningTime()));
		Calendar endTime = new GregorianCalendar();
		endTime.setTime(new Date(showEndTime.getTime()));
		String endHours = String.format("%02d", endTime.get(Calendar.HOUR_OF_DAY));
		String endMins = String.format("%02d", endTime.get(Calendar.MINUTE));
		
		Label labShowTime = new Label("Time: " + startHours + ":" + startMins + " - " + endHours + ":" + endMins);
		labShowTime.setStyle("-fx-text-fill: black; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		VBox vBoxSessionDetails = new VBox(5);
		vBoxSessionDetails.setPrefHeight(movie.getRunningTime()/5);
		vBoxSessionDetails.setPrefWidth(200);
		vBoxSessionDetails.setStyle("-fx-background-color: rgba(157, 188, 30, 0.95);");
		vBoxSessionDetails.getChildren().addAll(hBoxDeleteMovie, labShowType, labHall, labShowTime);
		
		ScrollPane sessionPane = new ScrollPane();
		sessionPane.setContent(vBoxSessionDetails);
		sessionPane.setFitToHeight(true);
		sessionPane.setFitToWidth(true);
		imgDelete.setOnMouseClicked(e -> {
			boolean hasBookedTicket = false;
			for(Ticket ticket: Ticket.retrieveAllFileRecords()){
				if(ticket.getMovieSessionID().equals(session.getMovieSessionID())){
					hasBookedTicket = true;
					break;
				}
			}
			if(!hasBookedTicket){
				gridHallsSchedule.getChildren().remove(sessionPane);
				currentSessionList.remove(session);
				session.deleteFileRecord();
			}
			else
				labError.setText("This session has\nbooked ticket!");
		});
		
		int indexHours = 0;
		if(Integer.valueOf(startHours) >= 0 && Integer.valueOf(startHours) < 4){
			indexHours = 15;
			for(int i=0; i<4; i++){
				if(Integer.valueOf(startHours) == i)
					break;
				else
					indexHours++;
			}
		}
		else{
			for(int i=9; i<24; i++){
				if(Integer.valueOf(startHours) == i)
					break;
				else
					indexHours++;
			}
		}
		gridHallsSchedule.add(sessionPane, session.getHallNumber() - 1, (indexHours * 12) + (Integer.parseInt(startMins)/5));
		GridPane.setRowSpan(sessionPane, movie.getRunningTime() / 5);
	}
}
