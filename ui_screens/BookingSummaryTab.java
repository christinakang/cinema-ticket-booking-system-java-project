package ui_screens;

import java.util.ArrayList;
import java.util.TreeMap;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import larn.modernUI.WaveButton;
import real_objects.AddOns;
import real_objects.Cinema;
import real_objects.Movie;
import real_objects.MovieSession;
import real_objects.Seat;
import real_objects.SeatInfo;
import real_objects.Ticket;

public class BookingSummaryTab extends BorderPane{
	
	private static ArrayList<SeatInfo> selectedSeats = new ArrayList<>();
	private static ArrayList<Label> labSeatsList = new ArrayList<>();
	private static ArrayList<Label> labAddOnsList = new ArrayList<>();
	private static TreeMap<String, Integer> mapSeats = new TreeMap<>();
	private static TreeMap<String, Integer> mapAddOns = new TreeMap<>();
	private static ArrayList<SeatInfo> seatInfoList = new ArrayList<>();
	private static GridPane gridSeatsAddOns = new GridPane();
	private static Label labError = new Label();
	private static WaveButton btnContinue = new WaveButton("Continue");
	private WaveButton btnBack = new WaveButton("Back");
	private Label labAddOnsTitle = new Label("ADD ONS");
	private Label labUnitPrice = new Label("UNIT PRICE(RM)");
	private Label labQty = new Label("QUANTITY");
	private static ArrayList<Label> labBill = new ArrayList<>();
	private static Label labTotal = new Label("TOTAL: RM ");
	private Ticket ticket = new Ticket();
	private static MovieSession session = null;
	private static boolean paymentDetailsValid;
	private static double ticketTotal = 0;
	private static double tokenReduction = 0;
	private static double subTotal = 0;
	private static double totalForSeats = 0;
	private static final String[] billLabels = {"SubTotal(RM):", "Token Reduction(RM):", "GST 6%(RM)", "Booking Fee(RM)"};
	private static final double BOOKING_FEE = 3;
	
	public BookingSummaryTab(MovieSession session){
		//Setting default values on new movie session invocation
		seatInfoList.clear();
		gridSeatsAddOns.getChildren().clear();
		labSeatsList.clear();
		labAddOnsList.clear();
		labTotal.setText("TOTAL: RM ");
		ticketTotal = 0;
		mapSeats.clear();
		mapAddOns.clear();
		selectedSeats.clear();
		labBill.clear();
		labError.setText("");
		paymentDetailsValid = false;
		
		//Setting selected movie title and rating
		BookingSummaryTab.session = session;
		Movie movie = new Movie();
		ImageView imgRating = new ImageView("Images/MovieRating/" + movie.retrieveFileRecord(session.getMovieID()).getRate() + ".png");
		imgRating.setFitWidth(30);
		imgRating.setFitHeight(30);
		
		Label labMovieTitle = new Label(movie.getTitle().toUpperCase());
		labMovieTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		labMovieTitle.setGraphic(imgRating);
		labMovieTitle.setContentDisplay(ContentDisplay.RIGHT);
		
		//Setting selected cinema name
		Cinema cinema = new Cinema();
		Label labCinemaName = new Label("Cinema: " + cinema.retrieveFileRecord(session.getCinemaID()).getName());
		labCinemaName.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		//Setting available hall number
		Label labHall = new Label("Hall: " + session.getHallNumber());
		labHall.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		//Setting selected show time
		Label labShowTime = new Label("Show Time: " + session);
		labShowTime.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		//Adding cinema name, hall number and show time to HBox
		HBox hBoxSessionDetails = new HBox(40);
		hBoxSessionDetails.getChildren().addAll(labCinemaName, labHall, labShowTime);
		
		//Adding the above HBox, movie title and rating into VBox
		VBox vBoxSessionDetails = new VBox(5);
		vBoxSessionDetails.setPadding(new Insets(5, 5, 5, 5));
		vBoxSessionDetails.getChildren().addAll(labMovieTitle, hBoxSessionDetails);
		vBoxSessionDetails.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
		
		BorderPane paneBooking = new BorderPane();
		paneBooking.setTop(vBoxSessionDetails);
		paneBooking.setCenter(new SeatSelectionPane(session));
		
		//Setting session details and booking details in BorderPane
		setCenter(paneBooking);
		setRight(getBookingSummaryPane());
	}
	
	public BorderPane getBookingSummaryPane(){
		//Title for Booking Summary Pane
		Label labTitle = new Label("YOUR BOOKING");
		labTitle.setStyle("-fx-text-fill: lime; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Pane paneTitle = new Pane(labTitle);
		paneTitle.setStyle("-fx-backgound-color: rgba(0, 0, 0, 0.75);");
		
		//Label for seats selected
		Label labSeat = new Label("SEAT");
		labSeat.setStyle("-fx-text-fill: lime; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		//Label for unit price
		Label labUnitPrice = new Label("UNIT PRICE(RM)");
		labUnitPrice.setStyle("-fx-text-fill: lime; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
				
		//Label for quantity selected
		Label labQty = new Label("QUANTITY");
		labQty.setStyle("-fx-text-fill: lime; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		//Laying out labels in GridPane
		gridSeatsAddOns.setPadding(new Insets(5, 5, 5, 5));
		gridSeatsAddOns.setVgap(10);
		gridSeatsAddOns.setHgap(10);
		gridSeatsAddOns.add(labSeat, 0, 0);
		gridSeatsAddOns.add(labUnitPrice, 1, 0);
		gridSeatsAddOns.add(labQty, 2, 0);
		
		//Label for total price
		labTotal.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		//Error message when seat is not selected
		labError.setWrapText(true);
		labError.setTextAlignment(TextAlignment.CENTER);
		labError.setTextFill(Color.MISTYROSE);
		labError.setFont(Font.font("Courier", 18));
		
		//Continue and back buttons' actions
		btnContinue.setOnAction(e -> continueBooking());
		btnContinue.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
		btnContinue.setFont(Font.font("Courier", FontWeight.BOLD, 20));
		btnBack.setOnAction(e -> goBack());
		btnBack.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white;");
		btnBack.setFont(Font.font("Courier", FontWeight.BOLD, 20));
		
		//HBox for laying out back and continue buttons
		HBox hBoxButtons = new HBox(10);
		hBoxButtons.setAlignment(Pos.CENTER);
		hBoxButtons.getChildren().addAll(btnBack, btnContinue);
		
		//VBox for laying out total label, error label and hBoxButtons
		VBox vBoxButtons = new VBox(10);
		vBoxButtons.setAlignment(Pos.CENTER);
		vBoxButtons.setPadding(new Insets(5, 5, 5, 5));
		vBoxButtons.getChildren().addAll(labTotal, labError, hBoxButtons);
		
		//BorderPane for booking summary details
		BorderPane paneBookingSummary = new BorderPane();
		paneBookingSummary.setTop(paneTitle);
		paneBookingSummary.setCenter(gridSeatsAddOns);
		paneBookingSummary.setBottom(vBoxButtons);
		paneBookingSummary.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
		paneBookingSummary.setPadding(new Insets(15, 15, 15, 15));
		return paneBookingSummary;
	}
	
	//Method for processing continue action in all panes
	public void continueBooking(){
		if(((BorderPane)getCenter()).getCenter() instanceof SeatSelectionPane){
			if(selectedSeats.isEmpty()){
				labError.setText("Please choose your \nseats before continuing!");
			}
			else{
				labError.setText("");
				labAddOnsTitle.setStyle("-fx-text-fill: lime; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
				labUnitPrice.setStyle("-fx-text-fill: lime; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
				labQty.setStyle("-fx-text-fill: lime; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
				gridSeatsAddOns.addColumn(0, labAddOnsTitle);
				gridSeatsAddOns.addColumn(1, labUnitPrice);
				gridSeatsAddOns.addColumn(2, labQty);
				((BorderPane)getCenter()).setCenter(new AddOnsPane());
				totalForSeats = ticketTotal;
			}
		}
		else if (((BorderPane)getCenter()).getCenter() instanceof AddOnsPane){
			((BorderPane)getCenter()).setCenter(new PaymentPane());
			btnContinue.setText("Confirm");
			subTotal = ticketTotal;
			processPayment();
		}
		/*else if (((BorderPane)getCenter()).getCenter() instanceof PaymentPane){
			if(paymentDetailsValid)
				BaseLayout.getPaneBaseLayout().setCenter(new ConfirmationTab(ticket));
			else
				labError.setText("Payment details incomplete \nor invalid!");
		}*/
	}
	
	//Method for processing payment information and UI design for payment
	public static void processPayment(){
		for(Label labBill: labBill){
			gridSeatsAddOns.getChildren().remove(labBill);
		}
		labBill.clear();
		for(int i=0; i<8; i+=2){
			labBill.add(i, new Label());
			labBill.add(i + 1, new Label());
			labBill.get(i).setText(billLabels[i/2]);
			labBill.get(i).setStyle("-fx-text-fill: lime; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
			labBill.get(i + 1).setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
			gridSeatsAddOns.addColumn(0, labBill.get(i));
			gridSeatsAddOns.addColumn(2, labBill.get(i + 1));
			GridPane.setColumnSpan(labBill.get(i), 2);
			GridPane.setHalignment(labBill.get(i + 1), HPos.CENTER);
		}
		labBill.get(1).setText(String.format("%.2f", subTotal));
		labBill.get(3).setText(String.format("%.2f", tokenReduction));
		ticketTotal -= tokenReduction;
		double gst = ticketTotal * 0.06;
		ticketTotal += gst;
		ticketTotal += BOOKING_FEE;
		labTotal.setText("TOTAL: RM " + String.format("%.2f", ticketTotal));
		labBill.get(5).setText(String.format("%.2f", gst));
		labBill.get(7).setText(String.format("%.2f", BOOKING_FEE));
	}
	
	//Method for processing back action in all panes
	public void goBack(){
		if(((BorderPane)getCenter()).getCenter() instanceof SeatSelectionPane){
			BaseLayout.getPaneBaseLayout().setCenter(new HomeTab());
		}
		else if (((BorderPane)getCenter()).getCenter() instanceof AddOnsPane){
			((BorderPane)getCenter()).setCenter(new SeatSelectionPane(session));
			gridSeatsAddOns.getChildren().remove(labAddOnsTitle);
			gridSeatsAddOns.getChildren().remove(labUnitPrice);
			gridSeatsAddOns.getChildren().remove(labQty);
			for(Label labAddOns: labAddOnsList){
				gridSeatsAddOns.getChildren().remove(labAddOns);
			}
			labAddOnsList.clear();
			mapAddOns.clear();
			labError.setText("");
			ticketTotal = totalForSeats;
			labTotal.setText("TOTAL: RM " + String.format("%.2f", ticketTotal));
		}
		else if (((BorderPane)getCenter()).getCenter() instanceof PaymentPane){
			((BorderPane)getCenter()).setCenter(new AddOnsPane());
			btnContinue.setText("Continue");
			labError.setText("");
			for(Label labBill: labBill){
				gridSeatsAddOns.getChildren().remove(labBill);
			}
			labBill.clear();
			ticketTotal = subTotal;
			labTotal.setText("TOTAL: RM " + String.format("%.2f", ticketTotal));
		}
	}
	
	//Method for processing selected seat change and updating UI appropriately
	public static void seatListChange(){
		for(Label labSeats: labSeatsList){
			gridSeatsAddOns.getChildren().remove(labSeats);
		}
		
		labSeatsList.clear();
		for(String type: mapSeats.keySet()){
			if(mapSeats.get(type) != 0){
				//Calculating final seat price
				Seat seat = new Seat();
				seat.retrieveFileRecord(type);
				double seatPrice = seat.getPrice();
				Movie movie = new Movie();
				movie.retrieveFileRecord(session.getMovieID());
				double moviePriceRate = movie.getMoviePriceRate();
				double finalSeatPrice = seatPrice * moviePriceRate;
				
				Label labSeat = new Label(type);
				labSeat.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
				Label labUnitPrice = new Label(String.format("%.2f", finalSeatPrice));
				labUnitPrice.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
				Label labQty = new Label(String.valueOf(mapSeats.get(type)));
				labQty.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
				
				labSeatsList.add(labSeat);
				labSeatsList.add(labQty);
				labSeatsList.add(labUnitPrice);
				gridSeatsAddOns.addColumn(0, labSeat);
				gridSeatsAddOns.addColumn(1, labUnitPrice);
				gridSeatsAddOns.addColumn(2, labQty);
				GridPane.setHalignment(labQty, HPos.CENTER);
				GridPane.setHalignment(labUnitPrice, HPos.CENTER);
			}
		}
		
		labTotal.setText("TOTAL: RM " + String.format("%.2f", ticketTotal));
	}
	
	//Method for processing selected add ons change and updating UI appropriately
	public static void addOnsListChange(){
		for(Label labAddOns: labAddOnsList){
			gridSeatsAddOns.getChildren().remove(labAddOns);
		}
		
		labAddOnsList.clear();
		for(String addOnsID: mapAddOns.keySet()){
			if(mapAddOns.get(addOnsID) != 0){
				//Calculating addOns price
				AddOns addOns = new AddOns();
				addOns.retrieveFileRecord(addOnsID);
				double addOnsPrice = addOns.getPrice();
				
				Label labAddOns = new Label(addOns.getName());
				labAddOns.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
				Label labUnitPrice = new Label(String.format("%.2f", addOnsPrice));
				labUnitPrice.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
				Label labQty = new Label(String.valueOf(mapAddOns.get(addOnsID)));
				labQty.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: Courier; -fx-font-weight: bold;");
				
				labAddOnsList.add(labAddOns);
				labAddOnsList.add(labQty);
				labAddOnsList.add(labUnitPrice);
				gridSeatsAddOns.addColumn(0, labAddOns);
				gridSeatsAddOns.addColumn(1, labUnitPrice);
				gridSeatsAddOns.addColumn(2, labQty);
				GridPane.setHalignment(labQty, HPos.CENTER);
				GridPane.setHalignment(labUnitPrice, HPos.CENTER);
			}
		}
		
		labTotal.setText("TOTAL: RM " + String.format("%.2f", ticketTotal));
	}

	public static ArrayList<SeatInfo> getSelectedSeats() {
		return selectedSeats;
	}
	
	public static TreeMap<String, Integer> getMapSeats() {
		return mapSeats;
	}
	
	public static TreeMap<String, Integer> getMapAddOns() {
		return mapAddOns;
	}
	
	public static ArrayList<SeatInfo> getSeatInfoList() {
		return seatInfoList;
	}

	public static void setSeatInfoList(ArrayList<SeatInfo> seatInfoList) {
		BookingSummaryTab.seatInfoList = seatInfoList;
	}

	public static GridPane getGridSeatsAddOns() {
		return gridSeatsAddOns;
	}

	public static Label getLabError() {
		return labError;
	}

	public static boolean isPaymentDetailsValid() {
		return paymentDetailsValid;
	}

	public static void setPaymentDetailsValid(boolean paymentDetailsValid) {
		BookingSummaryTab.paymentDetailsValid = paymentDetailsValid;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public static MovieSession getSession() {
		return session;
	}

	public static void setSession(MovieSession session) {
		BookingSummaryTab.session = session;
	}

	public static double getTicketTotal() {
		return ticketTotal;
	}

	public static void setTicketTotal(double ticketTotal) {
		BookingSummaryTab.ticketTotal = ticketTotal;
	}

	public static WaveButton getBtnContinue() {
		return btnContinue;
	}
}
