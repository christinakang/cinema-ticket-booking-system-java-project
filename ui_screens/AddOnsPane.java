package ui_screens;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import larn.modernUI.WaveButton;
import real_objects.AddOns;

public class AddOnsPane extends BorderPane
{
	public ArrayList<AddOns> addOnsLists = AddOns.retrieveAllFileRecords();
	public FlowPane flowAddOns = new FlowPane();
	
	public AddOnsPane()
	{
		Text txtEmptySection = new Text("No AddOns In This Section");
		txtEmptySection.setStyle("-fx-fill: white; -fx-font-size: 30px; -fx-font-family: Courier; -fx-font-weight: bold;");
		
		flowAddOns.setPadding(new Insets(5, 5, 5, 5));
		flowAddOns.setHgap(25);
		flowAddOns.setVgap(25);
		flowAddOns.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35)");
		
		flowAddOns.getChildren().clear();
		flowAddOns.setAlignment(Pos.BASELINE_LEFT);
		
		for(int i=0; i<addOnsLists.size(); i++)
		{
			addAddOnsToDisplay(i);
		}
		if(flowAddOns.getChildren().size() == 0)
		{
			flowAddOns.getChildren().add(txtEmptySection);
			flowAddOns.setAlignment(Pos.CENTER);
		}
		
		setCenter(flowAddOns);
	}
	
	public void addAddOnsToDisplay(int index)
	{
		try{
			ImageView imgAddOns = new ImageView(new Image(new FileInputStream("bin/" + addOnsLists.get(index).getImgAddress())));
			imgAddOns.setFitWidth(200);
			imgAddOns.setFitHeight(200);
			
			Label name = new Label(addOnsLists.get(index).getName());
			name.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
			
			Label labDetails = new Label();
			labDetails.setText(addOnsLists.get(index).getDetails());
			labDetails.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
			labDetails.setPrefWidth(200);
			labDetails.setWrapText(true);
			
			Double addOnPrice = addOnsLists.get(index).getPrice();
			Label labPrice = new Label("RM " + String.format("%.2f", addOnPrice));
			labPrice.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
			
			WaveButton minus = new WaveButton("-");
			TextField textfield = new TextField("0");
			textfield.setStyle("-fx-font-size: 20px; -fx-font-family: Courier; -fx-font-weight: bold;");
			textfield.setPrefWidth(100);
			textfield.setEditable(false);
			WaveButton plus = new WaveButton("+");
			
			textfield.setAlignment(Pos.CENTER);
			
			minus.setOnAction(e ->{
				int amount = Integer.parseInt(textfield.getText());
				amount = amount - 1;
				if(amount >= 0)
					BookingSummaryTab.setTicketTotal(BookingSummaryTab.getTicketTotal() - addOnsLists.get(index).getPrice());
				if(amount < 0)
				{
					amount = 0;
				}
				String amount2 = Integer.toString(amount);
				textfield.setText(amount2);
				
				BookingSummaryTab.getMapAddOns().put(addOnsLists.get(index).getAddOnsID(), amount);
				BookingSummaryTab.addOnsListChange();
			});
			
			plus.setOnAction(e ->{
				int amount = Integer.parseInt(textfield.getText());
				amount = amount + 1;
				String amount2 = Integer.toString(amount);
				textfield.setText(amount2);
				BookingSummaryTab.setTicketTotal(BookingSummaryTab.getTicketTotal() + addOnsLists.get(index).getPrice());
				BookingSummaryTab.getMapAddOns().put(addOnsLists.get(index).getAddOnsID(), amount);
				BookingSummaryTab.addOnsListChange();
			});
			
			HBox grouping = new HBox();
			grouping.getChildren().addAll(minus, textfield, plus);
			
			VBox grouping2 = new VBox();
			grouping2.setAlignment(Pos.CENTER);
			grouping2.getChildren().addAll(imgAddOns, name, labPrice,labDetails,grouping);
			
			flowAddOns.getChildren().add(grouping2);
		}
		catch(FileNotFoundException ex){
			System.out.println("Darling, image file at specified location does not exist!");
		}
	}
}
