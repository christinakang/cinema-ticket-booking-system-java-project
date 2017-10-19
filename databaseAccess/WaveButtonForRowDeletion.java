package databaseAccess;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import larn.modernUI.WaveButton;

public class WaveButtonForRowDeletion extends WaveButton{
	private int rowNum;
	private ObservableList<Node> inputRow = FXCollections.observableArrayList();
	
	public WaveButtonForRowDeletion(){
		ImageView imgDelete = new ImageView("Images/Icons/Delete.png");
		imgDelete.setFitHeight(15);
		imgDelete.setFitWidth(15);
		setGraphic(imgDelete);
		setOnMouseEntered(e -> setCursor(Cursor.HAND));
		setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public ObservableList<Node> getInputRow() {
		return inputRow;
	}

	public void setInputRow(ObservableList<Node> inputRow) {
		this.inputRow = inputRow;
	}
}
