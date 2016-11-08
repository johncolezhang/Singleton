package util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;

import cache.Kpi_cache;
import entity.GwnBaseDefaultData;
import entity.Kpi;

public class BuildChart {
	public static void main(String[] args){
		Kpi_cache cache = new Kpi_cache();
		//getChild(Kpi_cache.kpis,"100");
//		getDims("1,2,3,4,5,");//获取指标1,2,3,4,5的公共维度和私有维度
		getDimValuejson(getDims("20,"));//根据公共维度和私有维度生成json
		
	}
	public static String getChild(List<Kpi>kpis,String dim_value){//获取该维度值下的子节点
		List<String> child_dims = new ArrayList<String>();
		String dim="";
		for(int i = 0;i<kpis.size();i++){
			if(kpis.get(i).getDim_par_id() == Integer.parseInt(dim_value)){//get(i)的父维度值等于dim_id
				child_dims.add(kpis.get(i).getDim_value());//get(i)加入到子节点中
			}
		}
		if(child_dims.size() ==0){
			child_dims.add(dim_value);
		}
		
		for(int i =0;i<child_dims.size();i++){
			dim+= child_dims.get(i)+",";
		}
		System.out.println(dim);
		
		return dim;
	}
	
	public static String[] getDims(String kpis){//1,2,3,
		String []kpiarr = kpis.split(",");
		String []dimarr = new String[kpiarr.length];
		String []dimspecial = new String[kpiarr.length];
		String []result = new String[kpiarr.length+1];
		
		Map<String, Integer> strVal = new HashMap<String,Integer>();
		
		for(int i=0;i<kpiarr.length;i++){
			dimarr[i] = getDimValue(kpiarr[i]);//1,3,2,4,
			System.out.println(dimarr[i]);
			for(String str : dimarr[i].split(",")){
				if(strVal.get(str)==null){
					strVal.put(str, 1);
				}else{
					strVal.put(str, strVal.get(str)+1);
				}
			}
	
		}
		//System.out.println(strVal);
		List<String> list_dims = new ArrayList<String>();//出现的所有dim
		List<Integer> list_fre = new ArrayList<Integer>();//每个dim出现的频率
		List<String> common_dims = new ArrayList<String>();//记录公共的dim
		
		for (Map.Entry<String, Integer> entry : strVal.entrySet()) {  //遍历树
		    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		    list_dims.add(entry.getKey());
		    list_fre.add(entry.getValue());
		}
		for(int i =0;i<list_dims.size();i++){
			if(list_fre.get(i) == kpiarr.length){//如果dim在所有kpi中都出现过，则该dim为公共属性
				common_dims.add(list_dims.get(i));//将出现所有kpi长度个数的dim加到common_dims中
			}
		}
		
		
		String commons ="";
		for(int i =0;i<common_dims.size();i++){
			commons += common_dims.get(i)+",";
		}
		result[0] = commons;
		//System.out.println(commons);

		for(int i=0;i<dimarr.length;i++){
			dimspecial[i] ="";
			String []dims = dimarr[i].split(",");
			for(int j=0;j<common_dims.size();j++){
				for(int k=0;k<dims.length;k++){
					if(dims[k].equals(common_dims.get(j))){
						dims[k] ="--";
					}
				}
			}
			for(int k=0;k<dims.length;k++){
				if(!dims[k].equals("--")){
					dimspecial[i] += dims[k]+",";
				}
				//System.out.println(dims[k]);
			}
			if(dimspecial[i]==""){
				dimspecial[i] = ",";
			}
			//System.out.println(dimspecial[i]);
			result[i+1] = dimspecial[i];
		}
		
		System.out.println();
		for(int i=0;i<result.length;i++){
			System.out.println(result[i]);
		}
		
		return result;
	}
	
	
	public static String getDimValue(String kpi_id){//获取相应kpi的维度值
		String sql_dim = "select kpi_dim_id from sh_gwn_kpi_dim_rel "+
					 "where kpi_id="+kpi_id;
		String dim_ids ="";
		try {
			List<Object[]> list_dim = DataUtil.query(GwnBaseDefaultData.sqlName, sql_dim);
			for(int i=0;i<list_dim.size();i++){
				Object []oc =list_dim.get(i);
				String dim_id = (String)oc[0];
				dim_ids += dim_id+",";
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dim_ids;
	}
	
	public static String getDimValuejson(String []dimarr){
		String []jsons = new String[dimarr.length];
		String result = "";
		for(int i = 0;i<dimarr.length;i++){
			jsons[i] ="";
			String []dims =dimarr[i].split(",");//第i个的dim的个数
			String []json = new String[dims.length];//记录dim的名字
			String []type = new String[dims.length];
			for(int j = 0;j<dims.length;j++){
				for(int k=0; k<Kpi_cache.kpis.size(); k++){
					if(Integer.parseInt(dims[j]) ==(Kpi_cache.kpis.get(k).getDim_id1())){
						json[j] = Kpi_cache.kpis.get(k).getDim_desc();
						type[j] = Kpi_cache.kpis.get(k).getDim_type();
					}
				}
				Map<String,Object> tree= new HashMap<String,Object>();
				tree.put("value",dims[j]);
				tree.put("text",json[j]);
				tree.put("type", type[j]);
//				json[j] = new GsonBuilder().create().toJson(tree)+",";
				jsons[i] += dims[j]+","+json[j]+","+type[j]+"@";
//				jsons[i] += json[j];
			}
			if(jsons[i].length() > 0){
				jsons[i] = jsons[i].substring(0, jsons[i].length()-1)+"/";
			}
			else{
				jsons[i] = "/";
			}
			result += jsons[i];
//			jsons[i] =dimarr[i]+'@'+jsons[i];
//			System.out.println(jsons[i]);
		}
		result = result.substring(0, result.length()-1);
		System.out.println(result);
		return result;
	}
}
