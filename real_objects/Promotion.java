package real_objects;

import java.io.*;
import java.sql.Date;
import java.util.ArrayList;

import databaseAccess.DatabaseAccessUpdate;
import databaseAccess.DatabaseTablesView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class Promotion implements DatabaseAccessUpdate{
	private String topic;
	private String details;
	private Date startDate;
	private Date endDate;
	private String imgAddress;
	private static final int NUM_OF_FIELDS = 5;
	private static final String FILE_STRING = "Cinema Files/Promotion.dat";
	private static final String TEMP_FILE = "Cinema Files/PromotionTemp.dat";
	
	public Promotion(){
		this("");
	}
	
	public Promotion(String topic){
		this(topic, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
	}
	
	public Promotion(String topic, Date startDate, Date endDate){
		this.topic = topic;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
							String topicFromRecord = randAccessFile.readUTF();
							if (!topic.equals(topicFromRecord)){
								randTempFile.writeUTF(topicFromRecord);
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeLong(randAccessFile.readLong());
								randTempFile.writeLong(randAccessFile.readLong());
								randTempFile.writeUTF(randAccessFile.readUTF());
							}
							else{
								randAccessFile.readUTF();
								randAccessFile.readLong();
								randAccessFile.readLong();
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
			String topicFromRecord = randAccessFile.readUTF();
			long recordStartIndex = 0;
			while(!topic.equals(topicFromRecord)){
				randAccessFile.readUTF();
				randAccessFile.readLong();
				randAccessFile.readLong();
				randAccessFile.readUTF();
				recordStartIndex = randAccessFile.getFilePointer();
				topicFromRecord = randAccessFile.readUTF();
			}
			if (topic.equals(topicFromRecord)){
				randAccessFile.seek(recordStartIndex);
				return true;
			}
		}
		catch(EOFException ex){}
		return false;
	}
	
	//To write a single record to the file
	private void writeFileRecord(RandomAccessFile randAccessFile) throws IOException{
		randAccessFile.writeUTF(topic);
		randAccessFile.writeUTF(details);
		randAccessFile.writeLong(startDate.getTime());
		randAccessFile.writeLong(endDate.getTime());
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
								String topicFromRecord = randAccessFile.readUTF();
								if (!topic.equals(topicFromRecord)){
									randTempFile.writeUTF(topicFromRecord);
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeLong(randAccessFile.readLong());
									randTempFile.writeLong(randAccessFile.readLong());
									randTempFile.writeUTF(randAccessFile.readUTF());
								}
								else{
									randAccessFile.readUTF();
									randAccessFile.readLong();
									randAccessFile.readLong();
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
	public Promotion retrieveFileRecord(String topic){
		this.topic = topic;
		try{
			try(RandomAccessFile randAccessFile = new RandomAccessFile(FILE_STRING, "r")){
				if (existsFileRecord(randAccessFile)){
					this.topic = randAccessFile.readUTF();
					details = randAccessFile.readUTF();
					startDate = new Date(randAccessFile.readLong());
					endDate = new Date(randAccessFile.readLong());
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
	public static ArrayList<Promotion> retrieveAllFileRecords(){
		ArrayList<Promotion> promotionRecords = new ArrayList<>();
		
		try{
			try(DataInputStream inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_STRING)))){
				while(inputFile.available() != 0){
					Promotion promotion = new Promotion();
					promotion.setTopic(inputFile.readUTF());
					promotion.setDetails(inputFile.readUTF());
					promotion.setStartDate(new Date(inputFile.readLong()));
					promotion.setEndDate(new Date(inputFile.readLong()));
					promotion.setImgAddress(inputFile.readUTF());
					
					promotionRecords.add(promotion);
				}
			}
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with retrieving records!");
		}
		
		return promotionRecords;
	}
	
	@Override
	public int getNumOfFields(){
		return NUM_OF_FIELDS;
	}
	
	@Override
	public ObservableList<String> getTableColumns(){
		ObservableList<String> columnsList = FXCollections.observableArrayList();
		columnsList.addAll("Topic", "Details", "Start Date", "End Date", "Promotion Image");
		return columnsList;
	}
	
	@Override
	public ObservableList<Node> getInputFields(){
		TextField txtTopic = new TextField();
		txtTopic.setPromptText("Topic");
		TextArea txtDetails = new TextArea();
		txtDetails.setPromptText("Details");
		txtDetails.setWrapText(true);
		txtDetails.setPrefColumnCount(30);
		txtDetails.setPrefRowCount(3);
		
		DatePicker startDate = new DatePicker();
		startDate.setPromptText("Start Date");
		DatePicker endDate = new DatePicker();
		endDate.setPromptText("End Date");
		TextField txtImageAddress = new TextField();
		txtImageAddress.setPromptText("Promotion Image");
		FileChooser imageFileChooser = new FileChooser();
		imageFileChooser.setTitle("Choose Image File for Promotion");
		imageFileChooser.setInitialDirectory(new File("bin/Images/PromotionPoster"));
		txtImageAddress.setOnMouseClicked(e -> {
			File imageFile = imageFileChooser.showOpenDialog(null);
			if (imageFile != null){
				String imageAddress = "Images/PromotionPoster/" + imageFile.getName();
				txtImageAddress.setText(imageAddress);
			}
		});
		
		ObservableList<Node> inputFields = FXCollections.observableArrayList();
		inputFields.addAll(txtTopic, txtDetails, startDate, endDate, txtImageAddress);
		return inputFields;
	}
	
	@Override
	public void updateFile(ObservableList<Node> listOfInputFields) throws NumberFormatException{
		try{
			try (DataOutputStream fileOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(TEMP_FILE)))){
				for(int i=0; i<listOfInputFields.size(); i+=NUM_OF_FIELDS){
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i))).getText());
					fileOutput.writeUTF(((TextArea)(listOfInputFields.get(i + 1))).getText());
					fileOutput.writeLong((Date.valueOf(((DatePicker)(listOfInputFields.get(i + 2))).getValue())).getTime());
					fileOutput.writeLong((Date.valueOf(((DatePicker)(listOfInputFields.get(i + 3))).getValue())).getTime());
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
					((TextArea)(dataList.get(1))).setText(fileInput.readUTF());
					((DatePicker)(dataList.get(2))).setValue((new Date(fileInput.readLong())).toLocalDate());
					((DatePicker)(dataList.get(3))).setValue((new Date(fileInput.readLong())).toLocalDate());
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
