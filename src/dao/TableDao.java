package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;

import cache.Kpi_cache;
import entity.GwnBaseDefaultData;
import entity.Kpi;
import entity.TableValueAttr;
import util.BuildKpi;
import util.BuildTable;
import util.DataUtil;

public class TableDao {

	public static void init() {
		DataUtil.createDataSource(GwnBaseDefaultData.sqlName, GwnBaseDefaultData.Phoenix_JDBC, "", "",
				GwnBaseDefaultData.Phoenix_URL);
	}

	public static void main(String[] args) {
		 Kpi_cache cache = new Kpi_cache();
		// searchData("1","year","2016","4,","5,");
		//Kpi_cache cache = new Kpi_cache();
		searchData("2016/106,year,table,2/");
		//searchChart("20160901/104,day,chart,2/");
		//String results[] = getChart("201610/106,month,chart,1/");
	}
	
	public static String[] getChart(String variable){
		String [][]num = TableDao.searchChart(variable);
		String []results = {"","",""};
		//System.out.println(num[0][0]);
		for(int i =0;i<2;i++){
			for(int j = 0;j<num.length;j++){
					results[i] +=num[j][i]+",";
				}
			results[i] = results[i].substring(0, results[i].length()-1);
			//System.out.println(results[i]);
		}
		String []a = variable.split(",");// 拆 dim1/dim2,fre,opt,kpi_id
		String []kpi_id = a[3].split("/");
		//String []opts = a[2].split("/");
		
		results[2] = transfertoName(Integer.parseInt(kpi_id[0])); 
		for(int i=0;i<3;i++){
			System.out.println(results[i]);
		}
		return results;
	}

	public static String[] spiitVariable(String variable) {
		String[] a = variable.split("/");
		return a;
	}
	
	
	public static String[][] searchChart(String variable){
		String[][] num ={};
		String []a = variable.split(",");// 拆 dim1/dim2,fre,opt,kpi_id
		String dim = a[0];
		String fre = a[1];
		String opt = a[2];// chart/图类型/
		String opts[] = opt.split("/");

		String []b = dim.split("/");
		String dim1 = b[0];
		String dim2 = b[1];
		String []kpi_id = a[3].split("/");
		String kpi_ids ="";//kpi_id的格式: '101','102','103'
		/*String []kpi_id =new String[kpi_name.length];
		for(int i = 0;i<kpi_id.length;i++){
			kpi_id[i] = transfertoId(kpi_name[i])+"";
		}*/
		for(int i = 0;i<kpi_id.length-1;i++){
			kpi_ids += ""+kpi_id[i]+",";
		}
		kpi_ids +=""+kpi_id[kpi_id.length-1]+"";
		if(opt.equals("chart")){
			//String kpi_name = a[3];
			String time[] = BuildTable.getTime(fre, dim1);//time存着所有要查的时间
			String times = "";
			for(int i=0;i<time.length-1;i++){
				times += "'"+time[i]+"',";
			}
			times +="'"+time[time.length-1]+"'";//times
			String sql_chart = "select dim1,kpi_value,ext1,ext2,ext3,ext4 "+
					"from sh_gwn_kpi_value "+
					"where dim1 in("+times+") AND dim2='"+dim2+"' AND kpi_id="+kpi_ids+"";
			//System.out.println(sql_chart);

			if(fre.equals("year")){
				
				List<Object[]> list_year = new ArrayList<Object[]>();
				num = new String[1][6];
				try {
					list_year = DataUtil.query(GwnBaseDefaultData.sqlName, sql_chart);
					if(list_year.size()!=1){//list_year只会有一条
						num[0][0] = time[0];
						for(int i = 1;i<6;i++){
							num[0][i] = 0 + "";
						}
					}else{
						for (int i = 0; i < list_year.size(); i++) {
							Object []oc = list_year.get(i);
							for(int j = 0;j<6;j++){
								if(oc[j]!=null){
									num[0][j]=(String)oc[j];
								}else{
									num[0][j]="0";
								}
							}
						}
						for(int i = 0;i<6;i++){//前五项
							//result +=value[i]+" ";
							System.out.print(num[0][i]+" ");
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(fre.equals("month")){
				//String month[] = BuildTable.getTime("month", dim1);
				List<Object[]> list_month = new ArrayList<Object[]>();
				num = new String[13][6];//kpi_id dim2 kpi_value ext1 ext2 ext3 ext4
				try {
					list_month = DataUtil.query(GwnBaseDefaultData.sqlName, sql_chart);
					//System.out.println(list_month.size());					
					int flag[] = new int[13];
					for(int i = 0;i<13;i++){
						flag[i] = 0;
					}
					System.out.println(list_month.size());
					if(list_month.size()<13){//月份没有查完整
						for(int j = 0;j<time.length;j++){
							for(int i = 0;i<list_month.size();i++){
								Object[] oc = list_month.get(i);
								if(time[j].equals(oc[0])){//oc[0]为dim1
									flag[j] = 1;
								}
							}
						}//判断该kpi值是否已经读到，若读到则flag=1
						for(int i = 0;i<13;i++){
							if( flag[i] == 0){
								num[i][0] = time[i];//第一列存的是时间
								for(int j = 1; j<6;j++){
									num[i][j] = 0+"";//没查到数据就置为0
								}
							}
						}
					}/*else{//所有月份都查到
					for(int i = 0;i<list_month.size();i++){
						Object []oc = list_month.get(i);
						for(int j = 0;j<time.length;j++){
							if(time[j].equals(oc[0])){
								for(int k = 0;k<6;k++){
									num[j][k] =(String)oc[k];
								}
							}
						}
					}
					}*/
					for(int i = 0;i<list_month.size();i++){
						Object []oc = list_month.get(i);
						for(int j = 0;j<time.length;j++){
							if(time[j].equals(oc[0])){//oc[0]为时间,与查到的时间匹配
								for(int k = 0;k<6;k++){
									num[j][k] =(String)oc[k];
								}
							}
						}
					}
					for(int i = 0;i<13;i++){
						for(int j =0 ;j<6;j++){
							System.out.print(num[i][j]+" ");
						}
						System.out.println();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else if(fre.equals("day")){

				List<Object[]> list_day = new ArrayList<Object[]>();
				num = new String[31][6];//kpi_id dim2 kpi_value ext1 ext2 ext3 ext4
				try {
					list_day = DataUtil.query(GwnBaseDefaultData.sqlName, sql_chart);
					//System.out.println(list_month.size());					
					int flag[] = new int[31];
					for(int i = 0;i<31;i++){
						flag[i] = 0;
					}
					if(list_day.size()<31){//日期没有查完整
						for(int j = 0;j<time.length;j++){
							for(int i = 0;i<list_day.size();i++){
								Object[] oc = list_day.get(i);
								if(time[j].equals(oc[0])){//oc[0]为dim1
									flag[j] = 1;
								}
							}
						}//判断该kpi值是否已经读到，若读到则flag=1
						for(int i = 0;i<31;i++){
							if( flag[i] == 0){//flag与num有对应关系
								num[i][0] = time[i];
								for(int j = 1; j<6;j++){
									num[i][j] = 0+"";//没查到数据就置为0
								}
							}
						}
					}
					for(int i = 0;i<list_day.size();i++){
						Object []oc = list_day.get(i);
						for(int j = 0;j<time.length;j++){
							if(time[j].equals(oc[0])){//oc[0]为时间,与查到的时间匹配
								for(int k = 0;k<6;k++){
									num[j][k] =(String)oc[k];
								}
							}
						}
					}
					for(int i = 0;i<31;i++){
						for(int j =0 ;j<6;j++){
							System.out.print(num[i][j]+" ");
						}
						System.out.println();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		return num;
	}
	

	public static String searchData(String variable) {
		String[][] num ={};
		List<TableValueAttr> tvas = new ArrayList<TableValueAttr>();
		String []a = variable.split(",");// 拆 dim1/dim2,fre,opt,kpi_id
		String dim = a[0];
		String fre = a[1];
		String opt = a[2];
		String []b = dim.split("/");
		String dim1 = b[0];
		String dim2 = b[1];
		String []kpi = a[3].split("/");
		String []kpi_id = BuildKpi.buildKpi(kpi[0]);
		String kpi_ids ="";//kpi_id的格式: '101','102','103'
		/*String []kpi_id =new String[kpi_name.length];
		for(int i = 0;i<kpi_id.length;i++){
			kpi_id[i] = transfertoId(kpi_name[i])+"";
		}*/
		for(int i = 0;i<kpi_id.length-1;i++){
			kpi_ids += ""+kpi_id[i]+",";
		}
		kpi_ids +=""+kpi_id[kpi_id.length-1]+"";
		//String kpi_ids = BuildKpi.buildKpi(kpi_id[0]);
		String result = "";
		if(opt.equals("table")){
			String sql_table = "select K.kpi_name,V.dim1,D.kpi_dim_value,V.kpi_value,"+
					"V.ext1,V.ext2,V.ext3,V.ext4 "+
					"from sh_gwn_kpi_def K,sh_gwn_kpi_value V,"+
					"sh_gwn_kpi_dim_value D "+
					"where K.kpi_id=V.kpi_id AND V.dim2=D.kpi_dim_value "+
					"AND V.dim1='"+dim1+"' AND D.kpi_dim_value='"+dim2+"'";
			
			String sql_table1 = "select kpi_id,dim1,dim2,kpi_value,ext1,ext2,ext3,ext4 "+
								"from sh_gwn_kpi_value "+
								"where kpi_id in("+kpi_ids+") AND dim1='"+dim1+"' AND dim2='"+dim2+"'";
			System.out.println(sql_table1);
			
			List<Object[]> list_kpi_value = new ArrayList<Object[]>();
			try {
				//list_kpi_value = DataUtil.query(GwnBaseDefaultData.sqlName, sql_table);
				list_kpi_value = DataUtil.query(GwnBaseDefaultData.sqlName, sql_table1);
				//isinKpi(list_kpi_value,dim1,dim2,tvas);//把无数据的指标增加到tvas中
				for (int i = 0; i < list_kpi_value.size(); i++) {
					Object[] oc = list_kpi_value.get(i);
					TableValueAttr tva = new TableValueAttr();
					if(oc[0]!=null){
						tva.setKpi_name( transfertoName(Integer.parseInt(String.valueOf(oc[0]))) );
						tva.setKpi_id((String)oc[0]);
					}else{
						tva.setKpi_name("--");
					}
					if(oc[1]!=null){
						tva.setDim1((String) oc[1]);
					}else{
						tva.setDim1("--");
					}
					if(oc[2]!=null){
						tva.setDim2(transferDim2(Integer.parseInt((String)oc[2])));
						tva.setDim2_id((String)oc[2]);
					}else{
						tva.setDim2("--");
					}
					if(oc[3]!=null){
						tva.setKpi_value((String) oc[3]);
					}else{
						tva.setKpi_value("--");
					}
					if(oc[4]!=null){
						tva.setExt1((String) oc[4]);
					}else{
						tva.setExt1("--");
					}
					if(oc[5]!=null){
						tva.setExt2((String) oc[5]);
					}else{
						tva.setExt2("--");
					}
					if(oc[6]!=null){
						tva.setExt3((String) oc[6]);
					}else{
						tva.setExt3("--");
					}
					if(oc[7]!=null){
						tva.setExt4((String) oc[7]);
					}else{
						tva.setExt4("--");
					}
					/*System.out.println(oc[0] + " " + oc[1] + " " + oc[2] + " " + oc[3] + " " + oc[4] + " " + oc[5]
							+ " " + oc[6] + " " + oc[7]);*/
					tvas.add(tva);
				}
				
				if(list_kpi_value.size()<kpi_id.length){
					int flag[] = new int[kpi_id.length];
					for(int i = 0;i<flag.length;i++){
						flag[i] = 0;
						
					}
					for(int j = 0;j<kpi_id.length;j++){
						for(int i = 0;i<list_kpi_value.size();i++){
							Object[] oc = list_kpi_value.get(i);
							if(kpi_id[j].equals(oc[0])){//oc[0]为kpi的id
								flag[j] = 1;
							}
						}
					}//判断该kpi值是否已经读到，若读到则flag=1
					for(int i = 0;i<flag.length;i++){//flag与kpiid有对应关系
						System.out.print(flag[i]+" ");
						if(flag[i] == 0){
							TableValueAttr tva = new TableValueAttr();
							tva.setKpi_name( transfertoName(Integer.parseInt(kpi_id[i])) );
							tva.setDim1(dim1);
							tva.setDim2(transferDim2(Integer.parseInt(dim2)));
							tva.setKpi_value("--");
							tva.setExt1("--");
							tva.setExt2("--");
							tva.setExt3("--");
							tva.setExt4("--");
							tva.setDim2_id(dim2);
							tva.setKpi_id(kpi_id[i]);
							tvas.add(tva);
						}
					}
					System.out.println();
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(tvas.size());
			
			result = BuildKpi.buildKpiTree(tvas, Integer.parseInt(kpi_id[0]));
			
			//System.out.println(tvas.size());
			/*for (int i = 0; i < tvas.size(); i++) {
				Map<String, Object> tree = new HashMap<String, Object>();
				tree.put("kpi_name", tvas.get(i).getKpi_name());
				tree.put("dim1", tvas.get(i).getDim1());
				tree.put("dim2", tvas.get(i).getDim2());
				tree.put("kpi_value", tvas.get(i).getKpi_value());
				tree.put("ext1", tvas.get(i).getExt1());
				tree.put("ext2", tvas.get(i).getExt2());
				tree.put("ext3", tvas.get(i).getExt3());
				tree.put("ext4", tvas.get(i).getExt4());
				tree.put("kpi_id", tvas.get(i).getKpi_id());
				tree.put("dim2_id", tvas.get(i).getDim2_id());
				result += new GsonBuilder().create().toJson(tree) + ",";
			}
			if (result != "") {
				result = result.substring(0, result.length() - 1);
			}*/
			System.out.println("[" + result + "]");

		}else if(opt.equals("chart")){
			//String kpi_name = a[3];
			String time[] = BuildTable.getTime(fre, dim1);//time存着所有要查的时间
			String times = "";
			for(int i=0;i<time.length-1;i++){
				times += "'"+time[i]+"',";
			}
			times +="'"+time[time.length-1]+"'";//times
			String sql_chart = "select dim1,kpi_value,ext1,ext2,ext3,ext4 "+
					"from sh_gwn_kpi_value "+
					"where dim1 in("+times+") AND dim2='"+dim2+"' AND kpi_id="+kpi_ids+"";
			//System.out.println(sql_chart);

			if(fre.equals("year")){
				
				List<Object[]> list_year = new ArrayList<Object[]>();
				num = new String[1][6];
				try {
					list_year = DataUtil.query(GwnBaseDefaultData.sqlName, sql_chart);
					if(list_year.size()!=1){//list_year只会有一条
						num[1][0] = time[0];
						for(int i = 1;i<6;i++){
							num[0][i] = 0 + "";
						}
					}else{
						for (int i = 0; i < list_year.size(); i++) {
							Object []oc = list_year.get(i);
							for(int j = 0;j<6;j++){
								if(oc[j]!=null){
									num[0][j]=(String)oc[j];
								}else{
									num[0][j]="0";
								}
							}
						}
						for(int i = 0;i<6;i++){//前五项
							//result +=value[i]+" ";
							System.out.print(num[0][i]+" ");
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(fre.equals("month")){
				//String month[] = BuildTable.getTime("month", dim1);
				List<Object[]> list_month = new ArrayList<Object[]>();
				num = new String[13][6];//kpi_id dim2 kpi_value ext1 ext2 ext3 ext4
				try {
					list_month = DataUtil.query(GwnBaseDefaultData.sqlName, sql_chart);
					//System.out.println(list_month.size());					
					int flag[] = new int[13];
					for(int i = 0;i<13;i++){
						flag[i] = 0;
					}
					System.out.println(list_month.size());
					if(list_month.size()<13){//月份没有查完整
						for(int j = 0;j<time.length;j++){
							for(int i = 0;i<list_month.size();i++){
								Object[] oc = list_month.get(i);
								if(time[j].equals(oc[0])){//oc[0]为dim1
									flag[j] = 1;
								}
							}
						}//判断该kpi值是否已经读到，若读到则flag=1
						for(int i = 0;i<13;i++){
							if( flag[i] == 0){
								num[i][0] = time[i];//第一列存的是时间
								for(int j = 1; j<6;j++){
									num[i][j] = 0+"";//没查到数据就置为0
								}
							}
						}
					}/*else{//所有月份都查到
					for(int i = 0;i<list_month.size();i++){
						Object []oc = list_month.get(i);
						for(int j = 0;j<time.length;j++){
							if(time[j].equals(oc[0])){
								for(int k = 0;k<6;k++){
									num[j][k] =(String)oc[k];
								}
							}
						}
					}
					}*/
					for(int i = 0;i<list_month.size();i++){
						Object []oc = list_month.get(i);
						for(int j = 0;j<time.length;j++){
							if(time[j].equals(oc[0])){//oc[0]为时间,与查到的时间匹配
								for(int k = 0;k<6;k++){
									num[j][k] =(String)oc[k];
								}
							}
						}
					}
					for(int i = 0;i<13;i++){
						for(int j =0 ;j<6;j++){
							System.out.print(num[i][j]+" ");
						}
						System.out.println();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else if(fre.equals("day")){

				List<Object[]> list_day = new ArrayList<Object[]>();
				num = new String[31][6];//kpi_id dim2 kpi_value ext1 ext2 ext3 ext4
				try {
					list_day = DataUtil.query(GwnBaseDefaultData.sqlName, sql_chart);
					//System.out.println(list_month.size());					
					int flag[] = new int[31];
					for(int i = 0;i<31;i++){
						flag[i] = 0;
					}
					if(list_day.size()<31){//日期没有查完整
						for(int j = 0;j<time.length;j++){
							for(int i = 0;i<list_day.size();i++){
								Object[] oc = list_day.get(i);
								if(time[j].equals(oc[0])){//oc[0]为dim1
									flag[j] = 1;
								}
							}
						}//判断该kpi值是否已经读到，若读到则flag=1
						for(int i = 0;i<31;i++){
							if( flag[i] == 0){//flag与num有对应关系
								num[i][0] = time[i];
								for(int j = 1; j<6;j++){
									num[i][j] = 0+"";//没查到数据就置为0
								}
							}
						}
					}
					for(int i = 0;i<list_day.size();i++){
						Object []oc = list_day.get(i);
						for(int j = 0;j<time.length;j++){
							if(time[j].equals(oc[0])){//oc[0]为时间,与查到的时间匹配
								for(int k = 0;k<6;k++){
									num[j][k] =(String)oc[k];
								}
							}
						}
					}
					for(int i = 0;i<31;i++){
						for(int j =0 ;j<6;j++){
							System.out.print(num[i][j]+" ");
						}
						System.out.println();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return result;
	}
	
	public static String transferDim2(int dim2){
		for(int i = 0;i<Kpi_cache.kpis.size();i++){
			if(Kpi_cache.kpis.get(i).getDim_value()!=null && Kpi_cache.kpis.get(i).getDim_value().equals(dim2+"")){
				return Kpi_cache.kpis.get(i).getDim_value_name();
			}
		}
		return "";
	}
	
	//将没有搜到的指标用--表示
	public static int[] isinKpi(List<Object[]> list_kpi_value,String dim1,String dim2,List<TableValueAttr> tvas){
		int count = 0;
		int size = list_kpi_value.size();
		List<Kpi> kpi = new ArrayList<Kpi>();
		for(int i = 0;i<Kpi_cache.kpis.size();i++){
			if(Kpi_cache.kpis.get(i).getKpi_id()!=0){//搜索所有的指标
				kpi.add(Kpi_cache.kpis.get(i));
				count++;
			}
		}
		//System.out.println(count);
		int flag[] = new int[count];
		for(int i = 0;i<count;i++){
			flag[i] = 0;
			for(int j = 0;j<size;j++){
				Object[] oc = list_kpi_value.get(j);
				if(oc[0].equals(kpi.get(i).getKpi_name())){
					flag[i] = 1;//搜到的话flag=1
				}
			}
			//System.out.print(" "+flag[i]);
		}
		
		for(int i = 0;i<flag.length;i++){
			if(flag[i] == 0){
				TableValueAttr tva = new TableValueAttr();
				//System.out.println(kpi.get(i).getKpi_name());
				tva.setKpi_name(kpi.get(i).getKpi_name());
				tva.setDim1(dim1);
				tva.setDim2(dim2);
				tva.setExt1("--");
				tva.setExt2("--");
				tva.setExt3("--");
				tva.setExt4("--");
				tva.setKpi_value("--");
				tvas.add(tva);
			}
		}
		
		return flag;
	}

	
	public static String transfertoName(int id){
		for(int i = 0;i<Kpi_cache.kpis.size();i++){
			if(Kpi_cache.kpis.get(i).getKpi_id() == id){
				return Kpi_cache.kpis.get(i).getKpi_name();
			}
		}
		return "";
	}
	public static int transfertoId(String name){
		int id = -1;
		for(int i = 0;i<Kpi_cache.kpis.size();i++){
			if(Kpi_cache.kpis.get(i).getKpi_name()!=null && Kpi_cache.kpis.get(i).getKpi_name().equals(name)){
				id = Kpi_cache.kpis.get(i).getKpi_id();
			}
		}
		return id;
	}
	/*public static String searchData(String kpi_id, String frequence, String time1, String area_id, String brand_id) {
		init();
		List<String> time = new ArrayList<String>();
		time = Arrays.asList(BuildTable.getTime(frequence, time1));
		List<String> brand = BuildTable.getBrandOrArea(brand_id);
		List<String> area = BuildTable.getBrandOrArea(area_id);
		List<TableValueAttr> tvas = new ArrayList<TableValueAttr>();
		List<String> timesql = new ArrayList<String>();
		List<String> sqls = new ArrayList<>();
		String brandsql = "";
		String areasql = "";
		String sql = "select dim1,dim2,dim3,kpi_value,ext1,ext2,ext3,ext4 from sh_gwn_kpi_value where";
		String kpisql = " kpi_id=" + kpi_id;
		String result = "";
		for (int i = 0; i < time.size(); i++) {
			timesql.add(" dim1='" + time.get(i) + "' ");// dim1来表示时间
		}
		// timesql +=" dim1='"+time.get(time.size()-1)+"'";

		for (int j = 0; j < brand.size() - 1; j++) {
			brandsql += " dim3='" + brand.get(j) + "' and";// dim3用来表示品牌
		}
		brandsql += " dim3='" + brand.get(brand.size() - 1) + "'";

		for (int k = 0; k < area.size() - 1; k++) {
			areasql += " dim2='" + area.get(k) + "' and";// dim2用来表示地区
		}
		areasql += " dim2='" + area.get(area.size() - 1) + "'";

		for (int j = 0; j < timesql.size(); j++) {
			sqls.add(sql + kpisql + " and (" + timesql.get(j) + " ) and (" + brandsql + " ) and (" + areasql + " )");
			System.out.println(sqls.get(j));

			List<Object[]> list_kpi_value = new ArrayList<Object[]>();
			try {
				list_kpi_value = DataUtil.query(GwnBaseDefaultData.sqlName, sqls.get(j));
				if (list_kpi_value.size() != 0) {
					for (int i = 0; i < list_kpi_value.size(); i++) {
						Object[] oc = list_kpi_value.get(i);
						TableValueAttr tva = new TableValueAttr();
						tva.setTime((String) oc[0]);
						tva.setArea((String) oc[1]);
						tva.setSection((String) oc[2]);
						tva.setKpi_value((String) oc[3]);
						tva.setSameterm((String) oc[4]);
						tva.setPriorterm((String) oc[5]);
						tva.setCsameterm((String) oc[6]);
						tva.setCpriorterm((String) oc[7]);
						System.out.println(oc[0] + " " + oc[1] + " " + oc[2] + " " + oc[3] + " " + oc[4] + " " + oc[5]
								+ " " + oc[6] + " " + oc[7]);
						tvas.add(tva);
					}
				} else {// 未搜到的话手动加入时间，其余加入--
					TableValueAttr tva = new TableValueAttr();
					tva.setTime(time.get(j));
					tva.setArea("--");
					tva.setSection("--");
					tva.setKpi_value("--");
					tva.setSameterm("--");
					tva.setPriorterm("--");
					tva.setCsameterm("--");
					tva.setCpriorterm("--");
					tvas.add(tva);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(tvas.size());
		for (int i = 0; i < tvas.size(); i++) {
			Map<String, Object> tree = new HashMap<String, Object>();
			tree.put("area", tvas.get(i).getArea());
			tree.put("time", tvas.get(i).getTime());
			tree.put("section", tvas.get(i).getSection());
			tree.put("sameterm", tvas.get(i).getSameterm());
			tree.put("priorterm", tvas.get(i).getPriorterm());
			tree.put("csameterm", tvas.get(i).getCsameterm());
			tree.put("cpriorterm", tvas.get(i).getCpriorterm());
			result += new GsonBuilder().create().toJson(tree) + ",";
		}
		if (result != "") {
			result = result.substring(0, result.length() - 1);
		}
		System.out.println("[" + result + "]");

		return "[" + result + "]";
	}*/
}
