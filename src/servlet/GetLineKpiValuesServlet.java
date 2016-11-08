package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ChartDao;

/**
 * 一次选取多个指标生成折线
 * @author Song Zeheng
 *
 *             日期：2016年8月31日 时间：下午3:01:51
 */
@WebServlet("/GetLineKpiValuesServlet")
public class GetLineKpiValuesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
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
        
        PrintWriter out = null;
		try {
		    out = response.getWriter();
		    out.write(results);
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (out != null) {
		        out.close();
		    }
		}
	}

}
