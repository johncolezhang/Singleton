package util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cache.Dir_cache;
import entity.GwnBaseDefaultData;
import entity.Dir;;


public class UpdateDir_Cache {
	
	public static void main(String[] args) throws SQLException{
		Dir_cache cache = new Dir_cache();
		//addnode(2, "123", "01");
		//addnode(2,"12123","01");
		removenode(5501);
		//modifynodename(2,"数据图形");
		//modifynodename(2,"数据图形11");
		
	}
	
	public static void init(){
		DataUtil.createDataSource(GwnBaseDefaultData.sqlName, GwnBaseDefaultData.Phoenix_JDBC, "", "",
				GwnBaseDefaultData.Phoenix_URL);
	}
	
	//在dao方法中加入新节点，有了新的最大id后，载入最大id节点的缓存
	public static void addnode(int par_id, String name,String node_type) throws SQLException{
		
		init();
		String sql_searchnodeid = " select id from sh_gwn_visl_dir where par_id = "+par_id+"";
		List<Object[]> listdir = new ArrayList<Object[]>();
		listdir=DataUtil.query(GwnBaseDefaultData.sqlName, sql_searchnodeid);
		
//		for(Object[] tmp : listdir){
//			//System.out.println(tmp[0].toString());
//			int id=Integer.parseInt(tmp[0].toString());
//		}
		
		Object[] diroc = listdir.get(0);
		String st0 = diroc[0].toString();
		int maxid= Integer.parseInt(st0);
		
		System.out.println(maxid);
		
		for (int i=0;i<listdir.size();i++){
			Object[] oc = listdir.get(i);
			int dirid = Integer.parseInt((String)oc[0]);
			if(dirid>maxid){
				maxid=dirid;
			}
		}
		
		System.out.println(maxid);
		
		Dir d = new Dir();
		d.setDir_id(maxid);//dir的id
		d.setDir_par_id(par_id);
		d.setDir_name(name);
		d.setDir_node_type(node_type);
		Dir_cache.dirs.add(d);
		
//		String str=BuildTree2.buildTreedir();
//		System.out.println(str);
		
	}
	
	
	public static void removenode(int node_id){
		System.out.print(node_id);
		for(int i=0;i<Dir_cache.dirs.size();i++){
			if(Dir_cache.dirs.get(i).getDir_id()==node_id){
//				System.out.print("00000"+Kpi_cache.kpis.get(i).getDir_id());
//				System.out.print(",");
//				System.out.println("00000"+Kpi_cache.kpis.get(i).getDir_name());
				Dir_cache.dirs.remove(i);
//				System.out.print("00000"+Kpi_cache.kpis.get(i).getDir_id());
//				System.out.print(",");
//				System.out.println("00000"+Kpi_cache.kpis.get(i).getDir_name());
			}
		}
		
	}
	
	public static void modifynodename(int node_id,String new_name){
//		Kpi k = new Kpi();
		System.out.println(new_name);
		for(int i=0;i<Dir_cache.dirs.size();i++){
			if(Dir_cache.dirs.get(i).getDir_id()==node_id){
				Dir_cache.dirs.get(i).setDir_name(new_name);
				//System.out.println("00000"+Kpi_cache.kpis.get(i).getDir_name());
			}
		}
		
	}
	

}

//System.out.println(BuildTree2.buildTreedir());
//for(int i=0;i<Kpi_cache.kpis.size();i++)
//{
//	if((Kpi_cache.kpis.get(i).getDir_id()!=0)&&(Kpi_cache.kpis.get(i).getDir_name()!=null))
//	{
//		System.out.print(Kpi_cache.kpis.get(i).getDir_id());
//		System.out.print(",");
//		System.out.println(Kpi_cache.kpis.get(i).getDir_name());
//	}	
//}
