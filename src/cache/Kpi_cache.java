package cache;

import java.util.List;

import dao.DataDao;
import entity.Kpi;

/**
 * 缓存所有表的表头信息
 * @author Song Zeheng
 *
 *             日期：2016年7月28日 时间：上午9:57:50
 */
public class Kpi_cache {
	public static List<Kpi> kpis;
	public Kpi_cache(){
		kpis = DataDao.searchKpi();
	}
	
	
	//测试输出方法
	/*public static void main(String[] args){
		List<Kpi> kpis = DataDao.searchKpi();
		for (int i = 0; i < kpis.size(); i++) {
			if(kpis.get(i).getKpi_id()!=0){
				System.out.println("kpi:"+kpis.get(i).getKpi_id()+" "+kpis.get(i).getKpi_name());
			}
			else if(kpis.get(i).getDim_id1()!=0){
				System.out.println("dim:"+kpis.get(i).getDim_id1()+" "+kpis.get(i).getDim_name()+" "+kpis.get(i).getDim_value());
			}
			else if(kpis.get(i).getExt_id()!=0){
				System.out.println("ext:"+kpis.get(i).getExt_id()+" "+kpis.get(i).getExt_name());
			}
			else if(kpis.get(i).getDim_id2()!=0){
				System.out.println("dim_value:"+kpis.get(i).getDim_value_id()+" "+kpis.get(i).getDim_value_name()+" "+kpis.get(i).getDim_id2());
			}
		}
	}*/
}
