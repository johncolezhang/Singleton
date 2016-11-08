package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Dir;
import entity.GwnBaseDefaultData;
import util.DataUtil;

public class DirdataDao {
	
	public static void init(){
		DataUtil.createDataSource(GwnBaseDefaultData.sqlName, GwnBaseDefaultData.Phoenix_JDBC, "", "",
				GwnBaseDefaultData.Phoenix_URL);
	}
	
	public static List<Dir> searchKpi(){
		//初始化数据库源
		init();
		//获取所有表的表头
		String sql_dir = "select * from sh_gwn_visl_dir";
		List<Object[]> list_dir = new ArrayList<Object[]>();
		List<Dir> dirs = new ArrayList<Dir>();
		
		try {
			
			list_dir = DataUtil.query(GwnBaseDefaultData.sqlName, sql_dir);
			for(int i = 0;i<list_dir.size();i++){
				Dir d = new Dir();
				Object[] oc = list_dir.get(i);
				d.setDir_id(Integer.parseInt((String)oc[0]));//dir的id
				d.setDir_par_id(Integer.parseInt((String)oc[1]));//
				d.setDir_name((String)oc[2]);//
				d.setDir_node_type((String)oc[3]);//
				//k.setExt_desc((String)oc[4]);//ext的描述
				//System.out.println(oc[1]+" "+oc[2]+" "+oc[3]+" "+oc[4]+" "+oc[5]);
				dirs.add(d);
			}//dir表
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(kpis.size()+kpis.get(15).getExt_name());
		return dirs;
	}
	
}
