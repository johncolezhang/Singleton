package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.GwnBaseDefaultData;
import entity.Kpi;
import util.DataUtil;

/**
 * 数据查询方法
 * 建立缓存数据
 * @author Song Zeheng
 *
 *             日期：2016年7月28日 时间：上午9:24:13
 */
public class DataDao {
	
	public static void init(){
		DataUtil.createDataSource(GwnBaseDefaultData.sqlName, GwnBaseDefaultData.Phoenix_JDBC, "", "",
				GwnBaseDefaultData.Phoenix_URL);
	}
	
	public static List<Kpi> searchKpi(){
		//初始化数据库源
		init();
		//获取所有表的表头
		String sql_dim_value = "select * from sh_gwn_kpi_dim_value";
		String sql_dim = "select * from sh_gwn_kpi_dim";
		String sql_kpi = "select * from sh_gwn_kpi_def";
		String sql_ext = "select * from sh_gwn_kpi_ext";
		String sql_kpi_unit = "select * from sh_gwn_kpi_unit";
		List<Object[]> list_dim_value = new ArrayList<Object[]>();
		List<Object[]> list_dim = new ArrayList<Object[]>();
		List<Object[]> list_kpi = new ArrayList<Object[]>();
		List<Object[]> list_ext = new ArrayList<Object[]>();
		List<Object[]> list_kpi_unit = new ArrayList<Object[]>();
		List<Kpi> kpis = new ArrayList<Kpi>();
		try {
			
			/*
			 * CREATE TABLE sh_gwn_kpi_dim_value
				(
				id integer NOT NULL PRIMARY KEY ,
				kpi_dim_id integer,
				kpi_dim_value varchar(64),
				disp_ord integer,
				kpi_dim_value_name varchar(64),
				par_id integer
				);
			 */
			list_dim_value = DataUtil.query(GwnBaseDefaultData.sqlName, sql_dim_value);
			for(int i = 0;i<list_dim_value.size();i++){
				Kpi k = new Kpi();
				Object[] oc = list_dim_value.get(i);
				//System.out.println(oc.length);
				k.setDim_value_id(Integer.parseInt((String)oc[0]));//维度值id
				k.setDim_id2(Integer.parseInt((String)oc[1]));//维度id
				k.setDim_value((String)oc[2]);//维度值
				k.setDisp_ord(Integer.parseInt((String)oc[3]));//顺序
				k.setDim_value_name((String)oc[4]);//维度值名称
				k.setDim_par_id(Integer.parseInt((String)oc[5]));//维度值父id
				//System.out.println(oc[2]+" "+oc[5]);
				kpis.add(k);
			}//维度值表
			
			
			
			/*
			 * CREATE TABLE sh_gwn_kpi_dim
				(
				id integer NOT NULL PRIMARY KEY ,
				kpi_dim_id integer,
				dim_name varchar(64),
				dim_type varchar(32),
				dim_desc varchar(128)
				);
			 */
			list_dim = DataUtil.query(GwnBaseDefaultData.sqlName, sql_dim);
			for(int i = 0;i<list_dim.size();i++){
				Kpi k = new Kpi();
				Object[] oc = list_dim.get(i);
				//System.out.println(oc.length);
				k.setDim_id1(Integer.parseInt((String)oc[0]));//维度id
				k.setDim_name((String)oc[1]);//维度名称
				k.setDim_type((String)oc[2]);//维度类型
				k.setDim_desc((String)oc[3]);//维度描述
				//System.out.println(oc[0]+" "+oc[1]+" "+oc[2]+" "+oc[3]);
				kpis.add(k);
			}//维度表
			
			
			/*
			 * CREATE TABLE sh_gwn_kpi_def
				(
				id integer NOT NULL PRIMARY KEY ,
				kpi_id integer,
				kpi_name  varchar(64),
				par_kpi_id integer,
				kpi_type varchar(32),
				disp_ord integer
				);
			 */
			list_kpi= DataUtil.query(GwnBaseDefaultData.sqlName, sql_kpi);
			//System.out.println(list_kpi.size());
			for(int i = 0;i<list_kpi.size();i++){
				Kpi k = new Kpi();
				Object[] oc = list_kpi.get(i);
				k.setKpi_id(Integer.parseInt((String)oc[0]));//指标id
				k.setKpi_name((String)oc[1]);//指标名称
				if(oc[2]!=null){
					k.setPar_kpi_id(Integer.parseInt((String)oc[2]));//父指标id
				}
				k.setKpi_type((String)oc[3]);//指标类型
				if(oc[4]!=null){
					k.setKpi_ord(Integer.parseInt((String)oc[4]));//制表排序
				}
				if(oc[5]!=null){
					k.setKpi_unit_id1(Integer.parseInt((String)oc[5]));//指标单位
				}
				//System.out.println(oc[0]+" "+oc[1]+" "+oc[2]+" "+oc[3]+" "+oc[4]+" "+oc[5]);
				kpis.add(k);
			}//指标表
			
			
			/*
			 * CREATE TABLE sh_gwn_kpi_ext
				(
				id integer NOT NULL PRIMARY KEY ,
				kpi_ext_id integer,
				field_name varchar(64),
				field_type varchar(32),
				ext_name varchar(32),
				ext_desc varchar(128)
				);
			 */
			list_ext= DataUtil.query(GwnBaseDefaultData.sqlName, sql_ext);
			for(int i = 0;i<list_ext.size();i++){
				Kpi k = new Kpi();
				Object[] oc = list_ext.get(i);
				k.setExt_id(Integer.parseInt((String)oc[0]));//ext的id
				k.setField_name((String)oc[1]);//ext的field的名字;例:ext1
				k.setField_type((String)oc[2]);//ext的field的类型
				k.setExt_name((String)oc[3]);//ext的名字;例:上期值
				k.setExt_desc((String)oc[4]);//ext的描述
				//System.out.println(oc[1]+" "+oc[2]+" "+oc[3]+" "+oc[4]+" "+oc[5]);
				kpis.add(k);
			}//ext表
			
			list_kpi_unit = DataUtil.query(GwnBaseDefaultData.sqlName, sql_kpi_unit);
			for(int i=0; i<list_kpi_unit.size(); i++){
				Kpi k = new Kpi();
				Object[] oc = list_kpi_unit.get(i);
				k.setKpi_unit_id2(Integer.parseInt((String)oc[0]));//kpi_unit的id
				k.setKpi_unit_name((String)oc[1]);//kpi_unit的name
				kpis.add(k);
			}//指标单位表
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(kpis.size()+kpis.get(15).getExt_name());
		return kpis;
	}
	//测试方法
	/*
	public static void main(String[] args){
		init();
		searchKpi();
	}*/
	
}
