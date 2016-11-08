package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;

import cache.Org_cache;
import entity.Org;
import entity.SimpleOrgValue;

public class BuildOrgTree {
	
	public static Map<String,Object> tree= new HashMap<String,Object>();
	public static List<SimpleOrgValue> easynodes = new ArrayList<SimpleOrgValue>();
	
	public static void main(String[] args){
		Org_cache cache = new Org_cache();
		buildTreeorg();
	}
	//生成树
	public static String buildTreeorg(){
		//Kpi_cache cache = new Kpi_cache();
		transferOrg(Org_cache.orgs);
		SimpleOrgValue sumnode = getsumnodeOrg(Org_cache.orgs);
		addchildOrg(Org_cache.orgs,sumnode);
		String json = buildtreeOrg(sumnode);
		//writeTxtFile(json, filepath);
		System.out.println(json);
		return json;
	}

	/*
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
	*/
	//从缓存中拿到Org的数据并变成所要求的json的格式  还需要去看数据库从缓存那里拿的数据，就是数据库生成缓存那里
	public static void transferOrg(List<Org> orgs){
		int length = orgs.size();	
		easynodes.clear();
		for(int i=0;i<length;i++){
			if(orgs.get(i).getOrg_id()!=0){
				SimpleOrgValue node = new SimpleOrgValue();
				node.setId( orgs.get(i).getOrg_id() );
				node.setText( orgs.get(i).getOrg_name() );
				
//				node.setNode_type(orgs.get(i).getOrg_node_type());
//				
//				//System.out.println(dirs.get(i).getDir_node_type());
//				if(orgs.get(i).getDir_node_type().equals("00")){
//					node.setIconCls("myicon-mydir");
//				}
//				else{
//					node.setIconCls("myicon-myfile");
//				}
//				
				
				easynodes.add(node);
			}
		}
		//return easynodes;
	}
	
	//拿到父节点 
	public static SimpleOrgValue getsumnodeOrg(List<Org> orgs){
		int length = orgs.size();
		SimpleOrgValue parentnode = new SimpleOrgValue();
		for(int i=0;i<length;i++){
			if(orgs.get(i).getOrg_par_id()==-2 ){
				parentnode.setId( orgs.get(i).getOrg_id() );
				parentnode.setText( orgs.get(i).getOrg_name() );
				//parentnode.setNode_type( orgs.get(i).getOrg_node_type());
				//parentnode.setIconCls("myicon-mydir");
				//parentnode.setChildren(easynodes.get(i).getChildren());
				break;
			}
		}
		return parentnode;
	}
	
	//加入子节点并递归
	public static void addchildOrg(List<Org> orgs, SimpleOrgValue parentnode){
		int length = orgs.size();
		List<SimpleOrgValue> childnodes =new ArrayList<SimpleOrgValue>();
		for(int i =0;i<length;i++){
			if(orgs.get(i).getOrg_par_id()==parentnode.getId()){
				for(int j = 0;j<easynodes.size();j++){
					if( orgs.get(i).getOrg_id()==easynodes.get(j).getId()){						
						childnodes.add(easynodes.get(j));
						break;
					}
				}
			}
		}
		parentnode.setChildren(childnodes);
		for(int j = 0;j<childnodes.size();j++){
			addchildOrg(orgs,childnodes.get(j));
		}
	}
	
	//建立json并打印出来，返回json
	public static String buildtreeOrg(SimpleOrgValue sumnode){
		tree.put("id",sumnode.getId());
		tree.put("text",sumnode.getText());
		tree.put("children", sumnode.getChildren());
		String result = new GsonBuilder().create().toJson(tree);
		//tree.put("node_type", sumnode.getNode_type());
		//tree.put("iconCls", sumnode.getIconCls());
		//easynodes.clear();
		//System.out.println("["+result+"]");
		return "["+result+"]";
	}
	
	

}
