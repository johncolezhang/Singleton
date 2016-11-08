package entity;

/**
 * gwn_asy_op_type Id 对应的意�?
 * 
 * @author Administrator
 *
 */
public class GwnBaseDefaultData {
	public static String sqlName = "Portal";

	// gwn_asy_op_type id对应的操作类�?
	public static int Type_Create = 1;// "创建";
	public static int Type_Modify = 2;// "修改";
	public static int Type_Running = 3;// "运行";
	public static int Type_Interrupt = 4;// "中断";
	public static int Type_Error = 5;// "异常";
	public static int Type_Successs = 6;// "成功";
	public static int Type_DownLoad = 7;// "下载";

	// gwn_asy_task state状�?�类�?
	public static String taskState_Create = "创建";
	public static String taskState_Running = "运行";
	public static String taskState_Success = "成功 ";
	public static String taskState_Error = "异常";

	// hadoop相关信息配置
	public static String HADOOP_USER_NAME = "hadoop";
	public static String HADOOP_IP = "hdfs://172.16.1.210:9000/historystore/";

	// Hbase初始化配�?
	public static String Hbase_Master = "172.16.1.210:60000";
	public static String Hbase_Quorum = "172.16.1.210";
	public static String Hbase_ClientPort = "2181";
	public static String Hbase_Table_NAME = "historystore";

	// Phoenix 配置
	public static String Phoenix_JDBC = "org.apache.phoenix.jdbc.PhoenixDriver";
	public static String Phoenix_URL = "jdbc:phoenix:master,node1,node2:2181";

}
