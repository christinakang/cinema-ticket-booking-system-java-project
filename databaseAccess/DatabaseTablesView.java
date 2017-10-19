package databaseAccess;

import java.io.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import larn.modernUI.WaveButton;
import real_objects.*;

public class DatabaseTablesView extends Application{
	private Label labTableName = new Label();
	private Label labStatusMessage = new Label();
	private WaveButton btnAddRow = new WaveButton("Add Row");
	private WaveButton btnUpdateFile = new WaveButton("Update File");
	private GridPane gridCenter = new GridPane();
	private GridPane gridTable = new GridPane();
	private HBox hBoxInputFields;
	private ComboBox<String> comboTableNames = new ComboBox<>();
	private ObservableList<Node> inputFields = null;
	private DatabaseAccessUpdate cinemaObj = null;
	private ObservableList<Node> listOfInputFields = FXCollections.observableArrayList();
	private static int numOfDataRows = 1;
	
	public void start(Stage stage){
		
		gridTable.setVgap(5);
		gridTable.setHgap(5);
		gridTable.setStyle("-fx-background-color: lightgray;");
		
		labTableName.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
		labStatusMessage.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		labStatusMessage.setVisible(false);
		
		comboTableNames.setPromptText("Select Table View");
		comboTableNames.setStyle("-fx-background-color: lightgreen; -fx-font-weight: bold; -fx-font-size: 18; -fx-font-family: Times New Roman;");
		comboTableNames.getItems().addAll("Customer Table", "Admin Table", "Cinema Table", "Movie Table", "Movie Session Table", "Seat Table", "Ticket Table", "AddOns Table", "Payment Table", "Privilege Table", "Promotion Table");
		comboTableNames.setOnAction(e -> updateTableView(comboTableNames.getValue()));
		
		BorderPane headerPane = new BorderPane();
		headerPane.setPadding(new Insets(5, 5, 5, 5));
		headerPane.setLeft(labTableName);
		headerPane.setCenter(labStatusMessage);
		BorderPane.setAlignment(labStatusMessage, Pos.CENTER);
		headerPane.setRight(comboTableNames);
		
		btnAddRow.setOnAction(e -> {
			addRow(inputFields);
			gridCenter.getChildren().remove(hBoxInputFields);
			loadInputFields();
		});
		btnAddRow.setOnMouseEntered(e -> btnAddRow.setCursor(Cursor.HAND));
		btnAddRow.setOnMouseExited(e -> btnAddRow.setCursor(Cursor.DEFAULT));
		btnAddRow.setVisible(false);
		
		btnUpdateFile.setOnAction(e -> updateFile());
		btnUpdateFile.setOnMouseEntered(e -> btnUpdateFile.setCursor(Cursor.HAND));
		btnUpdateFile.setOnMouseExited(e -> btnUpdateFile.setCursor(Cursor.DEFAULT));
		btnUpdateFile.setVisible(false);
		
		gridCenter.setVgap(10);
		gridCenter.setHgap(5);
		gridCenter.add(gridTable, 0, 0);
		gridCenter.add(btnAddRow, 0, 2);
		gridCenter.add(btnUpdateFile, 0, 3);
		GridPane.setHalignment(btnAddRow, HPos.RIGHT);
		GridPane.setColumnSpan(btnAddRow, Integer.MAX_VALUE);
		GridPane.setHalignment(btnUpdateFile, HPos.RIGHT);
		GridPane.setColumnSpan(btnUpdateFile, Integer.MAX_VALUE);
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(5, 5, 5, 5));
		pane.setTop(headerPane);
		pane.setCenter(gridCenter);
		
		ScrollPane scrollPane = new ScrollPane(pane);
		
		Scene scene = new Scene(scrollPane);
		stage.setScene(scene);
		stage.setTitle("Database Tables View");
		stage.setMaximized(true);
		stage.show();
	}
	
	public void updateTableView(String tableName){
		btnAddRow.setVisible(true);
		btnUpdateFile.setVisible(true);
		numOfDataRows = 1;
		setTableParameters(tableName);
		
		gridTable.getChildren().clear();
		ObservableList<String> tableColumns = null;
		tableColumns = cinemaObj.getTableColumns();
		for (String tableColumn: tableColumns){
			Label labColumnName = new Label(tableColumn);
			labColumnName.setStyle("-fx-font-family: Courier; -fx-font-size: 18px; -fx-font-weight: bold;");
			gridTable.addRow(0, labColumnName);
		}
		
		listOfInputFields.clear();
		getRecordsFromFile();
		loadInputFields();
	}
	
	public void setTableParameters(String tableName){
		for (String tableNameFromList: comboTableNames.getItems()){
			if (tableNameFromList.equals(tableName)){
				labTableName.setText(tableName);
				break;
			}
		}
		if (tableName.equals("Customer Table"))
			cinemaObj = new Customer();
		else if (tableName.equals("Admin Table"))
			cinemaObj = new Admin();
		else if (tableName.equals("Cinema Table"))
			cinemaObj = new Cinema();
		else if (tableName.equals("Movie Table"))
			cinemaObj = new Movie();
		else if (tableName.equals("Movie Session Table"))
			cinemaObj = new MovieSession();
		else if (tableName.equals("Seat Table"))
			cinemaObj = new Seat();
		else if (tableName.equals("Ticket Table"))
			cinemaObj = new Ticket();
		else if (tableName.equals("AddOns Table"))
			cinemaObj = new AddOns();
		else if (tableName.equals("Payment Table"))
			cinemaObj = new Payment();
		else if (tableName.equals("Privilege Table"))
			cinemaObj = new Privilege();
		else if (tableName.equals("Promotion Table"))
			cinemaObj = new Promotion();
	}
	
	public void getRecordsFromFile(){
		ObservableList<Node> recordsFromFile = cinemaObj.getFileRecords();
		ObservableList<Node> dataFields = FXCollections.observableArrayList();
		for(int i=0; i<recordsFromFile.size(); i+=cinemaObj.getNumOfFields()){
			dataFields.addAll(recordsFromFile.subList(i, i + cinemaObj.getNumOfFields()));
			addRow(dataFields);
			dataFields.clear();
		}
	}
	
	public void loadInputFields(){
		gridCenter.getChildren().remove(hBoxInputFields);
		inputFields = cinemaObj.getInputFields();
		hBoxInputFields = new HBox(5);
		hBoxInputFields.getChildren().addAll(inputFields);
		gridCenter.add(hBoxInputFields, 0, 1);
		GridPane.setColumnSpan(hBoxInputFields, Integer.MAX_VALUE);
	}
	
	public void addRow(ObservableList<Node> dataFields){
		for (int j=0; j<dataFields.size(); j++){
			gridTable.addRow(numOfDataRows, dataFields.get(j));
		}
		listOfInputFields.addAll(dataFields);
		
		WaveButtonForRowDeletion btnDelete = new WaveButtonForRowDeletion();
		btnDelete.setRowNum(numOfDataRows);
		btnDelete.getInputRow().addAll(dataFields);
		btnDelete.getInputRow().add(btnDelete);
		btnDelete.setOnAction(e -> {
			for (int k=0; k<btnDelete.getInputRow().size(); k++){
				gridTable.getChildren().remove(btnDelete.getInputRow().get(k));
			}
			listOfInputFields.removeAll(btnDelete.getInputRow());
		});
		gridTable.addRow(numOfDataRows, btnDelete);
		numOfDataRows++;
	}
	
	public void updateFile(){
		try{
			Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to overwrite file?\nThis action cannot be undone!", ButtonType.YES, ButtonType.NO);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES){
				cinemaObj.updateFile(listOfInputFields);
				labStatusMessage.setVisible(true);
				labStatusMessage.setTextFill(Color.GREEN);
				labStatusMessage.setText("File successfully updated!");
			}
		}
		catch(NumberFormatException | NullPointerException ex){
			labStatusMessage.setVisible(true);
			labStatusMessage.setTextFill(Color.RED);
			labStatusMessage.setText("Hola amigos, invalid data entry detected!");
		}
		Timeline timeMessage = new Timeline(new KeyFrame(new Duration(5000), e -> labStatusMessage.setVisible(false)));
		timeMessage.play();
	}
	
	public static void fileCopy(File tempFile, File oriFile) throws IOException{
		try(
			DataInputStream fileInput = new DataInputStream(new BufferedInputStream(new FileInputStream(tempFile)));
			DataOutputStream fileOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(oriFile)));
		){
			int data;
			while((data = fileInput.read()) != -1){
				fileOutput.write(data);
			}
		}
		catch(FileNotFoundException ex){
			System.out.println("Hola amigos, input file does not exist!");
		}
		tempFile.delete();
	}
	
	public static void main(String[] args){
		launch();
	}
}
