package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.BuildChart;

/**
 * 加载每个Kpi标签页
 * @author Song Zeheng
 *
 *             日期：2016年8月15日 时间：上午11:35:01
 */
@WebServlet("/GetKpiTabs")
public class GetKpiTabs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String Kpi_id = request.getParameter("Kpi_id")+",";
        //获取不同指标的维度
        String result = BuildChart.getDimValuejson(BuildChart.getDims(Kpi_id));
        
        PrintWriter out = null;
		try {
		    out = response.getWriter();
		    out.write(result);
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (out != null) {
		        out.close();
		    }
		}
	}

}
