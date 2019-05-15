package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student {
	private SimpleIntegerProperty id = new SimpleIntegerProperty();
	private SimpleStringProperty belongClass = new SimpleStringProperty();
	private SimpleIntegerProperty room = new SimpleIntegerProperty();  // -1表无座
	private SimpleIntegerProperty seat = new SimpleIntegerProperty();  // -1表无座
	
	public Student(int id, String belongClass, int room, int seat){
		this.id.set(id);
		this.belongClass.set(belongClass);
		this.room.set(room);
		this.seat.set(seat);
	}
	
	public int getId() {
		return id.get();
	}
	public void setId(int id) {
		this.id.set(id);
	}
	public String getBelongClass() {
		return belongClass.get();
	}
	public void setBelongClass(String belongClass) {
		this.belongClass.set(belongClass);
	}
	public int getSeat() {
		return seat.get();
	}
	public void setSeat(int seat) {
		this.seat.set(seat);
	}
	
	public int getRoom() {
		return room.get();
	}

	public void setRoom(int room) {
		this.room.set(room);
	}
	
	public SimpleIntegerProperty getIdProperty(){
		return id;
	}
	
	public SimpleStringProperty getBelongClassProperty(){
		return belongClass;
	}
	
	public SimpleIntegerProperty getRoomProperty(){
		return room;
	}
	public SimpleIntegerProperty getSeatProperty(){
		return seat;
	}
}
