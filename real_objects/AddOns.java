package real_objects;

import java.io.*;
import java.util.ArrayList;
import databaseAccess.DatabaseAccessUpdate;
import databaseAccess.DatabaseTablesView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class AddOns implements DatabaseAccessUpdate{

	private String addOnsID;
	private String name;
	private double price;
	private String details;
	private String imgAddress;
	private static final int NUM_OF_FIELDS = 5;
	private static final String FILE_STRING = "Cinema Files/AddOns.dat";
	private static final String TEMP_FILE = "Cinema Files/AddOnsTemp.dat";
	
	public AddOns(){
		this("", "");
	}
	
	public AddOns(String addOnsID, String name){
		this(addOnsID, name, 0);
	}
	
	public AddOns(String addOnsID, String name, double price){
		this.addOnsID = addOnsID;
		this.name = name;
		this.price = price;
	}

	public String getAddOnsID() {
		return addOnsID;
	}

	public void setAddOnsID(String addOnsID) {
		this.addOnsID = addOnsID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
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
							String addOnsIDFromRecord = randAccessFile.readUTF();
							if (!addOnsID.equals(addOnsIDFromRecord)){
								randTempFile.writeUTF(addOnsIDFromRecord);
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeDouble(randAccessFile.readDouble());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
							}
							else{
								randAccessFile.readUTF();
								randAccessFile.readDouble();
								randAccessFile.readUTF();
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
			String addOnsIDFromRecord = randAccessFile.readUTF();
			String addOnsNameFromRecord = randAccessFile.readUTF();
			long recordStartIndex = 0;
			while(!(name.equals(addOnsNameFromRecord) || addOnsID.equals(addOnsIDFromRecord))){
				randAccessFile.readDouble();
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				
				recordStartIndex = randAccessFile.getFilePointer();
				addOnsIDFromRecord = randAccessFile.readUTF();
				addOnsNameFromRecord = randAccessFile.readUTF();
			}
			if (name.equals(addOnsNameFromRecord) || addOnsID.equals(addOnsIDFromRecord)){
				randAccessFile.seek(recordStartIndex);
				return true;
			}
		}
		catch(EOFException ex){}
		return false;
	}
	
	//To write a single record to the file
	private void writeFileRecord(RandomAccessFile randAccessFile) throws IOException{
		randAccessFile.writeUTF(addOnsID);
		randAccessFile.writeUTF(name);
		randAccessFile.writeDouble(price);
		randAccessFile.writeUTF(details);
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
								String addOnsIDFromRecord = randAccessFile.readUTF();
								if (!addOnsID.equals(addOnsIDFromRecord)){
									randTempFile.writeUTF(addOnsIDFromRecord);
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeDouble(randAccessFile.readDouble());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
								}
								else{
									randAccessFile.readUTF();
									randAccessFile.readDouble();
									randAccessFile.readUTF();
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
	public AddOns retrieveFileRecord(String addOnsID){
		this.addOnsID = addOnsID;
		try{
			try(RandomAccessFile randAccessFile = new RandomAccessFile(FILE_STRING, "r")){
				if (existsFileRecord(randAccessFile)){
					this.addOnsID = randAccessFile.readUTF();
					name = randAccessFile.readUTF();
					price = randAccessFile.readDouble();
					details = randAccessFile.readUTF();
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
	public static ArrayList<AddOns> retrieveAllFileRecords(){
		ArrayList<AddOns> addOnsRecords = new ArrayList<>();
		
		try{
			try(DataInputStream inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_STRING)))){
				while(inputFile.available() != 0){
					AddOns addOns = new AddOns();
					addOns.setAddOnsID(inputFile.readUTF());
					addOns.setName(inputFile.readUTF());
					addOns.setPrice(inputFile.readDouble());
					addOns.setDetails(inputFile.readUTF());
					addOns.setImgAddress(inputFile.readUTF());
					
					addOnsRecords.add(addOns);
				}
			}
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with retrieving records!");
		}
		
		return addOnsRecords;
	}
	
	public static String generateID(){
		String addOnsID = "ADDON1";
		ArrayList<Integer> IDNumbers = new ArrayList<>();
		try{
			try(DataInputStream inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_STRING)))){
				while(inputFile.available() != 0){
					IDNumbers.add(Integer.parseInt(inputFile.readUTF().replaceAll("[\\D]", "")));
					inputFile.readUTF();
					inputFile.readDouble();
					inputFile.readUTF();
					inputFile.readUTF();
				}
				int ID = 1;
				for (; ID < Integer.MAX_VALUE; ID++){
					if (!IDNumbers.contains(ID))
						break;
				}
				addOnsID = "ADDON" + ID;
			}
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with retrieving records to generate ID!");
		}
		
		return addOnsID;
	}
	
	@Override
	public int getNumOfFields(){
		return NUM_OF_FIELDS;
	}
	
	@Override
	public ObservableList<String> getTableColumns(){
		ObservableList<String> columnsList = FXCollections.observableArrayList();
		columnsList.addAll("AddOns ID", "Name", "Price", "Details", "AddOns Image");
		return columnsList;
	}
	
	@Override
	public ObservableList<Node> getInputFields(){
		TextField txtAddOnsID = new TextField();
		txtAddOnsID.setPromptText("AddOns ID");
		TextField txtName = new TextField();
		txtName.setPromptText("Name");
		TextField txtPrice = new TextField();
		txtPrice.setPromptText("Price");
		TextArea txtDetails = new TextArea();
		txtDetails.setPromptText("Details");
		txtDetails.setWrapText(true);
		txtDetails.setPrefColumnCount(30);
		txtDetails.setPrefRowCount(3);
		
		TextField txtImageAddress = new TextField();
		txtImageAddress.setPromptText("AddOns Image");
		FileChooser imageFileChooser = new FileChooser();
		imageFileChooser.setTitle("Choose Image File for AddOns");
		imageFileChooser.setInitialDirectory(new File("bin/Images/AddOns"));
		txtImageAddress.setOnMouseClicked(e -> {
			File imageFile = imageFileChooser.showOpenDialog(null);
			if (imageFile != null){
				String imageAddress = "Images/AddOns/" + imageFile.getName();
				txtImageAddress.setText(imageAddress);
			}
		});
		
		ObservableList<Node> inputFields = FXCollections.observableArrayList();
		inputFields.addAll(txtAddOnsID, txtName, txtPrice, txtDetails, txtImageAddress);
		return inputFields;
	}
	
	@Override
	public void updateFile(ObservableList<Node> listOfInputFields) throws NumberFormatException{
		try{
			try (DataOutputStream fileOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(TEMP_FILE)))){
				for(int i=0; i<listOfInputFields.size(); i+=NUM_OF_FIELDS){
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i))).getText());
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 1))).getText());
					fileOutput.writeDouble(Double.parseDouble(((TextField)(listOfInputFields.get(i + 2))).getText()));
					fileOutput.writeUTF(((TextArea)(listOfInputFields.get(i + 3))).getText());
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
					((TextField)(dataList.get(1))).setText(fileInput.readUTF());
					((TextField)(dataList.get(2))).setText(String.valueOf(fileInput.readDouble()));
					((TextArea)(dataList.get(3))).setText(fileInput.readUTF());
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
