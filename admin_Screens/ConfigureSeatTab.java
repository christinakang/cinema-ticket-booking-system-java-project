package admin_Screens;

import java.io.File;
import java.util.ArrayList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import larn.modernUI.WaveButton;
import real_objects.Cinema;
import real_objects.MovieSession;
import real_objects.Seat;
import real_objects.SeatInfo;
import real_objects.SeatPane;

public class ConfigureSeatTab extends BorderPane{
	private ComboBox<Cinema> comboCinemas = new ComboBox<>();
	private ComboBox<Integer> comboHalls = new ComboBox<>();
	private ArrayList<Image> seatStatusImageList = new ArrayList<>();
	private GridPane gridLeftSeats = new GridPane();
	private GridPane gridCenterSeats = new GridPane();
	private GridPane gridRightSeats = new GridPane();
	private GridPane gridSeatDetail = new GridPane();
	private HBox hBoxSeatColumns = new HBox(50);
	private WaveButton btnModify = new WaveButton("Modify");
	private WaveButton btnSave = new WaveButton("Save");
	private WaveButton btnAddHall = new WaveButton("Add Hall");
	private WaveButton btnDeleteHall = new WaveButton("Delete Hall");
	private Image imageEmptySpace = new Image("Images/Seat/EmptySpace.png");
	private ArrayList<SeatInfo> seatInfoList = new ArrayList<>();
	private Label labError = new Label();
	private int numOfRows = 10;
	private int numOfColumns = 18;
	private int leftColumns = 4;
	private int rightColumns = 4;
	private boolean isNewHallConfig = false;
	private static final int MAX_ROW = 26;
	private static final int MAX_COLUMN = 30;
	
	public ConfigureSeatTab(){
		//ComboBox for choosing cinema
		comboCinemas.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboCinemas.setPromptText("Select Cinema");
		comboCinemas.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboCinemas.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		for(Cinema cinema: Cinema.retrieveAllFileRecords())
			comboCinemas.getItems().add(cinema);
		comboCinemas.setOnAction(e -> comboCinemaChange());
		
		//ComboBox for choosing hall within the selected cinema
		comboHalls.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboHalls.setPromptText("Select Hall");
		comboHalls.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboHalls.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		comboHalls.setOnAction(e -> comboHallChange());
		
		//Button to modify hall layout
		btnModify.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		btnModify.setOnAction(e -> enableModification());
		btnModify.setDisable(true);
		
		//Button to save modified hall layout
		btnSave.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		btnSave.setOnAction(e -> saveConfiguration());
		btnSave.setDisable(true);
		
		//Button to add new hall layout to selected cinema
		btnAddHall.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		btnAddHall.setOnAction(e -> addHall());
		btnAddHall.setDisable(true);
		
		//Button to delete existing hall layout in selected cinema
		btnDeleteHall.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		btnDeleteHall.setOnAction(e -> deleteHall());
		btnDeleteHall.setDisable(true);
		
		//Label for error message display
		labError.setTextFill(Color.MISTYROSE);
		labError.setFont(Font.font("Courier", FontWeight.BOLD, 15));
		
		//Adding above UI controls to HBox
		FlowPane flowHeader = new FlowPane(20, 20);
		flowHeader.setPadding(new Insets(5, 5, 5, 5));
		flowHeader.getChildren().addAll(comboCinemas, comboHalls, btnAddHall, btnDeleteHall, btnModify, btnSave);
		
		//GridPane for adding seat type
		gridSeatDetail.setPadding(new Insets(5, 5, 5, 5));
		gridSeatDetail.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35)");
		gridSeatDetail.setHgap(10);
		gridSeatDetail.setVgap(10);
		
		//Adding all seat types to GridPane
		int rowIndex = 0;
		for(String type: Seat.getSeatTypes()){
			Seat seat = new Seat();
			seat.retrieveFileRecord(type);
			ImageView imgSeat = new ImageView(seat.getImgAddress());
			imgSeat.setOnMouseEntered(e -> setCursor(Cursor.HAND));
			imgSeat.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
			
			if(type.equals("Regular") || type.equals("Premier")){
				imgSeat.setFitWidth(40);
				imgSeat.setFitHeight(40);
			}
			else if(type.equals("Regular Twin") || type.equals("Premier Twin")){
				imgSeat.setFitWidth(85);
				imgSeat.setFitHeight(40);
			}
			else{
				imgSeat.setFitWidth(85);
				imgSeat.setFitHeight(55);
			}
			
			Pane seatPane = new Pane(imgSeat);
			imgSeat.setOnMouseDragged(e -> {
				imgSeat.setLayoutX(e.getX());
				imgSeat.setLayoutY(e.getY());
			});
			
			Label labSeatType = new Label(type);
			labSeatType.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
			
			gridSeatDetail.add(seatPane, 1, rowIndex);
			gridSeatDetail.add(labSeatType, 0, rowIndex);
			
			rowIndex++;
		}
		//Adding under repair seats label to the GridPane
		ImageView imageRepairSeat = new ImageView("Images/Seat/RegularRepair.png");
		imageRepairSeat.setFitWidth(40);
		imageRepairSeat.setFitHeight(40);
		imageRepairSeat.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		imageRepairSeat.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		Label labRepairSeat = new Label("Under Repair");
		labRepairSeat.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		gridSeatDetail.add(imageRepairSeat, 1, rowIndex);
		gridSeatDetail.add(labRepairSeat, 0, rowIndex);
		rowIndex++;
		
		//Adding delete bin label to the GridPane
		ImageView imageDelete = new ImageView("Images/Icons/Delete.png");
		imageDelete.setFitWidth(40);
		imageDelete.setFitHeight(40);
		imageDelete.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		imageDelete.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		Label labDelete = new Label("Delete Seat");
		labDelete.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Courier; -fx-font-weight: bold;");
		gridSeatDetail.add(imageDelete, 1, rowIndex);
		gridSeatDetail.add(labDelete, 0, rowIndex);
		
		//Preparing hall outline and default seat configuration
		setHallOutline();
		getDefaultSeatConfig(imageEmptySpace);
		
		//Load all seats' status images
		loadSeatStatusImages();
		
		//Setting the sides of the BorderPane
		setTop(flowHeader);
		setLeft(gridSeatDetail);
	}
	
	//Method to process cinema change in the ComboBox
	public void comboCinemaChange(){
		comboHalls.getItems().clear();
		labError.setText("");
		if(comboCinemas.getValue() != null){
			if(gridSeatDetail.getChildren().size() <= 14)
				btnAddHall.setDisable(false);
			btnDeleteHall.setDisable(true);
			btnModify.setDisable(true);
			btnSave.setDisable(true);
			for(int i=0; i<comboCinemas.getValue().getNumberOfHalls(); i++){
				comboHalls.getItems().add(i + 1);
			}
			numOfRows = 10;
			numOfColumns = 18;
			leftColumns = 4;
			rightColumns = 4;
			getDefaultSeatConfig(imageEmptySpace);
		}
	}
	
	//Method to process hall change in the ComboBox to display existing hall configuration
	public void comboHallChange(){
		//Retrieving seat configuration from file
		if(comboHalls.getValue() != null){
			labError.setText("");
			seatInfoList.clear();
			if(SeatInfo.loadHallRecord(seatInfoList, "Cinema Files/SeatConfiguration/" + comboCinemas.getValue().getCinemaID() + "/Hall " + comboHalls.getValue() + ".dat")){
				numOfColumns = SeatInfo.getNumOfColumns();
				numOfRows = SeatInfo.getNumOfRows();
				leftColumns = SeatInfo.getLeftColumns();
				rightColumns = SeatInfo.getRightColumns();
				
				getSeatConfig();
				
				//Making buttons available after loading existing hall configuration
				if(gridSeatDetail.getChildren().size() <= 14)
					btnAddHall.setDisable(false);
				boolean bookedSeatPresent = false;
				for(SeatInfo seatInfo: seatInfoList){
					if(seatInfo.getStatus().equals("Booked")){
						bookedSeatPresent = true;
						break;
					}
				}
				if(!bookedSeatPresent){
					btnDeleteHall.setDisable(false);
					btnModify.setDisable(false);
				}
			}
			else
				labError.setText("Hall configuration file could not be retrieved!");
		}
	}
	
	//Method to enable modification of seat configuration
	public void enableModification(){
		btnModify.setDisable(true);
		btnSave.setDisable(false);
		
		
	}
	
	//Method to save modified seat configuration 
	public void saveConfiguration(){
		//Saving new seat configuration to new hall file
		if(isNewHallConfig){
			int hallNum = comboCinemas.getValue().getNumberOfHalls() + 1;
			SeatInfo.setNumOfColumns(numOfColumns);
			SeatInfo.setNumOfRows(numOfRows);
			SeatInfo.setRightColumns(rightColumns);
			SeatInfo.setLeftColumns(leftColumns);
			SeatInfo.writeHallFile(seatInfoList, "Cinema Files/SeatConfiguration/" + comboCinemas.getValue().getCinemaID() + "/Hall " + hallNum + ".dat");
			comboCinemas.getValue().setNumberOfHalls(hallNum);
			comboCinemas.getValue().updateFileRecord();
			isNewHallConfig = false;
			
			//Updating the interface after saving seat configuration
			comboHalls.getItems().clear();
			for(int i=0; i<hallNum; i++){
				comboHalls.getItems().add(i + 1);
			}
			comboHalls.setValue(hallNum);
		}
		//Saving modified seat configuration to existing hall file
		else{
			Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to modify this hall configuration?\nThis action cannot be undone!", ButtonType.YES, ButtonType.NO);
			alert.showAndWait();
			if(alert.getResult() == ButtonType.YES)
				SeatInfo.writeHallFile(seatInfoList, "Cinema Files/SeatConfiguration/" + comboCinemas.getValue().getCinemaID() + "/Hall " + comboHalls.getValue() + ".dat");
		}
		btnModify.setDisable(false);
		btnDeleteHall.setDisable(false);
		btnSave.setDisable(true);
	}
	
	//Method to delete hall configuration from selected cinema
	public void deleteHall(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to delete this hall configuration?\nThis action cannot be undone!", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		
		if(alert.getResult() == ButtonType.YES){
			btnDeleteHall.setDisable(true);
			btnModify.setDisable(true);
			btnSave.setDisable(true);
			//Deleting the selected hall file and updating the selected cinema
			File seatConfigDirectory = new File("Cinema Files/SeatConfiguration/" + comboCinemas.getValue().getCinemaID());
			int selectedHallNum = comboHalls.getValue();
			File hallFileInDeletion = new File(seatConfigDirectory, "Hall " + selectedHallNum + ".dat");
			hallFileInDeletion.delete();
			comboCinemas.getValue().setNumberOfHalls(comboCinemas.getValue().getNumberOfHalls() - 1);
			comboCinemas.getValue().updateFileRecord();
			
			//Deleting all related movie sessions in the deleted hall
			for(MovieSession session: MovieSession.retrieveAllFileRecords()){
				if(session.getHallNumber() == selectedHallNum)
					session.deleteFileRecord();
			}
			
			//Renaming all other hall files respectively
			File hallFile = new File(seatConfigDirectory, "Hall " + ++selectedHallNum + ".dat");
			while(hallFile.exists()){
				hallFile.renameTo(new File(seatConfigDirectory, "Hall " + (selectedHallNum - 1) + ".dat"));
				hallFile = new File(seatConfigDirectory, "Hall " + ++selectedHallNum + ".dat");
			}
			
			//Updating hall combo list and setting default hall configuration
			comboHalls.getItems().clear();
			for(int i=0; i<comboCinemas.getValue().getNumberOfHalls(); i++){
				comboHalls.getItems().add(i + 1);
			}
			numOfRows = 10;
			numOfColumns = 18;
			leftColumns = 4;
			rightColumns = 4;
			getDefaultSeatConfig(imageEmptySpace);
		}
	}
	
	//Method to add new hall to selected cinema
	public void addHall(){
		btnAddHall.setDisable(true);
		leftColumns = 0;
		rightColumns = 0;
		
		//ComboBox for choosing number of rows
		ComboBox<Integer> comboRows = new ComboBox<>();
		comboRows.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboRows.setPromptText("Select Total Rows");
		comboRows.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboRows.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		comboRows.setOnAction(e -> numOfRows = comboRows.getValue());
		for(int i=0; i<MAX_ROW; i++)
			comboRows.getItems().add(i + 1);
		
		//ComboBox for choosing number of columns
		ComboBox<Integer> comboColumns = new ComboBox<>();
		comboColumns.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboColumns.setPromptText("Select Total Columns");
		comboColumns.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboColumns.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		for(int i=0; i<MAX_COLUMN; i++)
			comboColumns.getItems().add(i + 1);
		
		//Label for error messages
		Text txtError = new Text();
		txtError.setFill(Color.MISTYROSE);
		txtError.setFont(Font.font("Courier", 15));
		
		//ComboBox for choosing number of left columns
		ComboBox<Integer> comboLeftColumns = new ComboBox<>();
		comboLeftColumns.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboLeftColumns.setPromptText("Select Left Columns");
		comboLeftColumns.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboLeftColumns.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		comboLeftColumns.setOnAction(e -> {
			if(comboLeftColumns.getValue() != null)
				leftColumns = comboLeftColumns.getValue();
			else
				leftColumns = 0;
			txtError.setText("");
		});
		
		//ComboBox for choosing number of right columns
		ComboBox<Integer> comboRightColumns = new ComboBox<>();
		comboRightColumns.setStyle("-fx-background-color: lightseagreen; -fx-font-family: Courier; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: white;");
		comboRightColumns.setPromptText("Select Right Columns");
		comboRightColumns.setOnMouseEntered(e -> setCursor(Cursor.HAND));
		comboRightColumns.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
		comboRightColumns.setOnAction(e -> {
			if(comboRightColumns.getValue() != null)
				rightColumns = comboRightColumns.getValue();
			else
				rightColumns = 0;
			txtError.setText("");
		});
		
		//Button for creating hall with the desired seat configuration
		WaveButton btnCreateHall = new WaveButton("Create Hall");
		btnCreateHall.setStyle("-fx-background-color: lightseagreen; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-family: Courier; -fx-font-weight: bold;");
		btnCreateHall.setDisable(true);
		btnCreateHall.setOnAction(e -> {
			if(comboLeftColumns.getValue() == null)
				leftColumns = 0;
			if(comboRightColumns.getValue() == null)
				rightColumns = 0;
			if(rightColumns + leftColumns <= numOfColumns){
				Image imageRegularAvailable = new Image("Images/Seat/RegularAvailable.png");
				gridSeatDetail.getChildren().removeAll(comboRows, comboColumns, comboLeftColumns, comboRightColumns, btnCreateHall, txtError);
				getDefaultSeatConfig(imageRegularAvailable);
				isNewHallConfig = true;
				
				btnDeleteHall.setDisable(true);
				btnAddHall.setDisable(false);
				btnModify.setDisable(false);
				btnSave.setDisable(false);
			}
			else
				txtError.setText("Left and right columns higher \nthan total number of columns!");
		});
		
		//Setting maximum left and right columns to be configured
		comboColumns.setOnAction(e -> {
			numOfColumns = comboColumns.getValue();
			comboLeftColumns.getItems().clear();
			comboRightColumns.getItems().clear();
			txtError.setText("");
			
			for(int i=0; i<=numOfColumns; i++)
				comboLeftColumns.getItems().add(i);
			for(int i=0; i<=numOfColumns; i++)
				comboRightColumns.getItems().add(i);
			if(comboRows.getValue() != null)
				btnCreateHall.setDisable(false);
		});
		gridSeatDetail.addColumn(0, comboRows, comboColumns, comboLeftColumns, comboRightColumns, btnCreateHall, txtError);
	}
	
	//Method to set hall outline design
	public void setHallOutline(){
		//Setting appropriate spacing for all seat grids and identifying grids
		gridLeftSeats.setHgap(5);
		gridLeftSeats.setVgap(10);
		gridLeftSeats.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35);");
		gridLeftSeats.setPadding(new Insets(5, 5, 5, 5));
		gridRightSeats.setHgap(5);
		gridRightSeats.setVgap(10);
		gridRightSeats.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35);");
		gridRightSeats.setPadding(new Insets(5, 5, 5, 5));
		gridCenterSeats.setHgap(5);
		gridCenterSeats.setVgap(10);
		gridCenterSeats.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35);");
		gridCenterSeats.setPadding(new Insets(5, 5, 5, 5));
		gridCenterSeats.setGridLinesVisible(true);
		gridLeftSeats.setGridLinesVisible(true);
		gridRightSeats.setGridLinesVisible(true);
		
		//HBox for adding all hall elements
		hBoxSeatColumns.setAlignment(Pos.CENTER);
		hBoxSeatColumns.getChildren().addAll(gridLeftSeats, gridCenterSeats, gridRightSeats);
		
		//Label for indicating movie screen position
		Text txtScreen = new Text("Movie Screen");
		txtScreen.setStyle("-fx-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
		StackPane paneScreen = new StackPane(txtScreen);
		paneScreen.setStyle("-fx-background-color: rgba(0, 0, 0, 0.9);");
		paneScreen.setPadding(new Insets(10, 10, 10, 10));
		paneScreen.setMaxWidth(600);
		
		//VBox for displaying seats with screen
		VBox vBoxSeatWithScreen = new VBox(50);
		vBoxSeatWithScreen.getChildren().addAll(hBoxSeatColumns, paneScreen);
		vBoxSeatWithScreen.setPadding(new Insets(5, 5, 5, 5));
		vBoxSeatWithScreen.setAlignment(Pos.CENTER);
		setCenter(vBoxSeatWithScreen);
	}
	
	//Method to set hall seat configuration based on existing hall
	public void getSeatConfig(){
		gridLeftSeats.getChildren().clear();
		gridCenterSeats.getChildren().clear();
		gridRightSeats.getChildren().clear();
		Text txtColumn = null;
		int index = 0;
		
		//Setting seat configuration based on total rows, total columns, left and right columns
		for(int i=0; i<numOfRows; i++){
			Text txtRow = new Text(String.valueOf((char)('A' + i)));
			txtRow.setStyle("-fx-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
			GridPane.setHalignment(txtRow, HPos.CENTER);
			
			for(int j=0; j<numOfColumns; j++){
				SeatInfo seatInfo = seatInfoList.get(index);
				SeatPane seatPane = new SeatPane(getSeatStatusImage(seatInfo));
				seatPane.setSeatInfo(seatInfo);
				
				if(seatInfo.getStatus().equals("Hidden"))
					seatPane.setVisible(false);
				
				if(i == 0){
					txtColumn = new Text(String.valueOf(j + 1));
					txtColumn.setStyle("-fx-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
					GridPane.setHalignment(txtColumn, HPos.CENTER);
				}
				
				if(j>=0 && j<leftColumns){
					if(i == 0)
						gridLeftSeats.add(txtColumn, j + 1, i);
					if(j == 0)
						gridLeftSeats.add(txtRow, j, i + 1);
					gridLeftSeats.add(seatPane, j + 1, i + 1);
				}
				else if(j>=numOfColumns-rightColumns && j<numOfColumns){
					if(i == 0)
						gridRightSeats.add(txtColumn, j-(numOfColumns-rightColumns) + 1, i);
					if(j == 0)
						gridRightSeats.add(txtRow, j, i + 1);
					gridRightSeats.add(seatPane, j-(numOfColumns-rightColumns) + 1, i + 1);
				}
				else{
					if(i == 0)
						gridCenterSeats.add(txtColumn, j-leftColumns + 1, i);
					if(j == 0)
						gridCenterSeats.add(txtRow, j, i + 1);
					gridCenterSeats.add(seatPane, j-leftColumns + 1, i + 1);
				}
				if(seatInfo.getType() == "Premier Twin" || seatInfo.getType() == "Regular Twin" || seatInfo.getType() == "Luxury"){
					seatPane.setFitWidth(85);
					if(seatInfo.getType() == "Luxury")
						seatPane.setFitHeight(55);
					j++;
				}
				index++;
			}
		}
		
		//Adding only columns with seats present
		hBoxSeatColumns.getChildren().clear();
		if(!gridLeftSeats.getChildren().isEmpty())
			hBoxSeatColumns.getChildren().add(gridLeftSeats);
		if(!gridCenterSeats.getChildren().isEmpty())
			hBoxSeatColumns.getChildren().add(gridCenterSeats);
		if(!gridRightSeats.getChildren().isEmpty())
			hBoxSeatColumns.getChildren().add(gridRightSeats);
	}
	
	//Getting seat image based on seat type and seat status
	public Image getSeatStatusImage(SeatInfo seatInfo){
		String type = seatInfo.getType();
		String status = seatInfo.getStatus();
		int index = 0;
		if (type.equals("Regular"))
			index = 0;
		else if (type.equals("Regular Twin"))
			index = 3;
		else if (type.equals("Premier"))
			index = 6;
		else if (type.equals("Premier Twin"))
			index = 9;
		else if (type.equals("Luxury"))
			index = 12;
		
		if (status.equals("Booked"))
			index++;
		else if (status.equals("Repair"))
			index+=2;
		
		return seatStatusImageList.get(index);
	}
	
	//Method to set default hall seat configuration
	public void getDefaultSeatConfig(Image imageSeat){
		gridLeftSeats.getChildren().clear();
		gridCenterSeats.getChildren().clear();
		gridRightSeats.getChildren().clear();
		seatInfoList.clear();
		Text txtColumn = null;
		
		//Setting seat configuration based on total rows, total columns, left and right columns
		for(int i=0; i<numOfRows; i++){
			Text txtRow = new Text(String.valueOf((char)('A' + i)));
			txtRow.setStyle("-fx-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
			GridPane.setHalignment(txtRow, HPos.CENTER);
			
			for(int j=0; j<numOfColumns; j++){
				//Adding each seat to an array list
				SeatInfo seat = new SeatInfo();
				seat.setSeatNumber(String.valueOf((char)('A' + i)) + String.valueOf(j + 1));
				seatInfoList.add(seat);
				SeatPane seatPane = new SeatPane(imageSeat);
				seatPane.setSeatInfo(seat);
				
				if(i == 0){
					txtColumn = new Text(String.valueOf(j + 1));
					txtColumn.setStyle("-fx-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
					GridPane.setHalignment(txtColumn, HPos.CENTER);
				}
				
				if(j>=0 && j<leftColumns){
					if(i == 0)
						gridLeftSeats.add(txtColumn, j + 1, i);
					if(j == 0)
						gridLeftSeats.add(txtRow, j, i + 1);
					gridLeftSeats.add(seatPane, j + 1, i + 1);
				}
				else if(j>=numOfColumns-rightColumns && j<numOfColumns){
					if(i == 0)
						gridRightSeats.add(txtColumn, j-(numOfColumns-rightColumns) + 1, i);
					if(j == 0)
						gridRightSeats.add(txtRow, j, i + 1);
					gridRightSeats.add(seatPane, j-(numOfColumns-rightColumns) + 1, i + 1);
				}
				else{
					if(i == 0)
						gridCenterSeats.add(txtColumn, j-leftColumns + 1, i);
					if(j == 0)
						gridCenterSeats.add(txtRow, j, i + 1);
					gridCenterSeats.add(seatPane, j-leftColumns + 1, i + 1);
				}
			}
		}
		
		//Adding only columns with seats present
		hBoxSeatColumns.getChildren().clear();
		if(!gridLeftSeats.getChildren().isEmpty())
			hBoxSeatColumns.getChildren().add(gridLeftSeats);
		if(!gridCenterSeats.getChildren().isEmpty())
			hBoxSeatColumns.getChildren().add(gridCenterSeats);
		if(!gridRightSeats.getChildren().isEmpty())
			hBoxSeatColumns.getChildren().add(gridRightSeats);
	}
	
	//Method to load all seats' status images
	private void loadSeatStatusImages(){
		for(String seatType: Seat.getSeatTypes()){
			Seat seat = new Seat();
			seat.retrieveFileRecord(seatType);
			for(int i=0; i<3; i++){
				String imgAddress = seat.getImgAddress();
				seatStatusImageList.add(new Image(imgAddress.replace("Available", SeatInfo.getStatusString()[i])));
			}
		}
	}
}