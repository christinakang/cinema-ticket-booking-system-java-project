package ui_screens;

import java.util.Date;
import java.util.TreeMap;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import real_objects.AddOns;
import real_objects.Cinema;
import real_objects.Movie;
import real_objects.MovieSession;
import real_objects.Payment;
import real_objects.Ticket;

public class ConfirmationTab extends BorderPane{
	
	public ConfirmationTab(Ticket ticket)
	{
		MovieSession movieSession = new MovieSession();
		movieSession.retrieveFileRecord(ticket.getMovieSessionID());
		
		Movie movie = new Movie();
		movie.retrieveFileRecord(movieSession.getMovieID());
		
		Cinema cinema1 = new Cinema();
		cinema1.retrieveFileRecord(movieSession.getCinemaID());
		
		TreeMap<String, Integer> addOnsMap = BookingSummaryTab.getMapAddOns();
		
		Payment payment = new Payment();
		payment.retrieveFileRecord(ticket.getPaymentID());
		
		Label topTitle = new Label("Self-Print Ticket");
		topTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		ImageView imageView = new ImageView("Images/TicketQRCodes/QRCodeIcon.png");
		imageView.setFitWidth(100);
		imageView.setFitHeight(100);
		
		HBox hBoxQRTitle = new HBox(20);
		hBoxQRTitle.setPadding(new Insets(5, 5, 5, 5));
		hBoxQRTitle.getChildren().addAll(topTitle, imageView);
		setTop(hBoxQRTitle);
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(15);
		gridPane.setVgap(15);
		gridPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35)");
		
		Label confirmationID = new Label("Confirmation ID:");
		confirmationID.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbConfirmationID = new Label(ticket.getBookingID());
		lbConfirmationID.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		Label movieTitle = new Label("Movie Title:");
		movieTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbMovieTitle = new Label(movie.getTitle());
		lbMovieTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		Label cinema = new Label("Cinema:");
		cinema.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbCinema = new Label(cinema1.getName());
		lbCinema.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		Label hall = new Label("Hall:");
		hall.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbHall = new Label(movieSession.getHallNumber() + "");
		lbHall.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		Label movieDate = new Label("Movie Show Time:");
		movieDate.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbMovieDate = new Label(movieSession + "");
		lbMovieDate.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		ImageView image = new ImageView("Images/MovieRating/" + movie.getRate() + ".png");
		image.setFitWidth(30);
		image.setFitHeight(30);
		Label movieClassification = new Label("Movie Classification");
		movieClassification.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbMovieClassification = new Label();
		lbMovieClassification.setGraphic(image);
		
		String seatNumbers = "";
		
		for(String type: BookingSummaryTab.getMapSeats().keySet())
		{
			seatNumbers += type + " - " + BookingSummaryTab.getMapSeats().get(type) + "\n";
		}
		
		String addOnss = "";
		
		for(String addOnsID: addOnsMap.keySet())
		{
			AddOns addOns = new AddOns();
			addOns.retrieveFileRecord(addOnsID);
			addOnss += addOns.getName() + " - " + addOnsMap.get(addOnsID) + "\n";
		}
		
		Label seatNumber = new Label("Seat No.:");
		seatNumber.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbSeatNumber = new Label(seatNumbers);
		lbSeatNumber.setWrapText(true);
		lbSeatNumber.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		Label amount = new Label("Amount: ");
		amount.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbAmount = new Label(String.format("RM %.2f", payment.getTotalAmountPaid()));
		lbAmount.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		Label transactionDate = new Label("Transaction Date: ");
		transactionDate.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbTransactionDate = new Label(new Date(System.currentTimeMillis()) + "");
		lbTransactionDate.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		Label numberOfTicket = new Label("No. of Ticket:");
		numberOfTicket.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbNumberOfTicket = new Label(BookingSummaryTab.getMapSeats().size() + "");
		lbNumberOfTicket.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		Label token = new Label("Movie Tokens Used:");
		token.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbToken = new Label(payment.getMovieTokensUsed() + "");
		lbToken.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		Label addOns = new Label("AddOns:");
		addOns.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbAddOns = new Label(addOnss);
		lbAddOns.setWrapText(true);
		lbAddOns.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		Label paymentType = new Label("Payment Type:");
		paymentType.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		Label lbPaymentType = new Label("xxxx-xxxx-xxxx-" + payment.getCardNumber()%10000);
		lbPaymentType.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		
		gridPane.add(confirmationID, 0, 0);
		gridPane.add(lbConfirmationID, 1, 0);
		
		gridPane.add(movieTitle, 0, 1);
		gridPane.add(lbMovieTitle, 1, 1);
		
		gridPane.add(cinema, 0, 2);
		gridPane.add(lbCinema, 1, 2);
		
		gridPane.add(hall, 0, 3);
		gridPane.add(lbHall, 1, 3);
		
		gridPane.add(movieDate, 0, 4);
		gridPane.add(lbMovieDate, 1, 4);
		
		gridPane.add(movieClassification, 0, 5);
		gridPane.add(lbMovieClassification, 1, 5);
		
		gridPane.add(seatNumber, 0, 6);
		gridPane.add(lbSeatNumber, 1, 6);
		
		gridPane.add(addOns, 0, 7);
		gridPane.add(lbAddOns, 1, 7);
		
		gridPane.add(amount, 0, 8);
		gridPane.add(lbAmount, 1, 8);
		
		gridPane.add(transactionDate, 0, 9);
		gridPane.add(lbTransactionDate, 1, 9);
		
		gridPane.add(token, 0, 10);
		gridPane.add(lbToken, 1, 10);
		
		gridPane.add(numberOfTicket, 0, 11);
		gridPane.add(lbNumberOfTicket, 1, 11);
		
		gridPane.add(paymentType, 0, 12);
		gridPane.add(lbPaymentType, 1, 12);
		
		setCenter(gridPane);
	}
}
