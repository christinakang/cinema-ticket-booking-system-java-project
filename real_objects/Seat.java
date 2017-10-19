package real_objects;

import java.io.*;
import databaseAccess.DatabaseAccessUpdate;
import databaseAccess.DatabaseTablesView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

public class Seat extends Pane implements DatabaseAccessUpdate{
	
	private static final String[] seatTypes = {"Regular", "Regular Twin", "Premier", "Premier Twin", "Luxury"};
	private String type;
	private int capacity;
	private int movieTokens;
	private double price;
	private String imgAddress;
	private static final int NUM_OF_FIELDS = 5;
	private static final String FILE_STRING = "Cinema Files/Seat.dat";
	private static final String TEMP_FILE = "Cinema Files/SeatTemp.dat";
	
	public Seat(){
		this("");
	}
	
	public Seat(String type) {
		this(type, 0);
	}
	
	public Seat(String type, int capacity) {
		this(type, capacity, 0);
	}
	
	public Seat(String type, int capacity, double price) {
		this.type = type;
		this.capacity = capacity;
		this.price = price;
	}

	public static String[] getSeatTypes() {
		return seatTypes;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getMovieTokens() {
		return movieTokens;
	}

	public void setMovieTokens(int movieTokens) {
		this.movieTokens = movieTokens;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImgAddress() {
		return imgAddress;
	}

	public void setImgAddress(String imgAddress) {
		this.imgAddress = imgAddress;
	}
	
	//To add a new record to file
	public boolean addFileRecord(){
		try{
			try(RandomAccessFile randAccessFile = new RandomAccessFile(FILE_STRING, "rw")){
				if (!existsFileRecord(randAccessFile)){
					randAccessFile.seek(randAccessFile.length());
					writeFileRecord(randAccessFile);
				}
				else
					return false;
			}
		}
		catch(NumberFormatException | NullPointerException ex){
			System.out.println("Hola amigos, invalid data entry detected!");
			return false;
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with adding new record!");
			return false;
		}
		return true;
	}
	
	//To update the file with changes in single record
	public boolean updateFileRecord(){
		try{
			try(RandomAccessFile randAccessFile = new RandomAccessFile(FILE_STRING, "r");
				RandomAccessFile randTempFile = new RandomAccessFile(TEMP_FILE, "rw");
			){
				if (existsFileRecord(randAccessFile)){
					randAccessFile.seek(0);
					while(true){
						try{
							String typeFromRecord = randAccessFile.readUTF();
							if (!type.equals(typeFromRecord)){
								randTempFile.writeUTF(typeFromRecord);
								randTempFile.writeInt(randAccessFile.readInt());
								randTempFile.writeInt(randAccessFile.readInt());
								randTempFile.writeDouble(randAccessFile.readDouble());
								randTempFile.writeUTF(randAccessFile.readUTF());
							}
							else{
								randAccessFile.readUTF();
								randAccessFile.readInt();
								randAccessFile.readInt();
								randAccessFile.readDouble();
								randAccessFile.readUTF();
								
								writeFileRecord(randTempFile);
							}	
						}
						catch(EOFException ex){
							randAccessFile.close();
							randTempFile.close();
							break;
						}
					}
					File tempFile = new File(TEMP_FILE);
					File oriFile = new File(FILE_STRING);
					DatabaseTablesView.fileCopy(tempFile, oriFile);
				}
				else
					return false;
			}
		}
		catch(NumberFormatException | NullPointerException ex){
			System.out.println("Hola amigos, invalid data entry detected!");
			return false;
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with updating record!");
			return false;
		}
		return true;
	}
	
	//To search for a particular record and seek file pointer to the start of the record
	private boolean existsFileRecord(RandomAccessFile randAccessFile) throws IOException{
		try{
			String typeFromRecord = randAccessFile.readUTF();
			long recordStartIndex = 0;
			while(!type.equals(typeFromRecord)){
				randAccessFile.readInt();
				randAccessFile.readInt();
				randAccessFile.readDouble();
				randAccessFile.readUTF();
				recordStartIndex = randAccessFile.getFilePointer();
				typeFromRecord = randAccessFile.readUTF();
			}
			if (type.equals(typeFromRecord)){
				randAccessFile.seek(recordStartIndex);
				return true;
			}
		}
		catch(EOFException ex){}
		return false;
	}
	
	//To write a single record to the file
	private void writeFileRecord(RandomAccessFile randAccessFile) throws IOException{
		randAccessFile.writeUTF(type);
		randAccessFile.writeInt(capacity);
		randAccessFile.writeInt(movieTokens);
		randAccessFile.writeDouble(price);
		randAccessFile.writeUTF(imgAddress);
	}
	
	//To delete a record from file permanently
	public boolean deleteFileRecord(){
		try{
			try(RandomAccessFile randAccessFile = new RandomAccessFile(FILE_STRING, "r");
				RandomAccessFile randTempFile = new RandomAccessFile(TEMP_FILE, "rw");
				){
					if (existsFileRecord(randAccessFile)){
						randAccessFile.seek(0);
						while(true){
							try{
								String typeFromRecord = randAccessFile.readUTF();
								if (!type.equals(typeFromRecord)){
									randTempFile.writeUTF(typeFromRecord);
									randTempFile.writeInt(randAccessFile.readInt());
									randTempFile.writeInt(randAccessFile.readInt());
									randTempFile.writeDouble(randAccessFile.readDouble());
									randTempFile.writeUTF(randAccessFile.readUTF());
								}
								else{
									randAccessFile.readUTF();
									randAccessFile.readInt();
									randAccessFile.readInt();
									randAccessFile.readDouble();
									randAccessFile.readUTF();
								}	
							}
							catch(EOFException ex){
								randAccessFile.close();
								randTempFile.close();
								break;
							}
						}
						File tempFile = new File(TEMP_FILE);
						File oriFile = new File(FILE_STRING);
						DatabaseTablesView.fileCopy(tempFile, oriFile);
					}
					else
						return false;
				}
		}
		catch(NumberFormatException | NullPointerException ex){
			System.out.println("Hola amigos, invalid file search index provided!");
			return false;
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with deleting record!");
			return false;
		}
		return true;
	}
	
	//To retrieve a specific record from file
	public Seat retrieveFileRecord(String type){
		this.type = type;
		try{
			try(RandomAccessFile randAccessFile = new RandomAccessFile(FILE_STRING, "r")){
				if (existsFileRecord(randAccessFile)){
					this.type = randAccessFile.readUTF();
					capacity = randAccessFile.readInt();
					movieTokens = randAccessFile.readInt();
					price = randAccessFile.readDouble();
					imgAddress = randAccessFile.readUTF();
					
					return this;
				}
				else
					return null;
			}
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with retrieving record!");
			return null;
		}
	}
	
	@Override
	public int getNumOfFields(){
		return NUM_OF_FIELDS;
	}
	
	@Override
	public ObservableList<String> getTableColumns(){
		ObservableList<String> columnsList = FXCollections.observableArrayList();
		columnsList.addAll("Type", "Capacity", "Movie Tokens", "Price", "Seat Image");
		return columnsList;
	}
	
	@Override
	public ObservableList<Node> getInputFields(){
		TextField txtType = new TextField();
		txtType.setPromptText("Type");
		TextField txtCapacity = new TextField();
		txtCapacity.setPromptText("Capacity");
		TextField txtMovieTokens = new TextField();
		txtMovieTokens.setPromptText("Movie Tokens");
		TextField txtPrice = new TextField();
		txtPrice.setPromptText("Price");
		
		TextField txtImageAddress = new TextField();
		txtImageAddress.setPromptText("Seat Image");
		FileChooser imageFileChooser = new FileChooser();
		imageFileChooser.setTitle("Choose Image File for Seat");
		imageFileChooser.setInitialDirectory(new File("bin/Images/Seat"));
		txtImageAddress.setOnMouseClicked(e -> {
			File imageFile = imageFileChooser.showOpenDialog(null);
			if (imageFile != null){
				String imageAddress = "Images/Seat/" + imageFile.getName();
				txtImageAddress.setText(imageAddress);
			}
		});
		
		ObservableList<Node> inputFields = FXCollections.observableArrayList();
		inputFields.addAll(txtType, txtCapacity, txtMovieTokens, txtPrice, txtImageAddress);
		return inputFields;
	}
	
	@Override
	public void updateFile(ObservableList<Node> listOfInputFields) throws NumberFormatException{
		try{
			try (DataOutputStream fileOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(TEMP_FILE)))){
				for(int i=0; i<listOfInputFields.size(); i+=NUM_OF_FIELDS){
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i))).getText());
					fileOutput.writeInt(Integer.parseInt(((TextField)(listOfInputFields.get(i + 1))).getText()));
					fileOutput.writeInt(Integer.parseInt(((TextField)(listOfInputFields.get(i + 2))).getText()));
					fileOutput.writeDouble(Double.parseDouble(((TextField)(listOfInputFields.get(i + 3))).getText()));
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 4))).getText());
				}
			}
			File tempFile = new File(TEMP_FILE);
			File oriFile = new File(FILE_STRING);
			DatabaseTablesView.fileCopy(tempFile, oriFile);
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with updating file!");
		}
	}
	
	public ObservableList<Node> getFileRecords(){
		ObservableList<Node> dataListRows = FXCollections.observableArrayList();
		
		try{
			try(DataInputStream fileInput = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_STRING)))){	
				while(fileInput.available() != 0){
					ObservableList<Node> dataList = getInputFields();
					((TextField)(dataList.get(0))).setText(fileInput.readUTF());
					((TextField)(dataList.get(1))).setText(String.valueOf(fileInput.readInt()));
					((TextField)(dataList.get(2))).setText(String.valueOf(fileInput.readInt()));
					((TextField)(dataList.get(3))).setText(String.valueOf(fileInput.readDouble()));
					((TextField)(dataList.get(4))).setText(fileInput.readUTF());
					
					dataListRows.addAll(dataList);
				}
			}
		}
		catch(FileNotFoundException ex){
			System.out.println("Hola amigos, input file does not exist!");
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something wrong with reading file records!");
		}
		return dataListRows;
	}
}
