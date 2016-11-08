package util;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.GsonBuilder;
import cache.Kpi_cache;
import entity.Kpi;
import entity.SimpleDimValue;

public class BuildTree {
	
	public static List<SimpleDimValue> easynodes = new ArrayList<SimpleDimValue>();
	
	public static void main(String[] args){
		Kpi_cache cache = new Kpi_cache();
		buildTree(2);//建维度值得树
		//buildKpiTree();
	}
	
	public static String buildTree(int dim_id){
		transfer(Kpi_cache.kpis, dim_id);
		SimpleDimValue sumnode = getsumnode(Kpi_cache.kpis,dim_id);
		addchild(Kpi_cache.kpis,sumnode);
		String json = buildtree(sumnode);
		//writeTxtFile(json, filepath);
		return json;
	}
	
	public static String buildKpiTree(){
		transferKpi(Kpi_cache.kpis);
		SimpleDimValue sumnode = getKpisumnode(Kpi_cache.kpis);
		addkpichild(Kpi_cache.kpis, sumnode);
		String json = buildtree(sumnode);
		return json;
	}
	
	public static void transfer(List<Kpi> kpis,int dim_id){
		int length = kpis.size();	
		for(int i=0;i<length;i++){
			if(kpis.get(i).getDim_id2() == dim_id){
				SimpleDimValue node = new SimpleDimValue();
				node.setId( Integer.parseInt(kpis.get(i).getDim_value()) );
				node.setText(kpis.get(i).getDim_value_name());
				easynodes.add(node);
			}
		}
		//return easynodes;
	}
	
	public static void transferKpi(List<Kpi> kpis){
		int length = kpis.size();
		for(int i =0;i<length;i++){
			if(kpis.get(i).getKpi_id()!=0){
				SimpleDimValue node = new SimpleDimValue();
				node.setId( kpis.get(i).getKpi_id() );
				node.setText(kpis.get(i).getKpi_name());
				easynodes.add(node);
			}
		}
	}
	

	public static SimpleDimValue getsumnode(List<Kpi> kpis,int dim_id){
		int length = kpis.size();
		SimpleDimValue parentnode = new SimpleDimValue();
		for(int i=0;i<length;i++){
			if(kpis.get(i).getDim_par_id()==-2 && dim_id == kpis.get(i).getDim_id2()){
				parentnode.setId( Integer.parseInt(kpis.get(i).getDim_value()) );
				parentnode.setText(kpis.get(i).getDim_value_name());
				//parentnode.setChildren(easynodes.get(i).getChildren());
				break;
			}
		}
		return parentnode;
	}
	
	public static SimpleDimValue getKpisumnode(List<Kpi> kpis){
		int length = kpis.size();
		SimpleDimValue parentnode = new SimpleDimValue();
		for(int i=0;i<length;i++){
			if(kpis.get(i).getKpi_id()==-1){
				parentnode.setId( kpis.get(i).getKpi_id() );
				parentnode.setText(kpis.get(i).getKpi_name());
				//parentnode.setChildren(easynodes.get(i).getChildren());
				break;
			}
		}
		return parentnode;
	}
	
	public static void addchild(List<Kpi> kpis, SimpleDimValue parentnode){
		int length = kpis.size();
		List<SimpleDimValue> childnodes =new ArrayList<SimpleDimValue>();
		for(int i =0;i<length;i++){
			if(kpis.get(i).getDim_par_id()==parentnode.getId()){
				for(int j = 0;j<easynodes.size();j++){
					if( Integer.parseInt(kpis.get(i).getDim_value())==easynodes.get(j).getId()){						
						childnodes.add(easynodes.get(j));
						break;
					}
				}
			}
		}
		parentnode.setChildren(childnodes);
		for(int j = 0;j<childnodes.size();j++){
			addchild(kpis,childnodes.get(j));
		}
	}
	
	public static void addkpichild(List<Kpi> kpis, SimpleDimValue parentnode){
		int length = kpis.size();
		List<SimpleDimValue> childnodes =new ArrayList<SimpleDimValue>();
		for(int i =0;i<length;i++){
			if(kpis.get(i).getPar_kpi_id()==parentnode.getId()){//get(i)的父id等于parent的id
				for(int j = 0;j<easynodes.size();j++){
					if( kpis.get(i).getKpi_id()==easynodes.get(j).getId()){						
						childnodes.add(easynodes.get(j));
						break;
					}
				}
			}
		}
		parentnode.setChildren(childnodes);
		for(int j = 0;j<childnodes.size();j++){
			addkpichild(kpis,childnodes.get(j));
		}
	}
	
	public static String buildtree(SimpleDimValue sumnode){
		Map<String,Object> tree= new HashMap<String,Object>();
		tree.put("id",sumnode.getId());
		tree.put("text",sumnode.getText());
		tree.put("children", sumnode.getChildren());
		String result = new GsonBuilder().create().toJson(tree);
		System.out.println("["+result+"]");
		easynodes.clear();
		tree.clear();
		return "["+result+"]";
	}
	
	 public static boolean writeTxtFile(String content,String fileName){  
		  boolean flag=false;  
		  FileOutputStream o=null;  
		  try {
			  File file = new File(fileName);
			  o = new FileOutputStream(file);  
			  o.write(content.getBytes("UTF-8"));  
		      o.close();  
		      flag=true;  
		  } catch (Exception e) {  
		   // TODO: handle exception  
		   e.printStackTrace();  
		  }  
		  return flag;  
		 } 
	
}
