package dao;

import java.sql.SQLException;

import entity.GwnBaseDefaultData;
import util.DataUtil;

public class changetreeDao {
	
	public static void init() {
		DataUtil.createDataSource(GwnBaseDefaultData.sqlName, GwnBaseDefaultData.Phoenix_JDBC, "", "",
				GwnBaseDefaultData.Phoenix_URL);
	}
	
	//在数据库中增加节点
	public static void addnode(int parId,String name,String node_type){
		//String sql_count = "select count(*) from sh_gwn_visl_dir";
		/*try {
			new_node_id = DataUtil.query1(GwnBaseDefaultData.sqlName, sql_count);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		init();
		//int new_node_id = 17;		
		int param1=12;    //����ֶ���ʱ�ò�������д��
		String sql_addnode = "UPSERT INTO sh_gwn_visl_dir(id,par_id,name,node_type,param1) VALUES (next value for sh_gwn_visl_dir_id,"+parId+",'"+name+"','"+node_type+"',"+param1+")";
		try {
			    //System.out.println();
				System.out.println(DataUtil.update(GwnBaseDefaultData.sqlName, sql_addnode));
				System.out.println(sql_addnode);
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void removenode(int node_id){
		init();
		String sql_removenode ="DELETE FROM sh_gwn_visl_dir WHERE ID = "+node_id+"";
		try {
			System.out.println(DataUtil.update(GwnBaseDefaultData.sqlName, sql_removenode));
			System.out.println(sql_removenode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void changename(int node_id,String new_name){
		init();
		String sql_changename ="UPSERT INTO sh_gwn_visl_dir(id,name) VALUES ("+node_id+",'"+new_name+"')";
		try {
			System.out.println(DataUtil.update(GwnBaseDefaultData.sqlName, sql_changename));
			System.out.println(sql_changename);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
