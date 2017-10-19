package databaseAccess;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;

public class DateTimePicker extends HBox{
	
	private DatePicker datePicker = new DatePicker();
	private ComboBox<String> comboHours = new ComboBox<>();
	private ComboBox<String> comboMins = new ComboBox<>();
	
	public DateTimePicker(){
		for (int i=0; i<24; i++){
			comboHours.getItems().add(String.format("%02d", i));
		}
		comboHours.setValue(comboHours.getItems().get(0));
		
		for (int i=0; i<60; i++){
			comboMins.getItems().add(String.format("%02d", i));
		}
		comboMins.setValue(comboMins.getItems().get(0));
		
		getChildren().addAll(datePicker, comboHours, comboMins);
	}
	
	public DatePicker getDatePicker() {
		return datePicker;
	}

	public void setDatePicker(DatePicker datePicker) {
		this.datePicker = datePicker;
	}

	public ComboBox<String> getComboHours() {
		return comboHours;
	}

	public void setComboHours(ComboBox<String> comboHours) {
		this.comboHours = comboHours;
	}

	public ComboBox<String> getComboMins() {
		return comboMins;
	}

	public void setComboMins(ComboBox<String> comboMins) {
		this.comboMins = comboMins;
	}

	public Long getShowTime(){
		Long dateTime =  Date.valueOf(datePicker.getValue()).getTime();
		dateTime += Long.parseLong(comboHours.getValue()) * 3600000;
		dateTime += Long.parseLong(comboMins.getValue()) * 60000;
		return dateTime;
	}
	
	public void setShowTime(Long dateTime){
		Calendar date = new GregorianCalendar();
		date.setTime(new Date(dateTime));
		datePicker.setValue(new Date(dateTime).toLocalDate());
		comboHours.setValue(String.format("%02d", date.get(Calendar.HOUR_OF_DAY)));
		comboMins.setValue(String.format("%02d", date.get(Calendar.MINUTE)));
	}
}
