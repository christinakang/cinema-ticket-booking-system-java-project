package real_objects;

import java.io.*;
import java.sql.Date;
import databaseAccess.DatabaseTablesView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class Admin extends User{
	private Date startDate;
	private static final int NUM_OF_FIELDS = 5;
	private static final String FILE_STRING = "Cinema Files/Admin.dat";
	private static final String TEMP_FILE = "Cinema Files/AdminTemp.dat";
	
	public Admin(){
		super("", "");
	}
	
	public Admin(String username, String password){
		super(username, password);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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
							}
							else{
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readLong();
								
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
		randAccessFile.writeUTF(password);
		randAccessFile.writeUTF(firstName);
		randAccessFile.writeUTF(lastName);
		randAccessFile.writeLong(startDate.getTime());
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
								}
								else{
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readLong();
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
	public Admin retrieveFileRecord(String username){
		this.username = username;
		try{
			try(RandomAccessFile randAccessFile = new RandomAccessFile(FILE_STRING, "r")){
				if (existsFileRecord(randAccessFile)){
					this.username = randAccessFile.readUTF();
					password = randAccessFile.readUTF();
					firstName = randAccessFile.readUTF();
					lastName = randAccessFile.readUTF();
					startDate = new Date(randAccessFile.readLong());
					
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
		ObservableList<String> columnsList = super.getTableColumns();
		columnsList.addAll("Start Date");
		return columnsList;
	}
	
	@Override
	public ObservableList<Node> getInputFields(){
		DatePicker startDate = new DatePicker();
		startDate.setPromptText("Start Date");
		
		ObservableList<Node> inputFields = super.getInputFields();
		inputFields.addAll(startDate);
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
