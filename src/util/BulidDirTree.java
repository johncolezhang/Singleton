package util;

import entity.SimpleDirValue;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.GsonBuilder;

import cache.Dir_cache;
import entity.Dir;
import entity.SimpleDirValue;

public class BulidDirTree {
	
	public static Map<String,Object> tree= new HashMap<String,Object>();
	public static List<SimpleDirValue> easynodes = new ArrayList<SimpleDirValue>();
	
	public static void main(String[] args){
		Dir_cache cache = new Dir_cache();
		buildTreedir();
	}
	//生成树
	public static String buildTreedir(){
		//Kpi_cache cache = new Kpi_cache();
		transferDir(Dir_cache.dirs);
		SimpleDirValue sumnode = getsumnodeDir(Dir_cache.dirs);
		addchildDir(Dir_cache.dirs,sumnode);
		String json = buildtreeDir(sumnode);
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
	//从缓存中拿到dir的数据并变成所要求的json的格式  还需要去看数据库从缓存那里拿的数据，就是数据库生成缓存那里
	public static void transferDir(List<Dir> dirs){
		int length = dirs.size();	
		easynodes.clear();
		for(int i=0;i<length;i++){
			if(dirs.get(i).getDir_id()!=0){
				SimpleDirValue node = new SimpleDirValue();
				node.setId( dirs.get(i).getDir_id() );
				node.setText( dirs.get(i).getDir_name() );
				node.setNode_type(dirs.get(i).getDir_node_type());
				
				//System.out.println(dirs.get(i).getDir_node_type());
				if(dirs.get(i).getDir_node_type().equals("00")){
					node.setIconCls("myicon-mydir");
				}
				else{
					node.setIconCls("myicon-myfile");
				}
				
				
				easynodes.add(node);
			}
		}
		//return easynodes;
	}
	
	//拿到父节点 
	public static SimpleDirValue getsumnodeDir(List<Dir> dirs){
		int length = dirs.size();
		SimpleDirValue parentnode = new SimpleDirValue();
		for(int i=0;i<length;i++){
			if(dirs.get(i).getDir_par_id()==-2 ){
				parentnode.setId( dirs.get(i).getDir_id() );
				parentnode.setText( dirs.get(i).getDir_name() );
				parentnode.setNode_type( dirs.get(i).getDir_node_type());
				parentnode.setIconCls("myicon-mydir");
				//parentnode.setChildren(easynodes.get(i).getChildren());
				break;
			}
		}
		return parentnode;
	}
	
	//加入子节点并递归
	public static void addchildDir(List<Dir> dirs, SimpleDirValue parentnode){
		int length = dirs.size();
		List<SimpleDirValue> childnodes =new ArrayList<SimpleDirValue>();
		for(int i =0;i<length;i++){
			if(dirs.get(i).getDir_par_id()==parentnode.getId()){
				for(int j = 0;j<easynodes.size();j++){
					if( dirs.get(i).getDir_id()==easynodes.get(j).getId()){						
						childnodes.add(easynodes.get(j));
						break;
					}
				}
			}
		}
		parentnode.setChildren(childnodes);
		for(int j = 0;j<childnodes.size();j++){
			addchildDir(dirs,childnodes.get(j));
		}
	}
	
	//建立json并打印出来，返回json
	public static String buildtreeDir(SimpleDirValue sumnode){
		tree.put("id",sumnode.getId());
		tree.put("text",sumnode.getText());
		tree.put("children", sumnode.getChildren());
		tree.put("node_type", sumnode.getNode_type());
		tree.put("iconCls", sumnode.getIconCls());
		String result = new GsonBuilder().create().toJson(tree);
		//easynodes.clear();
		//System.out.println("["+result+"]");
		return "["+result+"]";
	}
	
}
