package real_objects;

import databaseAccess.DatabaseAccessUpdate;
import databaseAccess.DatabaseTablesView;
import java.io.*;
import java.sql.Date;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class Movie implements DatabaseAccessUpdate, Comparable<Movie>{
	
	private String movieID;
	private String title;
	private static final String[] genreTypes = {"Comedy", "Thriller", "Horror", "Adventure", "Fantasy", "Mystery", 
												"Sport", "Romance", "Animation", "Documentary", "Biography",
												"History", "Musical", "Drama", "Sci-Fi", "Crime", "Family"};
	private String genres;
	private static final String[] ratings = {"U", "P13", "18"};
	private String rate;
	private static final String[] languages = {"English", "Malay", "Tamil", "Telungu", "Malayalam", "Chinese", 
											   "Korean", "Japanese", "Hindi"};
	private String subtitles;
	private String language;
	private Date releaseDate;
	private int runningTime;
	private String director;
	private String cast;
	private String distributor;
	private String synopsis;
	private String moviePosterAddress;
	private String trailerAddress;
	private double moviePriceRate;
	private static final int NUM_OF_FIELDS = 15;
	private static final String FILE_STRING = "Cinema Files/Movie.dat";
	private static final String TEMP_FILE = "Cinema Files/MovieTemp.dat";
	
	public Movie(){
		this("");
	}
	
	public Movie(String title){
		this.title = title;
	}

	public String getMovieID() {
		return movieID;
	}

	public void setMovieID(String movieID) {
		this.movieID = movieID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getSubtitles() {
		return subtitles;
	}

	public void setSubtitles(String subtitles) {
		this.subtitles = subtitles;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(int runningTime) {
		this.runningTime = runningTime;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getDistributor() {
		return distributor;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getMoviePosterAddress() {
		return moviePosterAddress;
	}

	public void setMoviePosterAddress(String moviePosterAddress) {
		this.moviePosterAddress = moviePosterAddress;
	}

	public String getTrailerAddress() {
		return trailerAddress;
	}

	public void setTrailerAddress(String trailerAddress) {
		this.trailerAddress = trailerAddress;
	}

	public static String[] getGenreTypes() {
		return genreTypes;
	}

	public static String[] getRatings() {
		return ratings;
	}

	public static String[] getLanguages() {
		return languages;
	}

	public double getMoviePriceRate() {
		return moviePriceRate;
	}

	public void setMoviePriceRate(double moviePriceRate) {
		this.moviePriceRate = moviePriceRate;
	}
	
	@Override
	public String toString(){
		return getTitle();
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
							String movieIDFromRecord = randAccessFile.readUTF();
							if (!movieID.equals(movieIDFromRecord)){
								randTempFile.writeUTF(movieIDFromRecord);
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeLong(randAccessFile.readLong());
								randTempFile.writeInt(randAccessFile.readInt());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeUTF(randAccessFile.readUTF());
								randTempFile.writeDouble(randAccessFile.readDouble());
							}
							else{
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readLong();
								randAccessFile.readInt();
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readUTF();
								randAccessFile.readUTF();
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
			String movieIDFromRecord = randAccessFile.readUTF();
			String movieTitleFromRecord = randAccessFile.readUTF();
			long recordStartIndex = 0;
			while(!(title.equals(movieTitleFromRecord) || movieID.equals(movieIDFromRecord))){
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				randAccessFile.readLong();
				randAccessFile.readInt();
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				randAccessFile.readUTF();
				randAccessFile.readDouble();
				
				recordStartIndex = randAccessFile.getFilePointer();
				movieIDFromRecord = randAccessFile.readUTF();
				movieTitleFromRecord = randAccessFile.readUTF();
			}
			if (title.equals(movieTitleFromRecord) || movieID.equals(movieIDFromRecord)){
				randAccessFile.seek(recordStartIndex);
				return true;
			}
		}
		catch(EOFException ex){}
		return false;
	}
	
	//To write a single record to the file
	private void writeFileRecord(RandomAccessFile randAccessFile) throws IOException{
		randAccessFile.writeUTF(movieID);
		randAccessFile.writeUTF(title);
		randAccessFile.writeUTF(genres);
		randAccessFile.writeUTF(rate);
		randAccessFile.writeUTF(subtitles);
		randAccessFile.writeUTF(language);
		randAccessFile.writeLong(releaseDate.getTime());
		randAccessFile.writeInt(runningTime);
		randAccessFile.writeUTF(director);
		randAccessFile.writeUTF(cast);
		randAccessFile.writeUTF(distributor);
		randAccessFile.writeUTF(synopsis);
		randAccessFile.writeUTF(moviePosterAddress);
		randAccessFile.writeUTF(trailerAddress);
		randAccessFile.writeDouble(moviePriceRate);
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
								String movieIDFromRecord = randAccessFile.readUTF();
								if (!movieID.equals(movieIDFromRecord)){
									randTempFile.writeUTF(movieIDFromRecord);
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeLong(randAccessFile.readLong());
									randTempFile.writeInt(randAccessFile.readInt());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeUTF(randAccessFile.readUTF());
									randTempFile.writeDouble(randAccessFile.readDouble());
								}
								else{
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readLong();
									randAccessFile.readInt();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readUTF();
									randAccessFile.readDouble();
									
									for(MovieSession session: MovieSession.retrieveAllFileRecords()){
										if(session.getMovieID().equals(movieIDFromRecord))
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
	public Movie retrieveFileRecord(String movieID){
		this.movieID = movieID;
		try{
			try(RandomAccessFile randAccessFile = new RandomAccessFile(FILE_STRING, "r")){
				if (existsFileRecord(randAccessFile)){
					this.movieID = randAccessFile.readUTF();
					title = randAccessFile.readUTF();
					genres = randAccessFile.readUTF();
					rate = randAccessFile.readUTF();
					subtitles = randAccessFile.readUTF();
					language = randAccessFile.readUTF();
					releaseDate = new Date(randAccessFile.readLong());
					runningTime = randAccessFile.readInt();
					director = randAccessFile.readUTF();
					cast = randAccessFile.readUTF();
					distributor = randAccessFile.readUTF();
					synopsis = randAccessFile.readUTF();
					moviePosterAddress = randAccessFile.readUTF();
					trailerAddress = randAccessFile.readUTF();
					moviePriceRate = randAccessFile.readDouble();
					
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
	public static ArrayList<Movie> retrieveAllFileRecords(){
		ArrayList<Movie> movieRecords = new ArrayList<>();
		
		try{
			try(DataInputStream inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_STRING)))){
				while(inputFile.available() != 0){
					Movie movie = new Movie();
					movie.setMovieID(inputFile.readUTF());
					movie.setTitle(inputFile.readUTF());
					movie.setGenres(inputFile.readUTF());
					movie.setRate(inputFile.readUTF());
					movie.setSubtitles(inputFile.readUTF());
					movie.setLanguage(inputFile.readUTF());
					movie.setReleaseDate(new Date(inputFile.readLong()));
					movie.setRunningTime(inputFile.readInt());
					movie.setDirector(inputFile.readUTF());
					movie.setCast(inputFile.readUTF());
					movie.setDistributor(inputFile.readUTF());
					movie.setSynopsis(inputFile.readUTF());
					movie.setMoviePosterAddress(inputFile.readUTF());
					movie.setTrailerAddress(inputFile.readUTF());
					movie.setMoviePriceRate(inputFile.readDouble());
					
					movieRecords.add(movie);
				}
			}
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with retrieving records!");
		}
		
		return movieRecords;
	}
	
	public static String generateID(){
		String movieID = "MID1";
		ArrayList<Integer> IDNumbers = new ArrayList<>();
		try{
			try(DataInputStream inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_STRING)))){
				while(inputFile.available() != 0){
					IDNumbers.add(Integer.parseInt(inputFile.readUTF().replaceAll("[\\D]", "")));
					inputFile.readUTF();
					inputFile.readUTF();
					inputFile.readUTF();
					inputFile.readUTF();
					inputFile.readUTF();
					inputFile.readLong();
					inputFile.readInt();
					inputFile.readUTF();
					inputFile.readUTF();
					inputFile.readUTF();
					inputFile.readUTF();
					inputFile.readUTF();
					inputFile.readUTF();
					inputFile.readDouble();
				}
				int ID = 1;
				for (; ID < Integer.MAX_VALUE; ID++){
					if (!IDNumbers.contains(ID))
						break;
				}
				movieID = "MID" + ID;
			}
		}
		catch(IOException ex){
			System.out.println("Hola amigos, something went wrong with retrieving records to generate ID!");
		}
		
		return movieID;
	}
	
	@Override
	public int getNumOfFields(){
		return NUM_OF_FIELDS;
	}
	
	@Override
	public ObservableList<String> getTableColumns(){
		ObservableList<String> columnsList = FXCollections.observableArrayList();
		columnsList.addAll("Movie ID", "Title", "Genres", "Rate", "Subtitles", "Language", "Release Date", "Running Time", "Director", "Cast", "Distributor", "Synopsis", "Movie Poster", "Movie Trailer", "Movie Price Rate");
		return columnsList;
	}
	
	@Override
	public ObservableList<Node> getInputFields(){
		TextField txtMovieID = new TextField();
		txtMovieID.setPromptText("Movie ID");
		TextField txtTitle = new TextField();
		txtTitle.setPromptText("Title");
		TextField txtGenres = new TextField();
		txtGenres.setPromptText("Genres");
		TextField txtRate = new TextField();
		txtRate.setPromptText("Rate");
		TextField txtSubtitles = new TextField();
		txtSubtitles.setPromptText("Subtitles");
		TextField txtLanguage = new TextField();
		txtLanguage.setPromptText("Language");
		DatePicker releaseDate = new DatePicker();
		releaseDate.setPromptText("Release Date");
		TextField txtRunTime = new TextField();
		txtRunTime.setPromptText("Running Time");
		TextField txtDirector = new TextField();
		txtDirector.setPromptText("Director");
		TextField txtCast = new TextField();
		txtCast.setPromptText("Cast");
		TextField txtDistributor = new TextField();
		txtDistributor.setPromptText("Distributor");
		TextArea txtSynopsis = new TextArea();
		txtSynopsis.setPromptText("Synopsis");
		txtSynopsis.setWrapText(true);
		txtSynopsis.setPrefRowCount(3);
		txtSynopsis.setPrefColumnCount(30);
		
		TextField txtMoviePoster = new TextField();
		txtMoviePoster.setPromptText("Movie Poster");
		FileChooser posterFileChooser = new FileChooser();
		posterFileChooser.setTitle("Choose Image File for Movie Poster");
		posterFileChooser.setInitialDirectory(new File("bin/Images/MoviePoster"));
		txtMoviePoster.setOnMouseClicked(e -> {
			File imageFile = posterFileChooser.showOpenDialog(null);
			if (imageFile != null){
				String imageAddress = "Images/MoviePoster/" + imageFile.getName();
				txtMoviePoster.setText(imageAddress);
			}
		});
		
		TextField txtTrailer = new TextField();
		txtTrailer.setPromptText("Movie Trailer");
		FileChooser trailerFileChooser = new FileChooser();
		trailerFileChooser.setTitle("Choose Video File for Movie Trailer");
		trailerFileChooser.setInitialDirectory(new File("bin/Videos"));
		txtTrailer.setOnMouseClicked(e -> {
			File videoFile = trailerFileChooser.showOpenDialog(null);
			if (videoFile != null){
				String videoAddress = "Videos/" + videoFile.getName();
				txtTrailer.setText(videoAddress);
			}
		});
		
		TextField txtPriceRate = new TextField();
		txtPriceRate.setPromptText("Movie Price Rate");
		
		ObservableList<Node> inputFields = FXCollections.observableArrayList();
		inputFields.addAll(txtMovieID, txtTitle, txtGenres, txtRate, txtSubtitles, txtLanguage, releaseDate, txtRunTime, txtDirector, txtCast, txtDistributor, txtSynopsis, txtMoviePoster, txtTrailer, txtPriceRate);
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
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 4))).getText());
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 5))).getText());
					fileOutput.writeLong((Date.valueOf(((DatePicker)(listOfInputFields.get(i + 6))).getValue())).getTime());
					fileOutput.writeInt(Integer.parseInt(((TextField)(listOfInputFields.get(i + 7))).getText()));
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 8))).getText());
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 9))).getText());
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 10))).getText());
					fileOutput.writeUTF(((TextArea)(listOfInputFields.get(i + 11))).getText());
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 12))).getText());
					fileOutput.writeUTF(((TextField)(listOfInputFields.get(i + 13))).getText());
					fileOutput.writeDouble(Double.parseDouble(((TextField)(listOfInputFields.get(i + 14))).getText()));
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
					((TextField)(dataList.get(4))).setText(fileInput.readUTF());
					((TextField)(dataList.get(5))).setText(fileInput.readUTF());
					((DatePicker)(dataList.get(6))).setValue((new Date(fileInput.readLong())).toLocalDate());
					((TextField)(dataList.get(7))).setText(String.valueOf(fileInput.readInt()));
					((TextField)(dataList.get(8))).setText(fileInput.readUTF());
					((TextField)(dataList.get(9))).setText(fileInput.readUTF());
					((TextField)(dataList.get(10))).setText(fileInput.readUTF());
					((TextArea)(dataList.get(11))).setText(fileInput.readUTF());
					((TextField)(dataList.get(12))).setText(fileInput.readUTF());
					((TextField)(dataList.get(13))).setText(fileInput.readUTF());
					((TextField)(dataList.get(14))).setText(String.valueOf(fileInput.readDouble()));
					
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
	public int compareTo(Movie movie) {
		return this.getTitle().compareTo(movie.getTitle());
	}
}