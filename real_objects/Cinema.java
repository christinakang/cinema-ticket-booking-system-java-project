package real_objects;

import java.io.*;
import java.util.ArrayList;
import databaseAccess.DatabaseAccessUpdate;
import databaseAccess.DatabaseTablesView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class Cinema implements DatabaseAccessUpdate, Comparable<Cinema>{
	
	private String cinemaID;
	private String name;
	private String address;
	private int numberOfHalls;
	private int seatingCapacity;
	private String imgAddress;
	private static final int NUM_OF_FIELDS = 6;
	private static final String FILE_STRING = "Cinema Files/Cinema.dat";
	private static final String TEMP_FILE = "Cinema Files/CinemaTemp.dat";
	
	public Cinema(){
		this("", "");
	}
	
	public Cinema(String name, String cinemaID){
		this(name, cinemaID, 0, 0);
	}
	
	public Cinema(String name, String cinemaID, int numberOfHalls, int seatingCapacity){
		this.name = name;
		this.cinemaID = cinemaID;
		this.numberOfHalls = numberOfHalls;
		this.seatingCapacity = seatingCapacity;
	}
	
	public String getCinemaID() {
		return cinemaID;
	}

	public void setCinemaID(String cinemaID) {
		this.cinemaID = cinemaID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getNumberOfHalls() {
		return numberOfHalls;
	}

	public void setNumberOfHalls(int numberOfHalls) {
		this.numberOfHalls = numberOfHalls;
	}

	public int getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(int seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public String getImgAddress() {
		return imgAddress;
	}

	public void setImgAddress(String imgAddress) {
		this.imgAddress = imgAddress;
	}
	
	@Override
	public String toString(){
		return getName();
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
							String cinemaIDFromRecord = randAccessFile.readUTF();
							if (!cinemaID.equals(cinemaIDFromRecord)){
								randTempFile.writeUTF(cinemaIDFromRecord);
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeInt(randAccessFile.readInt());
								randTempFile.writeInt(randAccessFile.readInt());
								randTempFile.writeUTF(randAccessFile.readUTF());
							}
							else{
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readInt();
								randAccessFile.readInt();
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
			String cinemaIDFromRecord = randAccessFile.readUTF();
			String nameFromRecord = randAccessFile.readUTF();
			long recordStartIndex = 0;
			while(!(name.equals(nameFromRecord) || cinemaID.equals(cinemaIDFromRecord))){
				randAccessFile.readUTF();
				randAccessFile.readInt();
				randAccessFile.readInt();
				randAccessFile.readUTF();
				
				recordStartIndex = randAccessFile.getFilePointer();
				cinemaIDFromRecord = randAccessFile.readUTF();
				nameFromRecord = randAccessFile.readUTF();
			}
			if (name.equals(nameFromRecord) || cinemaID.equals(cinemaIDFromRecord)){
				randAccessFile.seek(recordStartIndex);
				return true;
			}
		}
		catch(EOFException ex){}
		return false;
	}
	
	//To write a single record to the file
	private void writeFileRecord(RandomAccessFile randAccessFile) throws IOException{
		randAccessFile.writeUTF(cinemaID);
		randAccessFile.writeUTF(name);
		randAccessFile.writeUTF(address);
		randAccessFile.writeInt(numberOfHalls);
		randAccessFile.writeInt(seatingCapacity);
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
								String cinemaIDFromRecord = randAccessFile.readUTF();
								if (!cinemaID.equals(cinemaIDFromRecord)){
									randTempFile.writeUTF(cinemaIDFromRecord);
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeInt(randAccessFile.readInt());
									randTempFile.writeInt(randAccessFile.readInt());
									randTempFile.writeUTF(randAccessFile.readUTF());
								}
								else{
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readInt();
									randAccessFile.readInt();
									randAccessFile.readUTF();
									
									for(MovieSession session: MovieSession.retrieveAllFileRecords()){
										if(session.getCinemaID().equals(cinemaIDFromRecord))
											session.deleteFileRecord();
									}
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
	public Cinema retrieveFileRecord(String cinemaID){
		this.cinemaID = cinemaID;
		try{
			try(RandomAccessFile randAccessFile = new RandomAccessFile(FILE_STRING, "r")){
				if (existsFileRecord(randAccessFile)){
					this.cinemaID = randAccessFile.readUTF();
					name = randAccessFile.readUTF();
					address = randAccessFile.readUTF();
					numberOfHalls = randAccessFile.readInt();
					seatingCapacity = randAccessFile.readInt();
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
	
	//To retrieve all the records from file
	public static ArrayList<Cinema> retrieveAllFileRecords(){
		ArrayList<Cinema> cinemaRecords = new ArrayList<>();
		
		try{
			try(DataInputStream inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_STRING)))){
				while(inputFile.available() != 0){
					Cinema cinema = new Cinema();
					cinema.setCinemaID(inputFile.readUTF());
					cinema.setName(inputFile.readUTF());
					cinema.setAddress(inputFile.readUTF());
					cinema.setNumberOfHalls(inputFile.readInt());
					cinema.setSeatingCapacity(inputFile.readInt());
					cinema.setImgAddress(inputFile.readUTF());
					
					cinemaRecords.add(cinema);
				}
			}
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with retrieving records!");
		}
		
		return cinemaRecords;
	}
	
	public static String generateID(){
		String cinemaID = "CID1";
		ArrayList<Integer> IDNumbers = new ArrayList<>();
		try{
			try(DataInputStream inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_STRING)))){
				while(inputFile.available() != 0){
					IDNumbers.add(Integer.parseInt(inputFile.readUTF().replaceAll("[\\D]", "")));
					inputFile.readUTF();
					inputFile.readUTF();
					inputFile.readInt();
					inputFile.readInt();
					inputFile.readUTF();
				}
				int ID = 1;
				for (; ID < Integer.MAX_VALUE; ID++){
					if (!IDNumbers.contains(ID))
						break;
				}
				cinemaID = "CID" + ID;
			}
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with retrieving records to generate ID!");
		}
		
		return cinemaID;
	}
	
	@Override
	public int getNumOfFields(){
		return NUM_OF_FIELDS;
	}
	
	@Override
	public ObservableList<String> getTableColumns(){
		ObservableList<String> columnsList = FXCollections.observableArrayList();
		columnsList.addAll("Cinema ID", "Cinema Name", "Address", "Num. Of Halls", "Seating Capacity", "Cinema Image");
		return columnsList;
	}
	
	@Override
	public ObservableList<Node> getInputFields(){
		TextField txtCinemaID = new TextField();
		txtCinemaID.setPromptText("Cinema ID");
		TextField txtName = new TextField();
		txtName.setPromptText("Cinema Name");
		TextField txtAddress = new TextField();
		txtAddress.setPromptText("Address");
		TextField txtNumOfHalls = new TextField();
		txtNumOfHalls.setPromptText("Num. Of Halls");
		TextField txtCapacity = new TextField();
		txtCapacity.setPromptText("Seating Capacity");
		TextField txtImageAddress = new TextField();
		txtImageAddress.setPromptText("Image Address");
		FileChooser imageFileChooser = new FileChooser();
		imageFileChooser.setTitle("Choose Image File for Cinema");
		imageFileChooser.setInitialDirectory(new File("bin/Images/Cinema"));
		txtImageAddress.setOnMouseClicked(e -> {
			File imageFile = imageFileChooser.showOpenDialog(null);
			if (imageFile != null){
				String imageAddress = "Images/Cinema/" + imageFile.getName();
				txtImageAddress.setText(imageAddress);
			}
		});
		
		ObservableList<Node> inputFields = FXCollections.observableArrayList();
		inputFields.addAll(txtCinemaID, txtName, txtAddress, txtNumOfHalls, txtCapacity, txtImageAddress);
		return inputFields;
	}
	
	@Override
	public void updateFile(ObservableList<Node> listOfInputFields) throws NumberFormatException{
		try{
			try (DataOutputStream fileOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(TEMP_FILE)))){
				for(int i=0; i<listOfInputFields.size(); i+=NUM_OF_FIELDS){
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i))).getText());
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 1))).getText());
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 2))).getText());
					fileOutput.writeInt(Integer.parseInt(((TextField)(listOfInputFields.get(i + 3))).getText()));
					fileOutput.writeInt(Integer.parseInt(((TextField)(listOfInputFields.get(i + 4))).getText()));
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 5))).getText());
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
					((TextField)(dataList.get(1))).setText(fileInput.readUTF());
					((TextField)(dataList.get(2))).setText(fileInput.readUTF());
					((TextField)(dataList.get(3))).setText(String.valueOf(fileInput.readInt()));
					((TextField)(dataList.get(4))).setText(String.valueOf(fileInput.readInt()));
					((TextField)(dataList.get(5))).setText(fileInput.readUTF());
					
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

	@Override
	public int compareTo(Cinema cinema) {
		return this.getName().compareTo(cinema.getName());
	}
}
