package ui_screens;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.util.ArrayList;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import larn.modernUI.WaveButton;
import real_objects.*;

public class HomeTab extends VBox{
	
	private ArrayList<Movie> movieList = Movie.retrieveAllFileRecords();
	private FlowPane flowMoviePosters = new FlowPane();
	
	public HomeTab() {
		
		HBox hBoxMovieSelection = new TicketSelectionPane();
		
		Text txtEmptySection = new Text("No Movies In This Section");
		txtEmptySection.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		//Buttons and HBox for laying out movie categories
		WaveButton btnNowShowing = new WaveButton("Now Showing");
		btnNowShowing.setStyle("-fx-background-color: rgb(0, 128, 128); -fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		setNowShowingMovies();
		
		WaveButton btnAdvancedSales = new WaveButton("Advanced Sales");
		btnAdvancedSales.setStyle("-fx-background-color: rgb(64, 130, 109); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		WaveButton btnComingSoon = new WaveButton("Coming Soon");
		btnComingSoon.setStyle("-fx-background-color: rgb(64, 130, 109); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		btnNowShowing.setOnAction(e -> {
			btnNowShowing.setStyle("-fx-background-color: rgb(0, 128, 128); -fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
			btnComingSoon.setStyle("-fx-background-color: rgb(64, 130, 109); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
			btnAdvancedSales.setStyle("-fx-background-color: rgb(64, 130, 109); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
			setNowShowingMovies();
			if (flowMoviePosters.getChildren().size() == 0){
				flowMoviePosters.getChildren().add(txtEmptySection);
				flowMoviePosters.setAlignment(Pos.CENTER);
			}
		});
		btnComingSoon.setOnAction(e -> {
			btnComingSoon.setStyle("-fx-background-color: rgb(0, 128, 128); -fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
			btnNowShowing.setStyle("-fx-background-color: rgb(64, 130, 109); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
			btnAdvancedSales.setStyle("-fx-background-color: rgb(64, 130, 109); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
			setComingSoonMovies();
			if (flowMoviePosters.getChildren().size() == 0){
				flowMoviePosters.getChildren().add(txtEmptySection);
				flowMoviePosters.setAlignment(Pos.CENTER);
			}
		});
		btnAdvancedSales.setOnAction(e -> {
			btnAdvancedSales.setStyle("-fx-background-color: rgb(0, 128, 128); -fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
			btnNowShowing.setStyle("-fx-background-color: rgb(64, 130, 109); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
			btnComingSoon.setStyle("-fx-background-color: rgb(64, 130, 109); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
			setAdvancedSalesMovies();
			if (flowMoviePosters.getChildren().size() == 0){
				flowMoviePosters.getChildren().add(txtEmptySection);
				flowMoviePosters.setAlignment(Pos.CENTER);
			}
		});
		
		HBox hBoxMovieGroups = new HBox();
		hBoxMovieGroups.getChildren().addAll(btnNowShowing, btnAdvancedSales, btnComingSoon);
		
		//FlowPane for laying out movie posters
		flowMoviePosters.setPadding(new Insets(5, 5, 5, 5));
		flowMoviePosters.setHgap(5);
		flowMoviePosters.setVgap(25);
		flowMoviePosters.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35)");
		
		//Laying out home tab elements in extended VBox in a vertical manner
		getChildren().addAll(hBoxMovieSelection, hBoxMovieGroups, flowMoviePosters);
	}
	
	//Method to add only now showing movies to display
	public void setNowShowingMovies(){
		flowMoviePosters.getChildren().clear();
		flowMoviePosters.setAlignment(Pos.BASELINE_LEFT);
		
		for (int i=0; i < movieList.size(); i++){
			Date releaseDate = movieList.get(i).getReleaseDate();
			Date currentDate = new Date(System.currentTimeMillis());
			if (releaseDate.compareTo(currentDate) <= 0)
				addMovieToDisplay(i, true);
		}
	}
	
	//Method to add only advanced sales movies to display
	public void setAdvancedSalesMovies(){
		flowMoviePosters.getChildren().clear();
		flowMoviePosters.setAlignment(Pos.BASELINE_LEFT);
		
		for (int i=0; i < movieList.size(); i++){
			Date releaseDate = movieList.get(i).getReleaseDate();
			Date releaseDateMinus3 = new Date(releaseDate.getTime() - 3600000 * 72);
			Date currentDate = new Date(System.currentTimeMillis());
			if (releaseDate.compareTo(currentDate) >= 0 && releaseDateMinus3.compareTo(currentDate) <= 0)
				addMovieToDisplay(i, true);
		}
	}
	
	//Method to add only coming soon movies to display
	public void setComingSoonMovies(){
		flowMoviePosters.getChildren().clear();
		flowMoviePosters.setAlignment(Pos.BASELINE_LEFT);
		
		for (int i=0; i < movieList.size(); i++){
			Date releaseDate = movieList.get(i).getReleaseDate();
			Date releaseDateMinus3 = new Date(releaseDate.getTime() - 3600000 * 72);
			Date currentDate = new Date(System.currentTimeMillis());
			if (releaseDateMinus3.compareTo(currentDate) > 0)
				addMovieToDisplay(i, false);
		}
	}
	
	//Method which adds movie poster and buy ticket button according to movie index
	public void addMovieToDisplay(int index, boolean canBuyTickets){
		try{
			ImageView imgPoster = new ImageView(new Image(new FileInputStream("bin/" + movieList.get(index).getMoviePosterAddress())));
			imgPoster.setFitWidth(200);
			imgPoster.setFitHeight(300);
			if(canBuyTickets){
				WaveButton btnBuyTicket = new WaveButton("Buy Tickets");
				btnBuyTicket.setStyle("-fx-background-color: rgb(214, 142, 27); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
				btnBuyTicket.setMinWidth(200);
				btnBuyTicket.setOnAction(e -> {
					BaseLayout.getPaneBaseLayout().setCenter(new MovieTab(movieList.get(index)));
				});
				
				VBox vBoxPosterBuyTicket = new VBox();
				vBoxPosterBuyTicket.setAlignment(Pos.CENTER);
				vBoxPosterBuyTicket.getChildren().addAll(imgPoster, btnBuyTicket);
				
				flowMoviePosters.getChildren().add(vBoxPosterBuyTicket);
			}
			else{
				flowMoviePosters.getChildren().add(imgPoster);
			}
		}
		catch(FileNotFoundException ex){
			System.out.println("Hola amigos, image file at specified location does not exist!");
		}
	}
}
