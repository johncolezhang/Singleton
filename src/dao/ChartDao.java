package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;

import cache.Kpi_cache;
import entity.GwnBaseDefaultData;
import entity.Kpi;
import util.BuildChart;
import util.BuildComboBox;
import util.BuildTable;
import util.BuildTree;
import util.DataUtil;
/**
 * 建表操作数据库预前台之间传值的方法
 * @author 
 *
 *             日期：2016年8月18日 时间：下午6:17:03
 */
public class ChartDao {
	public static void main(String[] args){
		Kpi_cache cache = new Kpi_cache();
		//getDimValue("2");
		//getChartData("2016,/101,102,103,",1,2);
		String []dimvalue1 ={"dim1@201601,201606,month","dim2@103","dim3@200","dim4@300"};
		String []dimvalue2 ={"dim1@2016,year","dim2@100","dim3@200","dim4@300"};
		//chartDatajson(dimvalue1,1,1,"pie");
		//getLegenddata(dimvalue1, 1);
		//chartDatajson(dimvalue2,2,1,"chartpie");
		//getLegenddata(dimvalue2, 2);
		//System.out.println();
		//getDimdata(dimvalue2,3);
		chartDatajson(dimvalue2,2,3,"chartline");//dim[],dim_id,kpi_id
		//getLegenddata(dimvalue2, 3);
		//System.out.println(getDimValueName("201"));
		//String[] kpis_temp = {"3","4","12","19"};
		//getKpiUnitName(kpis_temp);
		
	}
	
	public static String getLegenddata(String []dimvalue,int dim_id){//获取饼状图的图例,也就是x轴
		String dim = getDimdata(dimvalue,dim_id);
		String dim1 = dim.split("/")[0];
		dim1 = dim1.substring(5, dim1.length());
		String dimx = dim.split("/")[dim_id-1];//x代表要的dim，x=1就代表dim1
		dimx = dimx.substring(5, dimx.length());
		System.out.println(dimx);
		String []dims = dimx.split(",");
		String legenddata = "";
		if(dim_id !=1){
			for(int i = 0;i<dims.length;i++){
				legenddata += ""+getDimValueName(dims[i])+",";
			}
		}else{
			for(int i = 0;i<dims.length;i++){
				legenddata +=""+dims[i]+",";
			}
		}
		legenddata = ""+legenddata.substring(0, legenddata.length()-1)+"";
		System.out.println(legenddata);
		return legenddata;
	}
	
	public static String getDimdata(String []dimvalue,int dim_id){//根据dim1，dim2，以及dim_id确定dim的的格式
		//拼接的各个维度
		String result = "";
		//遍历所有传递过来的dim
		for(String s:dimvalue){
			String dim[] = s.split("@");
			if(dim[0].equals("dim1")){//当前维度为时间
				result += dim[0]+"@";
				if(dim_id == 1){//用户选择的横轴维度为时间
					String begintime = dim[1].split(",")[0];
					String endtime = dim[1].split(",")[1];
					String frequence =dim[1].split(",")[2];
					result += BuildTable.getTime(frequence, begintime, endtime);
				} else {//用户选择的横轴不是时间
					String endtime = dim[1].split(",")[0];
					String frequence =dim[1].split(",")[1];
					result += BuildTable.getTime(frequence, endtime, endtime);
				}
				result += "/";
			} else {//当前维度不为时间
				result += dim[0]+"@";
				if(dim_id == Integer.parseInt(dim[0].substring(dim[0].length()-1, dim[0].length()))){//判断当前读取的dim是用户选择的dim
					result += BuildChart.getChild(Kpi_cache.kpis, dim[1]);//维度为树时获取所有的子对象
				} else {//当前判断的dim不是用户选择的dim
					result += dim[1]+",";
				}
				result += "/";
			}
		}
		
		result = result.substring(0, result.length()-1);//去掉最后一个/
		
		
		System.out.println(result);
		return result;
		
//		String dim1 = dimvalue[0];
//		if(dim_id == 1){//用户选择时间维度作为横轴
//			String begintime = dim1.split(",")[0];
//			String endtime = dim1.split(",")[1];
//			String frequence =dim1.split(",")[2];
//			dim1 = BuildTable.getTime(frequence, begintime, endtime);
//		}else{//用户选择非时间作为横轴
//			String endtime = dim1.split(",")[0];
//			String frequence =dim1.split(",")[1];
//			dim1 = BuildTable.getTime(frequence, endtime, endtime);
//		}
//		String dim2="";
//		if(dim_id!=2){
//			dim2=dimvalue[1]+",";
//		}else{
//			dim2 = BuildChart.getChild(Kpi_cache.kpis, dimvalue[1]);//维度为部门时获取所有的部门
//		}
//		System.out.println(dim1 +"/"+dim2);
//		return dim1 +"/"+dim2;
	} 
	
	public static String chartDatajson(String []dimvalue,int dim_id,int kpi_id,String type){//获取数据的json
		String dim = getDimdata(dimvalue,dim_id);
		String json = getChartData(dim, kpi_id, dim_id,type);
		return json;
	}
	public static String getDimValue(String kpi_id){//获取相应kpi的维度值，形如：1,2@json1，json2
		String sql_dim = "select kpi_dim_id from sh_gwn_kpi_dim_rel "+
					 "where kpi_id="+kpi_id;
		String []jsons={};
		String json ="";
		try {
			List<Object[]> list_dim = DataUtil.query(GwnBaseDefaultData.sqlName, sql_dim);
			jsons = new String[list_dim.size()];
			String dim_ids ="";
			for(int i=0;i<list_dim.size();i++){
				Object []oc =list_dim.get(i);
				String dim_id = (String)oc[0];
				dim_ids += dim_id+",";
//				jsons[i] = dim_id+"@"+BuildTree.buildTree(Integer.parseInt(dim_id));
				for(int j=0; j<Kpi_cache.kpis.size(); j++){
					if(Integer.parseInt(dim_id) ==(Kpi_cache.kpis.get(j).getDim_id1())){
						jsons[i] = Kpi_cache.kpis.get(j).getDim_desc();
						System.out.println(jsons[i]);
					}
				}
				Map<String,Object> tree= new HashMap<String,Object>();
				tree.put("value",dim_id);
				tree.put("text",jsons[i]);
				json += new GsonBuilder().create().toJson(tree)+",";
			}	
			json = "["+json.substring(0,json.length()-1)+"]";
			json = dim_ids+"@"+json;
			System.out.println(json);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
	public static String getSearchKpi(String kpi_name){//通过字符查相似的kpi_name
		String sql_dim = "select kpi_id from sh_gwn_kpi_def "+
				 "where kpi_name like '%"+kpi_name+"%'";
		String json="";
		List<Kpi> kpis = new ArrayList<Kpi>();
		try {
			List<Object[]> list_kpi = DataUtil.query(GwnBaseDefaultData.sqlName, sql_dim);
			//jsons = new String[list_kpi.size()];
			for(int i=0;i<list_kpi.size();i++){
				Object []oc =list_kpi.get(i);
				for(int j = 0;j<Kpi_cache.kpis.size();j++){
					if(Integer.parseInt((String)oc[0]) == Kpi_cache.kpis.get(j).getKpi_id()){
						Kpi k = new Kpi();
						k.setKpi_id(Kpi_cache.kpis.get(j).getKpi_id());
						k.setKpi_name(Kpi_cache.kpis.get(j).getKpi_name());
						kpis.add(k);
					}
				}
			}
			json = BuildComboBox.getKpiCombo(kpis);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(json);
		return json;
	}
	
	public static String getChartData(String dim,int kpi_id,int dim_id,String type){
		
		String []dims = dim.split("/");//dims存放各个维度 包括名称和值 如dim2@101,102,
		int height = dims.length;//dim的个数
		int width = 0;//最大的dim值个数
		for(String s:dims){
			String dim_var[] = s.split("@");//@用来分隔每一个维度的名称和值
			int w = dim_var[1].split(",").length;
			if(w>width){
				width = w;
			}
		}
		
		//定义二维数组来存放所有维度的值
		String [][]dimvalues = new String [height][width];
		//定义一个一位数组来存放所有维度的名称
		String []dimnames = new String [height];
		for(int i=0; i<height; i++){
			String dim_var[] = dims[i].split("@");//@用来分隔每一个维度的名称和值
			dimnames[i] = dim_var[0];//存维度名称
			dimvalues[i] = dim_var[1].split(",");//存维度值
//			System.out.print(dimnames[i]+" ");
//			for(int j=0; j<dimvalues[i].length; j++){
//				System.out.print(dimvalues[i][j]+" ");
//			}
//			System.out.println();
		}
		
		//定义字符串存放
		String where = "";
		
		//循环拼接各个维度的维度值
		for(int i=0; i<height; i++){
			//拼接每个dim的名称
			where += dimnames[i];
			//循环拼接每个dim的值
			String value = "";
			for(int j=0; j<dimvalues[i].length; j++){
				value += "'"+dimvalues[i][j] + "',";
			}
			if(dimvalues[i].length > 1){//当前维度的维度值数量大于1 使用IN拼接
				value = " IN (" + value.substring(0, value.length()-1) + ")";				
			} else {//当前维度的维度值数量等于1 使用=拼接
				value = " = "+value.substring(0, value.length()-1);
			}
			//拼接这个where语句
			where += value + " AND ";
		}
		where = where.substring(0, where.length()-4);
		//System.out.println(where);
		
		String dim_name = getDim(dim_id);//要查的dim,例如要看部门分布就传部门dim,其他的dim就要求固定
		
		String sqlchart = "select kpi_value,"+dim_name+
						  " from sh_gwn_kpi_value WHERE "+ where + "AND kpi_id="+kpi_id+"";
		System.out.println(sqlchart);
		
		List<Object[]> list_chart = new ArrayList<Object[]>();
		String json ="";
		try {
			list_chart = DataUtil.query(GwnBaseDefaultData.sqlName, sqlchart);
			if(type.equals("chartline")){
				for(int i=0; i<Kpi_cache.kpis.size(); i++){
					if(Kpi_cache.kpis.get(i).getKpi_id()==kpi_id){
						json += Kpi_cache.kpis.get(i).getKpi_unit_id1()+"@";
					}
				}
				json += getLinedata(list_chart, dim_id, dimvalues);
			}else if(type.equals("chartpie")){
				json = getPiedata(list_chart, dim_id, dimvalues);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(json);
		return json;
		
//		String []dims = dim.split("/");//各维度之间用/分隔
//		int height = dims.length;//dim的个数
//		int width = 0;//最大的dim值个数
//		for(String s:dims){
//			int w = s.split(",").length;
//			if(w>width){
//				width = w;
//			}
//		}
//		//System.out.println(height+" "+width);
//		
//		String [][]dimvalues = new String [height][width];
//		for(int i =0;i<height;i++){
//			dimvalues[i] = dims[i].split(",");
//		}//将所有维度值放入到一个二维数组中
//		
//		
//		String dim1 = dims[0];
//		String []dim1s = dim1.split(",");//各维度值用,分隔
//		String strdim1 ="";
//		for(int i = 0;i<dim1s.length-1;i++){
//			strdim1 += "'"+dim1s[i]+"',";
//		}//转换成sql可查询条件：'1','2','3'
//		strdim1 +="'"+dim1s[dim1s.length-1]+"'";
//		
//		String dim2 = dims[1];
//		String []dim2s = dim2.split(",");
//		String strdim2 ="";
//		for(int i = 0;i<dim2s.length-1;i++){
//			strdim2 += "'"+dim2s[i]+"',";
//		}//转换成sql可查询条件：'1','2','3'
//		strdim2 +="'"+dim2s[dim2s.length-1]+"'";
//		
//		String dim_name = getDim(dim_id);//要查的dim,例如要看部门分布就传部门dim,其他的dim就要求固定
//		
//		String sqlchart = "select kpi_value,"+dim_name+
//						  " from sh_gwn_kpi_value "+
//						  "where dim1 in("+strdim1+") AND dim2 in("+strdim2+") AND kpi_id="+kpi_id+"";
//		System.out.println(sqlchart);
//		
//		int []flag = new int[dimvalues[dim_id-1].length];//该维度下应有的json长度
//		//System.out.println(flag.length);
//		for(int i =0;i<flag.length;i++){
//			flag[i] = 0;
//		}
//		
//		List<Object[]> list_chart = new ArrayList<Object[]>();
//		String json ="";
//		try {
//			list_chart = DataUtil.query(GwnBaseDefaultData.sqlName, sqlchart);
//			if(type.equals("chartline")){
//				json = getLinedata(list_chart, dim_id, dimvalues);
//			}else if(type.equals("chartpie")){
//				json = getPiedata(list_chart, dim_id, dimvalues);
//			}
//			/*for(int i=0;i<list_chart.size();i++){
//				Object []oc = list_chart.get(i);
//				Map<String, Object> tree = new HashMap<String, Object>();
//				tree.put("value", oc[0]);//value为查到的kpi_value值
//				if(dim_id ==2){//2表示部门
//					tree.put("name", getDimValueName((String)oc[1]));//name为dim_value_name
//				}else{
//					tree.put("name", (String)oc[1]);
//				}
//				json += new GsonBuilder().create().toJson(tree) + ",";
//				for(int j=0;j<flag.length;j++){
//					if(oc[1].equals(dimvalues[dim_id-1][j])){
//						flag[j] = 1;
//					}//dimvalues[dim_id-1][i]代表
//				}
//			}*/
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		/*for(int i =0;i<flag.length;i++){
//			System.out.print(flag[i]+" ");
//			if(flag[i] ==0){
//				Map<String, Object> tree = new HashMap<String, Object>();
//				tree.put("value", "--");
//				if(dim_id ==2){//2表示部门
//					tree.put("name", getDimValueName(dimvalues[dim_id-1][i]));//name为dim_value_name
//				}else{
//					tree.put("name", dimvalues[dim_id-1][i]);
//				}
//				json += new GsonBuilder().create().toJson(tree) + ",";
//			}
//		}
//		System.out.println();
//		
//		if (json != "") {
//			json = json.substring(0, json.length() - 1);
//		}
//		
//		System.out.println("["+json+"]");*/
//		return json;
	}
	
	public static String getLinedata(List<Object[]> list_chart,int dim_id,String [][]dimvalues){
		String data ="";
		String []datas = new String[dimvalues[dim_id-1].length];
		int []flag = new int[dimvalues[dim_id-1].length];//该维度下应有的json长度
		//System.out.println(flag.length);
		for(int i =0;i<flag.length;i++){
			flag[i] = 0;
		}
		for(int i=0;i<list_chart.size();i++){
			Object []oc = list_chart.get(i);
			for(int j=0;j<flag.length;j++){
				if(oc[1].equals(dimvalues[dim_id-1][j])){
					flag[j] = 1;
					datas[j] =(String)oc[0];
				}//dimvalues[dim_id-1][i]代表dim_id下的维度值
			}
		}
		for(int j=0;j<flag.length;j++){
			if(flag[j]==0){
				datas[j] ="--";
			}
			//System.out.print(datas[j]+" ");
			data += datas[j]+",";
		}
		data = data.substring(0, data.length()-1);
		System.out.println(data);
		return data;
	}
	
	public static String getPiedata(List<Object[]> list_chart,int dim_id,String [][]dimvalues){
		String json ="";
		int []flag = new int[dimvalues[dim_id-1].length];//该维度下应有的json长度
		//System.out.println(flag.length);
		for(int i =0;i<flag.length;i++){
			flag[i] = 0;
		}
		for(int i=0;i<list_chart.size();i++){
			Object []oc = list_chart.get(i);
			Map<String, Object> tree = new HashMap<String, Object>();
			tree.put("value", oc[0]);//value为查到的kpi_value值
			if(dim_id ==2){//2表示部门
				tree.put("name", getDimValueName((String)oc[1]));//name为dim_value_name
			}else{
				tree.put("name", (String)oc[1]);
			}
			json += new GsonBuilder().create().toJson(tree) + ",";
			for(int j=0;j<flag.length;j++){
				if(oc[1].equals(dimvalues[dim_id-1][j])){
					flag[j] = 1;
				}//dimvalues[dim_id-1][i]代表
			}
		}
		
		for(int i =0;i<flag.length;i++){
			System.out.print(flag[i]+" ");
			if(flag[i] ==0){
				Map<String, Object> tree = new HashMap<String, Object>();
				tree.put("value", "--");
				if(dim_id ==2){//2表示部门
					tree.put("name", getDimValueName(dimvalues[dim_id-1][i]));//name为dim_value_name
				}else{
					tree.put("name", dimvalues[dim_id-1][i]);
				}
				json += new GsonBuilder().create().toJson(tree) + ",";
			}
		}
		System.out.println();
		
		if (json != "") {
			json = json.substring(0, json.length() - 1);
		}
		
		System.out.println("["+json+"]");
		return "["+json+"]";
	}
	
	public static String getDim(int dim_id){//根据维度id找维度
		String dim_name ="";
		for(int i =0;i<Kpi_cache.kpis.size();i++){
			if(Kpi_cache.kpis.get(i).getDim_id1() == dim_id){
				dim_name = Kpi_cache.kpis.get(i).getDim_name();
			}
		}
		return dim_name;
	}
	
	public static String getDimValueName(String dim_value){//根据维度值获取维度值的名字
		String dim_value_name ="";
		for(int i =0;i<Kpi_cache.kpis.size();i++){
			if(Kpi_cache.kpis.get(i).getDim_value()!=null &&
					dim_value.equals(Kpi_cache.kpis.get(i).getDim_value()) ){
				dim_value_name = Kpi_cache.kpis.get(i).getDim_value_name();
			}
		}
		return dim_value_name;
	}
	
	public static String getKpiUnitName(String[] kpis){//根据kpi_id获取kpi单位名称
		
		String kpi_unit = "";
		String kpi_unit_id = "";
		String kpi_unit_name = "";
		String sqlchart = "";
		if(kpis.length==1){
			sqlchart = "select U.unit_id,U.unit_name from sh_gwn_kpi_def K, sh_gwn_kpi_unit U WHERE U.unit_id=K.unit_id AND kpi_id="+kpis[0];
		} else {
			String kpi = "";
			for(int i=0; i<kpis.length; i++){
				kpi += kpis[i]+",";
			}
			kpi = kpi.substring(0, kpi.length()-1);
			kpi = "("+kpi+")";
			sqlchart = "select distinct U.unit_id,U.unit_name from sh_gwn_kpi_def K, sh_gwn_kpi_unit U WHERE U.unit_id=K.unit_id AND kpi_id IN "+kpi;
		}
		System.out.println(sqlchart);
		
		List<Object[]> list_chart = new ArrayList<Object[]>();
		try {
			list_chart = DataUtil.query(GwnBaseDefaultData.sqlName, sqlchart);
			if(list_chart.size()==0){
				for(int i=0; i<list_chart.size(); i++){
					kpi_unit_name += (String)list_chart.get(i)[0]+",";
				}
			} else {
				for(int i=0; i<list_chart.size(); i++){
					kpi_unit_id += (String)list_chart.get(i)[0]+",";
					kpi_unit_name += (String)list_chart.get(i)[1]+",";
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kpi_unit_id = kpi_unit_id.substring(0, kpi_unit_id.length()-1);
		kpi_unit_name = kpi_unit_name.substring(0, kpi_unit_name.length()-1);
		kpi_unit = kpi_unit_id+"@"+kpi_unit_name;
		System.out.println(kpi_unit);
		return kpi_unit;
	}
}


