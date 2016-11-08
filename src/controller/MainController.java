package controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cache.Dir_cache;
import cache.Kpi_cache;
import dao.ChartDao;
import dao.TableDao;
import dao.changetreeDao;
import util.BuildChart;
import util.BuildComboBox;
import util.BuildTree;
import util.BulidDirTree;
import util.ResponseUtil;
import util.UpdateDir_Cache;

/**
 * 页面跳转传值方法类
 * @author Song Zeheng
 *
 *             日期：2016年9月1日 时间：下午3:56:31
 */
@Controller
@RequestMapping("/main")
public class MainController {
	
	/**
	 * 新增目录树节点，并修改数据库和缓存中的数据
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//AddtreeServlet
	@RequestMapping("/addTree")
	public String addTree(HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		//前台传进来数据，后台插入
		//id和param1先不管
		String st1 = request.getParameter("par_id");
		int parId =Integer.parseInt(st1);
		String st2 = request.getParameter("name");
		String st3 = request.getParameter("node_type");
		
		//在数据库中增加节点
		changetreeDao.addnode(parId, st2, st3);
		
		try {
			//更新缓存并重新生成json
			UpdateDir_Cache.addnode(parId, st2, st3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//更新缓存后生成树
		String result=BulidDirTree.buildTreedir();
		
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 更改目录树名字
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//ChangenameServlet
	@RequestMapping("/changeTree")
	public String changeTree(HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		String st1 = request.getParameter("node_id");
		int nodeid =Integer.parseInt(st1);
		String st2 = request.getParameter("name");
		
		//在数据中更改名字
		changetreeDao.changename(nodeid, st2);
		//在缓存中更改名字
		UpdateDir_Cache.modifynodename(nodeid, st2);
		
		//从缓存中生成树
		String result=BulidDirTree.buildTreedir();
		
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 获取一个维度的下拉框json数据
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//GetDimComboBox
	@RequestMapping("/getDimComboBox")
	public String getDimComboBox(HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		String dim_id = request.getParameter("dim_id");
        //获取该维度的下拉框json
        String result = BuildComboBox.getDimValueCombo(Kpi_cache.kpis,Integer.parseInt(dim_id));
		
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 获取一个维度下拉框树的json数据
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//GetDimComboTree
	@RequestMapping("/getDimComboTree")
	public String getDimComboTree(HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		String dim_id = request.getParameter("dim_id");
        //获取该维度的树json
        String result = BuildTree.buildTree(Integer.parseInt(dim_id));
        
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 获取当前指标对应的维度id或name
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//GetDimValueServlet
	@RequestMapping("/getDimValue")
	public String getDimValue(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String kpi_id = request.getParameter("kpi_id");
		String results = ChartDao.getDimValue(kpi_id);
		
		ResponseUtil.write(response, results);
		return null;
	}
	
	/**
	 * 根据获取的kpi获取所有kpi对应的维度，包括相同的维度和不同的维度
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//GetKpiTabs
	@RequestMapping("/getKpiTabs")
	public String getKpiTabs(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String Kpi_id = request.getParameter("Kpi_id")+",";
        //获取不同指标的维度
        String result = BuildChart.getDimValuejson(BuildChart.getDims(Kpi_id));
        
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 获取echarts图例
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//GetLegendServlet
	@RequestMapping("/getLegend")
	public String getLegend(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String var[] = new String [2];
        //接受字符串数组
        var[0] = "dim1@" + request.getParameter("var0");
        var[1] = "dim2@" + request.getParameter("var1");
        //接受kpi_id和dim_id
        String kd_id = request.getParameter("var2");
        String temp[] = kd_id.split(",");
        int dim_id = Integer.parseInt(temp[0]);
        int kpi_id = Integer.parseInt(temp[1]);
        System.out.println(var[0]+" "+var[1]);
        System.out.println(dim_id);
        
        String result = ChartDao.getLegenddata(var, dim_id);
        
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 用来查找一个指标的Kpi值，不包括其他额外值
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//GetLineKpiValueServlet
	@RequestMapping("/getLineKpiValue")
	public String getLineKpiValue(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String kpi_id = request.getParameter("kpi_id");
        
        String dim_id = request.getParameter("dim_id");
        
        String dims = request.getParameter("dims");
        dims = dims.substring(0, dims.length()-1);
        
        String[] dims_array = dims.split("\\^");
        
        String result = ChartDao.chartDatajson(dims_array, Integer.parseInt(dim_id), Integer.parseInt(kpi_id), "chartline");
        
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 用来查找多个指标的Kpi值，不包括其他额外值
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//GetLineKpiValuesServlet
	@RequestMapping("/getLineKpiValues")
	public String getLineKpiValues(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String kpi_ids = request.getParameter("kpi_ids");
        
        String dim_id = request.getParameter("dim_id");
        
        String kpis_dims = request.getParameter("kpis_dims");
        kpis_dims = kpis_dims.substring(0, kpis_dims.length()-1);//去除最后的-
        
        String[] kpis = kpi_ids.split("_");//将kpi转化位数组存放
        
        String[] dims_array = kpis_dims.split("-");//将每个kpi的dims转化为数组存放
        
        String results = "";
        //循环读取每个kpi
        for(int i=0; i<dims_array.length; i++){
        	String[] array = dims_array[i].split("\\^");
        	String result = ChartDao.chartDatajson(array, Integer.parseInt(dim_id), Integer.parseInt(kpis[i]), "chartline");
        	results += result+"!";
        }
        
        results = results.substring(0, results.length()-1);
		
		ResponseUtil.write(response, results);
		return null;
	}
	/**
	 * 获取表格折线图横坐标
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//GetLineKpiXAxis
	@RequestMapping("/getLineKpiXAxis")
	public String getLineKpiXAxis(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String dim_id = request.getParameter("dim_id");
        
        String dims = request.getParameter("dims");
        dims = dims.substring(0, dims.length()-1);
        
        String[] dims_array = dims.split("\\^");
        String result = ChartDao.getLegenddata(dims_array, Integer.parseInt(dim_id));
        
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 按要求查询指标的kpi值以及扩展值加载到表格中
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//GetValueServlet
	@RequestMapping("/getTableValue")
	public String getTableValue(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String variable = request.getParameter("variable");
		String result = TableDao.searchData(variable);
		result = "["+result+"]";
		
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 初始化目录树
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//InitDirServlet
	@RequestMapping("/initDirTree")
	public String initDirTree(HttpServletResponse response, HttpServletRequest request) throws Exception {

		Dir_cache cache = new Dir_cache();
        //生成树，在这里会打印出json在控制台下面
		String result = BulidDirTree.buildTreedir();
		
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 初始化缓存
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//InitServlet
	@RequestMapping("/init")
	public String init(HttpServletResponse response, HttpServletRequest request) throws Exception {

		Kpi_cache cache = new Kpi_cache();
        //生成地区树
		String result = BuildTree.buildKpiTree();
		
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 获取自定义折线图对应的横坐标
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//LineChartXServlet
	@RequestMapping("/lineChartX")
	public String lineChartX(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String variable = request.getParameter("variable");
		String results[] = TableDao.getChart(variable);
		
		ResponseUtil.write(response, results[0]);
		return null;
	}
	
	/**
	 * 表格折线图对应的Value值
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//LineChartYServlet
	@RequestMapping("/lineChartY")
	public String lineChartY(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String variable = request.getParameter("variable");
		String results[] = TableDao.getChart(variable);
		
		ResponseUtil.write(response, results[1]);
		return null;
	}
	
	/**
	 * 饼状图自定义获取参数
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//PieChartServlet
	@RequestMapping("/pieChart")
	public String pieChart(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String var[] = new String [2];
        //接受字符串数组
        var[0] = request.getParameter("var0");
        var[1] = request.getParameter("var1");
        String type = request.getParameter("type");
        //接受kpi_id和dim_id
        String kd_id = request.getParameter("var2");
        String temp[] = kd_id.split(",");
        int dim_id = Integer.parseInt(temp[0]);
        int kpi_id = Integer.parseInt(temp[1]);
        
        String result = ChartDao.chartDatajson(var, dim_id, kpi_id,type);
        
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 删除目录树节点
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//RemovetreeServlet
	@RequestMapping("/removeTree")
	public String removeTree(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String st1 = request.getParameter("node_id");
		int nodeid =Integer.parseInt(st1);
		//在数据库中删除节点
		changetreeDao.removenode(nodeid);
		//在缓存中更新删除后的树
		UpdateDir_Cache.removenode(nodeid);
		
		String result=BulidDirTree.buildTreedir();
		
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 窗口查找Kpi 加载窗口表格
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//SearchKpiServlet
	@RequestMapping("/searchKpi")
	public String searchKpi(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String variable = request.getParameter("variable");
		String result = "";
		//判断用户输入框是否输入
		if (variable == null) {//没有内容查找
			//输出所有的kpi
			result = BuildComboBox.getKpiCombo(Kpi_cache.kpis);
		} else {//有内容查找
			//输出按内容查找的结果
			result = ChartDao.getSearchKpi(variable);
		}
		
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 窗口获取选择kpi树
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//SelectKpiServlet
	@RequestMapping("/selectKpi")
	public String selectKpi(HttpServletResponse response, HttpServletRequest request) throws Exception {

		//后台获取窗口树json
		String result = BuildTree.buildKpiTree();
		
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 
	 */
	@RequestMapping("/getKpiUnitName")
	public String getKpiUnitName(HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		String kpi_ids = request.getParameter("kpi_ids");
		String kpis[] = kpi_ids.split("_");
		//后台获取窗口树json
		String result = ChartDao.getKpiUnitName(kpis);
		
		ResponseUtil.write(response, result);
		return null;
	}
	
}
