package databaseAccess;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public interface DatabaseAccessUpdate {
	
	ObservableList<String> getTableColumns();
	
	ObservableList<Node> getFileRecords();
	
	int getNumOfFields();
	
	ObservableList<Node> getInputFields();
	
	void updateFile(ObservableList<Node> rowOfInputFields);
	
	boolean addFileRecord();
	
	boolean updateFileRecord();
	
	boolean deleteFileRecord();
}
