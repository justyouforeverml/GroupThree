package application;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import javafx.util.StringConverter;


public class Controller implements Initializable{
	public String[] daysOfWeek = {"日", "一", "二", "三", "四", "五", "六"};
	public String[] classNames;
	public ArrayList<Student> students;  // 学生数组
	public int room = 0;  // 当前阅览室
	public int[] seats;  // 一个阅览室的座位状态
	public SeatView[][] seatViews;  // 座位表
	public String lastId;
	public String lastRoom;
	public String lastSeat;
	
	// Left
	@FXML
	private DatePicker datePicker;
	
	@FXML
	private TextField hourTextField;
	
	@FXML
	private TextField minuteTextField;
	
	@FXML
	private Button queryButton;  // 查询
	
	@FXML
	private TextField weekTextField;  // 教学周
	
	@FXML
	private TextField dayTextField;  // 星期
	
	@FXML
	private Label tip;  // 说明教学起始周
	
	@FXML
	private TabPane tabPane;
	
	@FXML
	private Tab tab0;
	
	@FXML
	private FlowPane seatPane;  // 阅览室
	
	@FXML
	private Button legend1;  // 占用
	
	@FXML
	private Button legend2;  // 空闲
	
	@FXML
	private Button legend3;  // 无座
	
	@FXML
	private TextField seatInfo;
	
	// Right
	@FXML
	private TableView<Student> studentTable;
	
	@FXML
	private TextField newId;
	
	@FXML
	private TextField chooseClass;
	
	@FXML
	private TextField chooseRoom;
	
	@FXML
	private TextField chooseSeat;
	
	@FXML
	private Button addButton;
	
	@FXML
	private ImageView imageView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image image = new Image(Util.IMAGE_OF_EXCEL_PATH);
		imageView.setImage(image);

		// 为图例标注颜色
		legend1.setStyle(Util.BUSY_STYLE);
		legend2.setStyle(Util.FREE_STYLE);
		legend3.setStyle(Util.NONE_STYLE);
		
		// 设置初始时间为当前时间
		LocalDateTime now = LocalDateTime.now();
		datePicker.setValue(now.toLocalDate());
		datePicker.setEditable(false);
		hourTextField.setText(String.valueOf(now.getHour()));
		minuteTextField.setText(String.valueOf(now.getMinute()));
		weekTextField.setText(String.valueOf(getWeek(now.toLocalDate())+1));
		dayTextField.setText(daysOfWeek[now.toLocalDate().getDayOfWeek().getValue()%7]);
		
		tip.setText("（第1周的周一为"+Util.WEEK_INIT+"）");
		weekTextField.setEditable(false);

		
		// 初始化座位表
		seatPane.setPadding(new Insets(8, 8, 8, 8));
		seatPane.setHgap(Util.Hgap);
		seatPane.setVgap(Util.Vgap);
		seatInfo.setEditable(false);
		SeatView.seatInfo = seatInfo;  // 座位信息展示
		seatViews = null;
		// TODO seats = query(room, LocalDateTime);
//		for(int i=0;i<Util.ROOM_NUM;i++){
			int[] a = {0, 1, -1, 50, 55, 20, -20, -35, -90, 10};   //通过n个的状态值设置座位（n<=81） 
			seats = a;  
			initSeat(room, seats);
//		}
		
		tab0.setOnSelectionChanged(new EventHandler<Event>() {  // 第0号阅览室
			@Override
			public void handle(Event event) {
				FlowPane blank = new FlowPane();
				Tab tab = (Tab) event.getSource();
				if(tab.isSelected()){
					room = 0;
					tab.setContent(blank);
				//  TODO seats = query(room, LocalDateTime);
					int[] a = {0, 1, -1, 50, 55, 2, -20, -35, -90, 10,0, 1, -1, 50, 55};  
					seats = a;
					updateSeat(room, seats);
					tab.setContent(seatPane);
				}
			}
		});
		
		for(int i=1;i<Util.ROOM_NUM;i++){  // 多个阅览室
			Tab tab = new Tab();
			tab.setText("阅览室"+i);
			tab.setId(String.valueOf(i));
			tabPane.getTabs().add(tab);
			tab.setOnSelectionChanged(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					FlowPane blank = new FlowPane();
					Tab tab = (Tab) event.getSource();
					if(tab.isSelected()){
						room = Integer.parseInt(tab.getId());
						tab.setContent(blank);
						//  TODO seats = query(room, LocalDateTime);
						int[] a = {0, 1, -1, 50, 55, 20, -20, -35, -90, 10,0, 1, -1, 50, 55, 20, -20, -35, -90, 10, -20, -35, -90, 10,0, 1, -1, 50, 55};
						seats = a;
						updateSeat(room, seats);
						tab.setContent(seatPane);
						}
					}
				});
			}
		
		
		//  TODO 初始学生信息和班级名称
		String [] c = {"旅行社151105", "旅行社151106","旅行社151107","旅行社151108"};
		classNames = c;
		students = new ArrayList<Student>();
		students.add(new Student(201853201, "旅行社151105", 0, 3));
		students.add(new Student(201853202, "旅行社151106", 1, 7));
		students.add(new Student(201853203, "旅行社151107", 2, 5));
		students.add(new Student(201853204, "旅行社151106", 6, 2));
		students.add(new Student(201853205, "旅行社151105", 3, 4));
		students.add(new Student(201853206, "旅行社151108", 5, 55));
//		students.add(new Student(201853207, "旅行社151105", 3, 2));
//		students.add(new Student(201853208, "旅行社151107", 6, 36));
		studentTable.setEditable(true);
		ObservableList<Student> studentData = FXCollections.observableArrayList(students);

		// 学号列 
		TableColumn<Student, Number>  idCol = new TableColumn<Student, Number>("学号");
		idCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student,Number>, ObservableValue<Number>>() {
			@Override
			public ObservableValue<Number> call(CellDataFeatures<Student, Number> param) {
				return param.getValue().getIdProperty();
			}
		});
		idCol.setMinWidth(100);
		idCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Number>(){
			@Override
			public String toString(Number object) {
				lastId = String.valueOf(object.intValue());
				return String.valueOf(object.intValue());
			}
			@Override
			public Number fromString(String string) {
				try{
				return Integer.valueOf(string);
				}catch(NumberFormatException e){
					showError(Util.NUMBER_ERROR);
					return Integer.valueOf(lastId);
				}
			}
			
		}));
		idCol.setOnEditCommit(new EventHandler<CellEditEvent<Student, Number>>(){
			@Override
			public void handle(CellEditEvent<Student, Number> event) {
				int row = event.getTablePosition().getRow();
				boolean flag = true;
				for(int i=0;i<students.size();i++){
					if(i!=row&&students.get(i).getId()==event.getNewValue().intValue()){
						flag = false;
						break;
					}
				}
				if(flag){
					students.get(row).setId(event.getNewValue().intValue());
				}else{
					students.get(row).setId(event.getOldValue().intValue());
					showError(Util.ID_ERROR);
					studentTable.refresh();
				}
			}
		});
		// 班级列
		TableColumn<Student, String>  classCol = new TableColumn<Student, String>("班级");
		classCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Student, String> param) {
				return param.getValue().getBelongClassProperty();
			}
		});
		classCol.setMinWidth(150);
		classCol.setCellFactory(TextFieldTableCell.forTableColumn());
		classCol.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>(){
			@Override
			public void handle(CellEditEvent<Student, String> event) {
				int row = event.getTablePosition().getRow();
				boolean flag = false;
				for(int i=0;i<classNames.length;i++){
					if(event.getNewValue().equals(classNames[i])){
						flag = true;
					}
				}
				if(flag){
					students.get(row).setBelongClass(event.getNewValue());
				}else{
					students.get(row).setBelongClass(event.getOldValue());
					showError(Util.CLASSNAME_ERROR);
					studentTable.refresh();
				}
			}
		});
//		// 阅览室列 
		TableColumn<Student, Number>  roomCol = new TableColumn<Student, Number>("阅览室");
		roomCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student,Number>, ObservableValue<Number>>() {
			@Override
			public ObservableValue<Number> call(CellDataFeatures<Student, Number> param) {
				return param.getValue().getRoomProperty();
			}
		});
		roomCol.setMinWidth(75);
		roomCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Number>(){
			@Override
			public String toString(Number object) {
				lastRoom = String.valueOf(object.intValue());
				return String.valueOf(object.intValue());
			}
			@Override
			public Number fromString(String string) {
				try{
				return Integer.valueOf(string);
				}catch(NumberFormatException e){
					showError(Util.NUMBER_ERROR);
					return Integer.valueOf(lastRoom);
				}
			}
			
		}));
		roomCol.setOnEditCommit(new EventHandler<CellEditEvent<Student, Number>>(){
			@Override
			public void handle(CellEditEvent<Student, Number> event) {
				int row = event.getTablePosition().getRow();
				if(isEmptySeat(event.getNewValue().intValue(),students.get(row).getSeat())){
					students.get(row).setRoom(event.getNewValue().intValue());
				}else{
					students.get(row).setRoom(event.getOldValue().intValue());
					studentTable.refresh();
				}
				
			}
		});
		// 座位号列
		TableColumn<Student, Number>  seatCol = new TableColumn<Student, Number>("座位号");
		seatCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student,Number>, ObservableValue<Number>>() {

			@Override
			public ObservableValue<Number> call(CellDataFeatures<Student, Number> param) {
				return param.getValue().getSeatProperty();
			}
		});
		seatCol.setMinWidth(75);
		seatCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Number>(){
			@Override
			public String toString(Number object) {
				lastSeat = String.valueOf(object.intValue());
				return String.valueOf(object.intValue());
			}
			@Override
			public Number fromString(String string) {
				try{
				return Integer.valueOf(string);
				}catch(NumberFormatException e){
					showError(Util.NUMBER_ERROR);
					return Integer.valueOf(lastSeat);
				}
			}
			
		}));
		seatCol.setOnEditCommit(new EventHandler<CellEditEvent<Student, Number>>(){
			@Override
			public void handle(CellEditEvent<Student, Number> event) {
				int row = event.getTablePosition().getRow();
				if(isEmptySeat(students.get(row).getRoom(), event.getNewValue().intValue())){
					students.get(row).setSeat(event.getNewValue().intValue());
				}else{
					students.get(row).setSeat(event.getOldValue().intValue());
					studentTable.refresh();
				}
			}
		});
		// 删除
		TableColumn<Student, Integer> deleteCol = new TableColumn<Student, Integer>("删除");
        deleteCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        deleteCol.setCellFactory(new Callback<TableColumn<Student, Integer>, TableCell<Student, Integer>>() {
            @Override
            public TableCell<Student, Integer> call(final TableColumn<Student, Integer> param) {
                final TableCell<Student, Integer> cell = new TableCell<Student, Integer>() {
                    final Button btn = new Button("删除");
                    @Override
                    public void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                            	Alert deleteStudentAlert = new Alert(AlertType.CONFIRMATION);
                            	deleteStudentAlert.setTitle("confirmation");
                            	deleteStudentAlert.setHeaderText(Util.DELETE_STUDENT_CONFIRM);
            					Optional<ButtonType> result = deleteStudentAlert.showAndWait();
            					if (result.get() == ButtonType.OK){
	                                getTableView().getItems().remove(getIndex());
	                                students.remove(getIndex());
            					}
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        // 修改
        TableColumn<Student, Integer> alterCol = new TableColumn<Student, Integer>("修改");
        alterCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        alterCol.setCellFactory(new Callback<TableColumn<Student, Integer>, TableCell<Student, Integer>>() {
            @Override
            public TableCell<Student, Integer> call(final TableColumn<Student, Integer> param) {
                final TableCell<Student, Integer> cell = new TableCell<Student, Integer>() {

                    final Button btn = new Button("修改");

                    @Override
                    public void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                            	// TODO 
                                Alert tipAlert = new Alert(AlertType.INFORMATION);
                        		tipAlert.setTitle("tip");
                        		tipAlert.setHeaderText(Util.TIP_INFO);
                        		tipAlert.show();
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        
        //  合并两列
        TableColumn<Student, Object> actionCol = new TableColumn<Student, Object>("操作");
        actionCol.getColumns().add(alterCol);
        actionCol.getColumns().add(deleteCol);
              
		studentTable.setItems(studentData);  // 加载数据
		studentTable.getColumns().add(idCol);
		studentTable.getColumns().add(classCol);
		studentTable.getColumns().add(roomCol);
		studentTable.getColumns().add(seatCol);
		studentTable.getColumns().add(actionCol);
		
		addButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				try{
					int id = Integer.parseInt(newId.getText());
					String className = chooseClass.getText();
					int room = Integer.parseInt(chooseRoom.getText());
					int seat = Integer.parseInt(chooseSeat.getText());
					addStudent(id, className, room, seat);
				}catch(NumberFormatException e){
					showError(Util.NUMBER_ERROR);
				}
			}
		});
		//  TODO 初始化课程表
		
		
	}
	
	// 根据时间查询座位使用情况
	@FXML
	public void query(){
		LocalDateTime dateTime = getDateTime();
		//是否成功获取时间
		if(dateTime!=null){
			weekTextField.setText(String.valueOf(getWeek(dateTime.toLocalDate())+1));
			dayTextField.setText(daysOfWeek[dateTime.toLocalDate().getDayOfWeek().getValue()%7]);
			//  TODO seats = query(room, LocalDateTime);
			int[] a = {0, 1, -1, 50, 55, 20, -20, -35, -90, 10,0, 1, -1, 50, 55, 20, -20, -35, -90, 10, -20, -35, -90, 10,0, 1, -1, 50, 55};
			seats = a;
			updateSeat(room, seats);
		}else{
			showError(Util.DATETIME_ERROR);
		}
	}
	
	// 获取输入时间
	public LocalDateTime getDateTime(){
		try{
			LocalDate localDate = datePicker.getValue();
			int hour = Integer.parseInt(hourTextField.getText());
			int minute = Integer.parseInt(minuteTextField.getText());
			LocalTime localTime = LocalTime.of(hour, minute);
			LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
			return localDateTime;
		}catch(NumberFormatException|NullPointerException|DateTimeException e){
			return null;
		}
	}
	
	// 初始化座位
	public void initSeat(int room, int[] seats){
		seatViews = new SeatView[Util.ROOM_NUM][Util.ROOM_SPACE];  // 阅览室数目和阅览室容量, 数组大小为8×81
		int id;
		for(id=0;id<seatViews[room].length;id++){  
			if(id<seats.length){
				seatViews[room][id] = new SeatView(room, id, seats[id]);
			}else{
				seatViews[room][id] = new SeatView(room, id);  // 默认状态为未设座
			}
		}
		// add Node
		for(int j=0;j<seatViews[room].length;j++){
			seatPane.getChildren().add(seatViews[room][j].getSeatButton());
		}
	}

	// 获取指定日期所处教学周,返回0代表第一周，以此类推
	public long getWeek(LocalDate date){
		long week;
		// 第一周的周一为2019-3-4
		LocalDate weekOne = LocalDate.of(Integer.parseInt(Util.WEEK_INIT.split("-")[0])
				, Integer.parseInt(Util.WEEK_INIT.split("-")[1])
				, Integer.parseInt(Util.WEEK_INIT.split("-")[2]));
		week = (date.toEpochDay()-weekOne.toEpochDay())/7;
		return week;
	}
	
	//  更新座位状态
	public void updateSeat(int room, int[] seats){
		seatPane.getChildren().remove(0, Util.ROOM_SPACE);
		initSeat(room, seats);
	}
	
	//  TODO 更新Library中的seats；注意：Library中的query必须先检查seatViews，若seatViews不为空，则用seatViews更新seats
	public int [][] getSeats(){
		int [][] seats = new int[Util.ROOM_NUM][Util.ROOM_SPACE];
		if(seatViews!=null){
			for(int i=0;i<Util.ROOM_NUM;i++){
				for(int j=0;j<Util.ROOM_SPACE;j++){
					seats[i][j]=seatViews[i][j].getState();
				}
			}
			return seats;
		}else{
			return null;
		}
	}
	
	//  添加学生
	public boolean addStudent(int id, String className, int room, int seat){
		if(isStudent(id, className, room, seat)){
			students.add(new Student(id, className, room, seat));
			studentTable.getItems().add(new Student(id, className, room, seat));
			if(this.room == room){
				// TODO  seats = query(room, LocalDateTime);updateSeat(m, seats);
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isStudent(int id, String className, int room, int seat){
		for(int i=0;i<students.size();i++){  // 判断学号是否已存在
			if(students.get(i).getId()==id){
				showError(Util.ID_ERROR);
				return false;
			}
		}
		boolean flag = false;
		for(int i=0;i<classNames.length;i++){  // 找班级
			if(classNames[i].equals(className)){
				flag = true;
				break;
			}
		}
		if(!flag){
			showError(Util.CLASSNAME_ERROR);
			return flag;
		}
		if(room<0||room>=Util.ROOM_NUM){
			showError(Util.ROOM_ERROR);
			return false;
		}
		if(seat<0||seat>=Util.ROOM_SPACE){
			showError(Util.SEAT_ERROR);
			return false;
		}
		if(!isEmptySeat(room, seat)){
			return false;
		}
		return true;
	}
	
	public boolean isEmptySeat(int room, int seat){
		System.out.println();
		//TODO seats = query(room, LocalDateTime);if(seats[seat]==0){
		if(seatViews[room][seat].getState()==0){  // 未设置座位
			showError(Util.SEAT_ERROR);
			return false;
		}
		for(int i=0;i<students.size();i++){  // 判断是否已为vip座位
			if(students.get(i).getRoom()==room&&students.get(i).getId()==seat){
				showError(Util.OCCUPY_ERROR);
				return false;
			}
		}
		return true;
	}
	
	public void showError(String error){
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setTitle("error");
		errorAlert.setHeaderText(error);
		errorAlert.show();
	}
}
