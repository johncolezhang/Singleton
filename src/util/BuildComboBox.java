package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;

import cache.Kpi_cache;
import entity.ComboBox;
import entity.Kpi;

public class BuildComboBox {
	
	public static void main(String[]args){
		Kpi_cache cache = new Kpi_cache();
//		getDimCombo(cache.kpis);
		getDimValueCombo(cache.kpis,4);
//		getKpiCombo(cache.kpis);
	}
	
	public static String getDimCombo(List<Kpi> kpis){
		int length =kpis.size();
		List<ComboBox> cbs =new ArrayList<ComboBox>();		
		for(int i = 0;i < length;i++){
			if(kpis.get(i).getDim_id1() != 0){
				ComboBox cb = new ComboBox();
				cb.setValue(kpis.get(i).getDim_id1());
				cb.setText(kpis.get(i).getDim_name());
				cbs.add(cb);
			}
		}
		
		String result = "";
		for(int i = 0;i<cbs.size();i++){
			Map<String,Object> tree= new HashMap<String,Object>();
			tree.put("value",cbs.get(i).getValue());
			tree.put("text",cbs.get(i).getText());
			result += new GsonBuilder().create().toJson(tree)+",";
		}
		if(result!=null){
			result = result.substring(0, result.length()-1);
		}
		System.out.println("["+result+"]");
		return "["+result+"]";
	}
	
	public static String getKpiCombo(List<Kpi> kpis){
		int length =kpis.size();
		List<ComboBox> cbs =new ArrayList<ComboBox>();		
		for(int i = 0;i < length;i++){
			if(kpis.get(i).getKpi_id() != 0){
				ComboBox cb = new ComboBox();
				cb.setValue(kpis.get(i).getKpi_id());
				cb.setText(kpis.get(i).getKpi_name());
				cbs.add(cb);
			}
		}
		
		String result = "";
		for(int i = 0;i<cbs.size();i++){
			Map<String,Object> tree= new HashMap<String,Object>();
			tree.put("value",cbs.get(i).getValue());
			tree.put("text",cbs.get(i).getText());
			result += new GsonBuilder().create().toJson(tree)+",";
		}
		if(result!=null){
			result = result.substring(0, result.length()-1);
		}
		System.out.println("["+result+"]");
		return "["+result+"]";
	}
	
	public static String getDimValueCombo(List<Kpi> kpis,int dim_id){
		int length = kpis.size();
		List<ComboBox> cbs =new ArrayList<ComboBox>();	
		for(int i = 0;i<length;i++){
			if(kpis.get(i).getDim_id2()==dim_id){
				ComboBox cb = new ComboBox();
				cb.setText(kpis.get(i).getDim_value_name());
				cb.setValue(Integer.parseInt(kpis.get(i).getDim_value()));
				cbs.add(cb);
			}
		}
		
		String result = "";
		for(int i = 0;i<cbs.size();i++){
			Map<String,Object> tree= new HashMap<String,Object>();
			tree.put("value",cbs.get(i).getValue());
			tree.put("text",cbs.get(i).getText());
			result += new GsonBuilder().create().toJson(tree)+",";
		}
		if(result!= ""){
			result = result.substring(0, result.length()-1);
		}
		System.out.println("["+result+"]");
		return "["+result+"]";
		
	}
}
