package real_objects;

import databaseAccess.DatabaseAccessUpdate;
import databaseAccess.DatabaseTablesView;
import databaseAccess.DateTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MovieSession implements DatabaseAccessUpdate, Comparable<MovieSession>{
	private String movieSessionID;
	private Date showTime;
	private int hallNumber;
	private String showType;
	private String movieID;
	private String cinemaID;
	private static final int NUM_OF_FIELDS = 6;
	private static final String[] showTypes = {"2D", "3D", "IMAX 2D", "IMAX 3D", "3D-X"};
	private static final String FILE_STRING = "Cinema Files/MovieSession.dat";
	private static final String TEMP_FILE = "Cinema Files/MovieSessionTemp.dat";
	
	public MovieSession(){
		this(0, new Date(System.currentTimeMillis()));
	}
	
	public MovieSession(int hallNumber, Date showTime){
		this.hallNumber = hallNumber;
		this.showTime = showTime;
	}
	
	public String getMovieSessionID() {
		return movieSessionID;
	}

	public void setMovieSessionID(String movieSessionID) {
		this.movieSessionID = movieSessionID;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public int getHallNumber() {
		return hallNumber;
	}

	public void setHallNumber(int hallNumber) {
		this.hallNumber = hallNumber;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getMovieID() {
		return movieID;
	}

	public void setMovieID(String movieID) {
		this.movieID = movieID;
	}

	public String getCinemaID() {
		return cinemaID;
	}

	public void setCinemaID(String cinemaID) {
		this.cinemaID = cinemaID;
	}

	public static String[] getShowTypes() {
		return showTypes;
	}

	@Override
	public String toString(){
		SimpleDateFormat showTimeFormat = new SimpleDateFormat("E, dd-MM-yyyy (hh:mm a)");
		return showTimeFormat.format(getShowTime()) + " - " + showType;
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
							String movieSessionIDFromRecord = randAccessFile.readUTF();
							if (!movieSessionID.equals(movieSessionIDFromRecord)){
								randTempFile.writeUTF(movieSessionIDFromRecord);
								randTempFile.writeLong(randAccessFile.readLong());
								randTempFile.writeInt(randAccessFile.readInt());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
							}
							else{
								randAccessFile.readLong();
								randAccessFile.readInt();
								randAccessFile.readUTF();
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
			String movieSessionIDFromRecord = randAccessFile.readUTF();
			long recordStartIndex = 0;
			while(!movieSessionID.equals(movieSessionIDFromRecord)){
				randAccessFile.readLong();
				randAccessFile.readInt();
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				recordStartIndex = randAccessFile.getFilePointer();
				movieSessionIDFromRecord = randAccessFile.readUTF();
			}
			if (movieSessionID.equals(movieSessionIDFromRecord)){
				randAccessFile.seek(recordStartIndex);
				return true;
			}
		}
		catch(EOFException ex){}
		return false;
	}
	
	//To write a single record to the file
	private void writeFileRecord(RandomAccessFile randAccessFile) throws IOException{
		randAccessFile.writeUTF(movieSessionID);
		randAccessFile.writeLong(showTime.getTime());
		randAccessFile.writeInt(hallNumber);
		randAccessFile.writeUTF(showType);
		randAccessFile.writeUTF(movieID);
		randAccessFile.writeUTF(cinemaID);
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
								String movieSessionIDFromRecord = randAccessFile.readUTF();
								if (!movieSessionID.equals(movieSessionIDFromRecord)){
									randTempFile.writeUTF(movieSessionIDFromRecord);
									randTempFile.writeLong(randAccessFile.readLong());
									randTempFile.writeInt(randAccessFile.readInt());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
								}
								else{
									randAccessFile.readLong();
									randAccessFile.readInt();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									
									for(Ticket ticket: Ticket.retrieveAllFileRecords()){
										if(ticket.getMovieSessionID().equals(movieSessionIDFromRecord))
											ticket.deleteFileRecord();
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
	public MovieSession retrieveFileRecord(String movieSessionID){
		this.movieSessionID = movieSessionID;
		try{
			try(RandomAccessFile randAccessFile = new RandomAccessFile(FILE_STRING, "r")){
				if (existsFileRecord(randAccessFile)){
					this.movieSessionID = randAccessFile.readUTF();
					showTime = new Date(randAccessFile.readLong());
					hallNumber = randAccessFile.readInt();
					showType = randAccessFile.readUTF();
					movieID = randAccessFile.readUTF();
					cinemaID = randAccessFile.readUTF();
					
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
	public static ArrayList<MovieSession> retrieveAllFileRecords(){
		ArrayList<MovieSession> movieSessionRecords = new ArrayList<>();
		
		try{
			try(DataInputStream inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_STRING)))){
				while(inputFile.available() != 0){
					MovieSession movieSession = new MovieSession();
					movieSession.setMovieSessionID(inputFile.readUTF());
					movieSession.setShowTime(new Date(inputFile.readLong()));
					movieSession.setHallNumber(inputFile.readInt());
					movieSession.setShowType(inputFile.readUTF());
					movieSession.setMovieID(inputFile.readUTF());
					movieSession.setCinemaID(inputFile.readUTF());
					
					movieSessionRecords.add(movieSession);
				}
			}
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with retrieving records!");
		}
		
		return movieSessionRecords;
	}
	
	public static String generateID(){
		String movieSessionID = "MSID1";
		ArrayList<Integer> IDNumbers = new ArrayList<>();
		try{
			try(DataInputStream inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_STRING)))){
				while(inputFile.available() != 0){
					IDNumbers.add(Integer.parseInt(inputFile.readUTF().replaceAll("[\\D]", "")));
					inputFile.readLong();
					inputFile.readInt();
					inputFile.readUTF();
					inputFile.readUTF();
					inputFile.readUTF();
				}
				int ID = 1;
				for (; ID < Integer.MAX_VALUE; ID++){
					if (!IDNumbers.contains(ID))
						break;
				}
				movieSessionID = "MSID" + ID;
			}
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with retrieving records to generate ID!");
		}
		
		return movieSessionID;
	}
	
	@Override
	public int getNumOfFields(){
		return NUM_OF_FIELDS;
	}
	
	@Override
	public ObservableList<String> getTableColumns(){
		ObservableList<String> columnsList = FXCollections.observableArrayList();
		columnsList.addAll("Movie Session ID", "Show Time", "Hall Number", "Show Type", "Movie ID", "Cinema ID");
		return columnsList;
	}
	
	@Override
	public ObservableList<Node> getInputFields(){
		TextField txtSessionID = new TextField();
		txtSessionID.setPromptText("Movie Session ID");
		DateTimePicker showTime = new DateTimePicker();
		showTime.getDatePicker().setPromptText("Show Date");
		TextField txtHallNum = new TextField();
		txtHallNum.setPromptText("Hall Number");
		TextField txtShowType = new TextField();
		txtShowType.setPromptText("Show Type");
		TextField txtMovieID = new TextField();
		txtMovieID.setPromptText("Movie ID");
		TextField txtCinemaID = new TextField();
		txtCinemaID.setPromptText("Cinema ID");
		
		ObservableList<Node> inputFields = FXCollections.observableArrayList();
		inputFields.addAll(txtSessionID, showTime, txtHallNum, txtShowType, txtMovieID, txtCinemaID);
		return inputFields;
	}
	
	@Override
	public void updateFile(ObservableList<Node> listOfInputFields) throws NumberFormatException{
		try{
			try (DataOutputStream fileOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(TEMP_FILE)))){
				for(int i=0; i<listOfInputFields.size(); i+=NUM_OF_FIELDS){
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i))).getText());
					fileOutput.writeLong(((DateTimePicker)(listOfInputFields.get(i + 1))).getShowTime());
					fileOutput.writeInt(Integer.parseInt(((TextField)(listOfInputFields.get(i + 2))).getText()));
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 3))).getText());
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 4))).getText());
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
					((DateTimePicker)(dataList.get(1))).setShowTime(fileInput.readLong());
					((TextField)(dataList.get(2))).setText(String.valueOf(fileInput.readInt()));
					((TextField)(dataList.get(3))).setText(fileInput.readUTF());
					((TextField)(dataList.get(4))).setText(fileInput.readUTF());
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
	public int compareTo(MovieSession session) {
		return this.getShowTime().compareTo(session.getShowTime());
	}
}
