package real_objects;

import databaseAccess.DatabaseAccessUpdate;
import databaseAccess.DatabaseTablesView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.io.*;
import java.sql.Date;
import java.util.ArrayList;

public class Payment implements DatabaseAccessUpdate{
	private String paymentID;
	private String cardHolderName;
	private long cardNumber;
	private int securityNumber;
	private int movieTokensUsed;
	private Date expiryDate;
	private double totalAmountPaid;
	private static String[] countries = {"Malaysia", "Singapore", "Thailand", "Indonesia", "United States", 
								  "United Kingdom", "India", "China"};
	private static String[] banks = {"AEON", "Affin Bank", "Alliance Bank", "AmBank", "CIMB", "Bank Islam", "BSN", 
							  "Citibank", "Hong Leong Bank", "HSBC Bank", "MayBank", "OCBC", "RHB Bank"};
	private static final int NUM_OF_FIELDS = 7;
	private static final String FILE_STRING = "Cinema Files/Payment.dat";
	private static final String TEMP_FILE = "Cinema Files/PaymentTemp.dat";
	
	public Payment(){
		this("", 0, 0, new Date(System.currentTimeMillis()));
	}
	
	public Payment(String cardHolderName, int cardNumber, int securityNumber, Date expiryDate){
		this.cardHolderName = cardHolderName;
		this.cardNumber = cardNumber;
		this.securityNumber = securityNumber;
		this.expiryDate = expiryDate;
	}
	
	public String getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(String paymentID) {
		this.paymentID = paymentID;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public long getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getSecurityNumber() {
		return securityNumber;
	}

	public void setSecurityNumber(int securityNumber) {
		this.securityNumber = securityNumber;
	}

	public int getMovieTokensUsed() {
		return movieTokensUsed;
	}

	public void setMovieTokensUsed(int movieTokensUsed) {
		this.movieTokensUsed = movieTokensUsed;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public double getTotalAmountPaid() {
		return totalAmountPaid;
	}

	public void setTotalAmountPaid(double totalAmountPaid) {
		this.totalAmountPaid = totalAmountPaid;
	}

	public static String[] getCountries() {
		return countries;
	}

	public static void setCountries(String[] countries) {
		Payment.countries = countries;
	}

	public static String[] getBanks() {
		return banks;
	}

	public static void setBanks(String[] banks) {
		Payment.banks = banks;
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
							String paymentIDFromRecord = randAccessFile.readUTF();
							if (!paymentID.equals(paymentIDFromRecord)){
								randTempFile.writeUTF(paymentIDFromRecord);
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeLong(randAccessFile.readLong());
								randTempFile.writeInt(randAccessFile.readInt());
								randTempFile.writeInt(randAccessFile.readInt());
								randTempFile.writeLong(randAccessFile.readLong());
								randTempFile.writeDouble(randAccessFile.readDouble());
							}
							else{
								randAccessFile.readUTF();
								randAccessFile.readLong();
								randAccessFile.readInt();
								randAccessFile.readInt();
								randAccessFile.readLong();
								randAccessFile.readDouble();
								
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
			String paymentIDFromRecord = randAccessFile.readUTF();
			long recordStartIndex = 0;
			while(!paymentID.equals(paymentIDFromRecord)){
				randAccessFile.readUTF();
				randAccessFile.readLong();
				randAccessFile.readInt();
				randAccessFile.readInt();
				randAccessFile.readLong();
				randAccessFile.readDouble();
				
				recordStartIndex = randAccessFile.getFilePointer();
				paymentIDFromRecord = randAccessFile.readUTF();
			}
			if (paymentID.equals(paymentIDFromRecord)){
				randAccessFile.seek(recordStartIndex);
				return true;
			}
		}
		catch(EOFException ex){}
		return false;
	}
	
	//To write a single record to the file
	private void writeFileRecord(RandomAccessFile randAccessFile) throws IOException{
		randAccessFile.writeUTF(paymentID);
		randAccessFile.writeUTF(cardHolderName);
		randAccessFile.writeLong(cardNumber);
		randAccessFile.writeInt(securityNumber);
		randAccessFile.writeInt(movieTokensUsed);
		randAccessFile.writeLong(expiryDate.getTime());
		randAccessFile.writeDouble(totalAmountPaid);
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
								String paymentIDFromRecord = randAccessFile.readUTF();
								if (!paymentID.equals(paymentIDFromRecord)){
									randTempFile.writeUTF(paymentIDFromRecord);
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeLong(randAccessFile.readLong());
									randTempFile.writeInt(randAccessFile.readInt());
									randTempFile.writeInt(randAccessFile.readInt());
									randTempFile.writeLong(randAccessFile.readLong());
									randTempFile.writeDouble(randAccessFile.readDouble());
								}
								else{
									randAccessFile.readUTF();
									randAccessFile.readLong();
									randAccessFile.readInt();
									randAccessFile.readInt();
									randAccessFile.readLong();
									randAccessFile.readDouble();
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
	public Payment retrieveFileRecord(String paymentID){
		this.paymentID = paymentID;
		try{
			try(RandomAccessFile randAccessFile = new RandomAccessFile(FILE_STRING, "r")){
				if (existsFileRecord(randAccessFile)){
					this.paymentID = randAccessFile.readUTF();
					cardHolderName = randAccessFile.readUTF();
					cardNumber = randAccessFile.readLong();
					securityNumber = randAccessFile.readInt();
					movieTokensUsed = randAccessFile.readInt();
					expiryDate = new Date(randAccessFile.readLong());
					totalAmountPaid = randAccessFile.readDouble();
					
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
	
	public static String generateID(){
		String paymentID = "PAYID1";
		ArrayList<Integer> IDNumbers = new ArrayList<>();
		try{
			try(DataInputStream inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_STRING)))){
				while(inputFile.available() != 0){
					IDNumbers.add(Integer.parseInt(inputFile.readUTF().replaceAll("[\\D]", "")));
					inputFile.readUTF();
					inputFile.readLong();
					inputFile.readInt();
					inputFile.readInt();
					inputFile.readLong();
					inputFile.readDouble();
				}
				int ID = 1;
				for (; ID < Integer.MAX_VALUE; ID++){
					if (!IDNumbers.contains(ID))
						break;
				}
				paymentID = "PAYID" + ID;
			}
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with retrieving records to generate ID!");
		}
		
		return paymentID;
	}
	
	@Override
	public int getNumOfFields(){
		return NUM_OF_FIELDS;
	}
	
	@Override
	public ObservableList<String> getTableColumns(){
		ObservableList<String> columnsList = FXCollections.observableArrayList();
		columnsList.addAll("Payment ID", "Card Holder Name", "Card Number", "Security Number", "Movie Tokens Used", "Expiry Date", "Total Amount Paid");
		return columnsList;
	}
	
	@Override
	public ObservableList<Node> getInputFields(){
		TextField txtPaymentID = new TextField();
		txtPaymentID.setPromptText("Payment ID");
		TextField txtName = new TextField();
		txtName.setPromptText("Card Holder Name");
		TextField txtCardNumber = new TextField();
		txtCardNumber.setPromptText("Card Number");
		TextField txtSecurityNumber = new TextField();
		txtSecurityNumber.setPromptText("Security Number");
		TextField txtMovieTokensUsed = new TextField();
		txtMovieTokensUsed.setPromptText("Movie Tokens Used");
		DatePicker expiryDate = new DatePicker();
		expiryDate.setPromptText("Expiry Date");
		TextField txtTotalAmountPaid = new TextField();
		txtTotalAmountPaid.setPromptText("Total Amount Paid");
		
		ObservableList<Node> inputFields = FXCollections.observableArrayList();
		inputFields.addAll(txtPaymentID, txtName, txtCardNumber, txtSecurityNumber, txtMovieTokensUsed, expiryDate, txtTotalAmountPaid);
		return inputFields;
	}
	
	@Override
	public void updateFile(ObservableList<Node> listOfInputFields) throws NumberFormatException{
		try{
			try (DataOutputStream fileOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(TEMP_FILE)))){
				for(int i=0; i<listOfInputFields.size(); i+=NUM_OF_FIELDS){
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i))).getText());
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 1))).getText());
					fileOutput.writeLong(Long.parseLong(((TextField)(listOfInputFields.get(i + 2))).getText()));
					fileOutput.writeInt(Integer.parseInt(((TextField)(listOfInputFields.get(i + 3))).getText()));
					fileOutput.writeInt(Integer.parseInt(((TextField)(listOfInputFields.get(i + 4))).getText()));
					fileOutput.writeLong((Date.valueOf(((DatePicker)(listOfInputFields.get(i + 5))).getValue())).getTime());
					fileOutput.writeDouble(Double.parseDouble(((TextField)(listOfInputFields.get(i + 6))).getText()));
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
					((TextField)(dataList.get(2))).setText(String.valueOf(fileInput.readLong()));
					((TextField)(dataList.get(3))).setText(String.valueOf(fileInput.readInt()));
					((TextField)(dataList.get(4))).setText(String.valueOf(fileInput.readInt()));
					((DatePicker)(dataList.get(5))).setValue((new Date(fileInput.readLong())).toLocalDate());
					((TextField)(dataList.get(6))).setText(String.valueOf(fileInput.readDouble()));
					
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
