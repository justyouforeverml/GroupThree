package application;

public class Util {
	
	public static final String APP_TITLE = "Library";
	public static final String LOGO_PATH = "/logo.jpg";
    public static final String FXML_PATH = "/Scene.fxml";
    public static final String EXCEL_PATH = "/课程总表.xls";
    public static final String IMAGE_OF_EXCEL_PATH = "/课程总表.jpg";
	
	// 座位的三种状态颜色
	public static final String BUSY_STYLE ="-fx-background-color: #E3170D";
	public static final String FREE_STYLE ="-fx-background-color: #00C957";
	public static final String NONE_STYLE ="-fx-background-color: #4169E1";
	
	// 座位大小
	public static final double prefWidth = 40;
	public static final double prefHeight = 40;
	
	// 座位间距
	public static final double Hgap = 15;  // 水平方向
	public static final double Vgap = 15;  // 竖直方向
	
	public static final String DATETIME_ERROR = "时间输入有误！";
	public static final String NUMBER_ERROR = "学号、阅览室、座位号均应为数字！";
	public static final String ID_ERROR = "该学号已存在！";
	public static final String CLASSNAME_ERROR = "该班级不存在！";
	public static final String ROOM_ERROR = "该阅览室不存在！";
	public static final String SEAT_ERROR = "该座位不存在！";
	public static final String OCCUPY_ERROR = "该座位已为vip所有！";
	public static final String TIP_INFO = "双击单元格可进行修改";
	public static final String ADD_SEAT_CONFIRM = "是否确定在此处添加座位？";
	public static final String DELETE_STUDENT_CONFIRM = "是否确定删除该学生信息？";
	
	
	public static final int ROOM_NUM = 9;  // 阅览室数目
	public static final int ROOM_SPACE = 81;  // 每个阅览室容量
	
	public static final String WEEK_INIT = "2018-9-3";
	
}
