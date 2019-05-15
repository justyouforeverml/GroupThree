package application;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SeatView {
	public static TextField seatInfo;  // 动态展示座位信息
	private int room;  // 房间号
	private int id;  // 座位号
	private int state; // 取值-a、0、+b，分别表示该座将被占用a分钟、未设座位、将空闲b分钟（a、b>0）
	private Button seatButton;  
	
	// 构造函数
	public SeatView(int room, int id, int state){
		this.setRoom(room);
		this.setId(id);
		this.setSeatButton(new Button());
		seatButton.setPrefSize(Util.prefWidth, Util.prefHeight);
		this.setState(state);
	}
	
	public SeatView(int room, int id){  // 默认无座
		this.setRoom(room);
		this.setId(id);
		this.setSeatButton(new Button());
		seatButton.setPrefSize(Util.prefWidth, Util.prefHeight);
		this.setState(0);
	}
	
	public String getInfo(){  // 获取展示信息
		String info;
		if(state < 0){
			info = room+"室"+id+"座"+"在未来"+(-state)+"分钟内被占用";
		}else if(state == 0){
			info = "单击设置新座位";
		}else{
			info = room+"室"+id+"座"+"在未来"+(state)+"分钟内空闲";
		}
		return info;
	}

	public int getRoom() {
		return room;
	}

	public void setRoom(int room) {
		this.room = room;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getState(){
		return state;
	}
	
	public void setState(int state){
		this.state = state;
		//初始化button样式
		String buttonColor;
		if(this.state < 0){
			buttonColor = Util.BUSY_STYLE;
			seatButton.setOnAction(null);
		}else if(this.state == 0){
			buttonColor = Util.NONE_STYLE;
			seatButton.setOnAction(new EventHandler<ActionEvent>(){  // 单击添加座位
				@Override
				public void handle(ActionEvent event) {
					Alert addSeatAlert = new Alert(AlertType.CONFIRMATION);
					addSeatAlert.setTitle("confirmation");
					addSeatAlert.setHeaderText(Util.ADD_SEAT_CONFIRM);
					Optional<ButtonType> result = addSeatAlert.showAndWait();
					if (result.get() == ButtonType.OK){
					    setState(1400);  // 未来24小时内空闲
					    seatInfo.setText(getInfo());
					}
				}
			});
		}else{
			buttonColor = Util.FREE_STYLE;
			seatButton.setOnAction(null);
		}
		seatButton.setStyle(buttonColor);

		seatButton.setOnMouseEntered(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				seatInfo.setText(getInfo());
			}
		});
		seatButton.setOnMouseExited(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				seatInfo.setText("");
			}
		});
	}
	
	public Button getSeatButton(){
		return seatButton;
	}
	
	public void setSeatButton(Button seatButton){
		this.seatButton = seatButton;
	}
}
