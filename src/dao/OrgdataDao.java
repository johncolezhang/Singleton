package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Dir;
import entity.GwnBaseDefaultData;
import entity.Org;
import util.DataUtil;

public class OrgdataDao {
	
	public static void init(){
		DataUtil.createDataSource(GwnBaseDefaultData.sqlName, GwnBaseDefaultData.Phoenix_JDBC, "", "",
				GwnBaseDefaultData.Phoenix_URL);
	}
	
	public static List<Org> searchKpi(){
		init();
		String sql_org = "select * from sh_gwn_org";
		List<Object[]> list_org = new ArrayList<Object[]>();
		List<Org> orgs = new ArrayList<Org>();
		
		try {
			list_org = DataUtil.query(GwnBaseDefaultData.sqlName, sql_org);
			for(int i = 0;i<list_org.size();i++){
				Org d = new Org();
				Object[] oc = list_org.get(i);
				d.setOrg_id(Integer.parseInt((String)oc[0]));
				d.setOrg_name((String)oc[1]);
				d.setOrg_par_id(Integer.parseInt((String)oc[2]));
				//需要什么再拿什么，顺序是固定的
				//System.out.println(oc[1]+" "+oc[2]+" "+oc[3]+" "+oc[4]+" "+oc[5]);
				orgs.add(d);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(kpis.size()+kpis.get(15).getExt_name());
		return orgs;
	}

}
