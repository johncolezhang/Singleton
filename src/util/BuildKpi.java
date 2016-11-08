package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;

import cache.Kpi_cache;
import entity.Kpi;
import entity.TableValueAttr;

public class BuildKpi {
	public static void main(String[] args){
		Kpi_cache cache = new Kpi_cache();
		buildKpi("14");
	}
	
	public static String[] buildKpi(String kpi_id){
		//String result ="";
		List<Integer> ids = new ArrayList<Integer>();
		int id = Integer.parseInt(kpi_id);
		ids.add(id);
		addchildId(id,Kpi_cache.kpis,ids);
		String a[] = new String[ids.size()];
		for(int i = 0;i<ids.size();i++){
			//System.out.println(ids.get(i));
			//result +=ids.get(i)+",";
			a[i] = ids.get(i)+"";
		}
		//result += ids.get(ids.size()-1);
		//System.out.println(result);
		return a;
	}
	
	public static void addchildId(int id,List<Kpi> kpis,List<Integer> ids){
		for(int i = 0;i<kpis.size();i++){
			if(kpis.get(i).getPar_kpi_id() == id){
				ids.add(kpis.get(i).getKpi_id());
				addchildId(kpis.get(i).getKpi_id(),kpis,ids);
			}
		}
	}
	
	public static String buildKpiTree(List<TableValueAttr> tvas,int id){
		String result ="";
		TableValueAttr sumnode = new TableValueAttr();
		for(int i = 0;i<tvas.size();i++){
			if(Integer.parseInt(tvas.get(i).getKpi_id()) == id){
				sumnode = tvas.get(i);
				addchild(sumnode,tvas,Kpi_cache.kpis);
			}
		}
		//System.out.println(sumnode.getKpi_id());
		Map<String, Object> tree = new HashMap<String, Object>();
		tree.put("kpi_name", sumnode.getKpi_name());
		tree.put("dim1", sumnode.getDim1());
		tree.put("dim2", sumnode.getDim2());
		tree.put("kpi_value", sumnode.getKpi_value());
		tree.put("ext1", sumnode.getExt1());
		tree.put("ext2", sumnode.getExt2());
		tree.put("ext3", sumnode.getExt3());
		tree.put("ext4", sumnode.getExt4());
		tree.put("kpi_id", sumnode.getKpi_id());
		tree.put("dim2_id", sumnode.getDim2_id());
		tree.put("children",sumnode.getChildren());
		result = new GsonBuilder().create().toJson(tree) ;
		//System.out.println(result);
		return result;
	}
	public static void addchild(TableValueAttr sumnode,
			List<TableValueAttr> tvas,List<Kpi> kpis){
		List<TableValueAttr> child = new ArrayList<TableValueAttr>();
		for(int i =0;i<kpis.size();i++){
			for(int j = 0;j<tvas.size();j++){
				if(kpis.get(i).getPar_kpi_id() == Integer.parseInt(sumnode.getKpi_id()) &&//kpis(i)的父节点为sumnode
						kpis.get(i).getKpi_id() == Integer.parseInt(tvas.get(j).getKpi_id())){//kpis(i)对应的tvas(j)
					child.add(tvas.get(j));
					addchild(tvas.get(j),tvas,kpis);
					sumnode.setChildren(child);
				}
			}
		}
	}
	
}
