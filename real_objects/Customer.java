package real_objects;

import java.io.*;
import databaseAccess.DatabaseTablesView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Date;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class Customer extends User{
	
	private Date dateOfBirth;
	private String phoneNumber;
	private String emailAddress;
	private int movieTokens;
	private static final int NUM_OF_FIELDS = 8;
	private static final String FILE_STRING = "Cinema Files/Customer.dat";
	private static final String TEMP_FILE = "Cinema Files/CustomerTemp.dat";
	
	public Customer(){
		super("", "");
	}
	
	public Customer(String username, String password){
		super(username, password);
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public int getMovieTokens() {
		return movieTokens;
	}

	public void setMovieTokens(int movieTokens) {
		this.movieTokens = movieTokens;
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
							String usernameFromRecord = randAccessFile.readUTF();
							if (!username.equals(usernameFromRecord)){
								randTempFile.writeUTF(usernameFromRecord);
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeLong(randAccessFile.readLong());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeInt(randAccessFile.readInt());
							}
							else{
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readLong();
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readInt();
								
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
			String usernameFromRecord = randAccessFile.readUTF();
			long recordStartIndex = 0;
			while(!username.equals(usernameFromRecord)){
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				randAccessFile.readLong();
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				randAccessFile.readInt();
				recordStartIndex = randAccessFile.getFilePointer();
				usernameFromRecord = randAccessFile.readUTF();
			}
			if (username.equals(usernameFromRecord)){
				randAccessFile.seek(recordStartIndex);
				return true;
			}
		}
		catch(EOFException ex){}
		return false;
	}
	
	//To write a single record to the file
	private void writeFileRecord(RandomAccessFile randAccessFile) throws IOException{
		randAccessFile.writeUTF(username);
		randAccessFile.writeUTF(encrypt(password));
		randAccessFile.writeUTF(firstName);
		randAccessFile.writeUTF(lastName);
		randAccessFile.writeLong(dateOfBirth.getTime());
		randAccessFile.writeUTF(emailAddress);
		randAccessFile.writeUTF(phoneNumber);
		randAccessFile.writeInt(movieTokens);
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
								String usernameFromRecord = randAccessFile.readUTF();
								if (!username.equals(usernameFromRecord)){
									randTempFile.writeUTF(usernameFromRecord);
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeLong(randAccessFile.readLong());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeInt(randAccessFile.readInt());
								}
								else{
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readLong();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readInt();
									
									for(Ticket ticket: Ticket.retrieveAllFileRecords()){
										if(ticket.getUsername().equals(usernameFromRecord))
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
	public Customer retrieveFileRecord(String username){
		this.username = username;
		try{
			try(RandomAccessFile randAccessFile = new RandomAccessFile(FILE_STRING, "r")){
				if (existsFileRecord(randAccessFile)){
					this.username = randAccessFile.readUTF();
					password = decrypt(randAccessFile.readUTF());
					firstName = randAccessFile.readUTF();
					lastName = randAccessFile.readUTF();
					dateOfBirth = new Date(randAccessFile.readLong());
					emailAddress = randAccessFile.readUTF();
					phoneNumber = randAccessFile.readUTF();
					movieTokens = randAccessFile.readInt();
					
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
	
	private static String encrypt(String password){
		char[] passwordChar = password.toCharArray();
		for(int i=0; i<password.length(); i++){
			passwordChar[i] = (char)(passwordChar[i] + (i + 5)/2);
		}
		return String.copyValueOf(passwordChar);
	}
	
	private static String decrypt(String password){
		char[] passwordChar = password.toCharArray();
		for(int i=0; i<password.length(); i++){
			passwordChar[i] = (char)(passwordChar[i] - (i + 5)/2);
		}
		return String.copyValueOf(passwordChar);
	}
	
	@Override
	public int getNumOfFields(){
		return NUM_OF_FIELDS;
	}
	
	@Override
	public ObservableList<String> getTableColumns(){
		ObservableList<String> columnsList = super.getTableColumns();
		columnsList.addAll("Date of Birth", "E-mail", "Phone number", "Movie Tokens");
		return columnsList;
	}
	
	@Override
	public ObservableList<Node> getInputFields(){
		DatePicker dateOfBirth = new DatePicker();
		dateOfBirth.setPromptText("Date Of Birth");
		TextField txtEmail = new TextField();
		txtEmail.setPromptText("E-mail");
		TextField txtPhone = new TextField();
		txtPhone.setPromptText("Phone number");
		TextField txtMovieTokens = new TextField();
		txtMovieTokens.setPromptText("Movie Tokens");
		
		ObservableList<Node> inputFields = super.getInputFields();
		inputFields.addAll(dateOfBirth, txtEmail, txtPhone, txtMovieTokens);
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
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 3))).getText());
					fileOutput.writeLong((Date.valueOf(((DatePicker)(listOfInputFields.get(i + 4))).getValue())).getTime());
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 5))).getText());
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 6))).getText());
					fileOutput.writeInt(Integer.parseInt(((TextField)(listOfInputFields.get(i + 7))).getText()));
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
					((TextField)(dataList.get(3))).setText(fileInput.readUTF());
					((DatePicker)(dataList.get(4))).setValue((new Date(fileInput.readLong())).toLocalDate());
					((TextField)(dataList.get(5))).setText(fileInput.readUTF());
					((TextField)(dataList.get(6))).setText(fileInput.readUTF());
					((TextField)(dataList.get(7))).setText(String.valueOf(fileInput.readInt()));
					
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
